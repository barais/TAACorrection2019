package fr.istic.taa.web.rest;

import fr.istic.taa.repository.TODOListRepository;
import fr.istic.taa.service.TODOListService;
import fr.istic.taa.service.dto.TODOListDTO;
import fr.istic.taa.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.istic.taa.domain.TODOList}.
 */
@RestController
@RequestMapping("/api")
public class TODOListResource {

    private final Logger log = LoggerFactory.getLogger(TODOListResource.class);

    private static final String ENTITY_NAME = "tODOList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TODOListService tODOListService;

    private final TODOListRepository tODOListRepository;

    public TODOListResource(TODOListService tODOListService, TODOListRepository tODOListRepository) {
        this.tODOListService = tODOListService;
        this.tODOListRepository = tODOListRepository;
    }

    /**
     * {@code POST  /todo-lists} : Create a new tODOList.
     *
     * @param tODOListDTO the tODOListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tODOListDTO, or with status {@code 400 (Bad Request)} if the tODOList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/todo-lists")
    public ResponseEntity<TODOListDTO> createTODOList(@RequestBody TODOListDTO tODOListDTO) throws URISyntaxException {
        log.debug("REST request to save TODOList : {}", tODOListDTO);
        if (tODOListDTO.getId() != null) {
            throw new BadRequestAlertException("A new tODOList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TODOListDTO result = tODOListService.save(tODOListDTO);
        return ResponseEntity
            .created(new URI("/api/todo-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /todo-lists/:id} : Updates an existing tODOList.
     *
     * @param id the id of the tODOListDTO to save.
     * @param tODOListDTO the tODOListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tODOListDTO,
     * or with status {@code 400 (Bad Request)} if the tODOListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tODOListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/todo-lists/{id}")
    public ResponseEntity<TODOListDTO> updateTODOList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TODOListDTO tODOListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TODOList : {}, {}", id, tODOListDTO);
        if (tODOListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tODOListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tODOListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TODOListDTO result = tODOListService.save(tODOListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tODOListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /todo-lists/:id} : Partial updates given fields of an existing tODOList, field will ignore if it is null
     *
     * @param id the id of the tODOListDTO to save.
     * @param tODOListDTO the tODOListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tODOListDTO,
     * or with status {@code 400 (Bad Request)} if the tODOListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tODOListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tODOListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/todo-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TODOListDTO> partialUpdateTODOList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TODOListDTO tODOListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TODOList partially : {}, {}", id, tODOListDTO);
        if (tODOListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tODOListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tODOListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TODOListDTO> result = tODOListService.partialUpdate(tODOListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tODOListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /todo-lists} : get all the tODOLists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tODOLists in body.
     */
    @GetMapping("/todo-lists")
    public ResponseEntity<List<TODOListDTO>> getAllTODOLists(Pageable pageable) {
        log.debug("REST request to get a page of TODOLists");
        Page<TODOListDTO> page = tODOListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /todo-lists/:id} : get the "id" tODOList.
     *
     * @param id the id of the tODOListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tODOListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/todo-lists/{id}")
    public ResponseEntity<TODOListDTO> getTODOList(@PathVariable Long id) {
        log.debug("REST request to get TODOList : {}", id);
        Optional<TODOListDTO> tODOListDTO = tODOListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tODOListDTO);
    }

    /**
     * {@code DELETE  /todo-lists/:id} : delete the "id" tODOList.
     *
     * @param id the id of the tODOListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/todo-lists/{id}")
    public ResponseEntity<Void> deleteTODOList(@PathVariable Long id) {
        log.debug("REST request to delete TODOList : {}", id);
        tODOListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
