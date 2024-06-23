package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkplaceMapper {
    WorkplaceMapper INSTANCE = Mappers.getMapper(WorkplaceMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "available", target = "available")
    @Mapping(source = "type", target = "type")
    WorkplaceDTO toWorkplaceDTO(Workplace workplace);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "available", target = "available")
    @Mapping(source = "type", target = "type")
    Workplace toWorkplace(WorkplaceDTO workplaceDTO);

    default Workplace toWorkplace(WorkplaceDTO workplaceDTO, long id) {
        if (workplaceDTO == null) {
            return null;
        }
        return new Workplace(id, workplaceDTO.getName(), workplaceDTO.getDescription(), workplaceDTO.isAvailable(), workplaceDTO.getType());
    }
}