package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.PasswordResetToken;
import com.vegi.vegilabback.model.RefreshToken;
import com.vegi.vegilabback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);
    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query(value = "delete from vegi.PasswordResetToken where vegi.PasswordResetToken.expiryDate <= ?1", nativeQuery = true)
    void deleteAllExpiredSince(Date now);
}
