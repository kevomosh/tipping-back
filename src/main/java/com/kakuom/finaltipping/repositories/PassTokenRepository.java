package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.model.PassToken;
import com.kakuom.finaltipping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassTokenRepository extends JpaRepository<PassToken, Long> {

    @Query(value = "SELECT pt FROM PassToken  pt WHERE pt.user.id = :userId")
    Optional<PassToken> getTokenByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT pt.user FROM PassToken pt JOIN FETCH pt.user.picks WHERE pt.token = :token")
    Optional<User> getUserByToken(@Param("token") UUID token);
}
