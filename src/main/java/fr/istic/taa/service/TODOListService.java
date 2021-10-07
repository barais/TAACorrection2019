package fr.istic.taa.service;

import fr.istic.taa.domain.TODOList;
import fr.istic.taa.repository.TODOListRepository;
import fr.istic.taa.service.dto.TODOListDTO;
import fr.istic.taa.service.mapper.TODOListMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TODOList}.
 */
@Service
@Transactional
public class TODOListService {

    private final Logger log = LoggerFactory.getLogger(TODOListService.class);

    private final TODOListRepository tODOListRepository;

    private final TODOListMapper tODOListMapper;

    public TODOListService(TODOListRepository tODOListRepository, TODOListMapper tODOListMapper) {
        this.tODOListRepository = tODOListRepository;
        this.tODOListMapper = tODOListMapper;
    }

    /**
     * Save a tODOList.
     *
     * @param tODOListDTO the entity to save.
     * @return the persisted entity.
     */
    public TODOListDTO save(TODOListDTO tODOListDTO) {
        log.debug("Request to save TODOList : {}", tODOListDTO);
        TODOList tODOList = tODOListMapper.toEntity(tODOListDTO);
        tODOList = tODOListRepository.save(tODOList);
        return tODOListMapper.toDto(tODOList);
    }

    /**
     * Partially update a tODOList.
     *
     * @param tODOListDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TODOListDTO> partialUpdate(TODOListDTO tODOListDTO) {
        log.debug("Request to partially update TODOList : {}", tODOListDTO);

        return tODOListRepository
            .findById(tODOListDTO.getId())
            .map(existingTODOList -> {
                tODOListMapper.partialUpdate(existingTODOList, tODOListDTO);

                return existingTODOList;
            })
            .map(tODOListRepository::save)
            .map(tODOListMapper::toDto);
    }

    /**
     * Get all the tODOLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TODOListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TODOLists");
        return tODOListRepository.findAll(pageable).map(tODOListMapper::toDto);
    }

    /**
     * Get one tODOList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TODOListDTO> findOne(Long id) {
        log.debug("Request to get TODOList : {}", id);
        return tODOListRepository.findById(id).map(tODOListMapper::toDto);
    }

    /**
     * Delete the tODOList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TODOList : {}", id);
        tODOListRepository.deleteById(id);
    }
}
