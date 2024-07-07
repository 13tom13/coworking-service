package io.ylab.tom13.coworkingservice.out.utils.mapper;


import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegistrationMapper {

    RegistrationMapper INSTANCE = Mappers.getMapper(RegistrationMapper.class);


    RegistrationDTO withNewPassword(RegistrationDTO registrationDTO, String password);
}
