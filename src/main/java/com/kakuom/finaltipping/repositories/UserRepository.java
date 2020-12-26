package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.dto.AuthDTO;
import com.kakuom.finaltipping.dto.ResultDTO;
import com.kakuom.finaltipping.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String nrlResult = "SELECT DISTINCT new com.kakuom.finaltipping.dto.ResultDTO " +
            "(u.name, u.nrlLastScore, u.nrlTotalScore as ts) FROM User u  " +
            "WHERE u.id IN (:idList) ";

    String aflResult = "SELECT DISTINCT new com.kakuom.finaltipping.dto.ResultDTO " +
            "(u.name, u.aflLastScore, u.aflTotalScore as ts) FROM User u  " +
            "WHERE u.id IN (:idList) ";

    String order = "ORDER BY ts DESC";

    Optional<User> findByEmail(String email);


    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.passToken LEFT JOIN FETCH u.picks p LEFT JOIN FETCH p.teamsSelected  WHERE u.id = :userId")
    Optional<User> getById(@Param("userId") Long userId);


    @Query(value = "SELECT new com.kakuom.finaltipping.dto.AuthDTO " +
            "(u.id, u.name, u.email, u.password, u.role) FROM User u WHERE u.email = :userEmail ")
    Optional<AuthDTO> loadByEmail(@Param("userEmail") String userEmail);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users u WHERE u.name = :name)", nativeQuery = true)
    Boolean existsByName(@Param("name") String name);


    @Query(value = "SELECT EXISTS(SELECT 1 FROM users u WHERE u.email = :email)", nativeQuery = true)
    Boolean existsByEmail(@Param("email") String email);

    @Query(value = nrlResult )
    Page<ResultDTO> findNrlResults(@Param("idList") List<Long> idList, Pageable pageable);

    @Query(value = aflResult + order)
    Page<ResultDTO> findAflResults(@Param("idList") List<Long> idList, Pageable pageable);

    @Query(value = nrlResult + " AND LOWER(u.name) LIKE LOWER(CONCAT('%', :un, '%')) " + order)
    Page<ResultDTO> findNrlResultsByName(@Param("idList") List<Long> idList,
                                         @Param("un") String name,
                                         Pageable pageable);

    @Query(value = aflResult + " AND LOWER(u.name) LIKE LOWER(CONCAT('%', :un, '%')) " + order)
    Page<ResultDTO> findAflResultsByName(@Param("idList") List<Long> idList,
                                         @Param("un") String name,
                                         Pageable pageable);

    @Query(value = "SELECT  p.user_id FROM pick p WHERE p.week_number = :weekNumber " +
            "AND p.comp = :comp", nativeQuery = true)
    List<Long> getAllIdsForUsersMadePick(@Param("weekNumber") Integer weekNumber,
                                         @Param("comp") String comp);

    @Query(value = "SELECT DISTINCT u FROM User u JOIN FETCH u.picks WHERE u.id IN (:idList) ")
    List<User> getUsersThatMadePick(@Param("idList") List<Long> idList);

    @Query(value = "SELECT DISTINCT u FROM User u  LEFT JOIN FETCH u.picks WHERE u.id NOT IN (:idList)")
    List<User> getUsersWithNoPick(@Param("idList") List<Long> idList);


}
