package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.dto.PlayerDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Boolean existsByFirstNameAndLastNameAndTeamNameAndTeamComp(String firstName,
                                                               String lastName, String teamName, Comp comp);

    @Query(value = "SELECT new com.kakuom.finaltipping.dto.PlayerDTO " +
            "(CONCAT(p.firstName, ' ', p.lastName), t.name) FROM Player p " +
            "JOIN p.team t WHERE t.name IN (:teamNames) AND t.comp = :comp")
    List<PlayerDTO> getPlayersByTeamName(@Param("teamNames") List<String> teamNames,
                                         @Param("comp") Comp comp);

}
