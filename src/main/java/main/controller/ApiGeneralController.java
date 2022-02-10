package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingResponse;
import main.model.service.CheckService;
import main.model.service.GlobalSettingService;
import main.model.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final GlobalSettingService globalSettingService;
    private final CheckService checkService;
    private final PostService postsService;

    public ApiGeneralController(InitResponse initResponse, GlobalSettingService globalSettingService,
                                CheckService checkService, PostService postsService) {
        this.initResponse = initResponse;
        this.globalSettingService = globalSettingService;
        this.checkService = checkService;
        this.postsService = postsService;
    }

    @GetMapping("/api/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/api/settings")
    public SettingResponse setting() {
        return globalSettingService.getInitGlobalSetting();
    }

    @GetMapping("/api/auth/check")
    public CheckService check() {
        return checkService;
    }

    @GetMapping("/api/post")
    public PostService post() {
        return postsService;
    }
}
