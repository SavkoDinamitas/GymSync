package raf.sk.userservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import komedija.CekicanjeDto;
import komedija.ManagerCheckDto;
import komedija.NotificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raf.sk.userservice.domain.Role;
import raf.sk.userservice.domain.User;
import raf.sk.userservice.dto.*;
import raf.sk.userservice.exception.BanException;
import raf.sk.userservice.exception.NotFoundException;
import raf.sk.userservice.mapper.UserMapper;
import raf.sk.userservice.repository.UserRepository;
import raf.sk.userservice.security.service.TokenService;
import raf.sk.userservice.service.NotificationService;
import raf.sk.userservice.service.UserService;

import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImplementation implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private NotificationService notificationService;
    @Value("${service.base}")
    private String baseUrl;

    public UserServiceImplementation(UserRepository userRepository, TokenService tokenService, UserMapper userMapper, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.notificationService = notificationService;
    }
    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public AktivacijaDto addManager(ManagerCreateDto managerCreateDto) {
        User user = userMapper.managerDtotoUser(managerCreateDto);
        user.setTip(Role.MENADZER);
        user.setBanovaca(true);
        //generisanje linka za aktivaciju
        Claims claims = Jwts.claims();
        claims.put("activate", user.getId());

        String link = baseUrl + "auth/activate"  + tokenService.generate(claims);

        //slanje mejla
        NotificationDto mejlic = new NotificationDto();
        mejlic.setNotification_type("Aktivacija");
        mejlic.setId_korisnika(userRepository.findUserByUsername(managerCreateDto.getUsername()).get().getId());
        mejlic.setEmail(managerCreateDto.getEmail());
        mejlic.setMessage("Postovani, \n za aktivaciju vaseg mejla, molimo otidjite na link:\n" + link);
        notificationService.notify(mejlic);

        userRepository.save(user);
        return new AktivacijaDto(link);
    }

    @Override
    public AktivacijaDto addUser(UserCreateDto userCreateDto) {
        User user = userMapper.userDtoToUser(userCreateDto);
        user.setTip(Role.KLIJENT);
        user.setId_sale(0);
        user.setBanovaca(true);
        //System.out.println(user.toString());
        userRepository.save(user);
        //generisanje linka za aktivaciju
        Claims claims = Jwts.claims();
        claims.put("activate", user.getId());


        String link = baseUrl + "/auth/activate/"  + tokenService.generate(claims);
        //slanje mejla
        NotificationDto mejlic = new NotificationDto();
        mejlic.setNotification_type("Aktivacija");
        mejlic.setId_korisnika(userRepository.findUserByUsername(userCreateDto.getUsername()).get().getId());
        mejlic.setEmail(userCreateDto.getEmail());
        mejlic.setMessage("Postovani, \n za aktivaciju vaseg mejla, molimo otidjite na link:\n" + link);
        notificationService.notify(mejlic);
        return new AktivacijaDto(link);
    }

    @Override
    public BanovacaDto banuj(BanovacaDto banovacaDto) {
        Optional<User> user = userRepository.findUserByUsername(banovacaDto.getUsername());
        if(user.isPresent()){
            user.get().setBanovaca(banovacaDto.isTiculica());
            userRepository.save(user.get());
        }
        else{
            throw new NotFoundException(String.format("User with username: %s not found", banovacaDto.getUsername()));
        }
        return banovacaDto;
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        if(user.isBanovaca()){
            throw new BanException("Banovani ste sa servera!");
        }
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getTip());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public UserDto putUser(Long id, MasanDto gazenje) {
        Optional<User> xd = userRepository.findUserById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji korisnik sa id: " + id);
        }
        User majmuncina = xd.get();
        userMapper.putUser(majmuncina, gazenje);
        userRepository.save(majmuncina);
        return userMapper.userToUserDto(majmuncina);
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> xd = userRepository.findUserById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji korisnik sa id: " + id);
        }
        return userMapper.userToUserDto(xd.get());
    }

    @Override
    public CekicanjeDto lolcina(Long id) {
        Optional<User> xd = userRepository.findUserById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji korisnik sa id: " + id);
        }
        CekicanjeDto ddx = new CekicanjeDto();
        ddx.setBroj_zakazanih_treninga(xd.get().getBroj_zakazanih_treninga());
        ddx.setEmail(xd.get().getEmail());
        return ddx;
    }

    @Override
    public CekicanjeDto uvecaj(Long id) {
        Optional<User> xd = userRepository.findUserById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji korisnik sa id: " + id);
        }
        xd.get().setBroj_zakazanih_treninga(xd.get().getBroj_zakazanih_treninga()+1);
        CekicanjeDto ddx = new CekicanjeDto();
        ddx.setBroj_zakazanih_treninga(xd.get().getBroj_zakazanih_treninga());
        ddx.setEmail(xd.get().getEmail());
        userRepository.save(xd.get());
        return ddx;
    }

    @Override
    public CekicanjeDto smanji(Long id) {
        Optional<User> xd = userRepository.findUserById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji korisnik sa id: " + id);
        }
        xd.get().setBroj_zakazanih_treninga(xd.get().getBroj_zakazanih_treninga()-1);
        CekicanjeDto ddx = new CekicanjeDto();
        ddx.setBroj_zakazanih_treninga(xd.get().getBroj_zakazanih_treninga());
        ddx.setEmail(xd.get().getEmail());
        userRepository.save(xd.get());
        return ddx;
    }

    @Override
    public ManagerCheckDto dajSalu(Long id) {
        Optional<User> xd = userRepository.findUserById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji korisnik sa id: " + id);
        }
        return new ManagerCheckDto(xd.get().getId_sale());
    }

    @Override
    public void aktivacija(String mast) {
        Claims ddx = tokenService.parseToken(mast);
        Long id = ddx.get("activate", Long.class);
        Optional<User> xd = userRepository.findUserById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji korisnik sa id: " + id);
        }
        xd.get().setBanovaca(false);
        userRepository.save(xd.get());
    }
}
