package rest.repositories;

import io.ylab.tom13.coworkingservice.out.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.out.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.out.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.implementation.CoworkingRepositoryJdbc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.TestcontainersConnector;

import java.util.Optional;

@DisplayName("Тесты репозитория коворкингов")
class CoworkingRepositoryJdbcTest extends TestcontainersConnector {

    @Autowired
    private CoworkingRepositoryJdbc coworkingRepository;

    private Coworking coworking;


    @BeforeEach
    void setUp() {
        coworking = new Workplace(0L, "Open Space", "Comfortable workplace", true, "Shared");
    }


    @Test
    @DisplayName("Тест создание коворкинга")
    void createCoworking_ShouldCreateCoworking_WhenValidCoworkingProvided() throws RepositoryException, CoworkingConflictException {
        Optional<Coworking> createdCoworking = coworkingRepository.createCoworking(coworking);

        Assertions.assertThat(createdCoworking).isPresent();
        Assertions.assertThat(createdCoworking.get().getId()).isGreaterThan(0);
        Assertions.assertThat(createdCoworking.get().getName()).isEqualTo(coworking.getName());
    }

    @Test
    @DisplayName("Тест нахождения коворкинга по ID")
    void getCoworkingById_ShouldReturnCoworking_WhenCoworkingExists() throws RepositoryException, CoworkingConflictException {
        Coworking createdCoworking = coworkingRepository.createCoworking(coworking).get();

        Optional<Coworking> foundCoworking = coworkingRepository.getCoworkingById(createdCoworking.getId());

        Assertions.assertThat(foundCoworking).isPresent();
        Assertions.assertThat(foundCoworking.get().getId()).isEqualTo(createdCoworking.getId());
    }

    @Test
    @DisplayName("Тест удаление коворкинга")
    void deleteCoworking_ShouldDeleteCoworking_WhenCoworkingExists() throws RepositoryException, CoworkingNotFoundException, CoworkingConflictException {
        Coworking createdCoworking = coworkingRepository.createCoworking(coworking).get();

        coworkingRepository.deleteCoworking(createdCoworking.getId());

        Optional<Coworking> deletedCoworking = coworkingRepository.getCoworkingById(createdCoworking.getId());
        Assertions.assertThat(deletedCoworking).isNotPresent();
    }

    @Test
    @DisplayName("Тест обновление коворкинга")
    void updateCoworking_ShouldUpdateCoworking_WhenCoworkingExists() throws RepositoryException, CoworkingConflictException, CoworkingNotFoundException {
        Coworking createdCoworking = coworkingRepository.createCoworking(coworking).get();

        Coworking updatedCoworking = new ConferenceRoom(createdCoworking.getId(), "Open Space", "Spacious room", true, 20);
        Optional<Coworking> result = coworkingRepository.updateCoworking(updatedCoworking);

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getName()).isEqualTo("Open Space");
        Assertions.assertThat(result.get().getDescription()).isEqualTo("Spacious room");
    }

    @Test
    @DisplayName("Тест создание коворкинга с дублирующим именем")
    void createCoworking_ShouldThrowException_WhenNameIsDuplicate() throws RepositoryException, CoworkingConflictException {
        coworkingRepository.createCoworking(coworking);

        Coworking duplicateCoworking = new Workplace(0L, "Open Space", "Another workplace", true, "Dedicated");

        Assertions.assertThatThrownBy(() -> coworkingRepository.createCoworking(duplicateCoworking))
                .isInstanceOf(CoworkingConflictException.class)
                .hasMessageContaining("Имя коворкинга уже занято");
    }
}
