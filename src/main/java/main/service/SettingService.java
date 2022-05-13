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
        if (globalSettingRepository.findById(1).get().getValue().equals("YES")) {
            settingResponse.setMultiuserMode(true);
        }
        if (globalSettingRepository.findById(2).get().getValue().equals("YES")) {
            settingResponse.setPostPremoderation(true);
        }
        if (globalSettingRepository.findById(3).get().getValue().equals("YES")) {
            settingResponse.setStatisticsIsPublic(true);
        }
        return settingResponse;
    }

    public void putSetting(SettingRequest settingRequest) {

        globalSettingRepository.findAll().forEach(globalSetting -> {
            if (globalSetting.getId() == 1) {
                globalSetting.setValue(booleanToString(settingRequest.isMultiuserMode()));
            }
            if (globalSetting.getId() == 2) {
                globalSetting.setValue(booleanToString(settingRequest.isPostPremoderation()));
            }
            if (globalSetting.getId() == 3) {
                globalSetting.setValue(booleanToString(settingRequest.isStatisticsIsPublic()));
            }
            globalSettingRepository.save(globalSetting);
        });

    }

    private String booleanToString(boolean result) {
        if (result) {
            return "NO";
        } else return "YES";
    }
}
