package io.ylab.tom13.coworkingservice.in.utils;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    User toUser(UserDTO userDTO, long id);

    User toUser(UserDTO userDTO, String password);

    User toUser(User user, String password);
}

