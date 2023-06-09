package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Day;

import java.time.LocalDate;
import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {

    public Day getDayById(Long Id);

    @Query(nativeQuery = true, value = """
        select days.* from days where day > :localDate
                             and day < cast(:localDate as date) + interval '10 days'
        """)
    List<Day> getcurrent10days(LocalDate localDate);
}
