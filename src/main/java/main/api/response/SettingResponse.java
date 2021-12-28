package main.api.response;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SettingResponse {

    private boolean multiuserMode;
    private boolean postPremoderation;
    private boolean statisticsIsPublic;




}
