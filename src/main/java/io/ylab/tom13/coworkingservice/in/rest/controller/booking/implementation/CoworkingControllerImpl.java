package io.ylab.tom13.coworkingservice.in.rest.controller.booking.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.booking.CoworkingController;
import io.ylab.tom13.coworkingservice.in.rest.services.booking.CoworkingService;
import io.ylab.tom13.coworkingservice.in.rest.services.booking.implementation.CoworkingServiceImpl;

import java.util.Map;

public class CoworkingControllerImpl implements CoworkingController {

    private final CoworkingService coworkingService;

    public CoworkingControllerImpl() {
        coworkingService = new CoworkingServiceImpl();
    }

    @Override
    public ResponseDTO<Map<String, CoworkingDTO>> getAllSpaces() {
        Map<String, CoworkingDTO> allSpaces = coworkingService.getAllCoworking();
        return ResponseDTO.success(allSpaces);
    }
}
