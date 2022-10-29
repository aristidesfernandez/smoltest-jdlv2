package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Device;
import co.com.ies.smol.domain.DeviceEstablishment;
import co.com.ies.smol.repository.DeviceEstablishmentRepository;
import co.com.ies.smol.service.criteria.DeviceEstablishmentCriteria;
import co.com.ies.smol.service.dto.DeviceEstablishmentDTO;
import co.com.ies.smol.service.mapper.DeviceEstablishmentMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link DeviceEstablishmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceEstablishmentResourceIT {

    private static final ZonedDateTime DEFAULT_REGISTRATION_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGISTRATION_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REGISTRATION_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DEPARTURE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEPARTURE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DEPARTURE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_DEVICE_NUMBER = 1;
    private static final Integer UPDATED_DEVICE_NUMBER = 2;
    private static final Integer SMALLER_DEVICE_NUMBER = 1 - 1;

    private static final Integer DEFAULT_CONSECUTIVE_DEVICE = 1;
    private static final Integer UPDATED_CONSECUTIVE_DEVICE = 2;
    private static final Integer SMALLER_CONSECUTIVE_DEVICE = 1 - 1;

    private static final Long DEFAULT_ESTABLISHMENT_ID = 1L;
    private static final Long UPDATED_ESTABLISHMENT_ID = 2L;
    private static final Long SMALLER_ESTABLISHMENT_ID = 1L - 1L;

    private static final Float DEFAULT_NEGATIVE_AWARD = 1F;
    private static final Float UPDATED_NEGATIVE_AWARD = 2F;
    private static final Float SMALLER_NEGATIVE_AWARD = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/device-establishments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeviceEstablishmentRepository deviceEstablishmentRepository;

    @Autowired
    private DeviceEstablishmentMapper deviceEstablishmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceEstablishmentMockMvc;

    private DeviceEstablishment deviceEstablishment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceEstablishment createEntity(EntityManager em) {
        DeviceEstablishment deviceEstablishment = new DeviceEstablishment()
            .registrationAt(DEFAULT_REGISTRATION_AT)
            .departureAt(DEFAULT_DEPARTURE_AT)
            .deviceNumber(DEFAULT_DEVICE_NUMBER)
            .consecutiveDevice(DEFAULT_CONSECUTIVE_DEVICE)
            .establishmentId(DEFAULT_ESTABLISHMENT_ID)
            .negativeAward(DEFAULT_NEGATIVE_AWARD);
        return deviceEstablishment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceEstablishment createUpdatedEntity(EntityManager em) {
        DeviceEstablishment deviceEstablishment = new DeviceEstablishment()
            .registrationAt(UPDATED_REGISTRATION_AT)
            .departureAt(UPDATED_DEPARTURE_AT)
            .deviceNumber(UPDATED_DEVICE_NUMBER)
            .consecutiveDevice(UPDATED_CONSECUTIVE_DEVICE)
            .establishmentId(UPDATED_ESTABLISHMENT_ID)
            .negativeAward(UPDATED_NEGATIVE_AWARD);
        return deviceEstablishment;
    }

    @BeforeEach
    public void initTest() {
        deviceEstablishment = createEntity(em);
    }

    @Test
    @Transactional
    void createDeviceEstablishment() throws Exception {
        int databaseSizeBeforeCreate = deviceEstablishmentRepository.findAll().size();
        // Create the DeviceEstablishment
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);
        restDeviceEstablishmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceEstablishment testDeviceEstablishment = deviceEstablishmentList.get(deviceEstablishmentList.size() - 1);
        assertThat(testDeviceEstablishment.getRegistrationAt()).isEqualTo(DEFAULT_REGISTRATION_AT);
        assertThat(testDeviceEstablishment.getDepartureAt()).isEqualTo(DEFAULT_DEPARTURE_AT);
        assertThat(testDeviceEstablishment.getDeviceNumber()).isEqualTo(DEFAULT_DEVICE_NUMBER);
        assertThat(testDeviceEstablishment.getConsecutiveDevice()).isEqualTo(DEFAULT_CONSECUTIVE_DEVICE);
        assertThat(testDeviceEstablishment.getEstablishmentId()).isEqualTo(DEFAULT_ESTABLISHMENT_ID);
        assertThat(testDeviceEstablishment.getNegativeAward()).isEqualTo(DEFAULT_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void createDeviceEstablishmentWithExistingId() throws Exception {
        // Create the DeviceEstablishment with an existing ID
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        int databaseSizeBeforeCreate = deviceEstablishmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceEstablishmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRegistrationAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceEstablishmentRepository.findAll().size();
        // set the field null
        deviceEstablishment.setRegistrationAt(null);

        // Create the DeviceEstablishment, which fails.
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        restDeviceEstablishmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishments() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList
        restDeviceEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceEstablishment.getId().toString())))
            .andExpect(jsonPath("$.[*].registrationAt").value(hasItem(sameInstant(DEFAULT_REGISTRATION_AT))))
            .andExpect(jsonPath("$.[*].departureAt").value(hasItem(sameInstant(DEFAULT_DEPARTURE_AT))))
            .andExpect(jsonPath("$.[*].deviceNumber").value(hasItem(DEFAULT_DEVICE_NUMBER)))
            .andExpect(jsonPath("$.[*].consecutiveDevice").value(hasItem(DEFAULT_CONSECUTIVE_DEVICE)))
            .andExpect(jsonPath("$.[*].establishmentId").value(hasItem(DEFAULT_ESTABLISHMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].negativeAward").value(hasItem(DEFAULT_NEGATIVE_AWARD.doubleValue())));
    }

    @Test
    @Transactional
    void getDeviceEstablishment() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get the deviceEstablishment
        restDeviceEstablishmentMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceEstablishment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceEstablishment.getId().toString()))
            .andExpect(jsonPath("$.registrationAt").value(sameInstant(DEFAULT_REGISTRATION_AT)))
            .andExpect(jsonPath("$.departureAt").value(sameInstant(DEFAULT_DEPARTURE_AT)))
            .andExpect(jsonPath("$.deviceNumber").value(DEFAULT_DEVICE_NUMBER))
            .andExpect(jsonPath("$.consecutiveDevice").value(DEFAULT_CONSECUTIVE_DEVICE))
            .andExpect(jsonPath("$.establishmentId").value(DEFAULT_ESTABLISHMENT_ID.intValue()))
            .andExpect(jsonPath("$.negativeAward").value(DEFAULT_NEGATIVE_AWARD.doubleValue()));
    }

    @Test
    @Transactional
    void getDeviceEstablishmentsByIdFiltering() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        UUID id = deviceEstablishment.getId();

        defaultDeviceEstablishmentShouldBeFound("id.equals=" + id);
        defaultDeviceEstablishmentShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByRegistrationAtIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where registrationAt equals to DEFAULT_REGISTRATION_AT
        defaultDeviceEstablishmentShouldBeFound("registrationAt.equals=" + DEFAULT_REGISTRATION_AT);

        // Get all the deviceEstablishmentList where registrationAt equals to UPDATED_REGISTRATION_AT
        defaultDeviceEstablishmentShouldNotBeFound("registrationAt.equals=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByRegistrationAtIsInShouldWork() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where registrationAt in DEFAULT_REGISTRATION_AT or UPDATED_REGISTRATION_AT
        defaultDeviceEstablishmentShouldBeFound("registrationAt.in=" + DEFAULT_REGISTRATION_AT + "," + UPDATED_REGISTRATION_AT);

        // Get all the deviceEstablishmentList where registrationAt equals to UPDATED_REGISTRATION_AT
        defaultDeviceEstablishmentShouldNotBeFound("registrationAt.in=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByRegistrationAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where registrationAt is not null
        defaultDeviceEstablishmentShouldBeFound("registrationAt.specified=true");

        // Get all the deviceEstablishmentList where registrationAt is null
        defaultDeviceEstablishmentShouldNotBeFound("registrationAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByRegistrationAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where registrationAt is greater than or equal to DEFAULT_REGISTRATION_AT
        defaultDeviceEstablishmentShouldBeFound("registrationAt.greaterThanOrEqual=" + DEFAULT_REGISTRATION_AT);

        // Get all the deviceEstablishmentList where registrationAt is greater than or equal to UPDATED_REGISTRATION_AT
        defaultDeviceEstablishmentShouldNotBeFound("registrationAt.greaterThanOrEqual=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByRegistrationAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where registrationAt is less than or equal to DEFAULT_REGISTRATION_AT
        defaultDeviceEstablishmentShouldBeFound("registrationAt.lessThanOrEqual=" + DEFAULT_REGISTRATION_AT);

        // Get all the deviceEstablishmentList where registrationAt is less than or equal to SMALLER_REGISTRATION_AT
        defaultDeviceEstablishmentShouldNotBeFound("registrationAt.lessThanOrEqual=" + SMALLER_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByRegistrationAtIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where registrationAt is less than DEFAULT_REGISTRATION_AT
        defaultDeviceEstablishmentShouldNotBeFound("registrationAt.lessThan=" + DEFAULT_REGISTRATION_AT);

        // Get all the deviceEstablishmentList where registrationAt is less than UPDATED_REGISTRATION_AT
        defaultDeviceEstablishmentShouldBeFound("registrationAt.lessThan=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByRegistrationAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where registrationAt is greater than DEFAULT_REGISTRATION_AT
        defaultDeviceEstablishmentShouldNotBeFound("registrationAt.greaterThan=" + DEFAULT_REGISTRATION_AT);

        // Get all the deviceEstablishmentList where registrationAt is greater than SMALLER_REGISTRATION_AT
        defaultDeviceEstablishmentShouldBeFound("registrationAt.greaterThan=" + SMALLER_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDepartureAtIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where departureAt equals to DEFAULT_DEPARTURE_AT
        defaultDeviceEstablishmentShouldBeFound("departureAt.equals=" + DEFAULT_DEPARTURE_AT);

        // Get all the deviceEstablishmentList where departureAt equals to UPDATED_DEPARTURE_AT
        defaultDeviceEstablishmentShouldNotBeFound("departureAt.equals=" + UPDATED_DEPARTURE_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDepartureAtIsInShouldWork() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where departureAt in DEFAULT_DEPARTURE_AT or UPDATED_DEPARTURE_AT
        defaultDeviceEstablishmentShouldBeFound("departureAt.in=" + DEFAULT_DEPARTURE_AT + "," + UPDATED_DEPARTURE_AT);

        // Get all the deviceEstablishmentList where departureAt equals to UPDATED_DEPARTURE_AT
        defaultDeviceEstablishmentShouldNotBeFound("departureAt.in=" + UPDATED_DEPARTURE_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDepartureAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where departureAt is not null
        defaultDeviceEstablishmentShouldBeFound("departureAt.specified=true");

        // Get all the deviceEstablishmentList where departureAt is null
        defaultDeviceEstablishmentShouldNotBeFound("departureAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDepartureAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where departureAt is greater than or equal to DEFAULT_DEPARTURE_AT
        defaultDeviceEstablishmentShouldBeFound("departureAt.greaterThanOrEqual=" + DEFAULT_DEPARTURE_AT);

        // Get all the deviceEstablishmentList where departureAt is greater than or equal to UPDATED_DEPARTURE_AT
        defaultDeviceEstablishmentShouldNotBeFound("departureAt.greaterThanOrEqual=" + UPDATED_DEPARTURE_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDepartureAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where departureAt is less than or equal to DEFAULT_DEPARTURE_AT
        defaultDeviceEstablishmentShouldBeFound("departureAt.lessThanOrEqual=" + DEFAULT_DEPARTURE_AT);

        // Get all the deviceEstablishmentList where departureAt is less than or equal to SMALLER_DEPARTURE_AT
        defaultDeviceEstablishmentShouldNotBeFound("departureAt.lessThanOrEqual=" + SMALLER_DEPARTURE_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDepartureAtIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where departureAt is less than DEFAULT_DEPARTURE_AT
        defaultDeviceEstablishmentShouldNotBeFound("departureAt.lessThan=" + DEFAULT_DEPARTURE_AT);

        // Get all the deviceEstablishmentList where departureAt is less than UPDATED_DEPARTURE_AT
        defaultDeviceEstablishmentShouldBeFound("departureAt.lessThan=" + UPDATED_DEPARTURE_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDepartureAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where departureAt is greater than DEFAULT_DEPARTURE_AT
        defaultDeviceEstablishmentShouldNotBeFound("departureAt.greaterThan=" + DEFAULT_DEPARTURE_AT);

        // Get all the deviceEstablishmentList where departureAt is greater than SMALLER_DEPARTURE_AT
        defaultDeviceEstablishmentShouldBeFound("departureAt.greaterThan=" + SMALLER_DEPARTURE_AT);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where deviceNumber equals to DEFAULT_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldBeFound("deviceNumber.equals=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceEstablishmentList where deviceNumber equals to UPDATED_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldNotBeFound("deviceNumber.equals=" + UPDATED_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where deviceNumber in DEFAULT_DEVICE_NUMBER or UPDATED_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldBeFound("deviceNumber.in=" + DEFAULT_DEVICE_NUMBER + "," + UPDATED_DEVICE_NUMBER);

        // Get all the deviceEstablishmentList where deviceNumber equals to UPDATED_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldNotBeFound("deviceNumber.in=" + UPDATED_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where deviceNumber is not null
        defaultDeviceEstablishmentShouldBeFound("deviceNumber.specified=true");

        // Get all the deviceEstablishmentList where deviceNumber is null
        defaultDeviceEstablishmentShouldNotBeFound("deviceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where deviceNumber is greater than or equal to DEFAULT_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldBeFound("deviceNumber.greaterThanOrEqual=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceEstablishmentList where deviceNumber is greater than or equal to UPDATED_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldNotBeFound("deviceNumber.greaterThanOrEqual=" + UPDATED_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where deviceNumber is less than or equal to DEFAULT_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldBeFound("deviceNumber.lessThanOrEqual=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceEstablishmentList where deviceNumber is less than or equal to SMALLER_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldNotBeFound("deviceNumber.lessThanOrEqual=" + SMALLER_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where deviceNumber is less than DEFAULT_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldNotBeFound("deviceNumber.lessThan=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceEstablishmentList where deviceNumber is less than UPDATED_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldBeFound("deviceNumber.lessThan=" + UPDATED_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where deviceNumber is greater than DEFAULT_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldNotBeFound("deviceNumber.greaterThan=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceEstablishmentList where deviceNumber is greater than SMALLER_DEVICE_NUMBER
        defaultDeviceEstablishmentShouldBeFound("deviceNumber.greaterThan=" + SMALLER_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByConsecutiveDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where consecutiveDevice equals to DEFAULT_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldBeFound("consecutiveDevice.equals=" + DEFAULT_CONSECUTIVE_DEVICE);

        // Get all the deviceEstablishmentList where consecutiveDevice equals to UPDATED_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldNotBeFound("consecutiveDevice.equals=" + UPDATED_CONSECUTIVE_DEVICE);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByConsecutiveDeviceIsInShouldWork() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where consecutiveDevice in DEFAULT_CONSECUTIVE_DEVICE or UPDATED_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldBeFound("consecutiveDevice.in=" + DEFAULT_CONSECUTIVE_DEVICE + "," + UPDATED_CONSECUTIVE_DEVICE);

        // Get all the deviceEstablishmentList where consecutiveDevice equals to UPDATED_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldNotBeFound("consecutiveDevice.in=" + UPDATED_CONSECUTIVE_DEVICE);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByConsecutiveDeviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where consecutiveDevice is not null
        defaultDeviceEstablishmentShouldBeFound("consecutiveDevice.specified=true");

        // Get all the deviceEstablishmentList where consecutiveDevice is null
        defaultDeviceEstablishmentShouldNotBeFound("consecutiveDevice.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByConsecutiveDeviceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where consecutiveDevice is greater than or equal to DEFAULT_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldBeFound("consecutiveDevice.greaterThanOrEqual=" + DEFAULT_CONSECUTIVE_DEVICE);

        // Get all the deviceEstablishmentList where consecutiveDevice is greater than or equal to UPDATED_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldNotBeFound("consecutiveDevice.greaterThanOrEqual=" + UPDATED_CONSECUTIVE_DEVICE);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByConsecutiveDeviceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where consecutiveDevice is less than or equal to DEFAULT_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldBeFound("consecutiveDevice.lessThanOrEqual=" + DEFAULT_CONSECUTIVE_DEVICE);

        // Get all the deviceEstablishmentList where consecutiveDevice is less than or equal to SMALLER_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldNotBeFound("consecutiveDevice.lessThanOrEqual=" + SMALLER_CONSECUTIVE_DEVICE);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByConsecutiveDeviceIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where consecutiveDevice is less than DEFAULT_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldNotBeFound("consecutiveDevice.lessThan=" + DEFAULT_CONSECUTIVE_DEVICE);

        // Get all the deviceEstablishmentList where consecutiveDevice is less than UPDATED_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldBeFound("consecutiveDevice.lessThan=" + UPDATED_CONSECUTIVE_DEVICE);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByConsecutiveDeviceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where consecutiveDevice is greater than DEFAULT_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldNotBeFound("consecutiveDevice.greaterThan=" + DEFAULT_CONSECUTIVE_DEVICE);

        // Get all the deviceEstablishmentList where consecutiveDevice is greater than SMALLER_CONSECUTIVE_DEVICE
        defaultDeviceEstablishmentShouldBeFound("consecutiveDevice.greaterThan=" + SMALLER_CONSECUTIVE_DEVICE);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByEstablishmentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where establishmentId equals to DEFAULT_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldBeFound("establishmentId.equals=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceEstablishmentList where establishmentId equals to UPDATED_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldNotBeFound("establishmentId.equals=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByEstablishmentIdIsInShouldWork() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where establishmentId in DEFAULT_ESTABLISHMENT_ID or UPDATED_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldBeFound("establishmentId.in=" + DEFAULT_ESTABLISHMENT_ID + "," + UPDATED_ESTABLISHMENT_ID);

        // Get all the deviceEstablishmentList where establishmentId equals to UPDATED_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldNotBeFound("establishmentId.in=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByEstablishmentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where establishmentId is not null
        defaultDeviceEstablishmentShouldBeFound("establishmentId.specified=true");

        // Get all the deviceEstablishmentList where establishmentId is null
        defaultDeviceEstablishmentShouldNotBeFound("establishmentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByEstablishmentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where establishmentId is greater than or equal to DEFAULT_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldBeFound("establishmentId.greaterThanOrEqual=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceEstablishmentList where establishmentId is greater than or equal to UPDATED_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldNotBeFound("establishmentId.greaterThanOrEqual=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByEstablishmentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where establishmentId is less than or equal to DEFAULT_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldBeFound("establishmentId.lessThanOrEqual=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceEstablishmentList where establishmentId is less than or equal to SMALLER_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldNotBeFound("establishmentId.lessThanOrEqual=" + SMALLER_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByEstablishmentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where establishmentId is less than DEFAULT_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldNotBeFound("establishmentId.lessThan=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceEstablishmentList where establishmentId is less than UPDATED_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldBeFound("establishmentId.lessThan=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByEstablishmentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where establishmentId is greater than DEFAULT_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldNotBeFound("establishmentId.greaterThan=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceEstablishmentList where establishmentId is greater than SMALLER_ESTABLISHMENT_ID
        defaultDeviceEstablishmentShouldBeFound("establishmentId.greaterThan=" + SMALLER_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByNegativeAwardIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where negativeAward equals to DEFAULT_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldBeFound("negativeAward.equals=" + DEFAULT_NEGATIVE_AWARD);

        // Get all the deviceEstablishmentList where negativeAward equals to UPDATED_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldNotBeFound("negativeAward.equals=" + UPDATED_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByNegativeAwardIsInShouldWork() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where negativeAward in DEFAULT_NEGATIVE_AWARD or UPDATED_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldBeFound("negativeAward.in=" + DEFAULT_NEGATIVE_AWARD + "," + UPDATED_NEGATIVE_AWARD);

        // Get all the deviceEstablishmentList where negativeAward equals to UPDATED_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldNotBeFound("negativeAward.in=" + UPDATED_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByNegativeAwardIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where negativeAward is not null
        defaultDeviceEstablishmentShouldBeFound("negativeAward.specified=true");

        // Get all the deviceEstablishmentList where negativeAward is null
        defaultDeviceEstablishmentShouldNotBeFound("negativeAward.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByNegativeAwardIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where negativeAward is greater than or equal to DEFAULT_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldBeFound("negativeAward.greaterThanOrEqual=" + DEFAULT_NEGATIVE_AWARD);

        // Get all the deviceEstablishmentList where negativeAward is greater than or equal to UPDATED_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldNotBeFound("negativeAward.greaterThanOrEqual=" + UPDATED_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByNegativeAwardIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where negativeAward is less than or equal to DEFAULT_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldBeFound("negativeAward.lessThanOrEqual=" + DEFAULT_NEGATIVE_AWARD);

        // Get all the deviceEstablishmentList where negativeAward is less than or equal to SMALLER_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldNotBeFound("negativeAward.lessThanOrEqual=" + SMALLER_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByNegativeAwardIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where negativeAward is less than DEFAULT_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldNotBeFound("negativeAward.lessThan=" + DEFAULT_NEGATIVE_AWARD);

        // Get all the deviceEstablishmentList where negativeAward is less than UPDATED_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldBeFound("negativeAward.lessThan=" + UPDATED_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByNegativeAwardIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        // Get all the deviceEstablishmentList where negativeAward is greater than DEFAULT_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldNotBeFound("negativeAward.greaterThan=" + DEFAULT_NEGATIVE_AWARD);

        // Get all the deviceEstablishmentList where negativeAward is greater than SMALLER_NEGATIVE_AWARD
        defaultDeviceEstablishmentShouldBeFound("negativeAward.greaterThan=" + SMALLER_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void getAllDeviceEstablishmentsByDeviceIsEqualToSomething() throws Exception {
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);
            device = DeviceResourceIT.createEntity(em);
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        em.persist(device);
        em.flush();
        deviceEstablishment.setDevice(device);
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);
        UUID deviceId = device.getId();

        // Get all the deviceEstablishmentList where device equals to deviceId
        defaultDeviceEstablishmentShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the deviceEstablishmentList where device equals to UUID.randomUUID()
        defaultDeviceEstablishmentShouldNotBeFound("deviceId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceEstablishmentShouldBeFound(String filter) throws Exception {
        restDeviceEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceEstablishment.getId().toString())))
            .andExpect(jsonPath("$.[*].registrationAt").value(hasItem(sameInstant(DEFAULT_REGISTRATION_AT))))
            .andExpect(jsonPath("$.[*].departureAt").value(hasItem(sameInstant(DEFAULT_DEPARTURE_AT))))
            .andExpect(jsonPath("$.[*].deviceNumber").value(hasItem(DEFAULT_DEVICE_NUMBER)))
            .andExpect(jsonPath("$.[*].consecutiveDevice").value(hasItem(DEFAULT_CONSECUTIVE_DEVICE)))
            .andExpect(jsonPath("$.[*].establishmentId").value(hasItem(DEFAULT_ESTABLISHMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].negativeAward").value(hasItem(DEFAULT_NEGATIVE_AWARD.doubleValue())));

        // Check, that the count call also returns 1
        restDeviceEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceEstablishmentShouldNotBeFound(String filter) throws Exception {
        restDeviceEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeviceEstablishment() throws Exception {
        // Get the deviceEstablishment
        restDeviceEstablishmentMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeviceEstablishment() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();

        // Update the deviceEstablishment
        DeviceEstablishment updatedDeviceEstablishment = deviceEstablishmentRepository.findById(deviceEstablishment.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceEstablishment are not directly saved in db
        em.detach(updatedDeviceEstablishment);
        updatedDeviceEstablishment
            .registrationAt(UPDATED_REGISTRATION_AT)
            .departureAt(UPDATED_DEPARTURE_AT)
            .deviceNumber(UPDATED_DEVICE_NUMBER)
            .consecutiveDevice(UPDATED_CONSECUTIVE_DEVICE)
            .establishmentId(UPDATED_ESTABLISHMENT_ID)
            .negativeAward(UPDATED_NEGATIVE_AWARD);
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(updatedDeviceEstablishment);

        restDeviceEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceEstablishmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
        DeviceEstablishment testDeviceEstablishment = deviceEstablishmentList.get(deviceEstablishmentList.size() - 1);
        assertThat(testDeviceEstablishment.getRegistrationAt()).isEqualTo(UPDATED_REGISTRATION_AT);
        assertThat(testDeviceEstablishment.getDepartureAt()).isEqualTo(UPDATED_DEPARTURE_AT);
        assertThat(testDeviceEstablishment.getDeviceNumber()).isEqualTo(UPDATED_DEVICE_NUMBER);
        assertThat(testDeviceEstablishment.getConsecutiveDevice()).isEqualTo(UPDATED_CONSECUTIVE_DEVICE);
        assertThat(testDeviceEstablishment.getEstablishmentId()).isEqualTo(UPDATED_ESTABLISHMENT_ID);
        assertThat(testDeviceEstablishment.getNegativeAward()).isEqualTo(UPDATED_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void putNonExistingDeviceEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();
        deviceEstablishment.setId(UUID.randomUUID());

        // Create the DeviceEstablishment
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceEstablishmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();
        deviceEstablishment.setId(UUID.randomUUID());

        // Create the DeviceEstablishment
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();
        deviceEstablishment.setId(UUID.randomUUID());

        // Create the DeviceEstablishment
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceEstablishmentWithPatch() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();

        // Update the deviceEstablishment using partial update
        DeviceEstablishment partialUpdatedDeviceEstablishment = new DeviceEstablishment();
        partialUpdatedDeviceEstablishment.setId(deviceEstablishment.getId());

        partialUpdatedDeviceEstablishment.deviceNumber(UPDATED_DEVICE_NUMBER).consecutiveDevice(UPDATED_CONSECUTIVE_DEVICE);

        restDeviceEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceEstablishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceEstablishment))
            )
            .andExpect(status().isOk());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
        DeviceEstablishment testDeviceEstablishment = deviceEstablishmentList.get(deviceEstablishmentList.size() - 1);
        assertThat(testDeviceEstablishment.getRegistrationAt()).isEqualTo(DEFAULT_REGISTRATION_AT);
        assertThat(testDeviceEstablishment.getDepartureAt()).isEqualTo(DEFAULT_DEPARTURE_AT);
        assertThat(testDeviceEstablishment.getDeviceNumber()).isEqualTo(UPDATED_DEVICE_NUMBER);
        assertThat(testDeviceEstablishment.getConsecutiveDevice()).isEqualTo(UPDATED_CONSECUTIVE_DEVICE);
        assertThat(testDeviceEstablishment.getEstablishmentId()).isEqualTo(DEFAULT_ESTABLISHMENT_ID);
        assertThat(testDeviceEstablishment.getNegativeAward()).isEqualTo(DEFAULT_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void fullUpdateDeviceEstablishmentWithPatch() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();

        // Update the deviceEstablishment using partial update
        DeviceEstablishment partialUpdatedDeviceEstablishment = new DeviceEstablishment();
        partialUpdatedDeviceEstablishment.setId(deviceEstablishment.getId());

        partialUpdatedDeviceEstablishment
            .registrationAt(UPDATED_REGISTRATION_AT)
            .departureAt(UPDATED_DEPARTURE_AT)
            .deviceNumber(UPDATED_DEVICE_NUMBER)
            .consecutiveDevice(UPDATED_CONSECUTIVE_DEVICE)
            .establishmentId(UPDATED_ESTABLISHMENT_ID)
            .negativeAward(UPDATED_NEGATIVE_AWARD);

        restDeviceEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceEstablishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceEstablishment))
            )
            .andExpect(status().isOk());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
        DeviceEstablishment testDeviceEstablishment = deviceEstablishmentList.get(deviceEstablishmentList.size() - 1);
        assertThat(testDeviceEstablishment.getRegistrationAt()).isEqualTo(UPDATED_REGISTRATION_AT);
        assertThat(testDeviceEstablishment.getDepartureAt()).isEqualTo(UPDATED_DEPARTURE_AT);
        assertThat(testDeviceEstablishment.getDeviceNumber()).isEqualTo(UPDATED_DEVICE_NUMBER);
        assertThat(testDeviceEstablishment.getConsecutiveDevice()).isEqualTo(UPDATED_CONSECUTIVE_DEVICE);
        assertThat(testDeviceEstablishment.getEstablishmentId()).isEqualTo(UPDATED_ESTABLISHMENT_ID);
        assertThat(testDeviceEstablishment.getNegativeAward()).isEqualTo(UPDATED_NEGATIVE_AWARD);
    }

    @Test
    @Transactional
    void patchNonExistingDeviceEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();
        deviceEstablishment.setId(UUID.randomUUID());

        // Create the DeviceEstablishment
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceEstablishmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();
        deviceEstablishment.setId(UUID.randomUUID());

        // Create the DeviceEstablishment
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = deviceEstablishmentRepository.findAll().size();
        deviceEstablishment.setId(UUID.randomUUID());

        // Create the DeviceEstablishment
        DeviceEstablishmentDTO deviceEstablishmentDTO = deviceEstablishmentMapper.toDto(deviceEstablishment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceEstablishmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceEstablishment in the database
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceEstablishment() throws Exception {
        // Initialize the database
        deviceEstablishmentRepository.saveAndFlush(deviceEstablishment);

        int databaseSizeBeforeDelete = deviceEstablishmentRepository.findAll().size();

        // Delete the deviceEstablishment
        restDeviceEstablishmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceEstablishment.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceEstablishment> deviceEstablishmentList = deviceEstablishmentRepository.findAll();
        assertThat(deviceEstablishmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
