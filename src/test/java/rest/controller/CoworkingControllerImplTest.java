package rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.CoworkingControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.services.CoworkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoworkingControllerImplTest {

    @Mock
    private CoworkingService coworkingService;

    @InjectMocks
    private CoworkingControllerImpl coworkingController;

    private final AuthenticationDTO authenticationDTO  = new AuthenticationDTO(1L);

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field coworkingControllerField = CoworkingControllerImpl.class.getDeclaredField("coworkingService");
        coworkingControllerField.setAccessible(true);
        coworkingControllerField.set(coworkingController, coworkingService);
    }

    @Test
    void getAllCoworking_success() throws NoAccessException {
        Map<String, CoworkingDTO> coworkings = Map.of(
                "1", new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A"),
                "2", new ConferenceRoomDTO(2L, "Conference Room 1", "Description 2", true, 10)
        );
        when(coworkingService.getAllCoworking(any(AuthenticationDTO.class))).thenReturn(coworkings);

        ResponseDTO<Map<String, CoworkingDTO>> response = coworkingController.getAllCoworking(authenticationDTO);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(coworkings);
        assertThat(response.message()).isNull();
    }

    @Test
    void getAllAvailableCoworkings_success() {
        Map<String, CoworkingDTO> availableCoworkings = Map.of(
                "1", new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A"),
                "2", new ConferenceRoomDTO(2L, "Conference Room 1", "Description 2", true, 10)
        );
        when(coworkingService.getAllAvailableCoworkings()).thenReturn(availableCoworkings);

        ResponseDTO<Map<String, CoworkingDTO>> response = coworkingController.getAllAvailableCoworkings();

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(availableCoworkings);
        assertThat(response.message()).isNull();
    }

    @Test
    void createCoworking_success() throws CoworkingConflictException, RepositoryException, NoAccessException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
        when(coworkingService.createCoworking(any(CoworkingDTO.class), any(AuthenticationDTO.class))).thenReturn(coworkingDTO);

        ResponseDTO<CoworkingDTO> response = coworkingController.createCoworking(coworkingDTO,authenticationDTO);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(coworkingDTO);
        assertThat(response.message()).isNull();
    }

    @Test
    void createCoworking_conflict() throws CoworkingConflictException, RepositoryException, NoAccessException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
        when(coworkingService.createCoworking(any(CoworkingDTO.class), any(AuthenticationDTO.class))).thenThrow(new CoworkingConflictException("Conflict"));

        ResponseDTO<CoworkingDTO> response = coworkingController.createCoworking(coworkingDTO,authenticationDTO);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Conflict");
    }

    @Test
    void updateCoworking_success() throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException, NoAccessException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
        when(coworkingService.updateCoworking(any(CoworkingDTO.class), any(AuthenticationDTO.class))).thenReturn(coworkingDTO);

        ResponseDTO<CoworkingDTO> response = coworkingController.updateCoworking(coworkingDTO, authenticationDTO);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(coworkingDTO);
        assertThat(response.message()).isNull();
    }

    @Test
    void updateCoworking_conflict() throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException, NoAccessException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
        when(coworkingService.updateCoworking(any(CoworkingDTO.class), any(AuthenticationDTO.class))).thenThrow(new CoworkingUpdatingExceptions("Conflict"));

        ResponseDTO<CoworkingDTO> response = coworkingController.updateCoworking(coworkingDTO, authenticationDTO);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).contains("Conflict");
    }

    @Test
    void deleteBooking_success() throws CoworkingNotFoundException, NoAccessException {
        doNothing().when(coworkingService).deleteBooking(1,authenticationDTO);

        ResponseDTO<Void> response = coworkingController.deleteBooking(1L,authenticationDTO);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isNull();
    }

    @Test
    void deleteBooking_notFound() throws CoworkingNotFoundException, NoAccessException {
        doThrow(new CoworkingNotFoundException("Not Found")).when(coworkingService).deleteBooking(1L,authenticationDTO);

        ResponseDTO<Void> response = coworkingController.deleteBooking(1L,authenticationDTO);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }
}

