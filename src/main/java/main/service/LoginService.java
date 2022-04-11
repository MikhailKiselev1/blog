package main.service;

import main.api.response.LoginResponse;
import main.api.request.LoginRequest;
import main.api.response.dto.LoginUserDto;
import main.model.User;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginService(UserRepository userRepository, PostRepository postRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse getUser(LoginRequest loginRequest) {
        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();


        main.model.User currentUser =
                userRepository.findByEmail(user.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);

        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setId(currentUser.getId());
        loginUserDto.setName(currentUser.getName());
        loginUserDto.setPhoto(currentUser.getPhoto());
        loginUserDto.setEmail(currentUser.getEmail());
        loginUserDto.setModeration(currentUser.getIsModerator() == 1);
        loginUserDto.setModerationCount(currentUser.getIsModerator() == 1 ? postRepository.getNewPosts().size() : null);
        loginUserDto.setSettings(currentUser.getIsModerator() == 1);
        loginResponse.setUser(loginUserDto);

        return loginResponse;
    }
}
