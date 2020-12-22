package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface WeekRepository extends JpaRepository<Week, Long> {

    @Query(value = "SELECT w FROM Week w WHERE w.number = :weekNumber AND w.comp = :comp")
    Optional<Week> findByNumber(@Param("weekNumber") Integer weekNumber,
                                @Param("comp") Comp comp);

    @Query(value = "SELECT w.deadLine FROM Week w WHERE w.number = :weekNumber AND w.comp = :comp")
    Optional<OffsetDateTime> getDeadlineForWeekNumber(@Param("weekNumber") Integer weekNumber,
                                                      @Param("comp") Comp comp);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM week w WHERE w.number = :weekNumber AND w.comp = :comp)",
            nativeQuery = true)
    Boolean existsByNumber(@Param("weekNumber") Integer weekNumber,
                           @Param("comp") String comp);

    @Modifying
    @Query(value = "UPDATE Week SET scoreUpdated = true WHERE number = :weekNumber AND comp = :comp")
    void updateScoreUpdated(@Param("weekNumber") Integer weekNumber,
                            @Param("comp") Comp comp);

    @Query(value = "SELECT w.scoreUpdated FROM Week w WHERE w.number = :weekNumber AND w.comp = :comp")
    Boolean checkScoreUpdated(@Param("weekNumber") Integer weekNumber,
                              @Param("comp") Comp comp);

    @Query(value = "SELECT COUNT(*) + 1 from week w where w.score_updated = true AND w.comp = :comp",
            nativeQuery = true)
    Long getLatestWeekNumber(@Param("comp") String comp);


    @Query(value = "SELECT w.id FROM Week w WHERE w.number = :weekNumber AND w.comp = :comp")
    Long getWeekId(@Param("weekNumber") Integer weekNumber,
                   @Param("comp") Comp comp);

    @Query(value = "select (select count(*) from result r where r.week_id = :weekId) = " +
            "(select count(*) from game g where g.week_id = :weekId)", nativeQuery = true)
    Boolean checkAllResultsEntered(@Param("weekId") Long weekId);


}
