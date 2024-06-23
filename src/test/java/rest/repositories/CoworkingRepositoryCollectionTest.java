package rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.CoworkingRepositoryCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoworkingRepositoryCollectionTest {

    private CoworkingRepository coworkingRepository;

    @BeforeEach
    void setUp() {
        coworkingRepository = CoworkingRepositoryCollection.getInstance();
    }

    @Test
    void testCreateWorkplace() throws CoworkingConflictException {
        WorkplaceDTO workplaceDTO = new WorkplaceDTO(1L, "Workplace1", "Description", true, "Desk");

        CoworkingDTO createdCoworking = coworkingRepository.createCoworking(workplaceDTO);

        assertThat(createdCoworking).isNotNull();
        assertThat(createdCoworking.getName()).isEqualTo("Workplace1");
        assertThat(createdCoworking).isInstanceOf(WorkplaceDTO.class);
    }

    @Test
    void testCreateConferenceRoom() throws CoworkingConflictException {
        ConferenceRoomDTO conferenceRoomDTO = new ConferenceRoomDTO(2L, "ConferenceRoom1", "Description", true, 10);

        CoworkingDTO createdCoworking = coworkingRepository.createCoworking(conferenceRoomDTO);

        assertThat(createdCoworking).isNotNull();
        assertThat(createdCoworking.getName()).isEqualTo("ConferenceRoom1");
        assertThat(createdCoworking).isInstanceOf(ConferenceRoomDTO.class);
    }

    @Test
    void testCreateCoworkingWithDuplicateName() throws CoworkingConflictException {
        WorkplaceDTO existingWorkplace = new WorkplaceDTO(3L, "ExistingWorkplace", "Description", true, "Desk");
        coworkingRepository.createCoworking(existingWorkplace);

        WorkplaceDTO duplicateWorkplace = new WorkplaceDTO(4L, "ExistingWorkplace", "Description", true, "Desk");

        assertThrows(CoworkingConflictException.class, () -> coworkingRepository.createCoworking(duplicateWorkplace));
    }

    @Test
    void testUpdateWorkplace() throws CoworkingConflictException, CoworkingNotFoundException {
        WorkplaceDTO workplaceDTO = new WorkplaceDTO(5L, "WorkplaceToUpdate", "Description", true, "Desk");
        CoworkingDTO coworking = coworkingRepository.createCoworking(workplaceDTO);

        workplaceDTO.setDescription("Updated Description");
        workplaceDTO.setId(coworking.getId());

        CoworkingDTO updatedCoworking = coworkingRepository.updateCoworking(workplaceDTO);

        assertThat(updatedCoworking).isNotNull();
        assertThat(updatedCoworking.getName()).isEqualTo("WorkplaceToUpdate");
        assertThat(updatedCoworking.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void testUpdateNonExistingCoworking() {
        WorkplaceDTO nonExistingCoworking = new WorkplaceDTO(999L, "NonExistingWorkplace", "Description", true, "Desk");

        assertThrows(CoworkingNotFoundException.class, () -> coworkingRepository.updateCoworking(nonExistingCoworking));
    }

    @Test
    void testDeleteWorkplace() throws CoworkingNotFoundException, CoworkingConflictException {
        WorkplaceDTO workplaceDTO = new WorkplaceDTO(6L, "WorkplaceToDelete", "Description", true, "Desk");
        CoworkingDTO coworking = coworkingRepository.createCoworking(workplaceDTO);

        coworkingRepository.deleteCoworking(coworking.getId());

        assertThrows(CoworkingNotFoundException.class, () -> coworkingRepository.updateCoworking(workplaceDTO));
    }

}
