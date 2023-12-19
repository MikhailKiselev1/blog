package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CalendarResponse;
import main.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Service class handling calendar-related operations.
 */
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final PostRepository postRepository;
    private final DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy");
    private final DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * Retrieves calendar data for the specified year.
     *
     * @param year the year for which to retrieve calendar data
     * @return CalendarResponse containing the calendar data
     */
    public CalendarResponse getCalendar(String year) {
        if (year == null) {
            year = LocalDate.now().format(yearFormat);
        }
        TreeSet<String> years = new TreeSet<>();
        TreeMap<String, Integer> postCount = new TreeMap<>();
        String finalYear = year;
        postRepository.getActionCurrentNewPosts().forEach(post -> {
            years.add(post.getTime().format(yearFormat));
            if (post.getTime().format(yearFormat).equals(finalYear)) {
                String key = post.getTime().format(day);
                if (postCount.containsKey(key)) {
                    postCount.put(key, postCount.get(key) + 1);
                } else {
                    postCount.put(key, 1);
                }
            }
        });
        return CalendarResponse.builder()
                .years(years)
                .posts(postCount)
                .build();
    }
}
