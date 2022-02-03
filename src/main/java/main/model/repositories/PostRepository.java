package main.model.repositories;

import main.model.User;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<User, Long> {
}
