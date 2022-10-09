package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query(value = "SELECT * FROM vegi.users WHERE vegi.users.email = ?1", nativeQuery = true)
    public User findByEmail(String email);

    public User findByResetPasswordToken(String token);

}
