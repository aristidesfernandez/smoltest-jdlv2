package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Country;
import co.com.ies.smol.domain.Province;
import co.com.ies.smol.repository.ProvinceRepository;
import co.com.ies.smol.service.criteria.ProvinceCriteria;
import co.com.ies.smol.service.dto.ProvinceDTO;
import co.com.ies.smol.service.mapper.ProvinceMapper;
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
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProvinceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DANE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DANE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinceMockMvc;

    private Province province;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity(EntityManager em) {
        Province province = new Province().code(DEFAULT_CODE).name(DEFAULT_NAME).daneCode(DEFAULT_DANE_CODE).phoneId(DEFAULT_PHONE_ID);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        province.setCountry(country);
        return province;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity(EntityManager em) {
        Province province = new Province().code(UPDATED_CODE).name(UPDATED_NAME).daneCode(UPDATED_DANE_CODE).phoneId(UPDATED_PHONE_ID);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createUpdatedEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        province.setCountry(country);
        return province;
    }

    @BeforeEach
    public void initTest() {
        province = createEntity(em);
    }

    @Test
    @Transactional
    void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();
        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvince.getDaneCode()).isEqualTo(DEFAULT_DANE_CODE);
        assertThat(testProvince.getPhoneId()).isEqualTo(DEFAULT_PHONE_ID);
    }

    @Test
    @Transactional
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId(1L);
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].daneCode").value(hasItem(DEFAULT_DANE_CODE)))
            .andExpect(jsonPath("$.[*].phoneId").value(hasItem(DEFAULT_PHONE_ID)));
    }

    @Test
    @Transactional
    void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.daneCode").value(DEFAULT_DANE_CODE))
            .andExpect(jsonPath("$.phoneId").value(DEFAULT_PHONE_ID));
    }

    @Test
    @Transactional
    void getProvincesByIdFiltering() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        Long id = province.getId();

        defaultProvinceShouldBeFound("id.equals=" + id);
        defaultProvinceShouldNotBeFound("id.notEquals=" + id);

        defaultProvinceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.greaterThan=" + id);

        defaultProvinceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProvincesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where code equals to DEFAULT_CODE
        defaultProvinceShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the provinceList where code equals to UPDATED_CODE
        defaultProvinceShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProvinceShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the provinceList where code equals to UPDATED_CODE
        defaultProvinceShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where code is not null
        defaultProvinceShouldBeFound("code.specified=true");

        // Get all the provinceList where code is null
        defaultProvinceShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByCodeContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where code contains DEFAULT_CODE
        defaultProvinceShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the provinceList where code contains UPDATED_CODE
        defaultProvinceShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where code does not contain DEFAULT_CODE
        defaultProvinceShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the provinceList where code does not contain UPDATED_CODE
        defaultProvinceShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name equals to DEFAULT_NAME
        defaultProvinceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the provinceList where name equals to UPDATED_NAME
        defaultProvinceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProvinceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the provinceList where name equals to UPDATED_NAME
        defaultProvinceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name is not null
        defaultProvinceShouldBeFound("name.specified=true");

        // Get all the provinceList where name is null
        defaultProvinceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByNameContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name contains DEFAULT_NAME
        defaultProvinceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the provinceList where name contains UPDATED_NAME
        defaultProvinceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name does not contain DEFAULT_NAME
        defaultProvinceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the provinceList where name does not contain UPDATED_NAME
        defaultProvinceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByDaneCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where daneCode equals to DEFAULT_DANE_CODE
        defaultProvinceShouldBeFound("daneCode.equals=" + DEFAULT_DANE_CODE);

        // Get all the provinceList where daneCode equals to UPDATED_DANE_CODE
        defaultProvinceShouldNotBeFound("daneCode.equals=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByDaneCodeIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where daneCode in DEFAULT_DANE_CODE or UPDATED_DANE_CODE
        defaultProvinceShouldBeFound("daneCode.in=" + DEFAULT_DANE_CODE + "," + UPDATED_DANE_CODE);

        // Get all the provinceList where daneCode equals to UPDATED_DANE_CODE
        defaultProvinceShouldNotBeFound("daneCode.in=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByDaneCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where daneCode is not null
        defaultProvinceShouldBeFound("daneCode.specified=true");

        // Get all the provinceList where daneCode is null
        defaultProvinceShouldNotBeFound("daneCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByDaneCodeContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where daneCode contains DEFAULT_DANE_CODE
        defaultProvinceShouldBeFound("daneCode.contains=" + DEFAULT_DANE_CODE);

        // Get all the provinceList where daneCode contains UPDATED_DANE_CODE
        defaultProvinceShouldNotBeFound("daneCode.contains=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByDaneCodeNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where daneCode does not contain DEFAULT_DANE_CODE
        defaultProvinceShouldNotBeFound("daneCode.doesNotContain=" + DEFAULT_DANE_CODE);

        // Get all the provinceList where daneCode does not contain UPDATED_DANE_CODE
        defaultProvinceShouldBeFound("daneCode.doesNotContain=" + UPDATED_DANE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByPhoneIdIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where phoneId equals to DEFAULT_PHONE_ID
        defaultProvinceShouldBeFound("phoneId.equals=" + DEFAULT_PHONE_ID);

        // Get all the provinceList where phoneId equals to UPDATED_PHONE_ID
        defaultProvinceShouldNotBeFound("phoneId.equals=" + UPDATED_PHONE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByPhoneIdIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where phoneId in DEFAULT_PHONE_ID or UPDATED_PHONE_ID
        defaultProvinceShouldBeFound("phoneId.in=" + DEFAULT_PHONE_ID + "," + UPDATED_PHONE_ID);

        // Get all the provinceList where phoneId equals to UPDATED_PHONE_ID
        defaultProvinceShouldNotBeFound("phoneId.in=" + UPDATED_PHONE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByPhoneIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where phoneId is not null
        defaultProvinceShouldBeFound("phoneId.specified=true");

        // Get all the provinceList where phoneId is null
        defaultProvinceShouldNotBeFound("phoneId.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByPhoneIdContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where phoneId contains DEFAULT_PHONE_ID
        defaultProvinceShouldBeFound("phoneId.contains=" + DEFAULT_PHONE_ID);

        // Get all the provinceList where phoneId contains UPDATED_PHONE_ID
        defaultProvinceShouldNotBeFound("phoneId.contains=" + UPDATED_PHONE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByPhoneIdNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where phoneId does not contain DEFAULT_PHONE_ID
        defaultProvinceShouldNotBeFound("phoneId.doesNotContain=" + DEFAULT_PHONE_ID);

        // Get all the provinceList where phoneId does not contain UPDATED_PHONE_ID
        defaultProvinceShouldBeFound("phoneId.doesNotContain=" + UPDATED_PHONE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByCountryIsEqualToSomething() throws Exception {
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            provinceRepository.saveAndFlush(province);
            country = CountryResourceIT.createEntity(em);
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(country);
        em.flush();
        province.setCountry(country);
        provinceRepository.saveAndFlush(province);
        Long countryId = country.getId();

        // Get all the provinceList where country equals to countryId
        defaultProvinceShouldBeFound("countryId.equals=" + countryId);

        // Get all the provinceList where country equals to (countryId + 1)
        defaultProvinceShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProvinceShouldBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].daneCode").value(hasItem(DEFAULT_DANE_CODE)))
            .andExpect(jsonPath("$.[*].phoneId").value(hasItem(DEFAULT_PHONE_ID)));

        // Check, that the count call also returns 1
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProvinceShouldNotBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).get();
        // Disconnect from session so that the updates on updatedProvince are not directly saved in db
        em.detach(updatedProvince);
        updatedProvince.code(UPDATED_CODE).name(UPDATED_NAME).daneCode(UPDATED_DANE_CODE).phoneId(UPDATED_PHONE_ID);
        ProvinceDTO provinceDTO = provinceMapper.toDto(updatedProvince);

        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getDaneCode()).isEqualTo(UPDATED_DANE_CODE);
        assertThat(testProvince.getPhoneId()).isEqualTo(UPDATED_PHONE_ID);
    }

    @Test
    @Transactional
    void putNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.code(UPDATED_CODE).daneCode(UPDATED_DANE_CODE);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvince.getDaneCode()).isEqualTo(UPDATED_DANE_CODE);
        assertThat(testProvince.getPhoneId()).isEqualTo(DEFAULT_PHONE_ID);
    }

    @Test
    @Transactional
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.code(UPDATED_CODE).name(UPDATED_NAME).daneCode(UPDATED_DANE_CODE).phoneId(UPDATED_PHONE_ID);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getDaneCode()).isEqualTo(UPDATED_DANE_CODE);
        assertThat(testProvince.getPhoneId()).isEqualTo(UPDATED_PHONE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Delete the province
        restProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, province.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
