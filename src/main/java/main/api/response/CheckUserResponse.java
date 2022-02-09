package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.model.User;

public class CheckUserResponse {
    User user;

    public CheckUserResponse(User user) {
        this.user = user;
    }

    @JsonProperty("id")
    int id = user.getId();

    @JsonProperty("name")
    String name = user.getName();

    @JsonProperty("photo")
    String photo = user.getPhoto();

    @JsonProperty("email")
    String email = user.getEmail();

    @JsonProperty("moderation")
    boolean moderation = user.getIsModerator() > 0;

    @JsonProperty("moderatorCount")
    int moderatorCount = 56;

    @JsonProperty("setting")
    boolean setting = true;
}
