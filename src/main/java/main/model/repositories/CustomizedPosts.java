package main.model.repositories;

import java.util.List;

public interface CustomizedPosts<Post> {
    List<Post> getActionCurrentNewPosts();
}
