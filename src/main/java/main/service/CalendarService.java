package main.service;

import main.api.response.CalendarResponse;
import main.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;
import java.util.TreeSet;

@Service
public class CalendarService {

    @Autowired
    private PostRepository postRepository;
    private CalendarResponse calendarResponse;
    private final DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy");
    private final DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public CalendarResponse getCalendar(String year) {
        if (year == null) {
            year = LocalDate.now().format(yearFormat);
        }
        calendarResponse = new CalendarResponse();
        TreeSet<String> years = new TreeSet<>();
        TreeMap<String, Integer> postCount = new TreeMap<>();
        String finalYear = year;
        postRepository.getActionCurrentNewPosts().forEach(post -> {
            years.add(post.getTime().format(yearFormat));
            if(post.getTime().format(yearFormat).equals(finalYear)) {
                String key = post.getTime().format(day);
                if (postCount.containsKey(key)) {
                    postCount.put(key, postCount.get(key) + 1);
                } else {
                    postCount.put(key, 1);
                }
            }
        });
        calendarResponse.setYears(years);
        calendarResponse.setPosts(postCount);
        return calendarResponse;
    }
}
