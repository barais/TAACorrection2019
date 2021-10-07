package fr.istic.taa.service.mapper;

import fr.istic.taa.domain.*;
import fr.istic.taa.service.dto.TODOListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TODOList} and its DTO {@link TODOListDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TODOListMapper extends EntityMapper<TODOListDTO, TODOList> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    TODOListDTO toDto(TODOList s);

    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    TODOListDTO toDtoNom(TODOList tODOList);
}
