package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.User;

@Data
public class CheckUserDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("email")
    private String email;

    @JsonProperty("moderation")
    private boolean moderation;

    @JsonProperty("moderatorCount")
    private int moderatorCount;

    @JsonProperty("setting")
    private boolean setting;
}
