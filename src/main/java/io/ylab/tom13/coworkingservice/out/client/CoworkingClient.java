package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.CoworkingController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.CoworkingControllerImpl;
import io.ylab.tom13.coworkingservice.out.exceptions.CoworkingException;

import java.util.Map;

public class CoworkingClient extends Client {

    private final CoworkingController coworkingController;

    public CoworkingClient() {
        coworkingController = new CoworkingControllerImpl();
    }

    public Map<String, CoworkingDTO> getAllCoworkings() {
        ResponseDTO<Map<String, CoworkingDTO>> AllCoworkings = coworkingController.getAllCoworking();
        return AllCoworkings.data();
    }

    public void createCoworking(CoworkingDTO coworkingDTO) throws CoworkingException {
        ResponseDTO<CoworkingDTO> response = coworkingController.createCoworking(coworkingDTO);
        if (!response.success()) {
            throw new CoworkingException("Не удалось создать коворкинг - " + response.message());
        }
    }

    public CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingException {
        ResponseDTO<CoworkingDTO> responseDTO = coworkingController.updateCoworking(coworkingDTO);
        if (responseDTO.success()) {
            return responseDTO.data();
        } else {
            throw new CoworkingException("Не удалось обновить коворкинг  - " + responseDTO.message());
        }
    }

    public void deleteCoworking(long coworkingId) throws CoworkingException {
        ResponseDTO<Void> responseDTO = coworkingController.deleteCoworking(coworkingId);
        if (!responseDTO.success()) {
            throw new CoworkingException("Не удалось удалить - " + responseDTO.message());
        }
    }
}
