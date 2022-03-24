package main.service;

import main.api.response.CheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

    CheckResponse checkResponse;

    public CheckResponse getCheck() {
        checkResponse = new CheckResponse();
        checkResponse.setResult(false);
//        checkResponse.setCheckUserDto(null);
        return checkResponse;
    }
}
