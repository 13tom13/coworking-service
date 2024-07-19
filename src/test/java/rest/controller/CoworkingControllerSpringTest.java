package rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.out.rest.controller.CoworkingControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.MvcTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Тесты сервиса работы с коворкингами")
class CoworkingControllerSpringTest extends MvcTest {

    @Mock
    private CoworkingService coworkingService;

    @InjectMocks
    private CoworkingControllerSpring coworkingController;

    private WorkplaceDTO workplaceDTO;
    private ConferenceRoomDTO conferenceRoomDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(coworkingController).build();
        workplaceDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
        conferenceRoomDTO = new ConferenceRoomDTO(2L, "Conference Room 1", "Description 2", true, 10);
    }

    @Test
    @DisplayName("Тест получения списка коворкингов")
    void getAllCoworking_success() throws Exception {
        Map<String, CoworkingDTO> coworkings = Map.of("1", workplaceDTO, "2", conferenceRoomDTO);
        when(coworkingService.getAllCoworking()).thenReturn(coworkings);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/coworking/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dummyToken)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        Map<String, CoworkingDTO> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertThat(responseMap).isEqualTo(coworkings);
    }

    @Test
    @DisplayName("Тест получения списка доступных коворкингов")
    void getAllAvailableCoworkings_success() throws Exception {
        Map<String, CoworkingDTO> availableCoworkings = Map.of("1", workplaceDTO, "2", conferenceRoomDTO);
        when(coworkingService.getAllAvailableCoworkings()).thenReturn(availableCoworkings);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/coworking/available")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dummyToken)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        Map<String, CoworkingDTO> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertThat(responseMap).isEqualTo(availableCoworkings);
    }

    @Test
    @DisplayName("Тест создания коворкинга")
    void createCoworking_success() throws Exception {
        CoworkingDTO coworkingDTO = workplaceDTO;
        when(coworkingService.createCoworking(any(CoworkingDTO.class))).thenReturn(coworkingDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/coworking/create")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dummyToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coworkingDTO)));

        resultActions.andExpect(status().isCreated());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        CoworkingDTO responseCoworking = objectMapper.readValue(responseBody, CoworkingDTO.class);

        assertThat(responseCoworking).isEqualTo(coworkingDTO);
    }

    @Test
    @DisplayName("Тест обновления коворкинга")
    void updateCoworking_success() throws Exception {
        CoworkingDTO coworkingDTO = workplaceDTO;
        when(coworkingService.updateCoworking(any(CoworkingDTO.class))).thenReturn(coworkingDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/coworking/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dummyToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coworkingDTO)));

        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        CoworkingDTO responseCoworking = objectMapper.readValue(responseBody, CoworkingDTO.class);

        assertThat(responseCoworking).isEqualTo(coworkingDTO);
    }

    @Test
    @DisplayName("Тест удаления коворкинга")
    void deleteCoworking_success() throws Exception {
        doNothing().when(coworkingService).deleteCoworking(anyLong());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/coworking/delete")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dummyToken)
                .param("coworkingId", "1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }
}