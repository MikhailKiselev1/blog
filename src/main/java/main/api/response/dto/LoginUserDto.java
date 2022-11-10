package main.api.response.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Builder
public class LoginUserDto {

    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    @Nullable
    private int moderationCount;
    private boolean settings;
}
