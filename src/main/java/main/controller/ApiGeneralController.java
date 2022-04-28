package main.controller;

import main.api.request.profileRequest.MyProfileRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingResponse globalSettingResponse;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final ProfileService profileService;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, SettingResponse globalSettingResponse,
                                TagService tagService, CalendarService calendarService, ProfileService imageService) {
        this.initResponse = initResponse;
        this.globalSettingResponse = globalSettingResponse;
        this.tagService = tagService;
        this.calendarService = calendarService;
        this.profileService = imageService;
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

//    @PostMapping(value = "/profile/my", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ErrorsResponse profileEditByNameEmail(@RequestBody ProfileEditNameEmailRequest request, Principal principal) {
//        return profileService.profileEditByNameEmail(request, principal);
//    }

    @PostMapping(value = "/profile/my", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ErrorsResponse profileEdit(@RequestBody MyProfileRequest request, Principal principal) throws IOException {
        return profileService.profileEdit(request, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/profile/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ErrorsResponse profileEditMultipart(@RequestBody MyProfileRequest request, Principal principal) throws IOException {
        return profileService.profileEdit(request, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ErrorsResponse imagePost(@RequestParam MultipartFile image) throws IOException {
        System.out.println("postImage controller");
        return profileService.postImage(image);
    }



}
