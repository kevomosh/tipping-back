package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query(value = "SELECT EXISTS (SELECT 1 FROM team t WHERE t.name = :name AND t.comp = :comp)", nativeQuery = true)
    Boolean existsByNameAndComp(@Param("name") String name, @Param("comp") String comp);

    @Query(value = "SELECT t FROM Team t WHERE t.name = :name AND t.comp = :comp")
    Optional<Team> findByNameAndComp(@Param("name") String name, @Param("comp") Comp comp);

    @Query(value = "SELECT t.name FROM Team t WHERE t.comp = :comp")
    List<String> getAllTeamsByComp(@Param("comp") Comp comp);
}
