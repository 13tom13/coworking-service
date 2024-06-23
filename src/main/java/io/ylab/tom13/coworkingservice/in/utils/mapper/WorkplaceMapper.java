package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkplaceMapper {
    WorkplaceMapper INSTANCE = Mappers.getMapper(WorkplaceMapper.class);

    WorkplaceDTO toWorkplaceDTO(Workplace workplace);

    Workplace toWorkplace(WorkplaceDTO workplaceDTO);

    default Workplace toWorkplace(WorkplaceDTO workplaceDTO, long id) {
        if (workplaceDTO == null) {
            return null;
        }
        return new Workplace(id, workplaceDTO.getName(), workplaceDTO.getDescription(), workplaceDTO.isAvailable(), workplaceDTO.getType());
    }
}