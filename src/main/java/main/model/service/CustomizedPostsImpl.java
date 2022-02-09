package main.model.service;

import main.model.repositories.CustomizedPosts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomizedPostsImpl implements CustomizedPosts {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List getActionCurrentNewPosts() {
        return em.createQuery(" from posts where is_active = 1, moderation_status = accepted, begin < NOW()").getResultList();
    }
}
