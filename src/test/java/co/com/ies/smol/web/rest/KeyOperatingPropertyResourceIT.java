package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.KeyOperatingProperty;
import co.com.ies.smol.repository.KeyOperatingPropertyRepository;
import co.com.ies.smol.service.criteria.KeyOperatingPropertyCriteria;
import co.com.ies.smol.service.dto.KeyOperatingPropertyDTO;
import co.com.ies.smol.service.mapper.KeyOperatingPropertyMapper;
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
 * Integration tests for the {@link KeyOperatingPropertyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KeyOperatingPropertyResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/key-operating-properties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KeyOperatingPropertyRepository keyOperatingPropertyRepository;

    @Autowired
    private KeyOperatingPropertyMapper keyOperatingPropertyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKeyOperatingPropertyMockMvc;

    private KeyOperatingProperty keyOperatingProperty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyOperatingProperty createEntity(EntityManager em) {
        KeyOperatingProperty keyOperatingProperty = new KeyOperatingProperty().description(DEFAULT_DESCRIPTION).property(DEFAULT_PROPERTY);
        return keyOperatingProperty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyOperatingProperty createUpdatedEntity(EntityManager em) {
        KeyOperatingProperty keyOperatingProperty = new KeyOperatingProperty().description(UPDATED_DESCRIPTION).property(UPDATED_PROPERTY);
        return keyOperatingProperty;
    }

    @BeforeEach
    public void initTest() {
        keyOperatingProperty = createEntity(em);
    }

    @Test
    @Transactional
    void createKeyOperatingProperty() throws Exception {
        int databaseSizeBeforeCreate = keyOperatingPropertyRepository.findAll().size();
        // Create the KeyOperatingProperty
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);
        restKeyOperatingPropertyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        KeyOperatingProperty testKeyOperatingProperty = keyOperatingPropertyList.get(keyOperatingPropertyList.size() - 1);
        assertThat(testKeyOperatingProperty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testKeyOperatingProperty.getProperty()).isEqualTo(DEFAULT_PROPERTY);
    }

    @Test
    @Transactional
    void createKeyOperatingPropertyWithExistingId() throws Exception {
        // Create the KeyOperatingProperty with an existing ID
        keyOperatingProperty.setId(1L);
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);

        int databaseSizeBeforeCreate = keyOperatingPropertyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeyOperatingPropertyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKeyOperatingProperties() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList
        restKeyOperatingPropertyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyOperatingProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].property").value(hasItem(DEFAULT_PROPERTY)));
    }

    @Test
    @Transactional
    void getKeyOperatingProperty() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get the keyOperatingProperty
        restKeyOperatingPropertyMockMvc
            .perform(get(ENTITY_API_URL_ID, keyOperatingProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(keyOperatingProperty.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.property").value(DEFAULT_PROPERTY));
    }

    @Test
    @Transactional
    void getKeyOperatingPropertiesByIdFiltering() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        Long id = keyOperatingProperty.getId();

        defaultKeyOperatingPropertyShouldBeFound("id.equals=" + id);
        defaultKeyOperatingPropertyShouldNotBeFound("id.notEquals=" + id);

        defaultKeyOperatingPropertyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultKeyOperatingPropertyShouldNotBeFound("id.greaterThan=" + id);

        defaultKeyOperatingPropertyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultKeyOperatingPropertyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where description equals to DEFAULT_DESCRIPTION
        defaultKeyOperatingPropertyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the keyOperatingPropertyList where description equals to UPDATED_DESCRIPTION
        defaultKeyOperatingPropertyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultKeyOperatingPropertyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the keyOperatingPropertyList where description equals to UPDATED_DESCRIPTION
        defaultKeyOperatingPropertyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where description is not null
        defaultKeyOperatingPropertyShouldBeFound("description.specified=true");

        // Get all the keyOperatingPropertyList where description is null
        defaultKeyOperatingPropertyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where description contains DEFAULT_DESCRIPTION
        defaultKeyOperatingPropertyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the keyOperatingPropertyList where description contains UPDATED_DESCRIPTION
        defaultKeyOperatingPropertyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where description does not contain DEFAULT_DESCRIPTION
        defaultKeyOperatingPropertyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the keyOperatingPropertyList where description does not contain UPDATED_DESCRIPTION
        defaultKeyOperatingPropertyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByPropertyIsEqualToSomething() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where property equals to DEFAULT_PROPERTY
        defaultKeyOperatingPropertyShouldBeFound("property.equals=" + DEFAULT_PROPERTY);

        // Get all the keyOperatingPropertyList where property equals to UPDATED_PROPERTY
        defaultKeyOperatingPropertyShouldNotBeFound("property.equals=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByPropertyIsInShouldWork() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where property in DEFAULT_PROPERTY or UPDATED_PROPERTY
        defaultKeyOperatingPropertyShouldBeFound("property.in=" + DEFAULT_PROPERTY + "," + UPDATED_PROPERTY);

        // Get all the keyOperatingPropertyList where property equals to UPDATED_PROPERTY
        defaultKeyOperatingPropertyShouldNotBeFound("property.in=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByPropertyIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where property is not null
        defaultKeyOperatingPropertyShouldBeFound("property.specified=true");

        // Get all the keyOperatingPropertyList where property is null
        defaultKeyOperatingPropertyShouldNotBeFound("property.specified=false");
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByPropertyContainsSomething() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where property contains DEFAULT_PROPERTY
        defaultKeyOperatingPropertyShouldBeFound("property.contains=" + DEFAULT_PROPERTY);

        // Get all the keyOperatingPropertyList where property contains UPDATED_PROPERTY
        defaultKeyOperatingPropertyShouldNotBeFound("property.contains=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllKeyOperatingPropertiesByPropertyNotContainsSomething() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        // Get all the keyOperatingPropertyList where property does not contain DEFAULT_PROPERTY
        defaultKeyOperatingPropertyShouldNotBeFound("property.doesNotContain=" + DEFAULT_PROPERTY);

        // Get all the keyOperatingPropertyList where property does not contain UPDATED_PROPERTY
        defaultKeyOperatingPropertyShouldBeFound("property.doesNotContain=" + UPDATED_PROPERTY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKeyOperatingPropertyShouldBeFound(String filter) throws Exception {
        restKeyOperatingPropertyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyOperatingProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].property").value(hasItem(DEFAULT_PROPERTY)));

        // Check, that the count call also returns 1
        restKeyOperatingPropertyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKeyOperatingPropertyShouldNotBeFound(String filter) throws Exception {
        restKeyOperatingPropertyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKeyOperatingPropertyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingKeyOperatingProperty() throws Exception {
        // Get the keyOperatingProperty
        restKeyOperatingPropertyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKeyOperatingProperty() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();

        // Update the keyOperatingProperty
        KeyOperatingProperty updatedKeyOperatingProperty = keyOperatingPropertyRepository.findById(keyOperatingProperty.getId()).get();
        // Disconnect from session so that the updates on updatedKeyOperatingProperty are not directly saved in db
        em.detach(updatedKeyOperatingProperty);
        updatedKeyOperatingProperty.description(UPDATED_DESCRIPTION).property(UPDATED_PROPERTY);
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(updatedKeyOperatingProperty);

        restKeyOperatingPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, keyOperatingPropertyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isOk());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
        KeyOperatingProperty testKeyOperatingProperty = keyOperatingPropertyList.get(keyOperatingPropertyList.size() - 1);
        assertThat(testKeyOperatingProperty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testKeyOperatingProperty.getProperty()).isEqualTo(UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void putNonExistingKeyOperatingProperty() throws Exception {
        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();
        keyOperatingProperty.setId(count.incrementAndGet());

        // Create the KeyOperatingProperty
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeyOperatingPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, keyOperatingPropertyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKeyOperatingProperty() throws Exception {
        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();
        keyOperatingProperty.setId(count.incrementAndGet());

        // Create the KeyOperatingProperty
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyOperatingPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKeyOperatingProperty() throws Exception {
        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();
        keyOperatingProperty.setId(count.incrementAndGet());

        // Create the KeyOperatingProperty
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyOperatingPropertyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKeyOperatingPropertyWithPatch() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();

        // Update the keyOperatingProperty using partial update
        KeyOperatingProperty partialUpdatedKeyOperatingProperty = new KeyOperatingProperty();
        partialUpdatedKeyOperatingProperty.setId(keyOperatingProperty.getId());

        restKeyOperatingPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeyOperatingProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKeyOperatingProperty))
            )
            .andExpect(status().isOk());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
        KeyOperatingProperty testKeyOperatingProperty = keyOperatingPropertyList.get(keyOperatingPropertyList.size() - 1);
        assertThat(testKeyOperatingProperty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testKeyOperatingProperty.getProperty()).isEqualTo(DEFAULT_PROPERTY);
    }

    @Test
    @Transactional
    void fullUpdateKeyOperatingPropertyWithPatch() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();

        // Update the keyOperatingProperty using partial update
        KeyOperatingProperty partialUpdatedKeyOperatingProperty = new KeyOperatingProperty();
        partialUpdatedKeyOperatingProperty.setId(keyOperatingProperty.getId());

        partialUpdatedKeyOperatingProperty.description(UPDATED_DESCRIPTION).property(UPDATED_PROPERTY);

        restKeyOperatingPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeyOperatingProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKeyOperatingProperty))
            )
            .andExpect(status().isOk());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
        KeyOperatingProperty testKeyOperatingProperty = keyOperatingPropertyList.get(keyOperatingPropertyList.size() - 1);
        assertThat(testKeyOperatingProperty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testKeyOperatingProperty.getProperty()).isEqualTo(UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void patchNonExistingKeyOperatingProperty() throws Exception {
        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();
        keyOperatingProperty.setId(count.incrementAndGet());

        // Create the KeyOperatingProperty
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeyOperatingPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, keyOperatingPropertyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKeyOperatingProperty() throws Exception {
        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();
        keyOperatingProperty.setId(count.incrementAndGet());

        // Create the KeyOperatingProperty
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyOperatingPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKeyOperatingProperty() throws Exception {
        int databaseSizeBeforeUpdate = keyOperatingPropertyRepository.findAll().size();
        keyOperatingProperty.setId(count.incrementAndGet());

        // Create the KeyOperatingProperty
        KeyOperatingPropertyDTO keyOperatingPropertyDTO = keyOperatingPropertyMapper.toDto(keyOperatingProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyOperatingPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(keyOperatingPropertyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeyOperatingProperty in the database
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKeyOperatingProperty() throws Exception {
        // Initialize the database
        keyOperatingPropertyRepository.saveAndFlush(keyOperatingProperty);

        int databaseSizeBeforeDelete = keyOperatingPropertyRepository.findAll().size();

        // Delete the keyOperatingProperty
        restKeyOperatingPropertyMockMvc
            .perform(delete(ENTITY_API_URL_ID, keyOperatingProperty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KeyOperatingProperty> keyOperatingPropertyList = keyOperatingPropertyRepository.findAll();
        assertThat(keyOperatingPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
