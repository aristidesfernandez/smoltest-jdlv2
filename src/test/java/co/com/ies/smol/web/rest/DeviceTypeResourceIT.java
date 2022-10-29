package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.DeviceType;
import co.com.ies.smol.repository.DeviceTypeRepository;
import co.com.ies.smol.service.criteria.DeviceTypeCriteria;
import co.com.ies.smol.service.dto.DeviceTypeDTO;
import co.com.ies.smol.service.mapper.DeviceTypeMapper;
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
 * Integration tests for the {@link DeviceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceTypeResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/device-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceTypeMockMvc;

    private DeviceType deviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceType createEntity(EntityManager em) {
        DeviceType deviceType = new DeviceType().description(DEFAULT_DESCRIPTION).name(DEFAULT_NAME);
        return deviceType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceType createUpdatedEntity(EntityManager em) {
        DeviceType deviceType = new DeviceType().description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        return deviceType;
    }

    @BeforeEach
    public void initTest() {
        deviceType = createEntity(em);
    }

    @Test
    @Transactional
    void createDeviceType() throws Exception {
        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().size();
        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);
        restDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeviceType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDeviceTypeWithExistingId() throws Exception {
        // Create the DeviceType with an existing ID
        deviceType.setId(1L);
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeviceTypes() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDeviceType() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get the deviceType
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceType.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getDeviceTypesByIdFiltering() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        Long id = deviceType.getId();

        defaultDeviceTypeShouldBeFound("id.equals=" + id);
        defaultDeviceTypeShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where description equals to DEFAULT_DESCRIPTION
        defaultDeviceTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the deviceTypeList where description equals to UPDATED_DESCRIPTION
        defaultDeviceTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDeviceTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the deviceTypeList where description equals to UPDATED_DESCRIPTION
        defaultDeviceTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where description is not null
        defaultDeviceTypeShouldBeFound("description.specified=true");

        // Get all the deviceTypeList where description is null
        defaultDeviceTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where description contains DEFAULT_DESCRIPTION
        defaultDeviceTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the deviceTypeList where description contains UPDATED_DESCRIPTION
        defaultDeviceTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultDeviceTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the deviceTypeList where description does not contain UPDATED_DESCRIPTION
        defaultDeviceTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where name equals to DEFAULT_NAME
        defaultDeviceTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the deviceTypeList where name equals to UPDATED_NAME
        defaultDeviceTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDeviceTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the deviceTypeList where name equals to UPDATED_NAME
        defaultDeviceTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where name is not null
        defaultDeviceTypeShouldBeFound("name.specified=true");

        // Get all the deviceTypeList where name is null
        defaultDeviceTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where name contains DEFAULT_NAME
        defaultDeviceTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the deviceTypeList where name contains UPDATED_NAME
        defaultDeviceTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeviceTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList where name does not contain DEFAULT_NAME
        defaultDeviceTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the deviceTypeList where name does not contain UPDATED_NAME
        defaultDeviceTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceTypeShouldBeFound(String filter) throws Exception {
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceTypeShouldNotBeFound(String filter) throws Exception {
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeviceType() throws Exception {
        // Get the deviceType
        restDeviceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeviceType() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType
        DeviceType updatedDeviceType = deviceTypeRepository.findById(deviceType.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceType are not directly saved in db
        em.detach(updatedDeviceType);
        updatedDeviceType.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(updatedDeviceType);

        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);

        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);

        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceType() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeDelete = deviceTypeRepository.findAll().size();

        // Delete the deviceType
        restDeviceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
