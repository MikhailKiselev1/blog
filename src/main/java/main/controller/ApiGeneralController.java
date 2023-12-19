package main.controller;

import lombok.RequiredArgsConstructor;
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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

/**
 * REST Controller managing general functionalities.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final ProfileService profileService;
    private final StatisticService statisticService;
    private final PostCommentsService postCommentsService;
    private final ModerationService moderationService;


    /**
     * Retrieves initialization response.
     *
     * @return Initialization response.
     */
    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }


    /**
     * Retrieves site settings.
     *
     * @return Response containing site settings.
     */
    @GetMapping("/settings")
    public SettingResponse getSetting() {
        return settingService.getSetting();
    }

    /**
     * Updates site settings.
     *
     * @param settingRequest Updated settings.
     */
    @PreAuthorize("hasAuthority('user:moderate')")
    @PutMapping("/settings")
    public void putSetting(@RequestBody SettingRequest settingRequest) {
        settingService.putSetting(settingRequest);
    }


    /**
     * Retrieves tags based on the query.
     *
     * @param query Tag query.
     * @return Response containing tags.
     */
    @GetMapping("/tag")
    public TagsResponse tag(@PathVariable @Nullable String query) {
        return tagService.getTag(query);
    }

    /**
     * Retrieves the calendar for a specific year.
     *
     * @param year Year for the calendar.
     * @return Response containing the calendar.
     */
    @GetMapping("/calendar")
    public CalendarResponse calendar(@RequestParam String year) {
        return calendarService.getCalendar(year);
    }

    /**
     * Edits the user profile with JSON input.
     *
     * @param request    MyProfileRequest containing updated profile information.
     * @param principal  Principal object representing the authenticated user.
     * @return           ErrorsResponse indicating success or failure of the operation.
     * @throws IOException If an I/O error occurs.
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/profile/my", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ErrorsResponse profileEdit(@RequestBody MyProfileRequest request, Principal principal) throws IOException {
        return profileService.profileEdit(request, principal);
    }

    /**
     * Edits the user profile with multipart form data.
     *
     * @param photo          User profile photo as byte array.
     * @param name           Updated user name.
     * @param email          Updated user email.
     * @param password       Updated user password.
     * @param removePhoto    Flag indicating whether to remove the user's profile photo.
     * @param principal      Principal object representing the authenticated user.
     * @param request        HttpServletRequest object.
     * @return               ErrorsResponse indicating success or failure of the operation.
     * @throws IOException   If an I/O error occurs.
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/profile/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ErrorsResponse profileEditMultipart( @RequestPart("photo") byte[] photo,
                                                @RequestParam(value = "name") String name,
                                                @RequestParam(value = "email") String email,
                                                @RequestParam(value = "password", required = false) String password,
                                                @RequestParam(value = "removePhoto", defaultValue = "0") Integer removePhoto,
                                                Principal principal, HttpServletRequest request) throws IOException {
        return profileService.profileImageEdit(photo, name, email, password, removePhoto, principal, request);
    }

//    @PreAuthorize("hasAuthority('user:write')")
//    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ErrorsResponse imagePost(@RequestParam MultipartFile image) throws IOException {
//        System.out.println("postImage controller");
//        return ImageDownloadService.postImage(image);
//    }

    /**
     * Posts an image to the system.
     *
     * @param image   MultipartFile representing the image to be posted.
     * @param request HttpServletRequest object.
     * @param principal Principal object representing the authenticated user.
     * @return        ErrorsResponse indicating success or failure of the operation.
     * @throws IOException If an I/O error occurs.
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ErrorsResponse imagePost(@RequestParam MultipartFile image, HttpServletRequest request, Principal principal) throws IOException {
        return ImageDownloadService.addImage(image, request, principal);
    }

    /**
     * Retrieves the statistic for the current user.
     *
     * @param principal the authenticated user principal
     * @return StatisticResponse containing the user's statistics
     */
    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/statistics/my")
    public StatisticResponse getStatisticMy(Principal principal) {
        return statisticService.getStatisticMy(principal);
    }

    /**
     * Retrieves overall statistics for all users.
     *
     * @return StatisticResponse containing overall statistics
     */
    @GetMapping("/statistics/all")
    public StatisticResponse getStatisticAll() {
        return statisticService.getStatisticAll();
    }

    /**
     * Posts a comment on a post.
     *
     * @param request   the PostCommentRequest containing the comment details
     * @param principal the authenticated user principal
     * @return PostCommentResponse indicating the status of the comment post
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/comment")
    public PostCommentResponse postComment(@RequestBody PostCommentRequest request, Principal principal) {
        return postCommentsService.postComments(request, principal);
    }

    /**
     * Posts a moderation decision on a post.
     *
     * @param request   the ModerationRequest containing the moderation decision details
     * @param principal the authenticated user principal
     * @return ModerationResponse indicating the status of the moderation decision post
     */
    @PreAuthorize("hasAuthority('user:moderate')")
    @PostMapping("/moderation")
    public ModerationResponse postModeration(@RequestBody ModerationRequest request, Principal principal) {
        return moderationService.postModeration(request, principal);
    }

}
