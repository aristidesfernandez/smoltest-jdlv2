package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Isle;
import co.com.ies.smol.repository.IsleRepository;
import co.com.ies.smol.service.criteria.IsleCriteria;
import co.com.ies.smol.service.dto.IsleDTO;
import co.com.ies.smol.service.mapper.IsleMapper;
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
 * Integration tests for the {@link IsleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IsleResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/isles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IsleRepository isleRepository;

    @Autowired
    private IsleMapper isleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIsleMockMvc;

    private Isle isle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Isle createEntity(EntityManager em) {
        Isle isle = new Isle().description(DEFAULT_DESCRIPTION).name(DEFAULT_NAME);
        return isle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Isle createUpdatedEntity(EntityManager em) {
        Isle isle = new Isle().description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        return isle;
    }

    @BeforeEach
    public void initTest() {
        isle = createEntity(em);
    }

    @Test
    @Transactional
    void createIsle() throws Exception {
        int databaseSizeBeforeCreate = isleRepository.findAll().size();
        // Create the Isle
        IsleDTO isleDTO = isleMapper.toDto(isle);
        restIsleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isleDTO)))
            .andExpect(status().isCreated());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeCreate + 1);
        Isle testIsle = isleList.get(isleList.size() - 1);
        assertThat(testIsle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIsle.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createIsleWithExistingId() throws Exception {
        // Create the Isle with an existing ID
        isle.setId(1L);
        IsleDTO isleDTO = isleMapper.toDto(isle);

        int databaseSizeBeforeCreate = isleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIsleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIsles() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList
        restIsleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isle.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getIsle() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get the isle
        restIsleMockMvc
            .perform(get(ENTITY_API_URL_ID, isle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(isle.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getIslesByIdFiltering() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        Long id = isle.getId();

        defaultIsleShouldBeFound("id.equals=" + id);
        defaultIsleShouldNotBeFound("id.notEquals=" + id);

        defaultIsleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIsleShouldNotBeFound("id.greaterThan=" + id);

        defaultIsleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIsleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIslesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where description equals to DEFAULT_DESCRIPTION
        defaultIsleShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the isleList where description equals to UPDATED_DESCRIPTION
        defaultIsleShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIslesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultIsleShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the isleList where description equals to UPDATED_DESCRIPTION
        defaultIsleShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIslesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where description is not null
        defaultIsleShouldBeFound("description.specified=true");

        // Get all the isleList where description is null
        defaultIsleShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllIslesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where description contains DEFAULT_DESCRIPTION
        defaultIsleShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the isleList where description contains UPDATED_DESCRIPTION
        defaultIsleShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIslesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where description does not contain DEFAULT_DESCRIPTION
        defaultIsleShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the isleList where description does not contain UPDATED_DESCRIPTION
        defaultIsleShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIslesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where name equals to DEFAULT_NAME
        defaultIsleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the isleList where name equals to UPDATED_NAME
        defaultIsleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIslesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultIsleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the isleList where name equals to UPDATED_NAME
        defaultIsleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIslesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where name is not null
        defaultIsleShouldBeFound("name.specified=true");

        // Get all the isleList where name is null
        defaultIsleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllIslesByNameContainsSomething() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where name contains DEFAULT_NAME
        defaultIsleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the isleList where name contains UPDATED_NAME
        defaultIsleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIslesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        // Get all the isleList where name does not contain DEFAULT_NAME
        defaultIsleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the isleList where name does not contain UPDATED_NAME
        defaultIsleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIsleShouldBeFound(String filter) throws Exception {
        restIsleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isle.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restIsleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIsleShouldNotBeFound(String filter) throws Exception {
        restIsleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIsleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIsle() throws Exception {
        // Get the isle
        restIsleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIsle() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        int databaseSizeBeforeUpdate = isleRepository.findAll().size();

        // Update the isle
        Isle updatedIsle = isleRepository.findById(isle.getId()).get();
        // Disconnect from session so that the updates on updatedIsle are not directly saved in db
        em.detach(updatedIsle);
        updatedIsle.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        IsleDTO isleDTO = isleMapper.toDto(updatedIsle);

        restIsleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
        Isle testIsle = isleList.get(isleList.size() - 1);
        assertThat(testIsle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIsle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingIsle() throws Exception {
        int databaseSizeBeforeUpdate = isleRepository.findAll().size();
        isle.setId(count.incrementAndGet());

        // Create the Isle
        IsleDTO isleDTO = isleMapper.toDto(isle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIsle() throws Exception {
        int databaseSizeBeforeUpdate = isleRepository.findAll().size();
        isle.setId(count.incrementAndGet());

        // Create the Isle
        IsleDTO isleDTO = isleMapper.toDto(isle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIsle() throws Exception {
        int databaseSizeBeforeUpdate = isleRepository.findAll().size();
        isle.setId(count.incrementAndGet());

        // Create the Isle
        IsleDTO isleDTO = isleMapper.toDto(isle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIsleWithPatch() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        int databaseSizeBeforeUpdate = isleRepository.findAll().size();

        // Update the isle using partial update
        Isle partialUpdatedIsle = new Isle();
        partialUpdatedIsle.setId(isle.getId());

        partialUpdatedIsle.name(UPDATED_NAME);

        restIsleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsle))
            )
            .andExpect(status().isOk());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
        Isle testIsle = isleList.get(isleList.size() - 1);
        assertThat(testIsle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIsle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateIsleWithPatch() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        int databaseSizeBeforeUpdate = isleRepository.findAll().size();

        // Update the isle using partial update
        Isle partialUpdatedIsle = new Isle();
        partialUpdatedIsle.setId(isle.getId());

        partialUpdatedIsle.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);

        restIsleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsle))
            )
            .andExpect(status().isOk());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
        Isle testIsle = isleList.get(isleList.size() - 1);
        assertThat(testIsle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIsle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingIsle() throws Exception {
        int databaseSizeBeforeUpdate = isleRepository.findAll().size();
        isle.setId(count.incrementAndGet());

        // Create the Isle
        IsleDTO isleDTO = isleMapper.toDto(isle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, isleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIsle() throws Exception {
        int databaseSizeBeforeUpdate = isleRepository.findAll().size();
        isle.setId(count.incrementAndGet());

        // Create the Isle
        IsleDTO isleDTO = isleMapper.toDto(isle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIsle() throws Exception {
        int databaseSizeBeforeUpdate = isleRepository.findAll().size();
        isle.setId(count.incrementAndGet());

        // Create the Isle
        IsleDTO isleDTO = isleMapper.toDto(isle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(isleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Isle in the database
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIsle() throws Exception {
        // Initialize the database
        isleRepository.saveAndFlush(isle);

        int databaseSizeBeforeDelete = isleRepository.findAll().size();

        // Delete the isle
        restIsleMockMvc
            .perform(delete(ENTITY_API_URL_ID, isle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Isle> isleList = isleRepository.findAll();
        assertThat(isleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
