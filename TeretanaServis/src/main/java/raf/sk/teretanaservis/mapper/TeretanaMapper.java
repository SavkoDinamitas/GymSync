package raf.sk.teretanaservis.mapper;

import komedija.NotificationDto;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import raf.sk.teretanaservis.domain.Rezervacija;
import raf.sk.teretanaservis.domain.Teretana;
import raf.sk.teretanaservis.domain.Trening;
import raf.sk.teretanaservis.dto.RezervacijaDto;
import raf.sk.teretanaservis.dto.TeretanaDto;
import raf.sk.teretanaservis.dto.TreningDto;

@Component
public class TeretanaMapper {
    public ModelMapper updateModelMapper(){
        var m = new ModelMapper();
        m.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        m.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return m;
    }

    public void putTeretana(Teretana n, TeretanaDto dto){
        var m = updateModelMapper();
        int popust = n.getPopust();
        int brojTrenera = n.getBrojTrenera();
        m.map(dto, n);
        if(n.getPopust() == 0)
            n.setPopust(popust);
        if(n.getBrojTrenera() == 0)
            n.setBrojTrenera(brojTrenera);
    }

    public void putTrening(Trening n, TreningDto dto){
        var m = updateModelMapper();
        m.map(dto, n);
        //hibernate se zali iz nekog razloga ako menjam foreign keys
        //n.getTeretana().setId(dto.getTeretanaId());
    }

    public Teretana DtoToTeretana(TeretanaDto dto){
        var m = new ModelMapper();
        return m.map(dto, Teretana.class);
    }

    public TeretanaDto TeretanaToDto(Teretana n){
        var m = new ModelMapper();
        return m.map(n, TeretanaDto.class);
    }

    public Rezervacija DtoToRezervacija(RezervacijaDto dto){
        var m = new ModelMapper();
        return m.map(dto, Rezervacija.class);
    }

    public RezervacijaDto RezervacijaToDto(Rezervacija n){
        var m = new ModelMapper();
        return m.map(n, RezervacijaDto.class);
    }

    public Trening DtoToTrening(TreningDto dto){
        var m = updateModelMapper();
        Trening xd = m.map(dto, Trening.class);
        return xd;
    }

    public TreningDto TreningToDto(Trening n){
        var m = updateModelMapper();
        TreningDto xd = m.map(n, TreningDto.class);
        return xd;
    }
}
