package main.repositories;

import main.model.Post;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value =
            "SELECT * FROM posts p " +
                    "WHERE p.is_active = 1 " +
                    "AND p.moderation_status = 'accepted' " +
                    "AND p.time <= NOW()",
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
                            + "LEFT JOIN post_votes pv1 ON  pv1.post_id = p.id AND pv1.value = 1 "
                            + "WHERE p.moderation_status = 'accepted' "
                            + "AND p.is_active = 1 "
                            + "AND p.time <= NOW() "
                            + "GROUP BY p.id "
                            + "ORDER BY COUNT(pv1.value) DESC"
                            + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findPostOrderByBest(@Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "LEFT JOIN posts_comments pc1 ON  pc1.post_id = p.id "
                    + "WHERE p.moderation_status = 'accepted' "
                    + "AND p.is_active = 1 "
                    + "AND p.time <= NOW() "
                    + "GROUP BY p.id "
                    + "ORDER BY COUNT(pc1.id) DESC"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findPostOrderByPopular(@Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.moderation_status = 'accepted' "
                    + "AND p.is_active = 1 "
                    + "AND p.time <= NOW() "
                    + "ORDER BY p.time DESC "
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findPostOrderByRecent(@Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.moderation_status = 'accepted' "
                    + "AND p.is_active = 1 "
                    + "AND p.time <= NOW() "
                    + "ORDER BY p.time"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findAllPostOrderByEarly(@Param("offset")int offset, @Param("limit")int limit);

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
                    + "AND p.user_id = :user_id "
                    + "ORDER BY p.time DESC "
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByInactive(@Param("user_id")int userId, @Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.user_id = :user_id "
                    + "AND p.moderation_status = 'NEW' "
                    + "ORDER BY p.time DESC"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByPending(@Param("user_id")int userId, @Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.user_id = :user_id "
                    + "AND p.moderation_status = 'DECLINED' "
                    + "ORDER BY p.time DESC"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByDeclined(@Param("user_id")int userId, @Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.user_id = :user_id "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "ORDER BY p.time DESC"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findMyPostOrderByPublished(@Param("user_id")int userId, @Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.moderation_status = 'NEW' "
                    + "ORDER BY p.time DESC"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findNewPostByModerate(@Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.moderation_status = 'DECLINED' "
                    + "AND p.moderator_id = :user_id "
                    + "ORDER BY p.time DESC"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findDeclinedPostByModerate(@Param("user_id")int userId, @Param("offset")int offset, @Param("limit")int limit);

    @Query(value =
            "SELECT * FROM posts p "
                    + "WHERE p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.moderator_id = :user_id "
                    + "ORDER BY p.time DESC"
                    + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    Collection<Post> findAcceptedPostByModerate(@Param("user_id")int userId, @Param("offset")int offset, @Param("limit")int limit);

}
