package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userDTO.firstName", target = "firstName")
    @Mapping(source = "userDTO.lastName", target = "lastName")
    @Mapping(source = "userDTO.email", target = "email")
    @Mapping(target = "password", ignore = true)
    @Mapping(source  = "userDTO.role", target = "role")
    User toUser(UserDTO userDTO, long id);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "userDTO.id", target = "id")
    @Mapping(source = "userDTO.firstName", target = "firstName")
    @Mapping(source = "userDTO.lastName", target = "lastName")
    @Mapping(source = "userDTO.email", target = "email")
    @Mapping(source  = "userDTO.role", target = "role")
    User toUser(UserDTO userDTO, String password);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.role", target = "role")
    User toUser(User user, String password);
}

