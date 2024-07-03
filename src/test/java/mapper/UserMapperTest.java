package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("Тесты маппера для объекта User")
public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private User user;
    private UserDTO userDTO;

    private final long USER_ID = 1L;
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "john.doe@example.com";
    private final Role ROLE = Role.USER;

    @BeforeEach
    void setUp() {
        String PASSWORD = "password";
        user = new User(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE);
        userDTO = new UserDTO(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, ROLE);
    }

    @Test
    @DisplayName("Тест маппинга объекта UserDTO из объекта User")
    void shouldMapUserToUserDTO() {

        UserDTO userDTO = userMapper.toUserDTO(user);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.id()).isEqualTo(USER_ID);
        assertThat(userDTO.firstName()).isEqualTo(FIRST_NAME);
        assertThat(userDTO.lastName()).isEqualTo(LAST_NAME);
        assertThat(userDTO.email()).isEqualTo(EMAIL);
        assertThat(userDTO.role()).isEqualTo(ROLE);
    }

    @Test
    @DisplayName("Тест маппинга объекта User из объекта UserDTO")
    void shouldMapUserDTOToUser() {

        User user = userMapper.toUser(userDTO);

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(USER_ID);
        assertThat(user.firstName()).isEqualTo(FIRST_NAME);
        assertThat(user.lastName()).isEqualTo(LAST_NAME);
        assertThat(user.email()).isEqualTo(EMAIL);
        assertThat(user.role()).isEqualTo(ROLE);
        assertThat(user.password()).isNull();
    }

    @Test
    @DisplayName("Тест маппинга объекта User из объекта UserDTO и вставка нового ID")
    void shouldMapUserDTOToUserWithId() {
        long id = 2L;

        User user = userMapper.toUser(userDTO, id);

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(id);
        assertThat(user.firstName()).isEqualTo(FIRST_NAME);
        assertThat(user.lastName()).isEqualTo(LAST_NAME);
        assertThat(user.email()).isEqualTo(EMAIL);
        assertThat(user.role()).isEqualTo(ROLE);
        assertThat(user.password()).isNull();
    }

    @Test
    @DisplayName("Тест маппинга объекта User из объекта UserDTO и вставка нового пароля")
    void shouldMapUserDTOToUserWithPassword() {
        String newPassword =  "newPassword";

        User user = userMapper.toUserWithPassword(userDTO, newPassword);

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(USER_ID);
        assertThat(user.firstName()).isEqualTo(FIRST_NAME);
        assertThat(user.lastName()).isEqualTo(LAST_NAME);
        assertThat(user.email()).isEqualTo(EMAIL);
        assertThat(user.role()).isEqualTo(ROLE);
        assertThat(user.password()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Тест маппинга объекта User в объект User с новым паролем")
    void shouldMapUserToUserWithPassword() {
        String newPassword = "newPassword";

        User userWithNewPassword = userMapper.toUserWithPassword(user, newPassword);

        assertThat(userWithNewPassword).isNotNull();
        assertThat(userWithNewPassword.id()).isEqualTo(USER_ID);
        assertThat(userWithNewPassword.firstName()).isEqualTo(FIRST_NAME);
        assertThat(userWithNewPassword.lastName()).isEqualTo(LAST_NAME);
        assertThat(userWithNewPassword.email()).isEqualTo(EMAIL);
        assertThat(userWithNewPassword.role()).isEqualTo(ROLE);
        assertThat(userWithNewPassword.password()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Тест маппинга объекта User из объекта UserDTO и вставка нового пароля")
    void shouldMapUserDTOToUserWithPasswordAndEmail() {
        String newPassword =  "newPassword";
        String newEmail =  "newEmail";

        User user = userMapper.toUserWithEmailAndPassword(userDTO, newEmail, newPassword);

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(USER_ID);
        assertThat(user.firstName()).isEqualTo(FIRST_NAME);
        assertThat(user.lastName()).isEqualTo(LAST_NAME);
        assertThat(user.email()).isEqualTo(newEmail);
        assertThat(user.role()).isEqualTo(ROLE);
        assertThat(user.password()).isEqualTo(newPassword);
    }
}

