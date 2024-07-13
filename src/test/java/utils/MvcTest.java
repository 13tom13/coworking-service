package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith({MockitoExtension.class})
public abstract class MvcTest {

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    protected final String dummyToken = "dummyToken";

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        lenient().when(jwtUtil.generateJwt(anyLong(), anyString())).thenReturn(dummyToken);
    }
}
