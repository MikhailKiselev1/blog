package main.repositories;

import main.model.Post;
import main.model.Tag;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);

    @Query(value =
            "SELECT * FROM tags t "
                    + "LEFT JOIN tag2post t2p ON  t2p.tag_id = t.id "
                    + "LEFT JOIN posts p ON  p.id = t2p.post_id "
                    + "WHERE p.moderation_status = 'accepted' "
                    + "AND p.is_active = 1 "
                    + "AND p.time <= NOW() "
                    + "GROUP BY t.name",
            nativeQuery = true)
    List<Tag> findAllActualTags();
}
