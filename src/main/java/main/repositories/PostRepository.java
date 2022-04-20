package main.repositories;

import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(
            value = "SELECT * FROM posts p WHERE p.is_active = 1 AND p.moderation_status = 'accepted' AND p.time < NOW()",
            nativeQuery = true)
    Collection<Post> getActionCurrentNewPosts();

    @Query(
            value = "SELECT * FROM posts p WHERE p.moderation_status = 'new'",
            nativeQuery = true)
    Collection<Post> getNewPosts();
}
