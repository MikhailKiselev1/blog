package main.repositories;

import main.model.Post;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomizedPostsImpl implements CustomizedPosts {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Post> getActionCurrentNewPosts() {
        return em.createQuery(" from posts where is_active = 1, moderation_status = accepted, begin < NOW()").getResultList();
    }
}
