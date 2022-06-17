package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.User;
import org.springframework.stereotype.Component;

@Data
public class CheckUserDto {

    private int id;

    private String name;

    private String photo;

    private String email;

    private boolean moderation;

    private int moderatorCount;

    private boolean setting;
}
