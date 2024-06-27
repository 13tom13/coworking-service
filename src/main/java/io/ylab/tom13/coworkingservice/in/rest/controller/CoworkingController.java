package io.ylab.tom13.coworkingservice.in.rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;

import java.util.Map;

public interface CoworkingController {

    ResponseDTO<Map<String, CoworkingDTO>> getAllCoworking();

    ResponseDTO<Map<String, CoworkingDTO>> getAllAvailableCoworkings();

    ResponseDTO<CoworkingDTO> createCoworking(CoworkingDTO coworkingDTO);

    ResponseDTO<CoworkingDTO> updateCoworking(CoworkingDTO coworkingDTO);

    ResponseDTO<Void> deleteCoworking(long coworkingId);
}
