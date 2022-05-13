package main.controller;

import main.api.request.ModerationRequest;
import main.api.request.MyProfileRequest;
import main.api.request.PostCommentRequest;
import main.api.request.SettingRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final ProfileService profileService;
    private final StatisticService statisticService;
    private final PostCommentsService postCommentsService;
    private final ModerationService moderationService;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, SettingService settingService,
                                TagService tagService, CalendarService calendarService,
                                ProfileService imageService, StatisticService statisticService, PostCommentsService postCommentsService, ModerationService moderationService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
        this.tagService = tagService;
        this.calendarService = calendarService;
        this.profileService = imageService;
        this.statisticService = statisticService;
        this.postCommentsService = postCommentsService;
        this.moderationService = moderationService;
    }

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }


    @GetMapping("/settings")
    public SettingResponse getSetting() {
        return settingService.getSetting();
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @PutMapping("/settings")
    public void putSetting(@RequestBody SettingRequest settingRequest) {
        settingService.putSetting(settingRequest);
    }


    @GetMapping("/tag")
    public TagsResponse tag(@PathVariable @Nullable String query) {
        return tagService.getTag(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse calendar(@RequestParam String year) {
        return calendarService.getCalendar(year);
    }

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

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/statistics/my")
    public StatisticResponse getStatisticMy(Principal principal) {
        return statisticService.getStatisticMy(principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/statistics/all")
    public StatisticResponse getStatisticAll() {
        return statisticService.getStatisticAll();
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/comment")
    public PostCommentResponse postComment(@RequestBody PostCommentRequest request, Principal principal) {
        return postCommentsService.postComments(request, principal);
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @PostMapping("/moderation")
    public ModerationResponse postModeration(@RequestBody ModerationRequest request, Principal principal) {
        return moderationService.postModeration(request, principal);
    }

}
