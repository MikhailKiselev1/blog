package main.repositories;

import main.model.Post;
import main.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Query(
            value = "SELECT * FROM posts p WHERE p.user_id = ?1",
            nativeQuery = true)
    Collection<Post> findAllByUserId(int userId);
}
