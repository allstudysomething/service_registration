package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serviceregistration.model.Day;

import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {

    public Day getDayById(Long Id);

}
