package main.repositories;

import main.model.Post;
import main.model.PostVotes;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {
    @Query(
            value = "SELECT * FROM Post_votes p WHERE p.user_id = ?1",
            nativeQuery = true)
    Collection<PostVotes> findAllByUserId(int userId);
}
