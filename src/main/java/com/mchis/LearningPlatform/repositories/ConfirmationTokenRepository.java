package com.mchis.LearningPlatform.repositories;

import com.mchis.LearningPlatform.entities.ConfirmationToken;
import com.mchis.LearningPlatform.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    Optional<ConfirmationToken> findByConfirmationToken(String token);
    Optional<ConfirmationToken> findByUser(User user);
}
