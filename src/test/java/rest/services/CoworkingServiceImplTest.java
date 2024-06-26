package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.CoworkingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса работы с коворкингами")
class CoworkingServiceImplTest {

    @Mock
    private CoworkingRepository coworkingRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CoworkingServiceImpl coworkingService;

    private final AuthenticationDTO authenticationDTO  = new AuthenticationDTO(1L);

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field coworkingServiceField = CoworkingServiceImpl.class.getDeclaredField("coworkingRepository");
        coworkingServiceField.setAccessible(true);
        coworkingServiceField.set(coworkingService, coworkingRepository);

        Field coworkingServiceField2 = CoworkingServiceImpl.class.getDeclaredField("bookingRepository");
        coworkingServiceField2.setAccessible(true);
        coworkingServiceField2.set(coworkingService, bookingRepository);
    }

    @Test
    void testCreateCoworking() throws CoworkingConflictException, RepositoryException, NoAccessException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace", "Description", true, "Office");
        when()

        CoworkingDTO createdCoworking = coworkingService.createCoworking(coworkingDTO,authenticationDTO);

        assertThat(createdCoworking).isNotNull();
        assertThat(createdCoworking.getName()).isEqualTo("Workplace");
    }

    @Test
    void testUpdateWorkplace() throws CoworkingUpdatingExceptions, CoworkingConflictException, CoworkingNotFoundException, NoAccessException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(2L, "Workplace", "Description", true, "Office");
        doReturn(coworkingDTO).when(coworkingRepository).updateCoworking(any(Workplace.class));

        CoworkingDTO updatedCoworking = coworkingService.updateCoworking(coworkingDTO,authenticationDTO);

        assertThat(updatedCoworking).isNotNull();
        assertThat(updatedCoworking.getName()).isEqualTo("Workplace");
    }

    @Test
    void testUpdateConferenceRoom() throws CoworkingUpdatingExceptions, CoworkingConflictException, CoworkingNotFoundException, NoAccessException {
        CoworkingDTO coworkingDTO = new ConferenceRoomDTO(3L, "Conference Room", "Description", true, 10);
        doReturn(coworkingDTO).when(coworkingRepository).updateCoworking(any(ConferenceRoom.class));

        CoworkingDTO updatedCoworking = coworkingService.updateCoworking(coworkingDTO,authenticationDTO);

        assertThat(updatedCoworking).isNotNull();
        assertThat(updatedCoworking.getName()).isEqualTo("Conference Room");
    }


    @Test
    void testDeleteCoworking() throws CoworkingNotFoundException, NoAccessException {
        long coworkingId = 5L;
        coworkingService.deleteBooking(coworkingId,authenticationDTO);
    }
}
