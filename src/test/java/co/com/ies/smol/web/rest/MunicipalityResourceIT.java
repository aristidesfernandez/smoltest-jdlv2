package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.domain.Province;
import co.com.ies.smol.repository.MunicipalityRepository;
import co.com.ies.smol.service.criteria.MunicipalityCriteria;
import co.com.ies.smol.service.dto.MunicipalityDTO;
import co.com.ies.smol.service.mapper.MunicipalityMapper;
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
 * Integration tests for the {@link MunicipalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MunicipalityResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DANE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DANE_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/municipalities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private MunicipalityMapper municipalityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMunicipalityMockMvc;

    private Municipality municipality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipality createEntity(EntityManager em) {
        Municipality municipality = new Municipality().code(DEFAULT_CODE).name(DEFAULT_NAME).daneCode(DEFAULT_DANE_CODE);
        // Add required entity
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            province = ProvinceResourceIT.createEntity(em);
            em.persist(province);
            em.flush();
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        municipality.setProvince(province);
        return municipality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipality createUpdatedEntity(EntityManager em) {
        Municipality municipality = new Municipality().code(UPDATED_CODE).name(UPDATED_NAME).daneCode(UPDATED_DANE_CODE);
        // Add required entity
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            province = ProvinceResourceIT.createUpdatedEntity(em);
            em.persist(province);
            em.flush();
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        municipality.setProvince(province);
        return municipality;
    }

    @BeforeEach
    public void initTest() {
        municipality = createEntity(em);
    }

    @Test
    @Transactional
    void createMunicipality() throws Exception {
        int databaseSizeBeforeCreate = municipalityRepository.findAll().size();
        // Create the Municipality
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);
        restMunicipalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeCreate + 1);
        Municipality testMunicipality = municipalityList.get(municipalityList.size() - 1);
        assertThat(testMunicipality.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMunicipality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMunicipality.getDaneCode()).isEqualTo(DEFAULT_DANE_CODE);
    }

    @Test
    @Transactional
    void createMunicipalityWithExistingId() throws Exception {
        // Create the Municipality with an existing ID
        municipality.setId(1L);
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        int databaseSizeBeforeCreate = municipalityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMunicipalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDaneCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = municipalityRepository.findAll().size();
        // set the field null
        municipality.setDaneCode(null);

        // Create the Municipality, which fails.
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        restMunicipalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isBadRequest());

        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMunicipalities() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList
        restMunicipalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(municipality.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].daneCode").value(hasItem(DEFAULT_DANE_CODE)));
    }

    @Test
    @Transactional
    void getMunicipality() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get the municipality
        restMunicipalityMockMvc
            .perform(get(ENTITY_API_URL_ID, municipality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(municipality.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.daneCode").value(DEFAULT_DANE_CODE));
    }

    @Test
    @Transactional
    void getMunicipalitiesByIdFiltering() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        Long id = municipality.getId();

        defaultMunicipalityShouldBeFound("id.equals=" + id);
        defaultMunicipalityShouldNotBeFound("id.notEquals=" + id);

        defaultMunicipalityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMunicipalityShouldNotBeFound("id.greaterThan=" + id);

        defaultMunicipalityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMunicipalityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where code equals to DEFAULT_CODE
        defaultMunicipalityShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the municipalityList where code equals to UPDATED_CODE
        defaultMunicipalityShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where code in DEFAULT_CODE or UPDATED_CODE
        defaultMunicipalityShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the municipalityList where code equals to UPDATED_CODE
        defaultMunicipalityShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where code is not null
        defaultMunicipalityShouldBeFound("code.specified=true");

        // Get all the municipalityList where code is null
        defaultMunicipalityShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByCodeContainsSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where code contains DEFAULT_CODE
        defaultMunicipalityShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the municipalityList where code contains UPDATED_CODE
        defaultMunicipalityShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where code does not contain DEFAULT_CODE
        defaultMunicipalityShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the municipalityList where code does not contain UPDATED_CODE
        defaultMunicipalityShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where name equals to DEFAULT_NAME
        defaultMunicipalityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the municipalityList where name equals to UPDATED_NAME
        defaultMunicipalityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMunicipalityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the municipalityList where name equals to UPDATED_NAME
        defaultMunicipalityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where name is not null
        defaultMunicipalityShouldBeFound("name.specified=true");

        // Get all the municipalityList where name is null
        defaultMunicipalityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where name contains DEFAULT_NAME
        defaultMunicipalityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the municipalityList where name contains UPDATED_NAME
        defaultMunicipalityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where name does not contain DEFAULT_NAME
        defaultMunicipalityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the municipalityList where name does not contain UPDATED_NAME
        defaultMunicipalityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByDaneCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where daneCode equals to DEFAULT_DANE_CODE
        defaultMunicipalityShouldBeFound("daneCode.equals=" + DEFAULT_DANE_CODE);

        // Get all the municipalityList where daneCode equals to UPDATED_DANE_CODE
        defaultMunicipalityShouldNotBeFound("daneCode.equals=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByDaneCodeIsInShouldWork() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where daneCode in DEFAULT_DANE_CODE or UPDATED_DANE_CODE
        defaultMunicipalityShouldBeFound("daneCode.in=" + DEFAULT_DANE_CODE + "," + UPDATED_DANE_CODE);

        // Get all the municipalityList where daneCode equals to UPDATED_DANE_CODE
        defaultMunicipalityShouldNotBeFound("daneCode.in=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByDaneCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where daneCode is not null
        defaultMunicipalityShouldBeFound("daneCode.specified=true");

        // Get all the municipalityList where daneCode is null
        defaultMunicipalityShouldNotBeFound("daneCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByDaneCodeContainsSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where daneCode contains DEFAULT_DANE_CODE
        defaultMunicipalityShouldBeFound("daneCode.contains=" + DEFAULT_DANE_CODE);

        // Get all the municipalityList where daneCode contains UPDATED_DANE_CODE
        defaultMunicipalityShouldNotBeFound("daneCode.contains=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByDaneCodeNotContainsSomething() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        // Get all the municipalityList where daneCode does not contain DEFAULT_DANE_CODE
        defaultMunicipalityShouldNotBeFound("daneCode.doesNotContain=" + DEFAULT_DANE_CODE);

        // Get all the municipalityList where daneCode does not contain UPDATED_DANE_CODE
        defaultMunicipalityShouldBeFound("daneCode.doesNotContain=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllMunicipalitiesByProvinceIsEqualToSomething() throws Exception {
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            municipalityRepository.saveAndFlush(municipality);
            province = ProvinceResourceIT.createEntity(em);
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        em.persist(province);
        em.flush();
        municipality.setProvince(province);
        municipalityRepository.saveAndFlush(municipality);
        Long provinceId = province.getId();

        // Get all the municipalityList where province equals to provinceId
        defaultMunicipalityShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the municipalityList where province equals to (provinceId + 1)
        defaultMunicipalityShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMunicipalityShouldBeFound(String filter) throws Exception {
        restMunicipalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(municipality.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].daneCode").value(hasItem(DEFAULT_DANE_CODE)));

        // Check, that the count call also returns 1
        restMunicipalityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMunicipalityShouldNotBeFound(String filter) throws Exception {
        restMunicipalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMunicipalityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMunicipality() throws Exception {
        // Get the municipality
        restMunicipalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMunicipality() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();

        // Update the municipality
        Municipality updatedMunicipality = municipalityRepository.findById(municipality.getId()).get();
        // Disconnect from session so that the updates on updatedMunicipality are not directly saved in db
        em.detach(updatedMunicipality);
        updatedMunicipality.code(UPDATED_CODE).name(UPDATED_NAME).daneCode(UPDATED_DANE_CODE);
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(updatedMunicipality);

        restMunicipalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, municipalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
        Municipality testMunicipality = municipalityList.get(municipalityList.size() - 1);
        assertThat(testMunicipality.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMunicipality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMunicipality.getDaneCode()).isEqualTo(UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void putNonExistingMunicipality() throws Exception {
        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();
        municipality.setId(count.incrementAndGet());

        // Create the Municipality
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, municipalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMunicipality() throws Exception {
        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();
        municipality.setId(count.incrementAndGet());

        // Create the Municipality
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMunicipality() throws Exception {
        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();
        municipality.setId(count.incrementAndGet());

        // Create the Municipality
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipalityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMunicipalityWithPatch() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();

        // Update the municipality using partial update
        Municipality partialUpdatedMunicipality = new Municipality();
        partialUpdatedMunicipality.setId(municipality.getId());

        partialUpdatedMunicipality.daneCode(UPDATED_DANE_CODE);

        restMunicipalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMunicipality))
            )
            .andExpect(status().isOk());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
        Municipality testMunicipality = municipalityList.get(municipalityList.size() - 1);
        assertThat(testMunicipality.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMunicipality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMunicipality.getDaneCode()).isEqualTo(UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void fullUpdateMunicipalityWithPatch() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();

        // Update the municipality using partial update
        Municipality partialUpdatedMunicipality = new Municipality();
        partialUpdatedMunicipality.setId(municipality.getId());

        partialUpdatedMunicipality.code(UPDATED_CODE).name(UPDATED_NAME).daneCode(UPDATED_DANE_CODE);

        restMunicipalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMunicipality))
            )
            .andExpect(status().isOk());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
        Municipality testMunicipality = municipalityList.get(municipalityList.size() - 1);
        assertThat(testMunicipality.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMunicipality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMunicipality.getDaneCode()).isEqualTo(UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingMunicipality() throws Exception {
        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();
        municipality.setId(count.incrementAndGet());

        // Create the Municipality
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, municipalityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMunicipality() throws Exception {
        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();
        municipality.setId(count.incrementAndGet());

        // Create the Municipality
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMunicipality() throws Exception {
        int databaseSizeBeforeUpdate = municipalityRepository.findAll().size();
        municipality.setId(count.incrementAndGet());

        // Create the Municipality
        MunicipalityDTO municipalityDTO = municipalityMapper.toDto(municipality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipalityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(municipalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipality in the database
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMunicipality() throws Exception {
        // Initialize the database
        municipalityRepository.saveAndFlush(municipality);

        int databaseSizeBeforeDelete = municipalityRepository.findAll().size();

        // Delete the municipality
        restMunicipalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, municipality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Municipality> municipalityList = municipalityRepository.findAll();
        assertThat(municipalityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
