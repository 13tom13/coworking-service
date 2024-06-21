package io.ylab.tom13.coworkingservice.in.rest.services.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;

import java.util.Map;

public interface CoworkingService {

    Map<String, CoworkingDTO> getAllCoworking();

}
