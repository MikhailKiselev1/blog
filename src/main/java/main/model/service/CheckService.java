package main.model.service;

import main.api.response.CheckResponse;
import org.springframework.stereotype.Service;

@Service
public class CheckService {
    CheckResponse checkResponse;

    public CheckResponse getCheck() {
        checkResponse.setResult(false);
        checkResponse.setCheckUserResponse(null);
        return checkResponse;
    }
}
