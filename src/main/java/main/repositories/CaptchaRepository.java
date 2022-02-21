package main.repositories;

import main.model.CaptchaCodes;
import org.springframework.data.repository.CrudRepository;

public interface CaptchaRepository extends CrudRepository<CaptchaCodes, Integer> {
}
