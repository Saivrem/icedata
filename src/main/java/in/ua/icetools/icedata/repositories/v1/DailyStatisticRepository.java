package in.ua.icetools.icedata.repositories.v1;

import in.ua.icetools.icedata.models.v1.DailyStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DailyStatisticRepository extends JpaRepository<DailyStatistic, Long> {

    /**
     * @param month Integer month
     * @param day   Integer day
     * @return List of DailyStatistic objects
     */
    @Query(value = "select * from daily_statistics where month(last_modified) = :month and day(last_modified) = :day", nativeQuery = true)
    List<DailyStatistic> getDailyStatistic(@Param("month") int month, @Param("day") int day);

    @Modifying
    @Transactional
    @Query(value = "truncate table daily_statistics", nativeQuery = true)
    void truncate();
}
