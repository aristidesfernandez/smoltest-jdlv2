package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Device;
import co.com.ies.smol.domain.DeviceInterface;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.enumeration.DeviceInterfaceStatus;
import co.com.ies.smol.repository.DeviceInterfaceRepository;
import co.com.ies.smol.service.criteria.DeviceInterfaceCriteria;
import co.com.ies.smol.service.dto.DeviceInterfaceDTO;
import co.com.ies.smol.service.mapper.DeviceInterfaceMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link DeviceInterfaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceInterfaceResourceIT {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_ESTABLISHMENT_ID = 1;
    private static final Integer UPDATED_ESTABLISHMENT_ID = 2;
    private static final Integer SMALLER_ESTABLISHMENT_ID = 1 - 1;

    private static final DeviceInterfaceStatus DEFAULT_STATE = DeviceInterfaceStatus.STOCK;
    private static final DeviceInterfaceStatus UPDATED_STATE = DeviceInterfaceStatus.OPERATION;

    private static final String ENTITY_API_URL = "/api/device-interfaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceInterfaceRepository deviceInterfaceRepository;

    @Autowired
    private DeviceInterfaceMapper deviceInterfaceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceInterfaceMockMvc;

    private DeviceInterface deviceInterface;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceInterface createEntity(EntityManager em) {
        DeviceInterface deviceInterface = new DeviceInterface()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .establishmentId(DEFAULT_ESTABLISHMENT_ID)
            .state(DEFAULT_STATE);
        return deviceInterface;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceInterface createUpdatedEntity(EntityManager em) {
        DeviceInterface deviceInterface = new DeviceInterface()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .establishmentId(UPDATED_ESTABLISHMENT_ID)
            .state(UPDATED_STATE);
        return deviceInterface;
    }

    @BeforeEach
    public void initTest() {
        deviceInterface = createEntity(em);
    }

    @Test
    @Transactional
    void createDeviceInterface() throws Exception {
        int databaseSizeBeforeCreate = deviceInterfaceRepository.findAll().size();
        // Create the DeviceInterface
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);
        restDeviceInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceInterface testDeviceInterface = deviceInterfaceList.get(deviceInterfaceList.size() - 1);
        assertThat(testDeviceInterface.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDeviceInterface.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDeviceInterface.getEstablishmentId()).isEqualTo(DEFAULT_ESTABLISHMENT_ID);
        assertThat(testDeviceInterface.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void createDeviceInterfaceWithExistingId() throws Exception {
        // Create the DeviceInterface with an existing ID
        deviceInterface.setId(1L);
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);

        int databaseSizeBeforeCreate = deviceInterfaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceInterfaceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeviceInterfaces() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList
        restDeviceInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceInterface.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].establishmentId").value(hasItem(DEFAULT_ESTABLISHMENT_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    void getDeviceInterface() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get the deviceInterface
        restDeviceInterfaceMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceInterface.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceInterface.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.establishmentId").value(DEFAULT_ESTABLISHMENT_ID))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getDeviceInterfacesByIdFiltering() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        Long id = deviceInterface.getId();

        defaultDeviceInterfaceShouldBeFound("id.equals=" + id);
        defaultDeviceInterfaceShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceInterfaceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceInterfaceShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceInterfaceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceInterfaceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where startDate equals to DEFAULT_START_DATE
        defaultDeviceInterfaceShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the deviceInterfaceList where startDate equals to UPDATED_START_DATE
        defaultDeviceInterfaceShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultDeviceInterfaceShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the deviceInterfaceList where startDate equals to UPDATED_START_DATE
        defaultDeviceInterfaceShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where startDate is not null
        defaultDeviceInterfaceShouldBeFound("startDate.specified=true");

        // Get all the deviceInterfaceList where startDate is null
        defaultDeviceInterfaceShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultDeviceInterfaceShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the deviceInterfaceList where startDate is greater than or equal to UPDATED_START_DATE
        defaultDeviceInterfaceShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where startDate is less than or equal to DEFAULT_START_DATE
        defaultDeviceInterfaceShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the deviceInterfaceList where startDate is less than or equal to SMALLER_START_DATE
        defaultDeviceInterfaceShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where startDate is less than DEFAULT_START_DATE
        defaultDeviceInterfaceShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the deviceInterfaceList where startDate is less than UPDATED_START_DATE
        defaultDeviceInterfaceShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where startDate is greater than DEFAULT_START_DATE
        defaultDeviceInterfaceShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the deviceInterfaceList where startDate is greater than SMALLER_START_DATE
        defaultDeviceInterfaceShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where endDate equals to DEFAULT_END_DATE
        defaultDeviceInterfaceShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the deviceInterfaceList where endDate equals to UPDATED_END_DATE
        defaultDeviceInterfaceShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultDeviceInterfaceShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the deviceInterfaceList where endDate equals to UPDATED_END_DATE
        defaultDeviceInterfaceShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where endDate is not null
        defaultDeviceInterfaceShouldBeFound("endDate.specified=true");

        // Get all the deviceInterfaceList where endDate is null
        defaultDeviceInterfaceShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultDeviceInterfaceShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the deviceInterfaceList where endDate is greater than or equal to UPDATED_END_DATE
        defaultDeviceInterfaceShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where endDate is less than or equal to DEFAULT_END_DATE
        defaultDeviceInterfaceShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the deviceInterfaceList where endDate is less than or equal to SMALLER_END_DATE
        defaultDeviceInterfaceShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where endDate is less than DEFAULT_END_DATE
        defaultDeviceInterfaceShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the deviceInterfaceList where endDate is less than UPDATED_END_DATE
        defaultDeviceInterfaceShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where endDate is greater than DEFAULT_END_DATE
        defaultDeviceInterfaceShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the deviceInterfaceList where endDate is greater than SMALLER_END_DATE
        defaultDeviceInterfaceShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEstablishmentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where establishmentId equals to DEFAULT_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldBeFound("establishmentId.equals=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceInterfaceList where establishmentId equals to UPDATED_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldNotBeFound("establishmentId.equals=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEstablishmentIdIsInShouldWork() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where establishmentId in DEFAULT_ESTABLISHMENT_ID or UPDATED_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldBeFound("establishmentId.in=" + DEFAULT_ESTABLISHMENT_ID + "," + UPDATED_ESTABLISHMENT_ID);

        // Get all the deviceInterfaceList where establishmentId equals to UPDATED_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldNotBeFound("establishmentId.in=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEstablishmentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where establishmentId is not null
        defaultDeviceInterfaceShouldBeFound("establishmentId.specified=true");

        // Get all the deviceInterfaceList where establishmentId is null
        defaultDeviceInterfaceShouldNotBeFound("establishmentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEstablishmentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where establishmentId is greater than or equal to DEFAULT_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldBeFound("establishmentId.greaterThanOrEqual=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceInterfaceList where establishmentId is greater than or equal to UPDATED_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldNotBeFound("establishmentId.greaterThanOrEqual=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEstablishmentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where establishmentId is less than or equal to DEFAULT_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldBeFound("establishmentId.lessThanOrEqual=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceInterfaceList where establishmentId is less than or equal to SMALLER_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldNotBeFound("establishmentId.lessThanOrEqual=" + SMALLER_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEstablishmentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where establishmentId is less than DEFAULT_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldNotBeFound("establishmentId.lessThan=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceInterfaceList where establishmentId is less than UPDATED_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldBeFound("establishmentId.lessThan=" + UPDATED_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByEstablishmentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where establishmentId is greater than DEFAULT_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldNotBeFound("establishmentId.greaterThan=" + DEFAULT_ESTABLISHMENT_ID);

        // Get all the deviceInterfaceList where establishmentId is greater than SMALLER_ESTABLISHMENT_ID
        defaultDeviceInterfaceShouldBeFound("establishmentId.greaterThan=" + SMALLER_ESTABLISHMENT_ID);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where state equals to DEFAULT_STATE
        defaultDeviceInterfaceShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the deviceInterfaceList where state equals to UPDATED_STATE
        defaultDeviceInterfaceShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where state in DEFAULT_STATE or UPDATED_STATE
        defaultDeviceInterfaceShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the deviceInterfaceList where state equals to UPDATED_STATE
        defaultDeviceInterfaceShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        // Get all the deviceInterfaceList where state is not null
        defaultDeviceInterfaceShouldBeFound("state.specified=true");

        // Get all the deviceInterfaceList where state is null
        defaultDeviceInterfaceShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByDeviceIsEqualToSomething() throws Exception {
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            deviceInterfaceRepository.saveAndFlush(deviceInterface);
            device = DeviceResourceIT.createEntity(em);
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        em.persist(device);
        em.flush();
        deviceInterface.setDevice(device);
        deviceInterfaceRepository.saveAndFlush(deviceInterface);
        UUID deviceId = device.getId();

        // Get all the deviceInterfaceList where device equals to deviceId
        defaultDeviceInterfaceShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the deviceInterfaceList where device equals to UUID.randomUUID()
        defaultDeviceInterfaceShouldNotBeFound("deviceId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllDeviceInterfacesByInterfaceBoardIsEqualToSomething() throws Exception {
        InterfaceBoard interfaceBoard;
        if (TestUtil.findAll(em, InterfaceBoard.class).isEmpty()) {
            deviceInterfaceRepository.saveAndFlush(deviceInterface);
            interfaceBoard = InterfaceBoardResourceIT.createEntity(em);
        } else {
            interfaceBoard = TestUtil.findAll(em, InterfaceBoard.class).get(0);
        }
        em.persist(interfaceBoard);
        em.flush();
        deviceInterface.setInterfaceBoard(interfaceBoard);
        deviceInterfaceRepository.saveAndFlush(deviceInterface);
        Long interfaceBoardId = interfaceBoard.getId();

        // Get all the deviceInterfaceList where interfaceBoard equals to interfaceBoardId
        defaultDeviceInterfaceShouldBeFound("interfaceBoardId.equals=" + interfaceBoardId);

        // Get all the deviceInterfaceList where interfaceBoard equals to (interfaceBoardId + 1)
        defaultDeviceInterfaceShouldNotBeFound("interfaceBoardId.equals=" + (interfaceBoardId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceInterfaceShouldBeFound(String filter) throws Exception {
        restDeviceInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceInterface.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].establishmentId").value(hasItem(DEFAULT_ESTABLISHMENT_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restDeviceInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceInterfaceShouldNotBeFound(String filter) throws Exception {
        restDeviceInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeviceInterface() throws Exception {
        // Get the deviceInterface
        restDeviceInterfaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeviceInterface() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();

        // Update the deviceInterface
        DeviceInterface updatedDeviceInterface = deviceInterfaceRepository.findById(deviceInterface.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceInterface are not directly saved in db
        em.detach(updatedDeviceInterface);
        updatedDeviceInterface
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .establishmentId(UPDATED_ESTABLISHMENT_ID)
            .state(UPDATED_STATE);
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(updatedDeviceInterface);

        restDeviceInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceInterfaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
        DeviceInterface testDeviceInterface = deviceInterfaceList.get(deviceInterfaceList.size() - 1);
        assertThat(testDeviceInterface.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDeviceInterface.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDeviceInterface.getEstablishmentId()).isEqualTo(UPDATED_ESTABLISHMENT_ID);
        assertThat(testDeviceInterface.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingDeviceInterface() throws Exception {
        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();
        deviceInterface.setId(count.incrementAndGet());

        // Create the DeviceInterface
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceInterfaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceInterface() throws Exception {
        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();
        deviceInterface.setId(count.incrementAndGet());

        // Create the DeviceInterface
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceInterface() throws Exception {
        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();
        deviceInterface.setId(count.incrementAndGet());

        // Create the DeviceInterface
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceInterfaceWithPatch() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();

        // Update the deviceInterface using partial update
        DeviceInterface partialUpdatedDeviceInterface = new DeviceInterface();
        partialUpdatedDeviceInterface.setId(deviceInterface.getId());

        partialUpdatedDeviceInterface.endDate(UPDATED_END_DATE);

        restDeviceInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceInterface.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceInterface))
            )
            .andExpect(status().isOk());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
        DeviceInterface testDeviceInterface = deviceInterfaceList.get(deviceInterfaceList.size() - 1);
        assertThat(testDeviceInterface.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDeviceInterface.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDeviceInterface.getEstablishmentId()).isEqualTo(DEFAULT_ESTABLISHMENT_ID);
        assertThat(testDeviceInterface.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void fullUpdateDeviceInterfaceWithPatch() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();

        // Update the deviceInterface using partial update
        DeviceInterface partialUpdatedDeviceInterface = new DeviceInterface();
        partialUpdatedDeviceInterface.setId(deviceInterface.getId());

        partialUpdatedDeviceInterface
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .establishmentId(UPDATED_ESTABLISHMENT_ID)
            .state(UPDATED_STATE);

        restDeviceInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceInterface.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceInterface))
            )
            .andExpect(status().isOk());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
        DeviceInterface testDeviceInterface = deviceInterfaceList.get(deviceInterfaceList.size() - 1);
        assertThat(testDeviceInterface.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDeviceInterface.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDeviceInterface.getEstablishmentId()).isEqualTo(UPDATED_ESTABLISHMENT_ID);
        assertThat(testDeviceInterface.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingDeviceInterface() throws Exception {
        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();
        deviceInterface.setId(count.incrementAndGet());

        // Create the DeviceInterface
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceInterfaceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceInterface() throws Exception {
        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();
        deviceInterface.setId(count.incrementAndGet());

        // Create the DeviceInterface
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceInterface() throws Exception {
        int databaseSizeBeforeUpdate = deviceInterfaceRepository.findAll().size();
        deviceInterface.setId(count.incrementAndGet());

        // Create the DeviceInterface
        DeviceInterfaceDTO deviceInterfaceDTO = deviceInterfaceMapper.toDto(deviceInterface);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceInterfaceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceInterface in the database
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceInterface() throws Exception {
        // Initialize the database
        deviceInterfaceRepository.saveAndFlush(deviceInterface);

        int databaseSizeBeforeDelete = deviceInterfaceRepository.findAll().size();

        // Delete the deviceInterface
        restDeviceInterfaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceInterface.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceInterface> deviceInterfaceList = deviceInterfaceRepository.findAll();
        assertThat(deviceInterfaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
