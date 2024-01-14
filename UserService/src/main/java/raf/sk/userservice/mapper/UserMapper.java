package raf.sk.userservice.mapper;

import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import raf.sk.userservice.domain.User;
import raf.sk.userservice.dto.ManagerCreateDto;
import raf.sk.userservice.dto.MasanDto;
import raf.sk.userservice.dto.UserCreateDto;
import raf.sk.userservice.dto.UserDto;

@Component
 public class UserMapper {

     public UserDto userToUserDto(User user){
        return new UserDto(user.getIme(), user.getEmail(), user.getPrezime(), user.getTip(), user.getDatum_rodjenja());
     }

     public User userDtoToUser(UserCreateDto dto){
        User xd = new User();
        xd.setIme(dto.getIme());
        xd.setPrezime(dto.getPrezime());
        xd.setEmail(dto.getEmail());
        xd.setUsername(dto.getUsername());
        xd.setPassword(dto.getPassword());
        xd.setDatum_rodjenja((dto.getDatum_rodjenja()));
        return xd;
     }

     public User managerDtotoUser(ManagerCreateDto dto){
         User xd = new User();
         xd.setIme(dto.getIme());
         xd.setPrezime(dto.getPrezime());
         xd.setEmail(dto.getEmail());
         xd.setUsername(dto.getUsername());
         xd.setPassword(dto.getPassword());
         xd.setDatum_zaposljavanja(dto.getDatum_zaposljavanja());
         xd.setDatum_rodjenja(dto.getDatum_rodjenja());
         xd.setId_sale(dto.getId_sale());
         return xd;
     }
    public void putUser(User ddx, MasanDto u) {
         var xd = updateModelMapper();
         xd.map(u, ddx);
    }

    public ModelMapper updateModelMapper(){
        var m = new ModelMapper();
        m.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        m.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return m;
    }
}
