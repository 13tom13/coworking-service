//package rest.controller;
//
//import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
//import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.ConferenceRoomDTO;
//import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
//import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.WorkplaceDTO;
//import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
//import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
//import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingUpdatingExceptions;
//import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
//import io.ylab.tom13.coworkingservice.out.rest.controller.implementation.CoworkingControllerImpl;
//import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import utils.SecurityHTTPControllerTest;
//
//import java.lang.reflect.Field;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Тесты сервиса работы с коворкингами")
//class CoworkingControllerImplTest extends SecurityHTTPControllerTest {
//
//    @Mock
//    private CoworkingService coworkingService;
//
//    @InjectMocks
//    private CoworkingControllerImpl coworkingController;
//
//    @BeforeEach
//    void setUp() throws NoSuchFieldException, IllegalAccessException {
//        Field coworkingControllerField = CoworkingControllerImpl.class.getDeclaredField("coworkingService");
//        coworkingControllerField.setAccessible(true);
//        coworkingControllerField.set(coworkingController, coworkingService);
//    }
//
//    @Test
//    @DisplayName("Тест получения списка коворкингов")
//    void getAllCoworking_success() throws RepositoryException {
//        Map<String, CoworkingDTO> coworkings = Map.of(
//                "1", new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A"),
//                "2", new ConferenceRoomDTO(2L, "Conference Room 1", "Description 2", true, 10)
//        );
//        when(coworkingService.getAllCoworking()).thenReturn(coworkings);
//
//
//        ResponseDTO<Map<String, CoworkingDTO>> response = coworkingController.getAllCoworking();
//
//        assertThat(response.success()).isTrue();
//        assertThat(response.data()).isEqualTo(coworkings);
//        assertThat(response.message()).isNull();
//    }
//
//    @Test
//    @DisplayName("Тест получения списка доступных коворкингов")
//    void getAllAvailableCoworkings_success() throws RepositoryException {
//        Map<String, CoworkingDTO> availableCoworkings = Map.of(
//                "1", new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A"),
//                "2", new ConferenceRoomDTO(2L, "Conference Room 1", "Description 2", true, 10)
//        );
//        when(coworkingService.getAllAvailableCoworkings()).thenReturn(availableCoworkings);
//
//        ResponseDTO<Map<String, CoworkingDTO>> response = coworkingController.getAllAvailableCoworkings();
//
//        assertThat(response.success()).isTrue();
//        assertThat(response.data()).isEqualTo(availableCoworkings);
//        assertThat(response.message()).isNull();
//    }
//
//    @Test
//    @DisplayName("Тест создания коворкинга")
//    void createCoworking_success() throws CoworkingConflictException, RepositoryException {
//        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
//        when(coworkingService.createCoworking(any(CoworkingDTO.class))).thenReturn(coworkingDTO);
//
//        ResponseDTO<CoworkingDTO> response = coworkingController.createCoworking(coworkingDTO);
//
//        assertThat(response.success()).isTrue();
//        assertThat(response.data()).isEqualTo(coworkingDTO);
//        assertThat(response.message()).isNull();
//    }
//
//    @Test
//    @DisplayName("Тест ошибки при создания коворкинга")
//    void createCoworking_conflict() throws CoworkingConflictException, RepositoryException {
//        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
//        when(coworkingService.createCoworking(any(CoworkingDTO.class))).thenThrow(new CoworkingConflictException("Conflict"));
//
//        ResponseDTO<CoworkingDTO> response = coworkingController.createCoworking(coworkingDTO);
//
//        assertThat(response.success()).isFalse();
//        assertThat(response.data()).isNull();
//        assertThat(response.message()).isEqualTo("Conflict");
//    }
//
//    @Test
//    @DisplayName("Тест изменения коворкинга")
//    void updateCoworking_success() throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException, RepositoryException {
//        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
//        when(coworkingService.updateCoworking(any(CoworkingDTO.class))).thenReturn(coworkingDTO);
//
//        ResponseDTO<CoworkingDTO> response = coworkingController.updateCoworking(coworkingDTO);
//
//        assertThat(response.success()).isTrue();
//        assertThat(response.data()).isEqualTo(coworkingDTO);
//        assertThat(response.message()).isNull();
//    }
//
//    @Test
//    @DisplayName("Тест ошибки при изменении коворкинга")
//    void updateCoworking_conflict() throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException, RepositoryException {
//        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace 1", "Description 1", true, "Type A");
//        when(coworkingService.updateCoworking(any(CoworkingDTO.class))).thenThrow(new CoworkingUpdatingExceptions("Conflict"));
//
//        ResponseDTO<CoworkingDTO> response = coworkingController.updateCoworking(coworkingDTO);
//
//        assertThat(response.success()).isFalse();
//        assertThat(response.data()).isNull();
//        assertThat(response.message()).contains("Conflict");
//    }
//
//    @Test
//    @DisplayName("Тест удаления коворкинга")
//    void deleteCoworking_success() throws CoworkingNotFoundException, RepositoryException {
//        doNothing().when(coworkingService).deleteCoworking(1);
//
//        ResponseDTO<Void> response = coworkingController.deleteCoworking(1L);
//
//        assertThat(response.success()).isTrue();
//        assertThat(response.data()).isNull();
//        assertThat(response.message()).isNull();
//    }
//
//    @Test
//    @DisplayName("Тест ошибки при удалении коворкинга")
//    void deleteCoworking_notFound() throws CoworkingNotFoundException, RepositoryException {
//        doThrow(new CoworkingNotFoundException("Not Found")).when(coworkingService).deleteCoworking(1L);
//
//        ResponseDTO<Void> response = coworkingController.deleteCoworking(1L);
//
//        assertThat(response.success()).isFalse();
//        assertThat(response.data()).isNull();
//        assertThat(response.message()).isEqualTo("Not Found");
//    }
//}
//
