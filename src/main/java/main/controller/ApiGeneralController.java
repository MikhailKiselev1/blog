package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingResponse settingResponse;

    public ApiGeneralController(InitResponse initResponse, SettingResponse settingResponse) {
        this.initResponse = initResponse;
        this.settingResponse = settingResponse;
    }

    @GetMapping("/api/init")
    public InitResponse init() {
        return initResponse;
    }

//    @GetMapping("/api/settings")
//    public SettingResponse setting() {
//        settingResponse.isMultiuserMode();
//        globalSetting.setGlobalSettingValue(GlobalSettingValue.MULTIUSER_MODE);
//        return settingResponse;
//    }


}
