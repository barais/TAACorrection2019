package fr.istic.taa.service.mapper;

import fr.istic.taa.domain.*;
import fr.istic.taa.service.dto.TaskDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, TagMapper.class, TODOListMapper.class })
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "affecteduser", source = "affecteduser", qualifiedByName = "login")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "nomSet")
    @Mapping(target = "todolist", source = "todolist", qualifiedByName = "nom")
    TaskDTO toDto(Task s);

    @Mapping(target = "removeTags", ignore = true)
    Task toEntity(TaskDTO taskDTO);
}
