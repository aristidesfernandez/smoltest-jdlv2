package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Formula;
import co.com.ies.smol.domain.Manufacturer;
import co.com.ies.smol.domain.Model;
import co.com.ies.smol.repository.ModelRepository;
import co.com.ies.smol.service.criteria.ModelCriteria;
import co.com.ies.smol.service.dto.ModelDTO;
import co.com.ies.smol.service.mapper.ModelMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModelResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUBTRACT_BONUS = false;
    private static final Boolean UPDATED_SUBTRACT_BONUS = true;

    private static final BigDecimal DEFAULT_COLLECTION_CEIL = new BigDecimal(1);
    private static final BigDecimal UPDATED_COLLECTION_CEIL = new BigDecimal(2);
    private static final BigDecimal SMALLER_COLLECTION_CEIL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ROLLOVER_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ROLLOVER_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ROLLOVER_LIMIT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModelMockMvc;

    private Model model;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createEntity(EntityManager em) {
        Model model = new Model()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .subtractBonus(DEFAULT_SUBTRACT_BONUS)
            .collectionCeil(DEFAULT_COLLECTION_CEIL)
            .rolloverLimit(DEFAULT_ROLLOVER_LIMIT);
        // Add required entity
        Manufacturer manufacturer;
        if (TestUtil.findAll(em, Manufacturer.class).isEmpty()) {
            manufacturer = ManufacturerResourceIT.createEntity(em);
            em.persist(manufacturer);
            em.flush();
        } else {
            manufacturer = TestUtil.findAll(em, Manufacturer.class).get(0);
        }
        model.setManufacturer(manufacturer);
        return model;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createUpdatedEntity(EntityManager em) {
        Model model = new Model()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .subtractBonus(UPDATED_SUBTRACT_BONUS)
            .collectionCeil(UPDATED_COLLECTION_CEIL)
            .rolloverLimit(UPDATED_ROLLOVER_LIMIT);
        // Add required entity
        Manufacturer manufacturer;
        if (TestUtil.findAll(em, Manufacturer.class).isEmpty()) {
            manufacturer = ManufacturerResourceIT.createUpdatedEntity(em);
            em.persist(manufacturer);
            em.flush();
        } else {
            manufacturer = TestUtil.findAll(em, Manufacturer.class).get(0);
        }
        model.setManufacturer(manufacturer);
        return model;
    }

    @BeforeEach
    public void initTest() {
        model = createEntity(em);
    }

    @Test
    @Transactional
    void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();
        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);
        restModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modelDTO)))
            .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModel.getSubtractBonus()).isEqualTo(DEFAULT_SUBTRACT_BONUS);
        assertThat(testModel.getCollectionCeil()).isEqualByComparingTo(DEFAULT_COLLECTION_CEIL);
        assertThat(testModel.getRolloverLimit()).isEqualByComparingTo(DEFAULT_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void createModelWithExistingId() throws Exception {
        // Create the Model with an existing ID
        model.setId(1L);
        ModelDTO modelDTO = modelMapper.toDto(model);

        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().size();
        // set the field null
        model.setCode(null);

        // Create the Model, which fails.
        ModelDTO modelDTO = modelMapper.toDto(model);

        restModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modelDTO)))
            .andExpect(status().isBadRequest());

        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].subtractBonus").value(hasItem(DEFAULT_SUBTRACT_BONUS.booleanValue())))
            .andExpect(jsonPath("$.[*].collectionCeil").value(hasItem(sameNumber(DEFAULT_COLLECTION_CEIL))))
            .andExpect(jsonPath("$.[*].rolloverLimit").value(hasItem(sameNumber(DEFAULT_ROLLOVER_LIMIT))));
    }

    @Test
    @Transactional
    void getModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get the model
        restModelMockMvc
            .perform(get(ENTITY_API_URL_ID, model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(model.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.subtractBonus").value(DEFAULT_SUBTRACT_BONUS.booleanValue()))
            .andExpect(jsonPath("$.collectionCeil").value(sameNumber(DEFAULT_COLLECTION_CEIL)))
            .andExpect(jsonPath("$.rolloverLimit").value(sameNumber(DEFAULT_ROLLOVER_LIMIT)));
    }

    @Test
    @Transactional
    void getModelsByIdFiltering() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        Long id = model.getId();

        defaultModelShouldBeFound("id.equals=" + id);
        defaultModelShouldNotBeFound("id.notEquals=" + id);

        defaultModelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultModelShouldNotBeFound("id.greaterThan=" + id);

        defaultModelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultModelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllModelsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where code equals to DEFAULT_CODE
        defaultModelShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the modelList where code equals to UPDATED_CODE
        defaultModelShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllModelsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where code in DEFAULT_CODE or UPDATED_CODE
        defaultModelShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the modelList where code equals to UPDATED_CODE
        defaultModelShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllModelsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where code is not null
        defaultModelShouldBeFound("code.specified=true");

        // Get all the modelList where code is null
        defaultModelShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllModelsByCodeContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where code contains DEFAULT_CODE
        defaultModelShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the modelList where code contains UPDATED_CODE
        defaultModelShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllModelsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where code does not contain DEFAULT_CODE
        defaultModelShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the modelList where code does not contain UPDATED_CODE
        defaultModelShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllModelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name equals to DEFAULT_NAME
        defaultModelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the modelList where name equals to UPDATED_NAME
        defaultModelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllModelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultModelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the modelList where name equals to UPDATED_NAME
        defaultModelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllModelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name is not null
        defaultModelShouldBeFound("name.specified=true");

        // Get all the modelList where name is null
        defaultModelShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllModelsByNameContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name contains DEFAULT_NAME
        defaultModelShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the modelList where name contains UPDATED_NAME
        defaultModelShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllModelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name does not contain DEFAULT_NAME
        defaultModelShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the modelList where name does not contain UPDATED_NAME
        defaultModelShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllModelsBySubtractBonusIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where subtractBonus equals to DEFAULT_SUBTRACT_BONUS
        defaultModelShouldBeFound("subtractBonus.equals=" + DEFAULT_SUBTRACT_BONUS);

        // Get all the modelList where subtractBonus equals to UPDATED_SUBTRACT_BONUS
        defaultModelShouldNotBeFound("subtractBonus.equals=" + UPDATED_SUBTRACT_BONUS);
    }

    @Test
    @Transactional
    void getAllModelsBySubtractBonusIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where subtractBonus in DEFAULT_SUBTRACT_BONUS or UPDATED_SUBTRACT_BONUS
        defaultModelShouldBeFound("subtractBonus.in=" + DEFAULT_SUBTRACT_BONUS + "," + UPDATED_SUBTRACT_BONUS);

        // Get all the modelList where subtractBonus equals to UPDATED_SUBTRACT_BONUS
        defaultModelShouldNotBeFound("subtractBonus.in=" + UPDATED_SUBTRACT_BONUS);
    }

    @Test
    @Transactional
    void getAllModelsBySubtractBonusIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where subtractBonus is not null
        defaultModelShouldBeFound("subtractBonus.specified=true");

        // Get all the modelList where subtractBonus is null
        defaultModelShouldNotBeFound("subtractBonus.specified=false");
    }

    @Test
    @Transactional
    void getAllModelsByCollectionCeilIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where collectionCeil equals to DEFAULT_COLLECTION_CEIL
        defaultModelShouldBeFound("collectionCeil.equals=" + DEFAULT_COLLECTION_CEIL);

        // Get all the modelList where collectionCeil equals to UPDATED_COLLECTION_CEIL
        defaultModelShouldNotBeFound("collectionCeil.equals=" + UPDATED_COLLECTION_CEIL);
    }

    @Test
    @Transactional
    void getAllModelsByCollectionCeilIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where collectionCeil in DEFAULT_COLLECTION_CEIL or UPDATED_COLLECTION_CEIL
        defaultModelShouldBeFound("collectionCeil.in=" + DEFAULT_COLLECTION_CEIL + "," + UPDATED_COLLECTION_CEIL);

        // Get all the modelList where collectionCeil equals to UPDATED_COLLECTION_CEIL
        defaultModelShouldNotBeFound("collectionCeil.in=" + UPDATED_COLLECTION_CEIL);
    }

    @Test
    @Transactional
    void getAllModelsByCollectionCeilIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where collectionCeil is not null
        defaultModelShouldBeFound("collectionCeil.specified=true");

        // Get all the modelList where collectionCeil is null
        defaultModelShouldNotBeFound("collectionCeil.specified=false");
    }

    @Test
    @Transactional
    void getAllModelsByCollectionCeilIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where collectionCeil is greater than or equal to DEFAULT_COLLECTION_CEIL
        defaultModelShouldBeFound("collectionCeil.greaterThanOrEqual=" + DEFAULT_COLLECTION_CEIL);

        // Get all the modelList where collectionCeil is greater than or equal to UPDATED_COLLECTION_CEIL
        defaultModelShouldNotBeFound("collectionCeil.greaterThanOrEqual=" + UPDATED_COLLECTION_CEIL);
    }

    @Test
    @Transactional
    void getAllModelsByCollectionCeilIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where collectionCeil is less than or equal to DEFAULT_COLLECTION_CEIL
        defaultModelShouldBeFound("collectionCeil.lessThanOrEqual=" + DEFAULT_COLLECTION_CEIL);

        // Get all the modelList where collectionCeil is less than or equal to SMALLER_COLLECTION_CEIL
        defaultModelShouldNotBeFound("collectionCeil.lessThanOrEqual=" + SMALLER_COLLECTION_CEIL);
    }

    @Test
    @Transactional
    void getAllModelsByCollectionCeilIsLessThanSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where collectionCeil is less than DEFAULT_COLLECTION_CEIL
        defaultModelShouldNotBeFound("collectionCeil.lessThan=" + DEFAULT_COLLECTION_CEIL);

        // Get all the modelList where collectionCeil is less than UPDATED_COLLECTION_CEIL
        defaultModelShouldBeFound("collectionCeil.lessThan=" + UPDATED_COLLECTION_CEIL);
    }

    @Test
    @Transactional
    void getAllModelsByCollectionCeilIsGreaterThanSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where collectionCeil is greater than DEFAULT_COLLECTION_CEIL
        defaultModelShouldNotBeFound("collectionCeil.greaterThan=" + DEFAULT_COLLECTION_CEIL);

        // Get all the modelList where collectionCeil is greater than SMALLER_COLLECTION_CEIL
        defaultModelShouldBeFound("collectionCeil.greaterThan=" + SMALLER_COLLECTION_CEIL);
    }

    @Test
    @Transactional
    void getAllModelsByRolloverLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where rolloverLimit equals to DEFAULT_ROLLOVER_LIMIT
        defaultModelShouldBeFound("rolloverLimit.equals=" + DEFAULT_ROLLOVER_LIMIT);

        // Get all the modelList where rolloverLimit equals to UPDATED_ROLLOVER_LIMIT
        defaultModelShouldNotBeFound("rolloverLimit.equals=" + UPDATED_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void getAllModelsByRolloverLimitIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where rolloverLimit in DEFAULT_ROLLOVER_LIMIT or UPDATED_ROLLOVER_LIMIT
        defaultModelShouldBeFound("rolloverLimit.in=" + DEFAULT_ROLLOVER_LIMIT + "," + UPDATED_ROLLOVER_LIMIT);

        // Get all the modelList where rolloverLimit equals to UPDATED_ROLLOVER_LIMIT
        defaultModelShouldNotBeFound("rolloverLimit.in=" + UPDATED_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void getAllModelsByRolloverLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where rolloverLimit is not null
        defaultModelShouldBeFound("rolloverLimit.specified=true");

        // Get all the modelList where rolloverLimit is null
        defaultModelShouldNotBeFound("rolloverLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllModelsByRolloverLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where rolloverLimit is greater than or equal to DEFAULT_ROLLOVER_LIMIT
        defaultModelShouldBeFound("rolloverLimit.greaterThanOrEqual=" + DEFAULT_ROLLOVER_LIMIT);

        // Get all the modelList where rolloverLimit is greater than or equal to UPDATED_ROLLOVER_LIMIT
        defaultModelShouldNotBeFound("rolloverLimit.greaterThanOrEqual=" + UPDATED_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void getAllModelsByRolloverLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where rolloverLimit is less than or equal to DEFAULT_ROLLOVER_LIMIT
        defaultModelShouldBeFound("rolloverLimit.lessThanOrEqual=" + DEFAULT_ROLLOVER_LIMIT);

        // Get all the modelList where rolloverLimit is less than or equal to SMALLER_ROLLOVER_LIMIT
        defaultModelShouldNotBeFound("rolloverLimit.lessThanOrEqual=" + SMALLER_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void getAllModelsByRolloverLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where rolloverLimit is less than DEFAULT_ROLLOVER_LIMIT
        defaultModelShouldNotBeFound("rolloverLimit.lessThan=" + DEFAULT_ROLLOVER_LIMIT);

        // Get all the modelList where rolloverLimit is less than UPDATED_ROLLOVER_LIMIT
        defaultModelShouldBeFound("rolloverLimit.lessThan=" + UPDATED_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void getAllModelsByRolloverLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where rolloverLimit is greater than DEFAULT_ROLLOVER_LIMIT
        defaultModelShouldNotBeFound("rolloverLimit.greaterThan=" + DEFAULT_ROLLOVER_LIMIT);

        // Get all the modelList where rolloverLimit is greater than SMALLER_ROLLOVER_LIMIT
        defaultModelShouldBeFound("rolloverLimit.greaterThan=" + SMALLER_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void getAllModelsByManufacturerIsEqualToSomething() throws Exception {
        Manufacturer manufacturer;
        if (TestUtil.findAll(em, Manufacturer.class).isEmpty()) {
            modelRepository.saveAndFlush(model);
            manufacturer = ManufacturerResourceIT.createEntity(em);
        } else {
            manufacturer = TestUtil.findAll(em, Manufacturer.class).get(0);
        }
        em.persist(manufacturer);
        em.flush();
        model.setManufacturer(manufacturer);
        modelRepository.saveAndFlush(model);
        Long manufacturerId = manufacturer.getId();

        // Get all the modelList where manufacturer equals to manufacturerId
        defaultModelShouldBeFound("manufacturerId.equals=" + manufacturerId);

        // Get all the modelList where manufacturer equals to (manufacturerId + 1)
        defaultModelShouldNotBeFound("manufacturerId.equals=" + (manufacturerId + 1));
    }

    @Test
    @Transactional
    void getAllModelsByFormulaIsEqualToSomething() throws Exception {
        Formula formula;
        if (TestUtil.findAll(em, Formula.class).isEmpty()) {
            modelRepository.saveAndFlush(model);
            formula = FormulaResourceIT.createEntity(em);
        } else {
            formula = TestUtil.findAll(em, Formula.class).get(0);
        }
        em.persist(formula);
        em.flush();
        model.setFormula(formula);
        modelRepository.saveAndFlush(model);
        Long formulaId = formula.getId();

        // Get all the modelList where formula equals to formulaId
        defaultModelShouldBeFound("formulaId.equals=" + formulaId);

        // Get all the modelList where formula equals to (formulaId + 1)
        defaultModelShouldNotBeFound("formulaId.equals=" + (formulaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultModelShouldBeFound(String filter) throws Exception {
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].subtractBonus").value(hasItem(DEFAULT_SUBTRACT_BONUS.booleanValue())))
            .andExpect(jsonPath("$.[*].collectionCeil").value(hasItem(sameNumber(DEFAULT_COLLECTION_CEIL))))
            .andExpect(jsonPath("$.[*].rolloverLimit").value(hasItem(sameNumber(DEFAULT_ROLLOVER_LIMIT))));

        // Check, that the count call also returns 1
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultModelShouldNotBeFound(String filter) throws Exception {
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model
        Model updatedModel = modelRepository.findById(model.getId()).get();
        // Disconnect from session so that the updates on updatedModel are not directly saved in db
        em.detach(updatedModel);
        updatedModel
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .subtractBonus(UPDATED_SUBTRACT_BONUS)
            .collectionCeil(UPDATED_COLLECTION_CEIL)
            .rolloverLimit(UPDATED_ROLLOVER_LIMIT);
        ModelDTO modelDTO = modelMapper.toDto(updatedModel);

        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getSubtractBonus()).isEqualTo(UPDATED_SUBTRACT_BONUS);
        assertThat(testModel.getCollectionCeil()).isEqualByComparingTo(UPDATED_COLLECTION_CEIL);
        assertThat(testModel.getRolloverLimit()).isEqualByComparingTo(UPDATED_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void putNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModelWithPatch() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel.subtractBonus(UPDATED_SUBTRACT_BONUS).collectionCeil(UPDATED_COLLECTION_CEIL);

        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModel))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModel.getSubtractBonus()).isEqualTo(UPDATED_SUBTRACT_BONUS);
        assertThat(testModel.getCollectionCeil()).isEqualByComparingTo(UPDATED_COLLECTION_CEIL);
        assertThat(testModel.getRolloverLimit()).isEqualByComparingTo(DEFAULT_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void fullUpdateModelWithPatch() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .subtractBonus(UPDATED_SUBTRACT_BONUS)
            .collectionCeil(UPDATED_COLLECTION_CEIL)
            .rolloverLimit(UPDATED_ROLLOVER_LIMIT);

        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModel))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getSubtractBonus()).isEqualTo(UPDATED_SUBTRACT_BONUS);
        assertThat(testModel.getCollectionCeil()).isEqualByComparingTo(UPDATED_COLLECTION_CEIL);
        assertThat(testModel.getRolloverLimit()).isEqualByComparingTo(UPDATED_ROLLOVER_LIMIT);
    }

    @Test
    @Transactional
    void patchNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(modelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeDelete = modelRepository.findAll().size();

        // Delete the model
        restModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, model.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
