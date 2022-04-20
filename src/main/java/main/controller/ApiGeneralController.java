package main.controller;

import main.api.response.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingResponse globalSettingResponse;
    private final TagService tagService;
    private final CalendarService calendarService;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, SettingResponse globalSettingResponse,
                               TagService tagService, CalendarService calendarService) {
        this.initResponse = initResponse;
        this.globalSettingResponse = globalSettingResponse;
        this.tagService = tagService;
        this.calendarService = calendarService;
    }

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }


    @GetMapping("/settings")
    public SettingResponse setting() {
        return globalSettingResponse;
    }


    @GetMapping("/tag")
    public TagsResponse tag(@PathVariable @Nullable String query) {
        return tagService.getTag(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse calendar(@RequestParam String year) {
        return calendarService.getCalendar(year);
    }

//    @PreAuthorize("hasAuthority('user:write')")
//    @PostMapping("image")
//    public ResponseEntity<ErrorsResponse> imagePost()



}
