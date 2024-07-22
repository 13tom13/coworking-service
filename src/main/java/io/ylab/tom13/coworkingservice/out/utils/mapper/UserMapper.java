package io.ylab.tom13.coworkingservice.out.utils.mapper;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
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
    @Mapping(target = "password", ignore = true)
    User toUser(UserDTO userDTO);

    /**
     * Преобразует объект UserDTO в User с указанным идентификатором.
     *
     * @param userDTO объект UserDTO для преобразования.
     * @param id      идентификатор пользователя.
     * @return соответствующий объект User с заданным идентификатором.
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "id", target = "id")
    User toUser(UserDTO userDTO, long id);

    /**
     * Преобразует объект UserDTO в User с указанным паролем.
     *
     * @param userDTO  объект UserDTO для преобразования.
     * @param password пароль пользователя.
     * @return соответствующий объект User с заданным паролем.
     */
    User toUserWithPassword(UserDTO userDTO, String password);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    User toUserWithEmailAndPassword(UserDTO userDTO, String email, String password);

    /**
     * Преобразует объект User в User с указанным паролем.
     *
     * @param user     объект User для преобразования.
     * @param password пароль пользователя.
     * @return соответствующий объект User с заданным паролем.
     */
    @Mapping(source = "password", target = "password")
    User toUserWithPassword(User user, String password);
}

