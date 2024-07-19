package io.ylab.tom13.coworkingservice.out.utils.mapper;


import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import org.mapstruct.Mapper;

/**
 * Маппер для преобразования объектов типа RegistrationDTO.
 */
@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    /**
     * Создает новый экземпляр RegistrationDTO с обновленным паролем.
     *
     * @param registrationDTO исходный объект RegistrationDTO
     * @param password        новый пароль
     * @return новый экземпляр RegistrationDTO с обновленным паролем
     */
    RegistrationDTO withNewPassword(RegistrationDTO registrationDTO, String password);
}
