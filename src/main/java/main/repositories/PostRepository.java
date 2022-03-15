package main.repositories;

import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM posts p WHERE p.is_active = 1 AND p.moderation_status = 'accepted'")
    Collection<Post> getActionCurrentNewPosts();
}
