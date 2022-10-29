package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Establishment;
import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.domain.enumeration.EstablishmentType;
import co.com.ies.smol.repository.EstablishmentRepository;
import co.com.ies.smol.service.criteria.EstablishmentCriteria;
import co.com.ies.smol.service.dto.EstablishmentDTO;
import co.com.ies.smol.service.mapper.EstablishmentMapper;
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
 * Integration tests for the {@link EstablishmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstablishmentResourceIT {

    private static final ZonedDateTime DEFAULT_LIQUIDATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LIQUIDATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LIQUIDATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final EstablishmentType DEFAULT_TYPE = EstablishmentType.CASINO;
    private static final EstablishmentType UPDATED_TYPE = EstablishmentType.RUTA;

    private static final String DEFAULT_NEIGHBORHOOD = "AAAAAAAAAA";
    private static final String UPDATED_NEIGHBORHOOD = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COLJUEGOS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COLJUEGOS_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CLOSE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CLOSE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CLOSE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;
    private static final Float SMALLER_LONGITUDE = 1F - 1F;

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final Float SMALLER_LATITUDE = 1F - 1F;

    private static final String DEFAULT_MERCANTILE_REGISTRATION = "AAAAAAAAAA";
    private static final String UPDATED_MERCANTILE_REGISTRATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/establishments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private EstablishmentMapper establishmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstablishmentMockMvc;

    private Establishment establishment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Establishment createEntity(EntityManager em) {
        Establishment establishment = new Establishment()
            .liquidationTime(DEFAULT_LIQUIDATION_TIME)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .neighborhood(DEFAULT_NEIGHBORHOOD)
            .address(DEFAULT_ADDRESS)
            .coljuegosCode(DEFAULT_COLJUEGOS_CODE)
            .startTime(DEFAULT_START_TIME)
            .closeTime(DEFAULT_CLOSE_TIME)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .mercantileRegistration(DEFAULT_MERCANTILE_REGISTRATION);
        // Add required entity
        Operator operator;
        if (TestUtil.findAll(em, Operator.class).isEmpty()) {
            operator = OperatorResourceIT.createEntity(em);
            em.persist(operator);
            em.flush();
        } else {
            operator = TestUtil.findAll(em, Operator.class).get(0);
        }
        establishment.setOperator(operator);
        // Add required entity
        Municipality municipality;
        if (TestUtil.findAll(em, Municipality.class).isEmpty()) {
            municipality = MunicipalityResourceIT.createEntity(em);
            em.persist(municipality);
            em.flush();
        } else {
            municipality = TestUtil.findAll(em, Municipality.class).get(0);
        }
        establishment.setMunicipality(municipality);
        return establishment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Establishment createUpdatedEntity(EntityManager em) {
        Establishment establishment = new Establishment()
            .liquidationTime(UPDATED_LIQUIDATION_TIME)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .neighborhood(UPDATED_NEIGHBORHOOD)
            .address(UPDATED_ADDRESS)
            .coljuegosCode(UPDATED_COLJUEGOS_CODE)
            .startTime(UPDATED_START_TIME)
            .closeTime(UPDATED_CLOSE_TIME)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .mercantileRegistration(UPDATED_MERCANTILE_REGISTRATION);
        // Add required entity
        Operator operator;
        if (TestUtil.findAll(em, Operator.class).isEmpty()) {
            operator = OperatorResourceIT.createUpdatedEntity(em);
            em.persist(operator);
            em.flush();
        } else {
            operator = TestUtil.findAll(em, Operator.class).get(0);
        }
        establishment.setOperator(operator);
        // Add required entity
        Municipality municipality;
        if (TestUtil.findAll(em, Municipality.class).isEmpty()) {
            municipality = MunicipalityResourceIT.createUpdatedEntity(em);
            em.persist(municipality);
            em.flush();
        } else {
            municipality = TestUtil.findAll(em, Municipality.class).get(0);
        }
        establishment.setMunicipality(municipality);
        return establishment;
    }

    @BeforeEach
    public void initTest() {
        establishment = createEntity(em);
    }

    @Test
    @Transactional
    void createEstablishment() throws Exception {
        int databaseSizeBeforeCreate = establishmentRepository.findAll().size();
        // Create the Establishment
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);
        restEstablishmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeCreate + 1);
        Establishment testEstablishment = establishmentList.get(establishmentList.size() - 1);
        assertThat(testEstablishment.getLiquidationTime()).isEqualTo(DEFAULT_LIQUIDATION_TIME);
        assertThat(testEstablishment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEstablishment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEstablishment.getNeighborhood()).isEqualTo(DEFAULT_NEIGHBORHOOD);
        assertThat(testEstablishment.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEstablishment.getColjuegosCode()).isEqualTo(DEFAULT_COLJUEGOS_CODE);
        assertThat(testEstablishment.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testEstablishment.getCloseTime()).isEqualTo(DEFAULT_CLOSE_TIME);
        assertThat(testEstablishment.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testEstablishment.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testEstablishment.getMercantileRegistration()).isEqualTo(DEFAULT_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void createEstablishmentWithExistingId() throws Exception {
        // Create the Establishment with an existing ID
        establishment.setId(1L);
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);

        int databaseSizeBeforeCreate = establishmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstablishmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEstablishments() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList
        restEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(establishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].liquidationTime").value(hasItem(sameInstant(DEFAULT_LIQUIDATION_TIME))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].neighborhood").value(hasItem(DEFAULT_NEIGHBORHOOD)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].coljuegosCode").value(hasItem(DEFAULT_COLJUEGOS_CODE)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(sameInstant(DEFAULT_CLOSE_TIME))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].mercantileRegistration").value(hasItem(DEFAULT_MERCANTILE_REGISTRATION)));
    }

    @Test
    @Transactional
    void getEstablishment() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get the establishment
        restEstablishmentMockMvc
            .perform(get(ENTITY_API_URL_ID, establishment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(establishment.getId().intValue()))
            .andExpect(jsonPath("$.liquidationTime").value(sameInstant(DEFAULT_LIQUIDATION_TIME)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.neighborhood").value(DEFAULT_NEIGHBORHOOD))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.coljuegosCode").value(DEFAULT_COLJUEGOS_CODE))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.closeTime").value(sameInstant(DEFAULT_CLOSE_TIME)))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.mercantileRegistration").value(DEFAULT_MERCANTILE_REGISTRATION));
    }

    @Test
    @Transactional
    void getEstablishmentsByIdFiltering() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        Long id = establishment.getId();

        defaultEstablishmentShouldBeFound("id.equals=" + id);
        defaultEstablishmentShouldNotBeFound("id.notEquals=" + id);

        defaultEstablishmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEstablishmentShouldNotBeFound("id.greaterThan=" + id);

        defaultEstablishmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEstablishmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLiquidationTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where liquidationTime equals to DEFAULT_LIQUIDATION_TIME
        defaultEstablishmentShouldBeFound("liquidationTime.equals=" + DEFAULT_LIQUIDATION_TIME);

        // Get all the establishmentList where liquidationTime equals to UPDATED_LIQUIDATION_TIME
        defaultEstablishmentShouldNotBeFound("liquidationTime.equals=" + UPDATED_LIQUIDATION_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLiquidationTimeIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where liquidationTime in DEFAULT_LIQUIDATION_TIME or UPDATED_LIQUIDATION_TIME
        defaultEstablishmentShouldBeFound("liquidationTime.in=" + DEFAULT_LIQUIDATION_TIME + "," + UPDATED_LIQUIDATION_TIME);

        // Get all the establishmentList where liquidationTime equals to UPDATED_LIQUIDATION_TIME
        defaultEstablishmentShouldNotBeFound("liquidationTime.in=" + UPDATED_LIQUIDATION_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLiquidationTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where liquidationTime is not null
        defaultEstablishmentShouldBeFound("liquidationTime.specified=true");

        // Get all the establishmentList where liquidationTime is null
        defaultEstablishmentShouldNotBeFound("liquidationTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLiquidationTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where liquidationTime is greater than or equal to DEFAULT_LIQUIDATION_TIME
        defaultEstablishmentShouldBeFound("liquidationTime.greaterThanOrEqual=" + DEFAULT_LIQUIDATION_TIME);

        // Get all the establishmentList where liquidationTime is greater than or equal to UPDATED_LIQUIDATION_TIME
        defaultEstablishmentShouldNotBeFound("liquidationTime.greaterThanOrEqual=" + UPDATED_LIQUIDATION_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLiquidationTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where liquidationTime is less than or equal to DEFAULT_LIQUIDATION_TIME
        defaultEstablishmentShouldBeFound("liquidationTime.lessThanOrEqual=" + DEFAULT_LIQUIDATION_TIME);

        // Get all the establishmentList where liquidationTime is less than or equal to SMALLER_LIQUIDATION_TIME
        defaultEstablishmentShouldNotBeFound("liquidationTime.lessThanOrEqual=" + SMALLER_LIQUIDATION_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLiquidationTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where liquidationTime is less than DEFAULT_LIQUIDATION_TIME
        defaultEstablishmentShouldNotBeFound("liquidationTime.lessThan=" + DEFAULT_LIQUIDATION_TIME);

        // Get all the establishmentList where liquidationTime is less than UPDATED_LIQUIDATION_TIME
        defaultEstablishmentShouldBeFound("liquidationTime.lessThan=" + UPDATED_LIQUIDATION_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLiquidationTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where liquidationTime is greater than DEFAULT_LIQUIDATION_TIME
        defaultEstablishmentShouldNotBeFound("liquidationTime.greaterThan=" + DEFAULT_LIQUIDATION_TIME);

        // Get all the establishmentList where liquidationTime is greater than SMALLER_LIQUIDATION_TIME
        defaultEstablishmentShouldBeFound("liquidationTime.greaterThan=" + SMALLER_LIQUIDATION_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where name equals to DEFAULT_NAME
        defaultEstablishmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the establishmentList where name equals to UPDATED_NAME
        defaultEstablishmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEstablishmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the establishmentList where name equals to UPDATED_NAME
        defaultEstablishmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where name is not null
        defaultEstablishmentShouldBeFound("name.specified=true");

        // Get all the establishmentList where name is null
        defaultEstablishmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where name contains DEFAULT_NAME
        defaultEstablishmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the establishmentList where name contains UPDATED_NAME
        defaultEstablishmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where name does not contain DEFAULT_NAME
        defaultEstablishmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the establishmentList where name does not contain UPDATED_NAME
        defaultEstablishmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where type equals to DEFAULT_TYPE
        defaultEstablishmentShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the establishmentList where type equals to UPDATED_TYPE
        defaultEstablishmentShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultEstablishmentShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the establishmentList where type equals to UPDATED_TYPE
        defaultEstablishmentShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where type is not null
        defaultEstablishmentShouldBeFound("type.specified=true");

        // Get all the establishmentList where type is null
        defaultEstablishmentShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNeighborhoodIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where neighborhood equals to DEFAULT_NEIGHBORHOOD
        defaultEstablishmentShouldBeFound("neighborhood.equals=" + DEFAULT_NEIGHBORHOOD);

        // Get all the establishmentList where neighborhood equals to UPDATED_NEIGHBORHOOD
        defaultEstablishmentShouldNotBeFound("neighborhood.equals=" + UPDATED_NEIGHBORHOOD);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNeighborhoodIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where neighborhood in DEFAULT_NEIGHBORHOOD or UPDATED_NEIGHBORHOOD
        defaultEstablishmentShouldBeFound("neighborhood.in=" + DEFAULT_NEIGHBORHOOD + "," + UPDATED_NEIGHBORHOOD);

        // Get all the establishmentList where neighborhood equals to UPDATED_NEIGHBORHOOD
        defaultEstablishmentShouldNotBeFound("neighborhood.in=" + UPDATED_NEIGHBORHOOD);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNeighborhoodIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where neighborhood is not null
        defaultEstablishmentShouldBeFound("neighborhood.specified=true");

        // Get all the establishmentList where neighborhood is null
        defaultEstablishmentShouldNotBeFound("neighborhood.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNeighborhoodContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where neighborhood contains DEFAULT_NEIGHBORHOOD
        defaultEstablishmentShouldBeFound("neighborhood.contains=" + DEFAULT_NEIGHBORHOOD);

        // Get all the establishmentList where neighborhood contains UPDATED_NEIGHBORHOOD
        defaultEstablishmentShouldNotBeFound("neighborhood.contains=" + UPDATED_NEIGHBORHOOD);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByNeighborhoodNotContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where neighborhood does not contain DEFAULT_NEIGHBORHOOD
        defaultEstablishmentShouldNotBeFound("neighborhood.doesNotContain=" + DEFAULT_NEIGHBORHOOD);

        // Get all the establishmentList where neighborhood does not contain UPDATED_NEIGHBORHOOD
        defaultEstablishmentShouldBeFound("neighborhood.doesNotContain=" + UPDATED_NEIGHBORHOOD);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where address equals to DEFAULT_ADDRESS
        defaultEstablishmentShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the establishmentList where address equals to UPDATED_ADDRESS
        defaultEstablishmentShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultEstablishmentShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the establishmentList where address equals to UPDATED_ADDRESS
        defaultEstablishmentShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where address is not null
        defaultEstablishmentShouldBeFound("address.specified=true");

        // Get all the establishmentList where address is null
        defaultEstablishmentShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByAddressContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where address contains DEFAULT_ADDRESS
        defaultEstablishmentShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the establishmentList where address contains UPDATED_ADDRESS
        defaultEstablishmentShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where address does not contain DEFAULT_ADDRESS
        defaultEstablishmentShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the establishmentList where address does not contain UPDATED_ADDRESS
        defaultEstablishmentShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByColjuegosCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where coljuegosCode equals to DEFAULT_COLJUEGOS_CODE
        defaultEstablishmentShouldBeFound("coljuegosCode.equals=" + DEFAULT_COLJUEGOS_CODE);

        // Get all the establishmentList where coljuegosCode equals to UPDATED_COLJUEGOS_CODE
        defaultEstablishmentShouldNotBeFound("coljuegosCode.equals=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByColjuegosCodeIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where coljuegosCode in DEFAULT_COLJUEGOS_CODE or UPDATED_COLJUEGOS_CODE
        defaultEstablishmentShouldBeFound("coljuegosCode.in=" + DEFAULT_COLJUEGOS_CODE + "," + UPDATED_COLJUEGOS_CODE);

        // Get all the establishmentList where coljuegosCode equals to UPDATED_COLJUEGOS_CODE
        defaultEstablishmentShouldNotBeFound("coljuegosCode.in=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByColjuegosCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where coljuegosCode is not null
        defaultEstablishmentShouldBeFound("coljuegosCode.specified=true");

        // Get all the establishmentList where coljuegosCode is null
        defaultEstablishmentShouldNotBeFound("coljuegosCode.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByColjuegosCodeContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where coljuegosCode contains DEFAULT_COLJUEGOS_CODE
        defaultEstablishmentShouldBeFound("coljuegosCode.contains=" + DEFAULT_COLJUEGOS_CODE);

        // Get all the establishmentList where coljuegosCode contains UPDATED_COLJUEGOS_CODE
        defaultEstablishmentShouldNotBeFound("coljuegosCode.contains=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByColjuegosCodeNotContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where coljuegosCode does not contain DEFAULT_COLJUEGOS_CODE
        defaultEstablishmentShouldNotBeFound("coljuegosCode.doesNotContain=" + DEFAULT_COLJUEGOS_CODE);

        // Get all the establishmentList where coljuegosCode does not contain UPDATED_COLJUEGOS_CODE
        defaultEstablishmentShouldBeFound("coljuegosCode.doesNotContain=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where startTime equals to DEFAULT_START_TIME
        defaultEstablishmentShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the establishmentList where startTime equals to UPDATED_START_TIME
        defaultEstablishmentShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultEstablishmentShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the establishmentList where startTime equals to UPDATED_START_TIME
        defaultEstablishmentShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where startTime is not null
        defaultEstablishmentShouldBeFound("startTime.specified=true");

        // Get all the establishmentList where startTime is null
        defaultEstablishmentShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where startTime is greater than or equal to DEFAULT_START_TIME
        defaultEstablishmentShouldBeFound("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the establishmentList where startTime is greater than or equal to UPDATED_START_TIME
        defaultEstablishmentShouldNotBeFound("startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where startTime is less than or equal to DEFAULT_START_TIME
        defaultEstablishmentShouldBeFound("startTime.lessThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the establishmentList where startTime is less than or equal to SMALLER_START_TIME
        defaultEstablishmentShouldNotBeFound("startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where startTime is less than DEFAULT_START_TIME
        defaultEstablishmentShouldNotBeFound("startTime.lessThan=" + DEFAULT_START_TIME);

        // Get all the establishmentList where startTime is less than UPDATED_START_TIME
        defaultEstablishmentShouldBeFound("startTime.lessThan=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where startTime is greater than DEFAULT_START_TIME
        defaultEstablishmentShouldNotBeFound("startTime.greaterThan=" + DEFAULT_START_TIME);

        // Get all the establishmentList where startTime is greater than SMALLER_START_TIME
        defaultEstablishmentShouldBeFound("startTime.greaterThan=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByCloseTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where closeTime equals to DEFAULT_CLOSE_TIME
        defaultEstablishmentShouldBeFound("closeTime.equals=" + DEFAULT_CLOSE_TIME);

        // Get all the establishmentList where closeTime equals to UPDATED_CLOSE_TIME
        defaultEstablishmentShouldNotBeFound("closeTime.equals=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByCloseTimeIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where closeTime in DEFAULT_CLOSE_TIME or UPDATED_CLOSE_TIME
        defaultEstablishmentShouldBeFound("closeTime.in=" + DEFAULT_CLOSE_TIME + "," + UPDATED_CLOSE_TIME);

        // Get all the establishmentList where closeTime equals to UPDATED_CLOSE_TIME
        defaultEstablishmentShouldNotBeFound("closeTime.in=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByCloseTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where closeTime is not null
        defaultEstablishmentShouldBeFound("closeTime.specified=true");

        // Get all the establishmentList where closeTime is null
        defaultEstablishmentShouldNotBeFound("closeTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByCloseTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where closeTime is greater than or equal to DEFAULT_CLOSE_TIME
        defaultEstablishmentShouldBeFound("closeTime.greaterThanOrEqual=" + DEFAULT_CLOSE_TIME);

        // Get all the establishmentList where closeTime is greater than or equal to UPDATED_CLOSE_TIME
        defaultEstablishmentShouldNotBeFound("closeTime.greaterThanOrEqual=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByCloseTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where closeTime is less than or equal to DEFAULT_CLOSE_TIME
        defaultEstablishmentShouldBeFound("closeTime.lessThanOrEqual=" + DEFAULT_CLOSE_TIME);

        // Get all the establishmentList where closeTime is less than or equal to SMALLER_CLOSE_TIME
        defaultEstablishmentShouldNotBeFound("closeTime.lessThanOrEqual=" + SMALLER_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByCloseTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where closeTime is less than DEFAULT_CLOSE_TIME
        defaultEstablishmentShouldNotBeFound("closeTime.lessThan=" + DEFAULT_CLOSE_TIME);

        // Get all the establishmentList where closeTime is less than UPDATED_CLOSE_TIME
        defaultEstablishmentShouldBeFound("closeTime.lessThan=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByCloseTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where closeTime is greater than DEFAULT_CLOSE_TIME
        defaultEstablishmentShouldNotBeFound("closeTime.greaterThan=" + DEFAULT_CLOSE_TIME);

        // Get all the establishmentList where closeTime is greater than SMALLER_CLOSE_TIME
        defaultEstablishmentShouldBeFound("closeTime.greaterThan=" + SMALLER_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where longitude equals to DEFAULT_LONGITUDE
        defaultEstablishmentShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the establishmentList where longitude equals to UPDATED_LONGITUDE
        defaultEstablishmentShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultEstablishmentShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the establishmentList where longitude equals to UPDATED_LONGITUDE
        defaultEstablishmentShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where longitude is not null
        defaultEstablishmentShouldBeFound("longitude.specified=true");

        // Get all the establishmentList where longitude is null
        defaultEstablishmentShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultEstablishmentShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the establishmentList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultEstablishmentShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultEstablishmentShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the establishmentList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultEstablishmentShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where longitude is less than DEFAULT_LONGITUDE
        defaultEstablishmentShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the establishmentList where longitude is less than UPDATED_LONGITUDE
        defaultEstablishmentShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where longitude is greater than DEFAULT_LONGITUDE
        defaultEstablishmentShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the establishmentList where longitude is greater than SMALLER_LONGITUDE
        defaultEstablishmentShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where latitude equals to DEFAULT_LATITUDE
        defaultEstablishmentShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the establishmentList where latitude equals to UPDATED_LATITUDE
        defaultEstablishmentShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultEstablishmentShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the establishmentList where latitude equals to UPDATED_LATITUDE
        defaultEstablishmentShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where latitude is not null
        defaultEstablishmentShouldBeFound("latitude.specified=true");

        // Get all the establishmentList where latitude is null
        defaultEstablishmentShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultEstablishmentShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the establishmentList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultEstablishmentShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultEstablishmentShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the establishmentList where latitude is less than or equal to SMALLER_LATITUDE
        defaultEstablishmentShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where latitude is less than DEFAULT_LATITUDE
        defaultEstablishmentShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the establishmentList where latitude is less than UPDATED_LATITUDE
        defaultEstablishmentShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where latitude is greater than DEFAULT_LATITUDE
        defaultEstablishmentShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the establishmentList where latitude is greater than SMALLER_LATITUDE
        defaultEstablishmentShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByMercantileRegistrationIsEqualToSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where mercantileRegistration equals to DEFAULT_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldBeFound("mercantileRegistration.equals=" + DEFAULT_MERCANTILE_REGISTRATION);

        // Get all the establishmentList where mercantileRegistration equals to UPDATED_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldNotBeFound("mercantileRegistration.equals=" + UPDATED_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByMercantileRegistrationIsInShouldWork() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where mercantileRegistration in DEFAULT_MERCANTILE_REGISTRATION or UPDATED_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldBeFound(
            "mercantileRegistration.in=" + DEFAULT_MERCANTILE_REGISTRATION + "," + UPDATED_MERCANTILE_REGISTRATION
        );

        // Get all the establishmentList where mercantileRegistration equals to UPDATED_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldNotBeFound("mercantileRegistration.in=" + UPDATED_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByMercantileRegistrationIsNullOrNotNull() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where mercantileRegistration is not null
        defaultEstablishmentShouldBeFound("mercantileRegistration.specified=true");

        // Get all the establishmentList where mercantileRegistration is null
        defaultEstablishmentShouldNotBeFound("mercantileRegistration.specified=false");
    }

    @Test
    @Transactional
    void getAllEstablishmentsByMercantileRegistrationContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where mercantileRegistration contains DEFAULT_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldBeFound("mercantileRegistration.contains=" + DEFAULT_MERCANTILE_REGISTRATION);

        // Get all the establishmentList where mercantileRegistration contains UPDATED_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldNotBeFound("mercantileRegistration.contains=" + UPDATED_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByMercantileRegistrationNotContainsSomething() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        // Get all the establishmentList where mercantileRegistration does not contain DEFAULT_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldNotBeFound("mercantileRegistration.doesNotContain=" + DEFAULT_MERCANTILE_REGISTRATION);

        // Get all the establishmentList where mercantileRegistration does not contain UPDATED_MERCANTILE_REGISTRATION
        defaultEstablishmentShouldBeFound("mercantileRegistration.doesNotContain=" + UPDATED_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void getAllEstablishmentsByOperatorIsEqualToSomething() throws Exception {
        Operator operator;
        if (TestUtil.findAll(em, Operator.class).isEmpty()) {
            establishmentRepository.saveAndFlush(establishment);
            operator = OperatorResourceIT.createEntity(em);
        } else {
            operator = TestUtil.findAll(em, Operator.class).get(0);
        }
        em.persist(operator);
        em.flush();
        establishment.setOperator(operator);
        establishmentRepository.saveAndFlush(establishment);
        Long operatorId = operator.getId();

        // Get all the establishmentList where operator equals to operatorId
        defaultEstablishmentShouldBeFound("operatorId.equals=" + operatorId);

        // Get all the establishmentList where operator equals to (operatorId + 1)
        defaultEstablishmentShouldNotBeFound("operatorId.equals=" + (operatorId + 1));
    }

    @Test
    @Transactional
    void getAllEstablishmentsByMunicipalityIsEqualToSomething() throws Exception {
        Municipality municipality;
        if (TestUtil.findAll(em, Municipality.class).isEmpty()) {
            establishmentRepository.saveAndFlush(establishment);
            municipality = MunicipalityResourceIT.createEntity(em);
        } else {
            municipality = TestUtil.findAll(em, Municipality.class).get(0);
        }
        em.persist(municipality);
        em.flush();
        establishment.setMunicipality(municipality);
        establishmentRepository.saveAndFlush(establishment);
        Long municipalityId = municipality.getId();

        // Get all the establishmentList where municipality equals to municipalityId
        defaultEstablishmentShouldBeFound("municipalityId.equals=" + municipalityId);

        // Get all the establishmentList where municipality equals to (municipalityId + 1)
        defaultEstablishmentShouldNotBeFound("municipalityId.equals=" + (municipalityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstablishmentShouldBeFound(String filter) throws Exception {
        restEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(establishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].liquidationTime").value(hasItem(sameInstant(DEFAULT_LIQUIDATION_TIME))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].neighborhood").value(hasItem(DEFAULT_NEIGHBORHOOD)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].coljuegosCode").value(hasItem(DEFAULT_COLJUEGOS_CODE)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(sameInstant(DEFAULT_CLOSE_TIME))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].mercantileRegistration").value(hasItem(DEFAULT_MERCANTILE_REGISTRATION)));

        // Check, that the count call also returns 1
        restEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstablishmentShouldNotBeFound(String filter) throws Exception {
        restEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstablishment() throws Exception {
        // Get the establishment
        restEstablishmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstablishment() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();

        // Update the establishment
        Establishment updatedEstablishment = establishmentRepository.findById(establishment.getId()).get();
        // Disconnect from session so that the updates on updatedEstablishment are not directly saved in db
        em.detach(updatedEstablishment);
        updatedEstablishment
            .liquidationTime(UPDATED_LIQUIDATION_TIME)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .neighborhood(UPDATED_NEIGHBORHOOD)
            .address(UPDATED_ADDRESS)
            .coljuegosCode(UPDATED_COLJUEGOS_CODE)
            .startTime(UPDATED_START_TIME)
            .closeTime(UPDATED_CLOSE_TIME)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .mercantileRegistration(UPDATED_MERCANTILE_REGISTRATION);
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(updatedEstablishment);

        restEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, establishmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
        Establishment testEstablishment = establishmentList.get(establishmentList.size() - 1);
        assertThat(testEstablishment.getLiquidationTime()).isEqualTo(UPDATED_LIQUIDATION_TIME);
        assertThat(testEstablishment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEstablishment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEstablishment.getNeighborhood()).isEqualTo(UPDATED_NEIGHBORHOOD);
        assertThat(testEstablishment.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEstablishment.getColjuegosCode()).isEqualTo(UPDATED_COLJUEGOS_CODE);
        assertThat(testEstablishment.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEstablishment.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
        assertThat(testEstablishment.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEstablishment.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEstablishment.getMercantileRegistration()).isEqualTo(UPDATED_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void putNonExistingEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();
        establishment.setId(count.incrementAndGet());

        // Create the Establishment
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, establishmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();
        establishment.setId(count.incrementAndGet());

        // Create the Establishment
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();
        establishment.setId(count.incrementAndGet());

        // Create the Establishment
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstablishmentWithPatch() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();

        // Update the establishment using partial update
        Establishment partialUpdatedEstablishment = new Establishment();
        partialUpdatedEstablishment.setId(establishment.getId());

        partialUpdatedEstablishment
            .type(UPDATED_TYPE)
            .neighborhood(UPDATED_NEIGHBORHOOD)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);

        restEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstablishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstablishment))
            )
            .andExpect(status().isOk());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
        Establishment testEstablishment = establishmentList.get(establishmentList.size() - 1);
        assertThat(testEstablishment.getLiquidationTime()).isEqualTo(DEFAULT_LIQUIDATION_TIME);
        assertThat(testEstablishment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEstablishment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEstablishment.getNeighborhood()).isEqualTo(UPDATED_NEIGHBORHOOD);
        assertThat(testEstablishment.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEstablishment.getColjuegosCode()).isEqualTo(DEFAULT_COLJUEGOS_CODE);
        assertThat(testEstablishment.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testEstablishment.getCloseTime()).isEqualTo(DEFAULT_CLOSE_TIME);
        assertThat(testEstablishment.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEstablishment.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEstablishment.getMercantileRegistration()).isEqualTo(DEFAULT_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void fullUpdateEstablishmentWithPatch() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();

        // Update the establishment using partial update
        Establishment partialUpdatedEstablishment = new Establishment();
        partialUpdatedEstablishment.setId(establishment.getId());

        partialUpdatedEstablishment
            .liquidationTime(UPDATED_LIQUIDATION_TIME)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .neighborhood(UPDATED_NEIGHBORHOOD)
            .address(UPDATED_ADDRESS)
            .coljuegosCode(UPDATED_COLJUEGOS_CODE)
            .startTime(UPDATED_START_TIME)
            .closeTime(UPDATED_CLOSE_TIME)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .mercantileRegistration(UPDATED_MERCANTILE_REGISTRATION);

        restEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstablishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstablishment))
            )
            .andExpect(status().isOk());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
        Establishment testEstablishment = establishmentList.get(establishmentList.size() - 1);
        assertThat(testEstablishment.getLiquidationTime()).isEqualTo(UPDATED_LIQUIDATION_TIME);
        assertThat(testEstablishment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEstablishment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEstablishment.getNeighborhood()).isEqualTo(UPDATED_NEIGHBORHOOD);
        assertThat(testEstablishment.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEstablishment.getColjuegosCode()).isEqualTo(UPDATED_COLJUEGOS_CODE);
        assertThat(testEstablishment.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEstablishment.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
        assertThat(testEstablishment.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEstablishment.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEstablishment.getMercantileRegistration()).isEqualTo(UPDATED_MERCANTILE_REGISTRATION);
    }

    @Test
    @Transactional
    void patchNonExistingEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();
        establishment.setId(count.incrementAndGet());

        // Create the Establishment
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, establishmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();
        establishment.setId(count.incrementAndGet());

        // Create the Establishment
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = establishmentRepository.findAll().size();
        establishment.setId(count.incrementAndGet());

        // Create the Establishment
        EstablishmentDTO establishmentDTO = establishmentMapper.toDto(establishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(establishmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Establishment in the database
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstablishment() throws Exception {
        // Initialize the database
        establishmentRepository.saveAndFlush(establishment);

        int databaseSizeBeforeDelete = establishmentRepository.findAll().size();

        // Delete the establishment
        restEstablishmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, establishment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Establishment> establishmentList = establishmentRepository.findAll();
        assertThat(establishmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
