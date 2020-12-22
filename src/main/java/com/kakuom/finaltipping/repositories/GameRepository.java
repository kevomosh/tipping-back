package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    String mainQuery = "SELECT new com.kakuom.finaltipping.dto.GameDTO " +
            "(g.gameNumber, g.homeTeam, g.awayTeam) " +
            "FROM Game g WHERE g.week.number = :weekNumber AND g.week.comp = :comp ";

    String gamesToUpdateQuery = mainQuery + "AND g.gameNumber NOT IN " +
            "(SELECT r.gameNumber FROM Result r WHERE r.week.number = :weekNumber AND r.week.comp = :comp) ";

    String order = "ORDER BY g.gameNumber ASC";

    @Query(value = mainQuery + order)
    List<GameDTO> getGamesForWeek(@Param("weekNumber") Integer weekNumber,
                                  @Param("comp") Comp comp);

    @Query(value = gamesToUpdateQuery + order)
    List<GameDTO> getGamesToUpdateResult(@Param("weekNumber") Integer weekNumber,
                                         @Param("comp") Comp comp);

    @Query(value = "SELECT COUNT(g) FROM Game g WHERE g.week.number = :weekNumber AND g.week.comp = :comp")
    Long getLimit(@Param("weekNumber") Integer weekNumber,
                  @Param("comp") Comp comp);
}
