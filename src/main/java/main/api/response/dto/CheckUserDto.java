package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.User;

@Data
public class CheckUserDto {

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

    @JsonProperty("photo")
    String photo;

    @JsonProperty("email")
    String email;

    @JsonProperty("moderation")
    boolean moderation;

    @JsonProperty("moderatorCount")
    int moderatorCount;

    @JsonProperty("setting")
    boolean setting;
}
