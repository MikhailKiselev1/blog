package main.repositories;

import main.model.Post;
import main.model.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {

    @Query(value =
            "SELECT * FROM tag2post t "
                    + "WHERE t.post_id = ?1 "
                    + "AND t.tag_id = ?2 ",
            nativeQuery = true)
    Optional<Tag2Post> findTag2Post(int postId, int tagId);
}
