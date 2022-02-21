package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

@Data
public class CalendarResponse {

    @JsonProperty("years")
    TreeSet<String> years;

    @JsonProperty("posts")
    TreeMap<String, Integer> posts;
}
