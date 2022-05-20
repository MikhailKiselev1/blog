package main.api.response;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class InitResponse {

    @Value("${blog.title}")
    private String title;

    @Value("${blog.subtitle}")
    private String subtitle;

    @Value("${blog.phone}")
    private String phone;

    @Value("${blog.email}")
    private String email;

    @Value("${blog.copyright}")
    private String copyright;

    @Value("${blog.copyrightFrom}")
    private String copyrightFrom;
}
