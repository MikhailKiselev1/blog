package main.repositories;

import main.model.CaptchaCodes;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCodes, Integer> {
    Optional<CaptchaCodes> findByCode(String code);
}
