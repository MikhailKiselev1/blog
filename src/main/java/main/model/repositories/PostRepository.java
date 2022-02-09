package main.model.repositories;

import main.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long>, CustomizedPosts<Post> {
}
