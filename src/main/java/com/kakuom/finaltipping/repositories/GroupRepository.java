package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.dto.GroupDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GroupRepository extends JpaRepository<Groups, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM groups g WHERE g.name = :name AND g.comp = :comp)", nativeQuery = true)
    Boolean existsByNameAndComp(@Param("name") String name, @Param("comp") String comp);

    @Query(value = "SELECT DISTINCT new com.kakuom.finaltipping.dto.GroupDTO " +
            "(g.id, g.name) FROM Groups g JOIN g.users u WHERE u.id = :userId AND g.comp = :comp")
    List<GroupDTO> getGroupsForUser(@Param("userId") Long userId,
                                    @Param("comp") Comp comp);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM group_user gu " +
            " JOIN groups g ON g.id = gu.group_id " +
            " WHERE gu.user_id = :userId AND g.comp = :comp )", nativeQuery = true)
    Boolean isInComp(@Param("userId") Long userId, @Param("comp") String comp);


    @Query(value = "SELECT DISTINCT gu.user_id FROM group_user gu " +
            "  WHERE  gu.group_id IN " +
            "(SELECT DISTINCT gu.group_id  FROM group_user gu JOIN groups g ON g.id = gu.group_id  " +
            "WHERE g.comp = :comp AND gu.user_id = :userId)", nativeQuery = true)
    List<Long> getIdsInSameCompGroup(@Param("userId") Long userId,
                                     @Param("comp") String comp);

    @Query(value = "SELECT DISTINCT gu.user_id FROM group_user gu JOIN groups g ON g.id = gu.group_id " +
            "WHERE gu.group_id IN (:groupIds) AND g.comp = :comp ", nativeQuery = true)
    List<Long> getIdsInCompGroup(@Param("groupIds") Set<Long> groupIds,
                                 @Param("comp") String comp);

    @Query(value = "SELECT new com.kakuom.finaltipping.dto.GroupDTO " +
            "(g.id, g.name) FROM Groups g WHERE g.comp = :comp")
    List<GroupDTO> getAllGroupsByComp(@Param("comp") Comp comp);

}
