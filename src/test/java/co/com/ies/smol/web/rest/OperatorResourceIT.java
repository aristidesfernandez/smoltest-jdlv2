package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.repository.OperatorRepository;
import co.com.ies.smol.service.criteria.OperatorCriteria;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.mapper.OperatorMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link OperatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperatorResourceIT {

    private static final String DEFAULT_PERMIT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PERMIT_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperatorMockMvc;

    private Operator operator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createEntity(EntityManager em) {
        Operator operator = new Operator()
            .permitDescription(DEFAULT_PERMIT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .nit(DEFAULT_NIT)
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .companyName(DEFAULT_COMPANY_NAME)
            .brand(DEFAULT_BRAND);
        // Add required entity
        Municipality municipality;
        if (TestUtil.findAll(em, Municipality.class).isEmpty()) {
            municipality = MunicipalityResourceIT.createEntity(em);
            em.persist(municipality);
            em.flush();
        } else {
            municipality = TestUtil.findAll(em, Municipality.class).get(0);
        }
        operator.setMunicipality(municipality);
        return operator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createUpdatedEntity(EntityManager em) {
        Operator operator = new Operator()
            .permitDescription(UPDATED_PERMIT_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nit(UPDATED_NIT)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .companyName(UPDATED_COMPANY_NAME)
            .brand(UPDATED_BRAND);
        // Add required entity
        Municipality municipality;
        if (TestUtil.findAll(em, Municipality.class).isEmpty()) {
            municipality = MunicipalityResourceIT.createUpdatedEntity(em);
            em.persist(municipality);
            em.flush();
        } else {
            municipality = TestUtil.findAll(em, Municipality.class).get(0);
        }
        operator.setMunicipality(municipality);
        return operator;
    }

    @BeforeEach
    public void initTest() {
        operator = createEntity(em);
    }

    @Test
    @Transactional
    void createOperator() throws Exception {
        int databaseSizeBeforeCreate = operatorRepository.findAll().size();
        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);
        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isCreated());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate + 1);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getPermitDescription()).isEqualTo(DEFAULT_PERMIT_DESCRIPTION);
        assertThat(testOperator.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOperator.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testOperator.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testOperator.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
        assertThat(testOperator.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testOperator.getBrand()).isEqualTo(DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void createOperatorWithExistingId() throws Exception {
        // Create the Operator with an existing ID
        operator.setId(1L);
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        int databaseSizeBeforeCreate = operatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOperators() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].permitDescription").value(hasItem(DEFAULT_PERMIT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)));
    }

    @Test
    @Transactional
    void getOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get the operator
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL_ID, operator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operator.getId().intValue()))
            .andExpect(jsonPath("$.permitDescription").value(DEFAULT_PERMIT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT))
            .andExpect(jsonPath("$.contractNumber").value(DEFAULT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND));
    }

    @Test
    @Transactional
    void getOperatorsByIdFiltering() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        Long id = operator.getId();

        defaultOperatorShouldBeFound("id.equals=" + id);
        defaultOperatorShouldNotBeFound("id.notEquals=" + id);

        defaultOperatorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.greaterThan=" + id);

        defaultOperatorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOperatorsByPermitDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where permitDescription equals to DEFAULT_PERMIT_DESCRIPTION
        defaultOperatorShouldBeFound("permitDescription.equals=" + DEFAULT_PERMIT_DESCRIPTION);

        // Get all the operatorList where permitDescription equals to UPDATED_PERMIT_DESCRIPTION
        defaultOperatorShouldNotBeFound("permitDescription.equals=" + UPDATED_PERMIT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOperatorsByPermitDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where permitDescription in DEFAULT_PERMIT_DESCRIPTION or UPDATED_PERMIT_DESCRIPTION
        defaultOperatorShouldBeFound("permitDescription.in=" + DEFAULT_PERMIT_DESCRIPTION + "," + UPDATED_PERMIT_DESCRIPTION);

        // Get all the operatorList where permitDescription equals to UPDATED_PERMIT_DESCRIPTION
        defaultOperatorShouldNotBeFound("permitDescription.in=" + UPDATED_PERMIT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOperatorsByPermitDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where permitDescription is not null
        defaultOperatorShouldBeFound("permitDescription.specified=true");

        // Get all the operatorList where permitDescription is null
        defaultOperatorShouldNotBeFound("permitDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByPermitDescriptionContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where permitDescription contains DEFAULT_PERMIT_DESCRIPTION
        defaultOperatorShouldBeFound("permitDescription.contains=" + DEFAULT_PERMIT_DESCRIPTION);

        // Get all the operatorList where permitDescription contains UPDATED_PERMIT_DESCRIPTION
        defaultOperatorShouldNotBeFound("permitDescription.contains=" + UPDATED_PERMIT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOperatorsByPermitDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where permitDescription does not contain DEFAULT_PERMIT_DESCRIPTION
        defaultOperatorShouldNotBeFound("permitDescription.doesNotContain=" + DEFAULT_PERMIT_DESCRIPTION);

        // Get all the operatorList where permitDescription does not contain UPDATED_PERMIT_DESCRIPTION
        defaultOperatorShouldBeFound("permitDescription.doesNotContain=" + UPDATED_PERMIT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOperatorsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where startDate equals to DEFAULT_START_DATE
        defaultOperatorShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the operatorList where startDate equals to UPDATED_START_DATE
        defaultOperatorShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultOperatorShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the operatorList where startDate equals to UPDATED_START_DATE
        defaultOperatorShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where startDate is not null
        defaultOperatorShouldBeFound("startDate.specified=true");

        // Get all the operatorList where startDate is null
        defaultOperatorShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultOperatorShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the operatorList where startDate is greater than or equal to UPDATED_START_DATE
        defaultOperatorShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where startDate is less than or equal to DEFAULT_START_DATE
        defaultOperatorShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the operatorList where startDate is less than or equal to SMALLER_START_DATE
        defaultOperatorShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where startDate is less than DEFAULT_START_DATE
        defaultOperatorShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the operatorList where startDate is less than UPDATED_START_DATE
        defaultOperatorShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where startDate is greater than DEFAULT_START_DATE
        defaultOperatorShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the operatorList where startDate is greater than SMALLER_START_DATE
        defaultOperatorShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where endDate equals to DEFAULT_END_DATE
        defaultOperatorShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the operatorList where endDate equals to UPDATED_END_DATE
        defaultOperatorShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultOperatorShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the operatorList where endDate equals to UPDATED_END_DATE
        defaultOperatorShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where endDate is not null
        defaultOperatorShouldBeFound("endDate.specified=true");

        // Get all the operatorList where endDate is null
        defaultOperatorShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultOperatorShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the operatorList where endDate is greater than or equal to UPDATED_END_DATE
        defaultOperatorShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where endDate is less than or equal to DEFAULT_END_DATE
        defaultOperatorShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the operatorList where endDate is less than or equal to SMALLER_END_DATE
        defaultOperatorShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where endDate is less than DEFAULT_END_DATE
        defaultOperatorShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the operatorList where endDate is less than UPDATED_END_DATE
        defaultOperatorShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where endDate is greater than DEFAULT_END_DATE
        defaultOperatorShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the operatorList where endDate is greater than SMALLER_END_DATE
        defaultOperatorShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit equals to DEFAULT_NIT
        defaultOperatorShouldBeFound("nit.equals=" + DEFAULT_NIT);

        // Get all the operatorList where nit equals to UPDATED_NIT
        defaultOperatorShouldNotBeFound("nit.equals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit in DEFAULT_NIT or UPDATED_NIT
        defaultOperatorShouldBeFound("nit.in=" + DEFAULT_NIT + "," + UPDATED_NIT);

        // Get all the operatorList where nit equals to UPDATED_NIT
        defaultOperatorShouldNotBeFound("nit.in=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit is not null
        defaultOperatorShouldBeFound("nit.specified=true");

        // Get all the operatorList where nit is null
        defaultOperatorShouldNotBeFound("nit.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByNitContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit contains DEFAULT_NIT
        defaultOperatorShouldBeFound("nit.contains=" + DEFAULT_NIT);

        // Get all the operatorList where nit contains UPDATED_NIT
        defaultOperatorShouldNotBeFound("nit.contains=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit does not contain DEFAULT_NIT
        defaultOperatorShouldNotBeFound("nit.doesNotContain=" + DEFAULT_NIT);

        // Get all the operatorList where nit does not contain UPDATED_NIT
        defaultOperatorShouldBeFound("nit.doesNotContain=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contractNumber equals to DEFAULT_CONTRACT_NUMBER
        defaultOperatorShouldBeFound("contractNumber.equals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the operatorList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultOperatorShouldNotBeFound("contractNumber.equals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contractNumber in DEFAULT_CONTRACT_NUMBER or UPDATED_CONTRACT_NUMBER
        defaultOperatorShouldBeFound("contractNumber.in=" + DEFAULT_CONTRACT_NUMBER + "," + UPDATED_CONTRACT_NUMBER);

        // Get all the operatorList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultOperatorShouldNotBeFound("contractNumber.in=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contractNumber is not null
        defaultOperatorShouldBeFound("contractNumber.specified=true");

        // Get all the operatorList where contractNumber is null
        defaultOperatorShouldNotBeFound("contractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByContractNumberContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contractNumber contains DEFAULT_CONTRACT_NUMBER
        defaultOperatorShouldBeFound("contractNumber.contains=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the operatorList where contractNumber contains UPDATED_CONTRACT_NUMBER
        defaultOperatorShouldNotBeFound("contractNumber.contains=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contractNumber does not contain DEFAULT_CONTRACT_NUMBER
        defaultOperatorShouldNotBeFound("contractNumber.doesNotContain=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the operatorList where contractNumber does not contain UPDATED_CONTRACT_NUMBER
        defaultOperatorShouldBeFound("contractNumber.doesNotContain=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllOperatorsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where companyName equals to DEFAULT_COMPANY_NAME
        defaultOperatorShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the operatorList where companyName equals to UPDATED_COMPANY_NAME
        defaultOperatorShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultOperatorShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the operatorList where companyName equals to UPDATED_COMPANY_NAME
        defaultOperatorShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where companyName is not null
        defaultOperatorShouldBeFound("companyName.specified=true");

        // Get all the operatorList where companyName is null
        defaultOperatorShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where companyName contains DEFAULT_COMPANY_NAME
        defaultOperatorShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the operatorList where companyName contains UPDATED_COMPANY_NAME
        defaultOperatorShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultOperatorShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the operatorList where companyName does not contain UPDATED_COMPANY_NAME
        defaultOperatorShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where brand equals to DEFAULT_BRAND
        defaultOperatorShouldBeFound("brand.equals=" + DEFAULT_BRAND);

        // Get all the operatorList where brand equals to UPDATED_BRAND
        defaultOperatorShouldNotBeFound("brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllOperatorsByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where brand in DEFAULT_BRAND or UPDATED_BRAND
        defaultOperatorShouldBeFound("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND);

        // Get all the operatorList where brand equals to UPDATED_BRAND
        defaultOperatorShouldNotBeFound("brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllOperatorsByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where brand is not null
        defaultOperatorShouldBeFound("brand.specified=true");

        // Get all the operatorList where brand is null
        defaultOperatorShouldNotBeFound("brand.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByBrandContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where brand contains DEFAULT_BRAND
        defaultOperatorShouldBeFound("brand.contains=" + DEFAULT_BRAND);

        // Get all the operatorList where brand contains UPDATED_BRAND
        defaultOperatorShouldNotBeFound("brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllOperatorsByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where brand does not contain DEFAULT_BRAND
        defaultOperatorShouldNotBeFound("brand.doesNotContain=" + DEFAULT_BRAND);

        // Get all the operatorList where brand does not contain UPDATED_BRAND
        defaultOperatorShouldBeFound("brand.doesNotContain=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllOperatorsByMunicipalityIsEqualToSomething() throws Exception {
        Municipality municipality;
        if (TestUtil.findAll(em, Municipality.class).isEmpty()) {
            operatorRepository.saveAndFlush(operator);
            municipality = MunicipalityResourceIT.createEntity(em);
        } else {
            municipality = TestUtil.findAll(em, Municipality.class).get(0);
        }
        em.persist(municipality);
        em.flush();
        operator.setMunicipality(municipality);
        operatorRepository.saveAndFlush(operator);
        Long municipalityId = municipality.getId();

        // Get all the operatorList where municipality equals to municipalityId
        defaultOperatorShouldBeFound("municipalityId.equals=" + municipalityId);

        // Get all the operatorList where municipality equals to (municipalityId + 1)
        defaultOperatorShouldNotBeFound("municipalityId.equals=" + (municipalityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperatorShouldBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].permitDescription").value(hasItem(DEFAULT_PERMIT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)));

        // Check, that the count call also returns 1
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperatorShouldNotBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOperator() throws Exception {
        // Get the operator
        restOperatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator
        Operator updatedOperator = operatorRepository.findById(operator.getId()).get();
        // Disconnect from session so that the updates on updatedOperator are not directly saved in db
        em.detach(updatedOperator);
        updatedOperator
            .permitDescription(UPDATED_PERMIT_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nit(UPDATED_NIT)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .companyName(UPDATED_COMPANY_NAME)
            .brand(UPDATED_BRAND);
        OperatorDTO operatorDTO = operatorMapper.toDto(updatedOperator);

        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getPermitDescription()).isEqualTo(UPDATED_PERMIT_DESCRIPTION);
        assertThat(testOperator.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOperator.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOperator.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testOperator.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testOperator.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testOperator.getBrand()).isEqualTo(UPDATED_BRAND);
    }

    @Test
    @Transactional
    void putNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        partialUpdatedOperator.endDate(UPDATED_END_DATE);

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getPermitDescription()).isEqualTo(DEFAULT_PERMIT_DESCRIPTION);
        assertThat(testOperator.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOperator.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOperator.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testOperator.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
        assertThat(testOperator.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testOperator.getBrand()).isEqualTo(DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void fullUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        partialUpdatedOperator
            .permitDescription(UPDATED_PERMIT_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nit(UPDATED_NIT)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .companyName(UPDATED_COMPANY_NAME)
            .brand(UPDATED_BRAND);

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getPermitDescription()).isEqualTo(UPDATED_PERMIT_DESCRIPTION);
        assertThat(testOperator.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOperator.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOperator.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testOperator.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testOperator.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testOperator.getBrand()).isEqualTo(UPDATED_BRAND);
    }

    @Test
    @Transactional
    void patchNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeDelete = operatorRepository.findAll().size();

        // Delete the operator
        restOperatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, operator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
