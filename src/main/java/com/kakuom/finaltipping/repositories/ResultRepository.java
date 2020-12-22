package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.dto.WeekResultDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query(value = "SELECT  new com.kakuom.finaltipping.dto.WeekResultDTO " +
            "(r.gameNumber, r.team)  FROM Result r WHERE r.week.number = :weekNumber AND r.week.comp = :comp" +
            " ORDER BY r.gameNumber")
    List<WeekResultDTO> findAllByWeekAndComp(@Param("weekNumber") Integer weekNumber,
                                             @Param("comp") Comp comp);


    @Query(value = "SELECT r.game_number from result r where r.week_id = :weekId", nativeQuery = true)
    List<Integer> getGameNumbersForWeek(@Param("weekId") Long weekId);


}
