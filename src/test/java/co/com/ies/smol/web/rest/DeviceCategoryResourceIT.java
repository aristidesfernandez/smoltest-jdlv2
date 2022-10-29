package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.DeviceCategory;
import co.com.ies.smol.repository.DeviceCategoryRepository;
import co.com.ies.smol.service.criteria.DeviceCategoryCriteria;
import co.com.ies.smol.service.dto.DeviceCategoryDTO;
import co.com.ies.smol.service.mapper.DeviceCategoryMapper;
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
 * Integration tests for the {@link DeviceCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceCategoryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/device-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceCategoryRepository deviceCategoryRepository;

    @Autowired
    private DeviceCategoryMapper deviceCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceCategoryMockMvc;

    private DeviceCategory deviceCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceCategory createEntity(EntityManager em) {
        DeviceCategory deviceCategory = new DeviceCategory().description(DEFAULT_DESCRIPTION).name(DEFAULT_NAME);
        return deviceCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceCategory createUpdatedEntity(EntityManager em) {
        DeviceCategory deviceCategory = new DeviceCategory().description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        return deviceCategory;
    }

    @BeforeEach
    public void initTest() {
        deviceCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createDeviceCategory() throws Exception {
        int databaseSizeBeforeCreate = deviceCategoryRepository.findAll().size();
        // Create the DeviceCategory
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);
        restDeviceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceCategory testDeviceCategory = deviceCategoryList.get(deviceCategoryList.size() - 1);
        assertThat(testDeviceCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeviceCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDeviceCategoryWithExistingId() throws Exception {
        // Create the DeviceCategory with an existing ID
        deviceCategory.setId(1L);
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);

        int databaseSizeBeforeCreate = deviceCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeviceCategories() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList
        restDeviceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDeviceCategory() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get the deviceCategory
        restDeviceCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceCategory.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getDeviceCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        Long id = deviceCategory.getId();

        defaultDeviceCategoryShouldBeFound("id.equals=" + id);
        defaultDeviceCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where description equals to DEFAULT_DESCRIPTION
        defaultDeviceCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the deviceCategoryList where description equals to UPDATED_DESCRIPTION
        defaultDeviceCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDeviceCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the deviceCategoryList where description equals to UPDATED_DESCRIPTION
        defaultDeviceCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where description is not null
        defaultDeviceCategoryShouldBeFound("description.specified=true");

        // Get all the deviceCategoryList where description is null
        defaultDeviceCategoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where description contains DEFAULT_DESCRIPTION
        defaultDeviceCategoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the deviceCategoryList where description contains UPDATED_DESCRIPTION
        defaultDeviceCategoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where description does not contain DEFAULT_DESCRIPTION
        defaultDeviceCategoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the deviceCategoryList where description does not contain UPDATED_DESCRIPTION
        defaultDeviceCategoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where name equals to DEFAULT_NAME
        defaultDeviceCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the deviceCategoryList where name equals to UPDATED_NAME
        defaultDeviceCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDeviceCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the deviceCategoryList where name equals to UPDATED_NAME
        defaultDeviceCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where name is not null
        defaultDeviceCategoryShouldBeFound("name.specified=true");

        // Get all the deviceCategoryList where name is null
        defaultDeviceCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where name contains DEFAULT_NAME
        defaultDeviceCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the deviceCategoryList where name contains UPDATED_NAME
        defaultDeviceCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeviceCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        // Get all the deviceCategoryList where name does not contain DEFAULT_NAME
        defaultDeviceCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the deviceCategoryList where name does not contain UPDATED_NAME
        defaultDeviceCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceCategoryShouldBeFound(String filter) throws Exception {
        restDeviceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restDeviceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceCategoryShouldNotBeFound(String filter) throws Exception {
        restDeviceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeviceCategory() throws Exception {
        // Get the deviceCategory
        restDeviceCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeviceCategory() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();

        // Update the deviceCategory
        DeviceCategory updatedDeviceCategory = deviceCategoryRepository.findById(deviceCategory.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceCategory are not directly saved in db
        em.detach(updatedDeviceCategory);
        updatedDeviceCategory.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(updatedDeviceCategory);

        restDeviceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        DeviceCategory testDeviceCategory = deviceCategoryList.get(deviceCategoryList.size() - 1);
        assertThat(testDeviceCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeviceCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDeviceCategory() throws Exception {
        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();
        deviceCategory.setId(count.incrementAndGet());

        // Create the DeviceCategory
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceCategory() throws Exception {
        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();
        deviceCategory.setId(count.incrementAndGet());

        // Create the DeviceCategory
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceCategory() throws Exception {
        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();
        deviceCategory.setId(count.incrementAndGet());

        // Create the DeviceCategory
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceCategoryWithPatch() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();

        // Update the deviceCategory using partial update
        DeviceCategory partialUpdatedDeviceCategory = new DeviceCategory();
        partialUpdatedDeviceCategory.setId(deviceCategory.getId());

        partialUpdatedDeviceCategory.description(UPDATED_DESCRIPTION);

        restDeviceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceCategory))
            )
            .andExpect(status().isOk());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        DeviceCategory testDeviceCategory = deviceCategoryList.get(deviceCategoryList.size() - 1);
        assertThat(testDeviceCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeviceCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDeviceCategoryWithPatch() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();

        // Update the deviceCategory using partial update
        DeviceCategory partialUpdatedDeviceCategory = new DeviceCategory();
        partialUpdatedDeviceCategory.setId(deviceCategory.getId());

        partialUpdatedDeviceCategory.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);

        restDeviceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceCategory))
            )
            .andExpect(status().isOk());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        DeviceCategory testDeviceCategory = deviceCategoryList.get(deviceCategoryList.size() - 1);
        assertThat(testDeviceCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeviceCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDeviceCategory() throws Exception {
        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();
        deviceCategory.setId(count.incrementAndGet());

        // Create the DeviceCategory
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceCategory() throws Exception {
        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();
        deviceCategory.setId(count.incrementAndGet());

        // Create the DeviceCategory
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceCategory() throws Exception {
        int databaseSizeBeforeUpdate = deviceCategoryRepository.findAll().size();
        deviceCategory.setId(count.incrementAndGet());

        // Create the DeviceCategory
        DeviceCategoryDTO deviceCategoryDTO = deviceCategoryMapper.toDto(deviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceCategory in the database
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceCategory() throws Exception {
        // Initialize the database
        deviceCategoryRepository.saveAndFlush(deviceCategory);

        int databaseSizeBeforeDelete = deviceCategoryRepository.findAll().size();

        // Delete the deviceCategory
        restDeviceCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceCategory> deviceCategoryList = deviceCategoryRepository.findAll();
        assertThat(deviceCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
