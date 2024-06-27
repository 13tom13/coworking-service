package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования между объектами Workplace и WorkplaceDTO.
 */
@Mapper
public interface WorkplaceMapper {

    /**
     * Получение экземпляра маппера WorkplaceMapper.
     */
    WorkplaceMapper INSTANCE = Mappers.getMapper(WorkplaceMapper.class);

    /**
     * Преобразует объект Workplace в WorkplaceDTO.
     *
     * @param workplace объект Workplace для преобразования.
     * @return соответствующий объект WorkplaceDTO.
     */
    default WorkplaceDTO toWorkplaceDTO(Workplace workplace) {
        return new WorkplaceDTO(workplace.getId(), workplace.getName(), workplace.getDescription(), workplace.isAvailable(), workplace.getType());
    }

    /**
     * Преобразует объект WorkplaceDTO в Workplace.
     *
     * @param workplaceDTO объект WorkplaceDTO для преобразования.
     * @return соответствующий объект Workplace.
     */
    default Workplace toWorkplace(WorkplaceDTO workplaceDTO) {
        return new Workplace(workplaceDTO.getId(), workplaceDTO.getName(), workplaceDTO.getDescription(), workplaceDTO.isAvailable(), workplaceDTO.getType());
    }

    /**
     * Преобразует объект WorkplaceDTO в Workplace с указанным идентификатором.
     *
     * @param workplaceDTO объект WorkplaceDTO для преобразования.
     * @param id           идентификатор рабочего места.
     * @return соответствующий объект Workplace с заданным идентификатором.
     */
    default Workplace toWorkplace(WorkplaceDTO workplaceDTO, long id) {
        if (workplaceDTO == null) {
            return null;
        }
        return new Workplace(id, workplaceDTO.getName(), workplaceDTO.getDescription(), workplaceDTO.isAvailable(), workplaceDTO.getType());
    }
}
