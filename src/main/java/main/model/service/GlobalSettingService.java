package main.model.service;

import main.api.response.SettingResponse;
import org.springframework.stereotype.Service;

@Service
public class GlobalSettingService {

    public SettingResponse getInitGlobalSetting() {
        SettingResponse settingResponse = new SettingResponse();
        settingResponse.setMultiuserMode(false);
        settingResponse.setPostPremoderation(true);
        settingResponse.setStatisticsIsPublic(true);
        return settingResponse;
    }

}
