package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.User;
import main.model.repositories.UserRepository;

import java.util.Optional;

@Data
public class UserResponse {

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;
}
