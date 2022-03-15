package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

@Data
@Component
public class CalendarResponse {

    @JsonProperty("years")
    TreeSet<String> years;

    @JsonProperty("posts")
    TreeMap<String, Integer> posts;
}
