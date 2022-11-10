import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCodes;
import main.repositories.CaptchaRepository;
import main.repositories.UserRepository;
import main.service.CaptchaService;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("classpath:sql/person/insert-person.sql")
@Transactional
public class ApiAuthControllerTest {

    private final static String registerUrl = "/api/auth/register";
    private final static String testEmail = "testtest@test.ru";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private CaptchaService captchaService;
    @MockBean
    private CaptchaRepository captchaRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void deleteAll() {
        userRepository.deleteByEmail(testEmail);
    }

    @Test
    public void profileRegister() throws Exception {

        CaptchaResponse captchaRs = CaptchaResponse.builder()
                .secret("1234")
                .build();

        CaptchaCodes captcha = CaptchaCodes.builder()
                .id(1)
                .time(LocalDateTime.now())
                .code("1234")
                .secretCode("12345")
                .build();

        when(captchaService.getCaptcha()).thenReturn(captchaRs);
        when(captchaRepository.findByCode(anyString())).thenReturn(Optional.ofNullable(captcha));

        String password = "12345678";

        RegisterRequest registerRq = new RegisterRequest();
        registerRq.setName("Тест");
        registerRq.setEmail(testEmail);
        registerRq.setPassword(password);
        registerRq.setCaptcha(captchaService.getCaptcha().getSecret());

        this.mockMvc.perform(post(registerUrl).content(objectMapper.writeValueAsString(registerRq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


}
