package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkplaceMapper {
    WorkplaceMapper INSTANCE = Mappers.getMapper(WorkplaceMapper.class);

    default WorkplaceDTO toWorkplaceDTO(Workplace workplace){
        return new WorkplaceDTO(workplace.getId(), workplace.getName(), workplace.getDescription(), workplace.isAvailable(), workplace.getType());
    }


    default Workplace toWorkplace(WorkplaceDTO workplaceDTO){
        return new Workplace(workplaceDTO.getId(), workplaceDTO.getName(), workplaceDTO.getDescription(), workplaceDTO.isAvailable(), workplaceDTO.getType());
    }

    default Workplace toWorkplace(WorkplaceDTO workplaceDTO, long id) {
        if (workplaceDTO == null) {
            return null;
        }
        return new Workplace(id, workplaceDTO.getName(), workplaceDTO.getDescription(), workplaceDTO.isAvailable(), workplaceDTO.getType());
    }
}