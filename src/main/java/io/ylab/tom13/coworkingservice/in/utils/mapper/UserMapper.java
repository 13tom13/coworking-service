package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования между объектами User и UserDTO.
 */
@Mapper
public interface UserMapper {

    /**
     * Получение экземпляра маппера UserMapper.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Преобразует объект User в UserDTO.
     *
     * @param user объект User для преобразования.
     * @return соответствующий объект UserDTO.
     */
    UserDTO toUserDTO(User user);

    /**
     * Преобразует объект UserDTO в User.
     *
     * @param userDTO объект UserDTO для преобразования.
     * @return соответствующий объект User.
     */
    User toUser(UserDTO userDTO);

    /**
     * Преобразует объект UserDTO в User с указанным идентификатором.
     *
     * @param userDTO объект UserDTO для преобразования.
     * @param id      идентификатор пользователя.
     * @return соответствующий объект User с заданным идентификатором.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "userDTO.firstName", target = "firstName")
    @Mapping(source = "userDTO.lastName", target = "lastName")
    @Mapping(source = "userDTO.email", target = "email")
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "userDTO.role", target = "role")
    User toUser(UserDTO userDTO, long id);

    /**
     * Преобразует объект UserDTO в User с указанным паролем.
     *
     * @param userDTO  объект UserDTO для преобразования.
     * @param password пароль пользователя.
     * @return соответствующий объект User с заданным паролем.
     */
    @Mapping(source = "password", target = "password")
    @Mapping(source = "userDTO.id", target = "id")
    @Mapping(source = "userDTO.firstName", target = "firstName")
    @Mapping(source = "userDTO.lastName", target = "lastName")
    @Mapping(source = "userDTO.email", target = "email")
    @Mapping(source = "userDTO.role", target = "role")
    User toUser(UserDTO userDTO, String password);

    /**
     * Преобразует объект User в User с указанным паролем.
     *
     * @param user     объект User для преобразования.
     * @param password пароль пользователя.
     * @return соответствующий объект User с заданным паролем.
     */
    @Mapping(source = "password", target = "password")
    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.role", target = "role")
    User toUser(User user, String password);
}

