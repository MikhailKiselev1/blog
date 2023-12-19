package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.LoginResponse;
import main.api.request.LoginRequest;
import main.api.response.dto.LoginUserDto;
import main.model.User;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user login and authentication.
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthenticationManager authenticationManager;


    /**
     * Retrieves a user based on the provided login credentials.
     *
     * @param loginRequest the LoginRequest containing user login information
     * @return LoginResponse containing the result of the login attempt and user information
     */
    public LoginResponse getUser(LoginRequest loginRequest) {

        User currentUser = userRepository.findByEmail(loginRequest.getEmail()).
                orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(String.valueOf(currentUser.getId()),
                                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        return LoginResponse.builder()
                .result(true)
                .user(getLoginUserDto(currentUser))
                .build();
    }

    /**
     * Retrieves user information for the provided user ID.
     *
     * @param id the user ID
     * @return LoginResponse containing the result of the operation and user information
     */
    public LoginResponse getCheck(String id) {

        main.model.User currentUser =
                userRepository.findById(Integer.parseInt(id))
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        return LoginResponse.builder()
                .result(true)
                .user(getLoginUserDto(currentUser))
                .build();
    }

    /**
     * Retrieves a LoginUserDto based on the provided User entity.
     *
     * @param user the User entity
     * @return LoginUserDto containing user information
     */
    private LoginUserDto getLoginUserDto(User user) {
        return LoginUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .photo(user.getPhoto())
                .email(user.getEmail())
                .moderation(user.getIsModerator() == 1)
                .moderationCount(user.getIsModerator() == 1 ?
                        postRepository.getNewPosts().size() : null)
                .settings(user.getIsModerator() == 1)
                .build();
    }
}
