package fr.istic.taa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.istic.taa.IntegrationTest;
import fr.istic.taa.domain.TODOList;
import fr.istic.taa.repository.TODOListRepository;
import fr.istic.taa.service.dto.TODOListDTO;
import fr.istic.taa.service.mapper.TODOListMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TODOListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TODOListResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/todo-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TODOListRepository tODOListRepository;

    @Autowired
    private TODOListMapper tODOListMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTODOListMockMvc;

    private TODOList tODOList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TODOList createEntity(EntityManager em) {
        TODOList tODOList = new TODOList().nom(DEFAULT_NOM);
        return tODOList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TODOList createUpdatedEntity(EntityManager em) {
        TODOList tODOList = new TODOList().nom(UPDATED_NOM);
        return tODOList;
    }

    @BeforeEach
    public void initTest() {
        tODOList = createEntity(em);
    }

    @Test
    @Transactional
    void createTODOList() throws Exception {
        int databaseSizeBeforeCreate = tODOListRepository.findAll().size();
        // Create the TODOList
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);
        restTODOListMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeCreate + 1);
        TODOList testTODOList = tODOListList.get(tODOListList.size() - 1);
        assertThat(testTODOList.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createTODOListWithExistingId() throws Exception {
        // Create the TODOList with an existing ID
        tODOList.setId(1L);
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);

        int databaseSizeBeforeCreate = tODOListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTODOListMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTODOLists() throws Exception {
        // Initialize the database
        tODOListRepository.saveAndFlush(tODOList);

        // Get all the tODOListList
        restTODOListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tODOList.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getTODOList() throws Exception {
        // Initialize the database
        tODOListRepository.saveAndFlush(tODOList);

        // Get the tODOList
        restTODOListMockMvc
            .perform(get(ENTITY_API_URL_ID, tODOList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tODOList.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingTODOList() throws Exception {
        // Get the tODOList
        restTODOListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTODOList() throws Exception {
        // Initialize the database
        tODOListRepository.saveAndFlush(tODOList);

        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();

        // Update the tODOList
        TODOList updatedTODOList = tODOListRepository.findById(tODOList.getId()).get();
        // Disconnect from session so that the updates on updatedTODOList are not directly saved in db
        em.detach(updatedTODOList);
        updatedTODOList.nom(UPDATED_NOM);
        TODOListDTO tODOListDTO = tODOListMapper.toDto(updatedTODOList);

        restTODOListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tODOListDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isOk());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
        TODOList testTODOList = tODOListList.get(tODOListList.size() - 1);
        assertThat(testTODOList.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingTODOList() throws Exception {
        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();
        tODOList.setId(count.incrementAndGet());

        // Create the TODOList
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTODOListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tODOListDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTODOList() throws Exception {
        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();
        tODOList.setId(count.incrementAndGet());

        // Create the TODOList
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTODOListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTODOList() throws Exception {
        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();
        tODOList.setId(count.incrementAndGet());

        // Create the TODOList
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTODOListMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTODOListWithPatch() throws Exception {
        // Initialize the database
        tODOListRepository.saveAndFlush(tODOList);

        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();

        // Update the tODOList using partial update
        TODOList partialUpdatedTODOList = new TODOList();
        partialUpdatedTODOList.setId(tODOList.getId());

        partialUpdatedTODOList.nom(UPDATED_NOM);

        restTODOListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTODOList.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTODOList))
            )
            .andExpect(status().isOk());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
        TODOList testTODOList = tODOListList.get(tODOListList.size() - 1);
        assertThat(testTODOList.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateTODOListWithPatch() throws Exception {
        // Initialize the database
        tODOListRepository.saveAndFlush(tODOList);

        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();

        // Update the tODOList using partial update
        TODOList partialUpdatedTODOList = new TODOList();
        partialUpdatedTODOList.setId(tODOList.getId());

        partialUpdatedTODOList.nom(UPDATED_NOM);

        restTODOListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTODOList.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTODOList))
            )
            .andExpect(status().isOk());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
        TODOList testTODOList = tODOListList.get(tODOListList.size() - 1);
        assertThat(testTODOList.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingTODOList() throws Exception {
        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();
        tODOList.setId(count.incrementAndGet());

        // Create the TODOList
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTODOListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tODOListDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTODOList() throws Exception {
        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();
        tODOList.setId(count.incrementAndGet());

        // Create the TODOList
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTODOListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTODOList() throws Exception {
        int databaseSizeBeforeUpdate = tODOListRepository.findAll().size();
        tODOList.setId(count.incrementAndGet());

        // Create the TODOList
        TODOListDTO tODOListDTO = tODOListMapper.toDto(tODOList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTODOListMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tODOListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TODOList in the database
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTODOList() throws Exception {
        // Initialize the database
        tODOListRepository.saveAndFlush(tODOList);

        int databaseSizeBeforeDelete = tODOListRepository.findAll().size();

        // Delete the tODOList
        restTODOListMockMvc
            .perform(delete(ENTITY_API_URL_ID, tODOList.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TODOList> tODOListList = tODOListRepository.findAll();
        assertThat(tODOListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
