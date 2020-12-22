package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Pick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {
    String pickIdsQuery = "SELECT p.id FROM Pick p WHERE " +
            "p.weekNumber = :weekNumber AND p.comp = :comp AND  p.user.id IN (:sameGroupUsersId) ";

    String mainQuery = "SELECT DISTINCT p FROM Pick p " +
            "LEFT JOIN FETCH p.teamsSelected ts WHERE p.weekNumber = :weekNumber " +
            "AND p.comp = :comp AND  p.user.id ";

    @Query(value = pickIdsQuery)
    Page<Long> getPickIds(@Param("weekNumber") Integer weekNumber,
                          @Param("comp") Comp comp,
                          @Param("sameGroupUsersId") List<Long> sameGroupUsersId,
                          Pageable pageable);

    @Query(value = pickIdsQuery + "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<Long> getPickIdsWithName(@Param("weekNumber") Integer weekNumber,
                                  @Param("comp") Comp comp,
                                  @Param("sameGroupUsersId") List<Long> sameGroupUsersId,
                                  @Param("name") String name,
                                  Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Pick p " +
            "LEFT JOIN FETCH p.teamsSelected ts WHERE p.id IN (:pickIds) ORDER BY p.score DESC")
    List<Pick> getPicksWithIds(@Param("pickIds") List<Long> pickIds);

    @Query(value = mainQuery + "= :userId")
    List<Pick> getPickBeforeDeadLine(@Param("weekNumber") Integer weekNumber,
                                     @Param("comp") Comp comp,
                                     @Param("userId") Long userId);

    @Query(value = "SELECT DISTINCT p FROM Pick p JOIN FETCH " +
            "p.teamsSelected ts WHERE p.weekNumber = :weekNumber AND p.comp = :comp ")
    List<Pick> getAllWithWeekNumber(@Param("weekNumber") Integer weekNumber,
                                    @Param("comp") Comp comp);

    @Query(value = "SELECT p FROM Pick p JOIN FETCH p.teamsSelected " +
            "WHERE  p.user.id = :userId AND p.weekNumber = :weekNumber AND p.comp = :comp")
    Optional<Pick> findByWeekNumberAndUserId(
            @Param("weekNumber") Integer weekNumber,
            @Param("comp") Comp comp,
            @Param("userId") Long userId);
}
