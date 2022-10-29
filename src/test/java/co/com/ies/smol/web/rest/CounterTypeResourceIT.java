package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.CounterType;
import co.com.ies.smol.repository.CounterTypeRepository;
import co.com.ies.smol.service.criteria.CounterTypeCriteria;
import co.com.ies.smol.service.dto.CounterTypeDTO;
import co.com.ies.smol.service.mapper.CounterTypeMapper;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link CounterTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CounterTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INCLUDED_IN_FORMULA = false;
    private static final Boolean UPDATED_INCLUDED_IN_FORMULA = true;

    private static final Boolean DEFAULT_PRIZE = false;
    private static final Boolean UPDATED_PRIZE = true;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Integer DEFAULT_UDTE_WAIT_TIME = 1;
    private static final Integer UPDATED_UDTE_WAIT_TIME = 2;
    private static final Integer SMALLER_UDTE_WAIT_TIME = 1 - 1;

    private static final String ENTITY_API_URL = "/api/counter-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{counterCode}";

    @Autowired
    private CounterTypeRepository counterTypeRepository;

    @Autowired
    private CounterTypeMapper counterTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCounterTypeMockMvc;

    private CounterType counterType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterType createEntity(EntityManager em) {
        CounterType counterType = new CounterType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .includedInFormula(DEFAULT_INCLUDED_IN_FORMULA)
            .prize(DEFAULT_PRIZE)
            .category(DEFAULT_CATEGORY)
            .udteWaitTime(DEFAULT_UDTE_WAIT_TIME);
        return counterType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterType createUpdatedEntity(EntityManager em) {
        CounterType counterType = new CounterType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .includedInFormula(UPDATED_INCLUDED_IN_FORMULA)
            .prize(UPDATED_PRIZE)
            .category(UPDATED_CATEGORY)
            .udteWaitTime(UPDATED_UDTE_WAIT_TIME);
        return counterType;
    }

    @BeforeEach
    public void initTest() {
        counterType = createEntity(em);
    }

    @Test
    @Transactional
    void createCounterType() throws Exception {
        int databaseSizeBeforeCreate = counterTypeRepository.findAll().size();
        // Create the CounterType
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);
        restCounterTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CounterType testCounterType = counterTypeList.get(counterTypeList.size() - 1);
        assertThat(testCounterType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCounterType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCounterType.getIncludedInFormula()).isEqualTo(DEFAULT_INCLUDED_IN_FORMULA);
        assertThat(testCounterType.getPrize()).isEqualTo(DEFAULT_PRIZE);
        assertThat(testCounterType.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testCounterType.getUdteWaitTime()).isEqualTo(DEFAULT_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void createCounterTypeWithExistingId() throws Exception {
        // Create the CounterType with an existing ID
        counterType.setCounterCode("existing_id");
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);

        int databaseSizeBeforeCreate = counterTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounterTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCounterTypes() throws Exception {
        // Initialize the database
        counterType.setCounterCode(UUID.randomUUID().toString());
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList
        restCounterTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=counterCode,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].counterCode").value(hasItem(counterType.getCounterCode())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].includedInFormula").value(hasItem(DEFAULT_INCLUDED_IN_FORMULA.booleanValue())))
            .andExpect(jsonPath("$.[*].prize").value(hasItem(DEFAULT_PRIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].udteWaitTime").value(hasItem(DEFAULT_UDTE_WAIT_TIME)));
    }

    @Test
    @Transactional
    void getCounterType() throws Exception {
        // Initialize the database
        counterType.setCounterCode(UUID.randomUUID().toString());
        counterTypeRepository.saveAndFlush(counterType);

        // Get the counterType
        restCounterTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, counterType.getCounterCode()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.counterCode").value(counterType.getCounterCode()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.includedInFormula").value(DEFAULT_INCLUDED_IN_FORMULA.booleanValue()))
            .andExpect(jsonPath("$.prize").value(DEFAULT_PRIZE.booleanValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.udteWaitTime").value(DEFAULT_UDTE_WAIT_TIME));
    }

    @Test
    @Transactional
    void getCounterTypesByIdFiltering() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        String id = counterType.getCounterCode();

        defaultCounterTypeShouldBeFound("counterCode.equals=" + id);
        defaultCounterTypeShouldNotBeFound("counterCode.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCounterTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where name equals to DEFAULT_NAME
        defaultCounterTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the counterTypeList where name equals to UPDATED_NAME
        defaultCounterTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCounterTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the counterTypeList where name equals to UPDATED_NAME
        defaultCounterTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where name is not null
        defaultCounterTypeShouldBeFound("name.specified=true");

        // Get all the counterTypeList where name is null
        defaultCounterTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where name contains DEFAULT_NAME
        defaultCounterTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the counterTypeList where name contains UPDATED_NAME
        defaultCounterTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where name does not contain DEFAULT_NAME
        defaultCounterTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the counterTypeList where name does not contain UPDATED_NAME
        defaultCounterTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where description equals to DEFAULT_DESCRIPTION
        defaultCounterTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the counterTypeList where description equals to UPDATED_DESCRIPTION
        defaultCounterTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCounterTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCounterTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the counterTypeList where description equals to UPDATED_DESCRIPTION
        defaultCounterTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCounterTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where description is not null
        defaultCounterTypeShouldBeFound("description.specified=true");

        // Get all the counterTypeList where description is null
        defaultCounterTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where description contains DEFAULT_DESCRIPTION
        defaultCounterTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the counterTypeList where description contains UPDATED_DESCRIPTION
        defaultCounterTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCounterTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultCounterTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the counterTypeList where description does not contain UPDATED_DESCRIPTION
        defaultCounterTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCounterTypesByIncludedInFormulaIsEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where includedInFormula equals to DEFAULT_INCLUDED_IN_FORMULA
        defaultCounterTypeShouldBeFound("includedInFormula.equals=" + DEFAULT_INCLUDED_IN_FORMULA);

        // Get all the counterTypeList where includedInFormula equals to UPDATED_INCLUDED_IN_FORMULA
        defaultCounterTypeShouldNotBeFound("includedInFormula.equals=" + UPDATED_INCLUDED_IN_FORMULA);
    }

    @Test
    @Transactional
    void getAllCounterTypesByIncludedInFormulaIsInShouldWork() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where includedInFormula in DEFAULT_INCLUDED_IN_FORMULA or UPDATED_INCLUDED_IN_FORMULA
        defaultCounterTypeShouldBeFound("includedInFormula.in=" + DEFAULT_INCLUDED_IN_FORMULA + "," + UPDATED_INCLUDED_IN_FORMULA);

        // Get all the counterTypeList where includedInFormula equals to UPDATED_INCLUDED_IN_FORMULA
        defaultCounterTypeShouldNotBeFound("includedInFormula.in=" + UPDATED_INCLUDED_IN_FORMULA);
    }

    @Test
    @Transactional
    void getAllCounterTypesByIncludedInFormulaIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where includedInFormula is not null
        defaultCounterTypeShouldBeFound("includedInFormula.specified=true");

        // Get all the counterTypeList where includedInFormula is null
        defaultCounterTypeShouldNotBeFound("includedInFormula.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterTypesByPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where prize equals to DEFAULT_PRIZE
        defaultCounterTypeShouldBeFound("prize.equals=" + DEFAULT_PRIZE);

        // Get all the counterTypeList where prize equals to UPDATED_PRIZE
        defaultCounterTypeShouldNotBeFound("prize.equals=" + UPDATED_PRIZE);
    }

    @Test
    @Transactional
    void getAllCounterTypesByPrizeIsInShouldWork() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where prize in DEFAULT_PRIZE or UPDATED_PRIZE
        defaultCounterTypeShouldBeFound("prize.in=" + DEFAULT_PRIZE + "," + UPDATED_PRIZE);

        // Get all the counterTypeList where prize equals to UPDATED_PRIZE
        defaultCounterTypeShouldNotBeFound("prize.in=" + UPDATED_PRIZE);
    }

    @Test
    @Transactional
    void getAllCounterTypesByPrizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where prize is not null
        defaultCounterTypeShouldBeFound("prize.specified=true");

        // Get all the counterTypeList where prize is null
        defaultCounterTypeShouldNotBeFound("prize.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterTypesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where category equals to DEFAULT_CATEGORY
        defaultCounterTypeShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the counterTypeList where category equals to UPDATED_CATEGORY
        defaultCounterTypeShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCounterTypesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultCounterTypeShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the counterTypeList where category equals to UPDATED_CATEGORY
        defaultCounterTypeShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCounterTypesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where category is not null
        defaultCounterTypeShouldBeFound("category.specified=true");

        // Get all the counterTypeList where category is null
        defaultCounterTypeShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterTypesByCategoryContainsSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where category contains DEFAULT_CATEGORY
        defaultCounterTypeShouldBeFound("category.contains=" + DEFAULT_CATEGORY);

        // Get all the counterTypeList where category contains UPDATED_CATEGORY
        defaultCounterTypeShouldNotBeFound("category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCounterTypesByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where category does not contain DEFAULT_CATEGORY
        defaultCounterTypeShouldNotBeFound("category.doesNotContain=" + DEFAULT_CATEGORY);

        // Get all the counterTypeList where category does not contain UPDATED_CATEGORY
        defaultCounterTypeShouldBeFound("category.doesNotContain=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCounterTypesByUdteWaitTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where udteWaitTime equals to DEFAULT_UDTE_WAIT_TIME
        defaultCounterTypeShouldBeFound("udteWaitTime.equals=" + DEFAULT_UDTE_WAIT_TIME);

        // Get all the counterTypeList where udteWaitTime equals to UPDATED_UDTE_WAIT_TIME
        defaultCounterTypeShouldNotBeFound("udteWaitTime.equals=" + UPDATED_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByUdteWaitTimeIsInShouldWork() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where udteWaitTime in DEFAULT_UDTE_WAIT_TIME or UPDATED_UDTE_WAIT_TIME
        defaultCounterTypeShouldBeFound("udteWaitTime.in=" + DEFAULT_UDTE_WAIT_TIME + "," + UPDATED_UDTE_WAIT_TIME);

        // Get all the counterTypeList where udteWaitTime equals to UPDATED_UDTE_WAIT_TIME
        defaultCounterTypeShouldNotBeFound("udteWaitTime.in=" + UPDATED_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByUdteWaitTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where udteWaitTime is not null
        defaultCounterTypeShouldBeFound("udteWaitTime.specified=true");

        // Get all the counterTypeList where udteWaitTime is null
        defaultCounterTypeShouldNotBeFound("udteWaitTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterTypesByUdteWaitTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where udteWaitTime is greater than or equal to DEFAULT_UDTE_WAIT_TIME
        defaultCounterTypeShouldBeFound("udteWaitTime.greaterThanOrEqual=" + DEFAULT_UDTE_WAIT_TIME);

        // Get all the counterTypeList where udteWaitTime is greater than or equal to UPDATED_UDTE_WAIT_TIME
        defaultCounterTypeShouldNotBeFound("udteWaitTime.greaterThanOrEqual=" + UPDATED_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByUdteWaitTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where udteWaitTime is less than or equal to DEFAULT_UDTE_WAIT_TIME
        defaultCounterTypeShouldBeFound("udteWaitTime.lessThanOrEqual=" + DEFAULT_UDTE_WAIT_TIME);

        // Get all the counterTypeList where udteWaitTime is less than or equal to SMALLER_UDTE_WAIT_TIME
        defaultCounterTypeShouldNotBeFound("udteWaitTime.lessThanOrEqual=" + SMALLER_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByUdteWaitTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where udteWaitTime is less than DEFAULT_UDTE_WAIT_TIME
        defaultCounterTypeShouldNotBeFound("udteWaitTime.lessThan=" + DEFAULT_UDTE_WAIT_TIME);

        // Get all the counterTypeList where udteWaitTime is less than UPDATED_UDTE_WAIT_TIME
        defaultCounterTypeShouldBeFound("udteWaitTime.lessThan=" + UPDATED_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void getAllCounterTypesByUdteWaitTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        counterTypeRepository.saveAndFlush(counterType);

        // Get all the counterTypeList where udteWaitTime is greater than DEFAULT_UDTE_WAIT_TIME
        defaultCounterTypeShouldNotBeFound("udteWaitTime.greaterThan=" + DEFAULT_UDTE_WAIT_TIME);

        // Get all the counterTypeList where udteWaitTime is greater than SMALLER_UDTE_WAIT_TIME
        defaultCounterTypeShouldBeFound("udteWaitTime.greaterThan=" + SMALLER_UDTE_WAIT_TIME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCounterTypeShouldBeFound(String filter) throws Exception {
        restCounterTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=counterCode,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].counterCode").value(hasItem(counterType.getCounterCode())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].includedInFormula").value(hasItem(DEFAULT_INCLUDED_IN_FORMULA.booleanValue())))
            .andExpect(jsonPath("$.[*].prize").value(hasItem(DEFAULT_PRIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].udteWaitTime").value(hasItem(DEFAULT_UDTE_WAIT_TIME)));

        // Check, that the count call also returns 1
        restCounterTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=counterCode,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCounterTypeShouldNotBeFound(String filter) throws Exception {
        restCounterTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=counterCode,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCounterTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=counterCode,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCounterType() throws Exception {
        // Get the counterType
        restCounterTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCounterType() throws Exception {
        // Initialize the database
        counterType.setCounterCode(UUID.randomUUID().toString());
        counterTypeRepository.saveAndFlush(counterType);

        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();

        // Update the counterType
        CounterType updatedCounterType = counterTypeRepository.findById(counterType.getCounterCode()).get();
        // Disconnect from session so that the updates on updatedCounterType are not directly saved in db
        em.detach(updatedCounterType);
        updatedCounterType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .includedInFormula(UPDATED_INCLUDED_IN_FORMULA)
            .prize(UPDATED_PRIZE)
            .category(UPDATED_CATEGORY)
            .udteWaitTime(UPDATED_UDTE_WAIT_TIME);
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(updatedCounterType);

        restCounterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterTypeDTO.getCounterCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterType testCounterType = counterTypeList.get(counterTypeList.size() - 1);
        assertThat(testCounterType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCounterType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCounterType.getIncludedInFormula()).isEqualTo(UPDATED_INCLUDED_IN_FORMULA);
        assertThat(testCounterType.getPrize()).isEqualTo(UPDATED_PRIZE);
        assertThat(testCounterType.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCounterType.getUdteWaitTime()).isEqualTo(UPDATED_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void putNonExistingCounterType() throws Exception {
        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();
        counterType.setCounterCode(UUID.randomUUID().toString());

        // Create the CounterType
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterTypeDTO.getCounterCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounterType() throws Exception {
        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();
        counterType.setCounterCode(UUID.randomUUID().toString());

        // Create the CounterType
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounterType() throws Exception {
        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();
        counterType.setCounterCode(UUID.randomUUID().toString());

        // Create the CounterType
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCounterTypeWithPatch() throws Exception {
        // Initialize the database
        counterType.setCounterCode(UUID.randomUUID().toString());
        counterTypeRepository.saveAndFlush(counterType);

        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();

        // Update the counterType using partial update
        CounterType partialUpdatedCounterType = new CounterType();
        partialUpdatedCounterType.setCounterCode(counterType.getCounterCode());

        partialUpdatedCounterType
            .description(UPDATED_DESCRIPTION)
            .includedInFormula(UPDATED_INCLUDED_IN_FORMULA)
            .category(UPDATED_CATEGORY);

        restCounterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterType.getCounterCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterType))
            )
            .andExpect(status().isOk());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterType testCounterType = counterTypeList.get(counterTypeList.size() - 1);
        assertThat(testCounterType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCounterType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCounterType.getIncludedInFormula()).isEqualTo(UPDATED_INCLUDED_IN_FORMULA);
        assertThat(testCounterType.getPrize()).isEqualTo(DEFAULT_PRIZE);
        assertThat(testCounterType.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCounterType.getUdteWaitTime()).isEqualTo(DEFAULT_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void fullUpdateCounterTypeWithPatch() throws Exception {
        // Initialize the database
        counterType.setCounterCode(UUID.randomUUID().toString());
        counterTypeRepository.saveAndFlush(counterType);

        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();

        // Update the counterType using partial update
        CounterType partialUpdatedCounterType = new CounterType();
        partialUpdatedCounterType.setCounterCode(counterType.getCounterCode());

        partialUpdatedCounterType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .includedInFormula(UPDATED_INCLUDED_IN_FORMULA)
            .prize(UPDATED_PRIZE)
            .category(UPDATED_CATEGORY)
            .udteWaitTime(UPDATED_UDTE_WAIT_TIME);

        restCounterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterType.getCounterCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterType))
            )
            .andExpect(status().isOk());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterType testCounterType = counterTypeList.get(counterTypeList.size() - 1);
        assertThat(testCounterType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCounterType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCounterType.getIncludedInFormula()).isEqualTo(UPDATED_INCLUDED_IN_FORMULA);
        assertThat(testCounterType.getPrize()).isEqualTo(UPDATED_PRIZE);
        assertThat(testCounterType.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCounterType.getUdteWaitTime()).isEqualTo(UPDATED_UDTE_WAIT_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingCounterType() throws Exception {
        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();
        counterType.setCounterCode(UUID.randomUUID().toString());

        // Create the CounterType
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, counterTypeDTO.getCounterCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounterType() throws Exception {
        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();
        counterType.setCounterCode(UUID.randomUUID().toString());

        // Create the CounterType
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounterType() throws Exception {
        int databaseSizeBeforeUpdate = counterTypeRepository.findAll().size();
        counterType.setCounterCode(UUID.randomUUID().toString());

        // Create the CounterType
        CounterTypeDTO counterTypeDTO = counterTypeMapper.toDto(counterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(counterTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterType in the database
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCounterType() throws Exception {
        // Initialize the database
        counterType.setCounterCode(UUID.randomUUID().toString());
        counterTypeRepository.saveAndFlush(counterType);

        int databaseSizeBeforeDelete = counterTypeRepository.findAll().size();

        // Delete the counterType
        restCounterTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, counterType.getCounterCode()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CounterType> counterTypeList = counterTypeRepository.findAll();
        assertThat(counterTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
