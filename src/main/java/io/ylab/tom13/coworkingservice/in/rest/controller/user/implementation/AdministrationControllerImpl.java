package io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.AdministrationController;
import io.ylab.tom13.coworkingservice.in.rest.services.user.AdministrationService;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.AdministrationServiceImpl;

import java.util.List;
import java.util.Map;

public class AdministrationControllerImpl implements AdministrationController {

    private final AdministrationService administrationService;

    public AdministrationControllerImpl() {
        administrationService = new AdministrationServiceImpl();
    }

    @Override
    public ResponseDTO<List<UserDTO>> getAllUsers(AuthenticationDTO authenticationDTO) {
        try {
            List<UserDTO> allUsers = administrationService.getAllUsers(authenticationDTO);
            return ResponseDTO.success(allUsers);
        } catch (NoAccessException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
