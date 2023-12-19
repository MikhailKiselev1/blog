package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.SettingRequest;
import main.api.response.SettingResponse;
import main.repositories.GlobalSettingRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling application settings.
 */
@Service
@RequiredArgsConstructor
public class SettingService {

    private final GlobalSettingRepository globalSettingRepository;

    /**
     * Retrieves the current application settings.
     *
     * @return SettingResponse containing the current application settings
     */
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

    /**
     * Updates the application settings based on the provided SettingRequest.
     *
     * @param settingRequest the SettingRequest containing updated settings
     */
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

    /**
     * Converts a boolean value to its corresponding "YES" or "NO" string representation.
     *
     * @param result the boolean value to be converted
     * @return "YES" if true, "NO" if false
     */
    private String booleanToString(boolean result) {
        if (result) {
            return "YES";
        } else return "NO";
    }
}
