package main.controller;

import main.api.response.CheckResponse;
import main.api.response.InitResponse;
import main.api.response.SettingResponse;
import main.model.service.GlobalSettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final GlobalSettingService globalSettingService;

    public ApiGeneralController(InitResponse initResponse, GlobalSettingService globalSettingService
                ) {
        this.initResponse = initResponse;
        this.globalSettingService = globalSettingService;
    }

    @GetMapping("/api/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/api/settings")
    public SettingResponse setting() {
        return globalSettingService.getInitGlobalSetting();
    }

//    @GetMapping("/api/auth/check")
//    public CheckResponse check() {
//        return checkResponse;
//    }

}
