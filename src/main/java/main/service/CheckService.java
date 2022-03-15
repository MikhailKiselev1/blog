package main.service;

import main.api.response.CheckResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CheckService {
    CheckResponse checkResponse;
    private ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");

    public CheckResponse getCheck() {
        checkResponse = applicationContext.getBean("checkResponse", CheckResponse.class);
        checkResponse.setResult(false);
//        checkResponse.setCheckUserDto(null);
        return checkResponse;
    }
}
