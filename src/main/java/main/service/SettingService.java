package main.service;

import main.api.request.SettingRequest;
import main.api.response.SettingResponse;
import main.repositories.GlobalSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SettingService {

    private final GlobalSettingRepository globalSettingRepository;

    @Autowired
    public SettingService(GlobalSettingRepository globalSettingRepository) {
        this.globalSettingRepository = globalSettingRepository;
    }

    public SettingResponse getSetting() {

        SettingResponse settingResponse = new SettingResponse();
        if (globalSettingRepository.findByCode("MULTIUSER_MODE").orElseThrow().getValue().equals("YES")) {
            settingResponse.setMultiuserMode(true);
        }
        if (globalSettingRepository.findByCode("POST_PREMODERATION").orElseThrow().getValue().equals("YES")) {
            settingResponse.setPostPremoderation(true);
        }
        if (globalSettingRepository.findByCode("STATISTICS_IS_PUBLIC").orElseThrow().getValue().equals("YES")) {
            settingResponse.setStatisticsIsPublic(true);
        }
        return settingResponse;
    }

    public void putSetting(SettingRequest settingRequest) {

        globalSettingRepository.findAll().forEach(globalSetting -> {
            if (globalSetting.getCode().equals("MULTIUSER_MODE")) {
                globalSetting.setValue(booleanToString(settingRequest.isMultiuserMode()));
            }
            if (globalSetting.getCode().equals("POST_PREMODERATION")) {
                globalSetting.setValue(booleanToString(settingRequest.isPostPremoderation()));
            }
            if (globalSetting.getCode().equals("STATISTICS_IS_PUBLIC")) {
                globalSetting.setValue(booleanToString(settingRequest.isStatisticsIsPublic()));
            }
            globalSettingRepository.save(globalSetting);
        });

    }

    private String booleanToString(boolean result) {
        if (result) {
            return "YES";
        } else return "NO";
    }
}
