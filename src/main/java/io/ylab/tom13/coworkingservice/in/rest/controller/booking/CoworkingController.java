package io.ylab.tom13.coworkingservice.in.rest.controller.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;

import java.util.Map;

public interface CoworkingController {

    ResponseDTO<Map<String, CoworkingDTO>> getAllCoworking();
}
