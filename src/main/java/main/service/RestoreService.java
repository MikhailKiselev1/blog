package main.service;

import main.api.request.RestoreRequest;
import main.api.response.RestoreResponse;
import main.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestoreService {

    private final UserRepository userRepository;

    @Autowired
    public RestoreService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RestoreResponse getRestore(RestoreRequest request) {
        RestoreResponse response = new RestoreResponse();
        response.setResult(false);
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            response.setResult(true);
            String hash = RandomStringUtils.randomAlphabetic(15);
            user.setCode(hash);
            userRepository.save(user);
        });
        return response;
    }
}
