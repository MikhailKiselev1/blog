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
public class CalendarResponse {

    TreeSet<String> years;

    TreeMap<String, Integer> posts;
}
