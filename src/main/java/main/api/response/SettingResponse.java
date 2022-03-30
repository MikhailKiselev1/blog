package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SettingResponse {

    @JsonProperty("MULTIUSER_MODE")
    @Value("${blog.global_setting.MULTIUSER_MODE}")
    private boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    @Value("${blog.global_setting.POST_PREMODERATION}")
    private boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    @Value("${blog.global_setting.STATISTICS_IS_PUBLIC}")
    private boolean statisticsIsPublic;




}
