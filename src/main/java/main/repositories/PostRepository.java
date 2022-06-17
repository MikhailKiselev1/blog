package main.repositories;

import main.model.Post;
import main.model.PostVotes;
import main.model.User;
import org.springframework.data.domain.Pageable;
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
            value = "SELECT * FROM posts p WHERE p.is_active = 1 AND p.moderation_status = 'accepted' AND p.time <= NOW()",
            nativeQuery = true)
    Collection<Post> getActionCurrentNewPosts();

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.id = ?1 "
                    + "AND p.moderator_id = 1 "
                    + "AND p.time <= NOW()"
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Optional<Post> getActionCurrentNewPostsById(int userId);


    @Query(value =
                    "SELECT * FROM posts p "
                            + "LEFT JOIN post_votes pv1 ON  pv1.post_id = p.id "
                            + "WHERE p.moderation_status = 'accepted' "
                            + "AND p.is_active = 1 "
                            + "AND p.time <= NOW() "
                            + "GROUP BY p.id "
                            + "ORDER BY COUNT(pv1.value = 1) DESC",
            nativeQuery = true)
    Collection<Post> findAllPostOrderByBest();

    @Query(value =
            "SELECT * FROM posts p "
                    + "LEFT JOIN posts_comments pc1 ON  pc1.post_id = p.id "
                    + "WHERE p.moderation_status = 'accepted' "
                    + "AND p.is_active = 1 "
                    + "AND p.time <= NOW() "
                    + "GROUP BY p.id "
                    + "ORDER BY COUNT(pc1.id) DESC",
            nativeQuery = true)
    Collection<Post> findAllPostOrderByPopular();

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.moderation_status = 'accepted' "
                    + "AND p.is_active = 1 "
                    + "AND p.time <= NOW() "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findAllPostOrderByRecent();

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.moderation_status = 'accepted' "
                    + "AND p.is_active = 1 "
                    + "AND p.time <= NOW() "
                    + "ORDER BY p.time",
            nativeQuery = true)
    Collection<Post> findAllPostOrderByEarly();

    @Query(
            value = "SELECT * FROM posts p WHERE p.moderation_status = 'new'",
            nativeQuery = true)
    Collection<Post> getNewPosts();

    @Query(
            value = "SELECT * FROM posts p WHERE p.user_id = ?1",
            nativeQuery = true)
    Collection<Post> findByUserId(int userId);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 0 "
                    + "AND p.user_id = ?1 "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByInactive(int userId);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.user_id = ?1 "
                    + "AND p.moderation_status = 'NEW' "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByPending(int userId);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.user_id = ?1 "
                    + "AND p.moderation_status = 'DECLINED' "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByDeclined(int userId);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.user_id = ?1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByPublished(int userId);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.moderation_status = 'NEW' "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findAllNewPostByModerate();

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.moderation_status = 'DECLINED' "
                    + "AND p.moderator_id = ?1 "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findAllDeclinedPostByModerate(int userId);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.moderator_id = ?1 "
                    + "ORDER BY p.time DESC",
            nativeQuery = true)
    Collection<Post> findAllAcceptedPostByModerate(int userId);

    Collection<Post> findByUser(User user);
}
