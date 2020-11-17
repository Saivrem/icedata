package in.ua.icetools.icedata.resources;

import in.ua.icetools.icedata.models.DailyStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyStatisticRepository extends JpaRepository<DailyStatistic, Long> {

    /**
     * @param month      Integer month
     * @param day        Integer day
     * @param repository String repository
     * @return List of DailyStatistic objects
     */
    @Query(value = "select * from daily_statistics where month(last_modified) = :month and day(last_modified) = :day and repo = :repository", nativeQuery = true)
    List<DailyStatistic> getDailyStatistic(@Param("month") int month, @Param("day") int day, @Param("repository") String repository);
}
