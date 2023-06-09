package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.model.Day;
import serviceregistration.repository.DayRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DayService{

    private final DayRepository dayRepository;

    public DayService(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    public List<Day> listAll() {
        return dayRepository.findAll();
    }

    public Day getDayById(Long Id){
        return dayRepository.getDayById(Id);
    }

    public List<Day> getcurrent10days() {
        LocalDate localDate = LocalDate.now();
        return dayRepository.getcurrent10days(localDate);
    }
}
