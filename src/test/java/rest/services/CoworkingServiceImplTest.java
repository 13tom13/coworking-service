package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.CoworkingServiceImpl;
import io.ylab.tom13.coworkingservice.in.utils.mapper.CoworkingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса работы с коворкингами")
class CoworkingServiceImplTest {

    @Mock
    private CoworkingRepository coworkingRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CoworkingServiceImpl coworkingService;

    private CoworkingDTO coworkingDTO;

    private final CoworkingMapper coworkingMapper = CoworkingMapper.INSTANCE;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field coworkingServiceField = CoworkingServiceImpl.class.getDeclaredField("coworkingRepository");
        coworkingServiceField.setAccessible(true);
        coworkingServiceField.set(coworkingService, coworkingRepository);

        Field coworkingServiceField2 = CoworkingServiceImpl.class.getDeclaredField("bookingRepository");
        coworkingServiceField2.setAccessible(true);
        coworkingServiceField2.set(coworkingService, bookingRepository);

        coworkingDTO = new WorkplaceDTO(1L, "Workplace", "Description", true, "Office");
    }

    @Test
    @DisplayName("Тест успешного создания коворкинга")
    void testCreateCoworking() throws CoworkingConflictException, RepositoryException {
        Workplace workplace = coworkingMapper.toWorkplace((WorkplaceDTO) coworkingDTO);

        when(coworkingRepository.createCoworking(any(Coworking.class))).thenReturn(Optional.ofNullable(workplace));

        CoworkingDTO createdCoworking = coworkingService.createCoworking(coworkingDTO);

        assertThat(createdCoworking).isNotNull();
        assertThat(createdCoworking.getName()).isEqualTo(coworkingDTO.getName());
    }

    @Test
    @DisplayName("Тест успешного изменения рабочего места")
    void testUpdateWorkplace() throws CoworkingUpdatingExceptions, CoworkingConflictException, CoworkingNotFoundException, RepositoryException {
        Coworking workplace = coworkingMapper.toWorkplace((WorkplaceDTO) coworkingDTO);

        when(coworkingRepository.updateCoworking(any(Coworking.class))).thenReturn(Optional.ofNullable(workplace));

        CoworkingDTO updatedCoworking = coworkingService.updateCoworking(coworkingDTO);

        assertThat(updatedCoworking).isNotNull();
        assertThat(updatedCoworking.getName()).isEqualTo("Workplace");
    }

    @Test
    @DisplayName("Тест успешного изменения конференц-зала")
    void testUpdateConferenceRoom() throws CoworkingUpdatingExceptions, CoworkingConflictException, CoworkingNotFoundException, RepositoryException {
        coworkingDTO = new ConferenceRoomDTO(3L, "Conference Room", "Description", true, 10);

        ConferenceRoom conferenceRoom = CoworkingMapper.INSTANCE.toConferenceRoom((ConferenceRoomDTO) coworkingDTO);

        doReturn(Optional.ofNullable(conferenceRoom)).when(coworkingRepository).updateCoworking(any(ConferenceRoom.class));

        CoworkingDTO updatedCoworking = coworkingService.updateCoworking(coworkingDTO);

        assertThat(updatedCoworking).isNotNull();
        assertThat(updatedCoworking.getName()).isEqualTo("Conference Room");
    }


    @Test
    @DisplayName("Тест ошибки при попытке удалить коворкинг с несуществующим ID")
    void testDeleteCoworking() throws CoworkingNotFoundException, RepositoryException {
        long coworkingId = 5L;
        doThrow(CoworkingNotFoundException.class).when(coworkingRepository).deleteCoworking(coworkingId);

        assertThrows(CoworkingNotFoundException.class, () -> coworkingService.deleteCoworking(coworkingId));

        verify(coworkingRepository).deleteCoworking(coworkingId);
    }
}
