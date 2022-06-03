package main.repositories;

import main.model.Post;
import main.model.PostVotes;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {

    List<PostVotes> findByUserId(int userId);
}
