package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.model.User;
import main.model.repositories.UserRepository;

import java.util.Optional;

public class UserResponse {
    private User user;

    public UserResponse(User user) {
        this.user = user;
    }

    @JsonProperty("id")
    int id = user.getId();

    @JsonProperty("name")
    String name = user.getName();
}
