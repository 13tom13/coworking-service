package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.coworking.implementation.CoworkingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CoworkingServiceImplTest {

    @Mock
    private CoworkingRepository coworkingRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CoworkingServiceImpl coworkingService;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field coworkingServiceField1 = CoworkingServiceImpl.class.getDeclaredField("coworkingRepository");
        coworkingServiceField1.setAccessible(true);
        coworkingServiceField1.set(coworkingService, coworkingRepository);

        Field coworkingServiceField2 = CoworkingServiceImpl.class.getDeclaredField("bookingRepository");
        coworkingServiceField2.setAccessible(true);
        coworkingServiceField2.set(coworkingService, bookingRepository);
    }

    @Test
    void testCreateCoworking() throws CoworkingConflictException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(1L, "Workplace", "Description", true, "Office");
        doReturn(coworkingDTO).when(coworkingRepository).createCoworking(any(WorkplaceDTO.class));

        CoworkingDTO createdCoworking = coworkingService.createCoworking(coworkingDTO);

        assertThat(createdCoworking).isNotNull();
        assertThat(createdCoworking.getName()).isEqualTo("Workplace");
    }

    @Test
    void testUpdateWorkplace() throws CoworkingUpdatingExceptions, CoworkingConflictException, CoworkingNotFoundException {
        CoworkingDTO coworkingDTO = new WorkplaceDTO(2L, "Workplace", "Description", true, "Office");
        doReturn(coworkingDTO).when(coworkingRepository).updateCoworking(any(WorkplaceDTO.class));

        CoworkingDTO updatedCoworking = coworkingService.updateCoworking(coworkingDTO);

        assertThat(updatedCoworking).isNotNull();
        assertThat(updatedCoworking.getName()).isEqualTo("Workplace");
    }

    @Test
    void testUpdateConferenceRoom() throws CoworkingUpdatingExceptions, CoworkingConflictException, CoworkingNotFoundException {
        CoworkingDTO coworkingDTO = new ConferenceRoomDTO(3L, "Conference Room", "Description", true, 10);
        doReturn(coworkingDTO).when(coworkingRepository).updateCoworking(any(ConferenceRoomDTO.class));

        CoworkingDTO updatedCoworking = coworkingService.updateCoworking(coworkingDTO);

        assertThat(updatedCoworking).isNotNull();
        assertThat(updatedCoworking.getName()).isEqualTo("Conference Room");
    }


    @Test
    void testDeleteCoworking() throws CoworkingNotFoundException {
        long coworkingId = 5L;
        coworkingService.deleteBooking(coworkingId);
    }
}
