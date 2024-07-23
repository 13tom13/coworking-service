package rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.GlobalExceptionHandler;
import io.ylab.tom13.coworkingservice.out.rest.controller.CoworkingControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.MvcTest;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Тесты сервиса работы с коворкингами")
class CoworkingControllerSpringTest extends MvcTest {

    private static final String COWORKING_ALL = "/coworking/all";
    private static final String COWORKING_AVAILABLE = "/coworking/available";
    private static final String COWORKING_CREATE = "/coworking/create";
    private static final String COWORKING_UPDATE = "/coworking/update";
    private static final String COWORKING_DELETE = "/coworking/delete";
    private static final String COWORKING_ID = "coworkingId";
    @Mock
    private CoworkingService coworkingService;

    @InjectMocks
    private CoworkingControllerSpring coworkingController;

    private WorkplaceDTO workplaceDTO;
    private ConferenceRoomDTO conferenceRoomDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(coworkingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        workplaceDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
        conferenceRoomDTO = new ConferenceRoomDTO(2L, "Conference Room 1", "Description 2", true, 10);
    }

    @Test
    @DisplayName("Тест получения списка коворкингов")
    void getAllCoworking_success() throws Exception {
        Map<String, CoworkingDTO> coworkings = Map.of(workplaceDTO.getName(), workplaceDTO, conferenceRoomDTO.getName(), conferenceRoomDTO);
        when(coworkingService.getAllCoworking()).thenReturn(coworkings);

        mockMvc.perform(get(COWORKING_ALL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(coworkings)));
    }

    @Test
    @DisplayName("Тест получения списка доступных коворкингов")
    void getAllAvailableCoworkings_success() throws Exception {
        Map<String, CoworkingDTO> availableCoworkings = Map.of(workplaceDTO.getName(), workplaceDTO, conferenceRoomDTO.getName(), conferenceRoomDTO);
        when(coworkingService.getAllAvailableCoworkings()).thenReturn(availableCoworkings);

        mockMvc.perform(get(COWORKING_AVAILABLE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(availableCoworkings)));
    }

    @Test
    @DisplayName("Тест создания коворкинга")
    void createCoworking_success() throws Exception {
        when(coworkingService.createCoworking(workplaceDTO)).thenReturn(workplaceDTO);

        mockMvc.perform(post(COWORKING_CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workplaceDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(workplaceDTO)));
    }

    @Test
    @DisplayName("Тест обновления коворкинга")
    void updateCoworking_success() throws Exception {
        when(coworkingService.updateCoworking(workplaceDTO)).thenReturn(workplaceDTO);

        mockMvc.perform(patch(COWORKING_UPDATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workplaceDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(workplaceDTO)));
    }

    @Test
    @DisplayName("Тест удаления коворкинга")
    void deleteCoworking_success() throws Exception {
        mockMvc.perform(delete(COWORKING_DELETE)
                        .param(COWORKING_ID, String.valueOf(workplaceDTO.getId())))
                .andExpect(status().isNoContent());
    }
}