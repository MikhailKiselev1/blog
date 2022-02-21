package main.controller;

import main.api.response.*;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService globalSettingService;
    private final CheckService checkService;
    private final PostService postsService;
    private final TagService tagService;
    private final CalendarService calendarService;

    public ApiGeneralController(InitResponse initResponse, SettingService globalSettingService,
                                CheckService checkService, PostService postsService, TagService tagService, CalendarService calendarService) {
        this.initResponse = initResponse;
        this.globalSettingService = globalSettingService;
        this.checkService = checkService;
        this.postsService = postsService;
        this.tagService = tagService;
        this.calendarService = calendarService;
    }

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    public SettingResponse setting() {
        return globalSettingService.getInitGlobalSetting();
    }

    @GetMapping("/auth/check")
    public CheckResponse check() {
        return checkService.getCheck();
    }

    @GetMapping("/post")
    public ResponseEntity<PostsResponse> post(@PathVariable @Nullable Integer offset, @PathVariable @Nullable Integer limit, @PathVariable @Nullable String mode) {
        return new ResponseEntity<>(postsService.getPost(offset,limit,mode), HttpStatus.OK);
    }

//    @GetMapping("/post/search")
//    public ResponseEntity<PostsResponse> postSearch(@PathVariable @Nullable Integer offset, @PathVariable @Nullable Integer limit,
//                                    @PathVariable @Nullable String mode, @PathVariable String query) {
//        return new ResponseEntity<>(postsService.getPostSearch(offset,limit,mode, query), HttpStatus.OK);
//    }
//
//    @GetMapping("/post/byDate")
//    public ResponseEntity<PostsResponse> postByDate(@PathVariable @Nullable Integer offset, @PathVariable @Nullable Integer limit,
//                                    @PathVariable @Nullable String mode, @PathVariable String date) {
//        return new ResponseEntity<>(postsService.getPostByDate(offset,limit, mode, date), HttpStatus.OK);
//    }
//
//    @GetMapping("/post/byTag")
//    public ResponseEntity<PostsResponse> postByTag(@PathVariable @Nullable Integer offset, @PathVariable @Nullable Integer limit,
//                                   @PathVariable @Nullable String mode, @PathVariable String tag) {
//        return new ResponseEntity<>(postsService.getPostByTag(offset, limit, mode, tag), HttpStatus.OK);
//    }
//
//    @GetMapping("/post/{ID}")
//    public ResponseEntity<PostIdResponse> postById(@PathVariable Integer id) {
//        return new ResponseEntity<>(postsService.getPostById(id), HttpStatus.NOT_FOUND);
//    }
//
//    @GetMapping("/tag")
//    public TagsResponse tag(@PathVariable String query) {
//        return tagService.getTag(query);
//    }
//
//    @GetMapping("/calendar")
//    public CalendarResponse calendar(@PathVariable String year) {
//        return calendarService.getCalendar(year);
//    }

}
