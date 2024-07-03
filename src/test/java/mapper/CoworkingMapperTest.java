package mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.utils.mapper.CoworkingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест маппинга наследников класса Coworking")
public class CoworkingMapperTest {

    private final CoworkingMapper coworkingMapper = CoworkingMapper.INSTANCE;

    private ConferenceRoom conferenceRoom;
    private ConferenceRoomDTO conferenceRoomDTO;
    private Workplace workplace;
    private WorkplaceDTO workplaceDTO;

    private final long ID = 1L;
    private final String NAME = "Test Coworking";
    private final String DESCRIPTION = "Description";
    private final boolean AVAILABLE = true;
    private final int CAPACITY = 50;
    private final String TYPE = "Open Space";

    @BeforeEach
    void setUp() {
        conferenceRoom = new ConferenceRoom(ID, NAME, DESCRIPTION, AVAILABLE, CAPACITY);
        conferenceRoomDTO = new ConferenceRoomDTO(ID, NAME, DESCRIPTION, AVAILABLE, CAPACITY);
        workplace = new Workplace(ID, NAME, DESCRIPTION, AVAILABLE, TYPE);
        workplaceDTO = new WorkplaceDTO(ID, NAME, DESCRIPTION, AVAILABLE, TYPE);
    }

    @Test
    @DisplayName("Преобразование ConferenceRoom в ConferenceRoomDTO")
    void shouldMapConferenceRoomToConferenceRoomDTO() {
        ConferenceRoomDTO conferenceRoomDTO = coworkingMapper.toConferenceRoomDTO(conferenceRoom);

        assertThat(conferenceRoomDTO).isNotNull();
        assertThat(conferenceRoomDTO.getId()).isEqualTo(ID);
        assertThat(conferenceRoomDTO.getName()).isEqualTo(NAME);
        assertThat(conferenceRoomDTO.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(conferenceRoomDTO.isAvailable()).isEqualTo(AVAILABLE);
        assertThat(conferenceRoomDTO.getCapacity()).isEqualTo(CAPACITY);
    }

    @Test
    @DisplayName("Преобразование ConferenceRoomDTO в ConferenceRoom")
    void shouldMapConferenceRoomDTOToConferenceRoom() {
        ConferenceRoom conferenceRoom = coworkingMapper.toConferenceRoom(conferenceRoomDTO);

        assertThat(conferenceRoom).isNotNull();
        assertThat(conferenceRoom.getId()).isEqualTo(ID);
        assertThat(conferenceRoom.getName()).isEqualTo(NAME);
        assertThat(conferenceRoom.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(conferenceRoom.isAvailable()).isEqualTo(AVAILABLE);
        assertThat(conferenceRoom.getCapacity()).isEqualTo(CAPACITY);
    }

    @Test
    @DisplayName("Преобразование Workplace в WorkplaceDTO")
    void shouldMapWorkplaceToWorkplaceDTO() {
        WorkplaceDTO workplaceDTO = coworkingMapper.toWorkplaceDTO(workplace);

        assertThat(workplaceDTO).isNotNull();
        assertThat(workplaceDTO.getId()).isEqualTo(ID);
        assertThat(workplaceDTO.getName()).isEqualTo(NAME);
        assertThat(workplaceDTO.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(workplaceDTO.isAvailable()).isEqualTo(AVAILABLE);
        assertThat(workplaceDTO.getType()).isEqualTo(TYPE);
    }

    @Test
    @DisplayName("Преобразование WorkplaceDTO в Workplace")
    void shouldMapWorkplaceDTOToWorkplace() {

        Workplace workplace = coworkingMapper.toWorkplace(workplaceDTO);

        assertThat(workplace).isNotNull();
        assertThat(workplace.getId()).isEqualTo(ID);
        assertThat(workplace.getName()).isEqualTo(NAME);
        assertThat(workplace.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(workplace.isAvailable()).isEqualTo(AVAILABLE);
        assertThat(workplace.getType()).isEqualTo(TYPE);
    }

}
