package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingResponse;
import main.model.GlobalSetting;
import main.model.enums.GlobalSettingValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingResponse settingResponse;
    private final GlobalSetting globalSetting;

    public ApiGeneralController(InitResponse initResponse, SettingResponse settingResponse, GlobalSetting globalSetting) {
        this.initResponse = initResponse;
        this.settingResponse = settingResponse;
        this.globalSetting = globalSetting;
    }

    @GetMapping("/api/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/api/settings")
    public SettingResponse setting() {
        settingResponse.isMultiuserMode();
        globalSetting.setGlobalSettingValue(GlobalSettingValue.MULTIUSER_MODE);
        return settingResponse;
    }


}
