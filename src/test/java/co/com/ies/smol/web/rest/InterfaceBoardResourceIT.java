package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.repository.InterfaceBoardRepository;
import co.com.ies.smol.service.criteria.InterfaceBoardCriteria;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.mapper.InterfaceBoardMapper;
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
 * Integration tests for the {@link InterfaceBoardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterfaceBoardResourceIT {

    private static final Boolean DEFAULT_IS_ASSIGNED = false;
    private static final Boolean UPDATED_IS_ASSIGNED = true;

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_PORT = "AAAAAAAAAA";
    private static final String UPDATED_PORT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interface-boards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterfaceBoardRepository interfaceBoardRepository;

    @Autowired
    private InterfaceBoardMapper interfaceBoardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterfaceBoardMockMvc;

    private InterfaceBoard interfaceBoard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterfaceBoard createEntity(EntityManager em) {
        InterfaceBoard interfaceBoard = new InterfaceBoard()
            .isAssigned(DEFAULT_IS_ASSIGNED)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .hash(DEFAULT_HASH)
            .serial(DEFAULT_SERIAL)
            .version(DEFAULT_VERSION)
            .port(DEFAULT_PORT);
        return interfaceBoard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterfaceBoard createUpdatedEntity(EntityManager em) {
        InterfaceBoard interfaceBoard = new InterfaceBoard()
            .isAssigned(UPDATED_IS_ASSIGNED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .hash(UPDATED_HASH)
            .serial(UPDATED_SERIAL)
            .version(UPDATED_VERSION)
            .port(UPDATED_PORT);
        return interfaceBoard;
    }

    @BeforeEach
    public void initTest() {
        interfaceBoard = createEntity(em);
    }

    @Test
    @Transactional
    void createInterfaceBoard() throws Exception {
        int databaseSizeBeforeCreate = interfaceBoardRepository.findAll().size();
        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);
        restInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeCreate + 1);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIsAssigned()).isEqualTo(DEFAULT_IS_ASSIGNED);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testInterfaceBoard.getSerial()).isEqualTo(DEFAULT_SERIAL);
        assertThat(testInterfaceBoard.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testInterfaceBoard.getPort()).isEqualTo(DEFAULT_PORT);
    }

    @Test
    @Transactional
    void createInterfaceBoardWithExistingId() throws Exception {
        // Create the InterfaceBoard with an existing ID
        interfaceBoard.setId(1L);
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        int databaseSizeBeforeCreate = interfaceBoardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterfaceBoardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInterfaceBoards() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interfaceBoard.getId().intValue())))
            .andExpect(jsonPath("$.[*].isAssigned").value(hasItem(DEFAULT_IS_ASSIGNED.booleanValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)));
    }

    @Test
    @Transactional
    void getInterfaceBoard() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get the interfaceBoard
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL_ID, interfaceBoard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interfaceBoard.getId().intValue()))
            .andExpect(jsonPath("$.isAssigned").value(DEFAULT_IS_ASSIGNED.booleanValue()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT));
    }

    @Test
    @Transactional
    void getInterfaceBoardsByIdFiltering() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        Long id = interfaceBoard.getId();

        defaultInterfaceBoardShouldBeFound("id.equals=" + id);
        defaultInterfaceBoardShouldNotBeFound("id.notEquals=" + id);

        defaultInterfaceBoardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterfaceBoardShouldNotBeFound("id.greaterThan=" + id);

        defaultInterfaceBoardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterfaceBoardShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIsAssignedIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where isAssigned equals to DEFAULT_IS_ASSIGNED
        defaultInterfaceBoardShouldBeFound("isAssigned.equals=" + DEFAULT_IS_ASSIGNED);

        // Get all the interfaceBoardList where isAssigned equals to UPDATED_IS_ASSIGNED
        defaultInterfaceBoardShouldNotBeFound("isAssigned.equals=" + UPDATED_IS_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIsAssignedIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where isAssigned in DEFAULT_IS_ASSIGNED or UPDATED_IS_ASSIGNED
        defaultInterfaceBoardShouldBeFound("isAssigned.in=" + DEFAULT_IS_ASSIGNED + "," + UPDATED_IS_ASSIGNED);

        // Get all the interfaceBoardList where isAssigned equals to UPDATED_IS_ASSIGNED
        defaultInterfaceBoardShouldNotBeFound("isAssigned.in=" + UPDATED_IS_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIsAssignedIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where isAssigned is not null
        defaultInterfaceBoardShouldBeFound("isAssigned.specified=true");

        // Get all the interfaceBoardList where isAssigned is null
        defaultInterfaceBoardShouldNotBeFound("isAssigned.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress equals to DEFAULT_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.equals=" + DEFAULT_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress in DEFAULT_IP_ADDRESS or UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.in=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress is not null
        defaultInterfaceBoardShouldBeFound("ipAddress.specified=true");

        // Get all the interfaceBoardList where ipAddress is null
        defaultInterfaceBoardShouldNotBeFound("ipAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress contains DEFAULT_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.contains=" + DEFAULT_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress contains UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.contains=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByIpAddressNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where ipAddress does not contain DEFAULT_IP_ADDRESS
        defaultInterfaceBoardShouldNotBeFound("ipAddress.doesNotContain=" + DEFAULT_IP_ADDRESS);

        // Get all the interfaceBoardList where ipAddress does not contain UPDATED_IP_ADDRESS
        defaultInterfaceBoardShouldBeFound("ipAddress.doesNotContain=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash equals to DEFAULT_HASH
        defaultInterfaceBoardShouldBeFound("hash.equals=" + DEFAULT_HASH);

        // Get all the interfaceBoardList where hash equals to UPDATED_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.equals=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash in DEFAULT_HASH or UPDATED_HASH
        defaultInterfaceBoardShouldBeFound("hash.in=" + DEFAULT_HASH + "," + UPDATED_HASH);

        // Get all the interfaceBoardList where hash equals to UPDATED_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.in=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash is not null
        defaultInterfaceBoardShouldBeFound("hash.specified=true");

        // Get all the interfaceBoardList where hash is null
        defaultInterfaceBoardShouldNotBeFound("hash.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash contains DEFAULT_HASH
        defaultInterfaceBoardShouldBeFound("hash.contains=" + DEFAULT_HASH);

        // Get all the interfaceBoardList where hash contains UPDATED_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.contains=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByHashNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where hash does not contain DEFAULT_HASH
        defaultInterfaceBoardShouldNotBeFound("hash.doesNotContain=" + DEFAULT_HASH);

        // Get all the interfaceBoardList where hash does not contain UPDATED_HASH
        defaultInterfaceBoardShouldBeFound("hash.doesNotContain=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsBySerialIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where serial equals to DEFAULT_SERIAL
        defaultInterfaceBoardShouldBeFound("serial.equals=" + DEFAULT_SERIAL);

        // Get all the interfaceBoardList where serial equals to UPDATED_SERIAL
        defaultInterfaceBoardShouldNotBeFound("serial.equals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsBySerialIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where serial in DEFAULT_SERIAL or UPDATED_SERIAL
        defaultInterfaceBoardShouldBeFound("serial.in=" + DEFAULT_SERIAL + "," + UPDATED_SERIAL);

        // Get all the interfaceBoardList where serial equals to UPDATED_SERIAL
        defaultInterfaceBoardShouldNotBeFound("serial.in=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsBySerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where serial is not null
        defaultInterfaceBoardShouldBeFound("serial.specified=true");

        // Get all the interfaceBoardList where serial is null
        defaultInterfaceBoardShouldNotBeFound("serial.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsBySerialContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where serial contains DEFAULT_SERIAL
        defaultInterfaceBoardShouldBeFound("serial.contains=" + DEFAULT_SERIAL);

        // Get all the interfaceBoardList where serial contains UPDATED_SERIAL
        defaultInterfaceBoardShouldNotBeFound("serial.contains=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsBySerialNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where serial does not contain DEFAULT_SERIAL
        defaultInterfaceBoardShouldNotBeFound("serial.doesNotContain=" + DEFAULT_SERIAL);

        // Get all the interfaceBoardList where serial does not contain UPDATED_SERIAL
        defaultInterfaceBoardShouldBeFound("serial.doesNotContain=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where version equals to DEFAULT_VERSION
        defaultInterfaceBoardShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the interfaceBoardList where version equals to UPDATED_VERSION
        defaultInterfaceBoardShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultInterfaceBoardShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the interfaceBoardList where version equals to UPDATED_VERSION
        defaultInterfaceBoardShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where version is not null
        defaultInterfaceBoardShouldBeFound("version.specified=true");

        // Get all the interfaceBoardList where version is null
        defaultInterfaceBoardShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByVersionContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where version contains DEFAULT_VERSION
        defaultInterfaceBoardShouldBeFound("version.contains=" + DEFAULT_VERSION);

        // Get all the interfaceBoardList where version contains UPDATED_VERSION
        defaultInterfaceBoardShouldNotBeFound("version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where version does not contain DEFAULT_VERSION
        defaultInterfaceBoardShouldNotBeFound("version.doesNotContain=" + DEFAULT_VERSION);

        // Get all the interfaceBoardList where version does not contain UPDATED_VERSION
        defaultInterfaceBoardShouldBeFound("version.doesNotContain=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByPortIsEqualToSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where port equals to DEFAULT_PORT
        defaultInterfaceBoardShouldBeFound("port.equals=" + DEFAULT_PORT);

        // Get all the interfaceBoardList where port equals to UPDATED_PORT
        defaultInterfaceBoardShouldNotBeFound("port.equals=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByPortIsInShouldWork() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where port in DEFAULT_PORT or UPDATED_PORT
        defaultInterfaceBoardShouldBeFound("port.in=" + DEFAULT_PORT + "," + UPDATED_PORT);

        // Get all the interfaceBoardList where port equals to UPDATED_PORT
        defaultInterfaceBoardShouldNotBeFound("port.in=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where port is not null
        defaultInterfaceBoardShouldBeFound("port.specified=true");

        // Get all the interfaceBoardList where port is null
        defaultInterfaceBoardShouldNotBeFound("port.specified=false");
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByPortContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where port contains DEFAULT_PORT
        defaultInterfaceBoardShouldBeFound("port.contains=" + DEFAULT_PORT);

        // Get all the interfaceBoardList where port contains UPDATED_PORT
        defaultInterfaceBoardShouldNotBeFound("port.contains=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllInterfaceBoardsByPortNotContainsSomething() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        // Get all the interfaceBoardList where port does not contain DEFAULT_PORT
        defaultInterfaceBoardShouldNotBeFound("port.doesNotContain=" + DEFAULT_PORT);

        // Get all the interfaceBoardList where port does not contain UPDATED_PORT
        defaultInterfaceBoardShouldBeFound("port.doesNotContain=" + UPDATED_PORT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterfaceBoardShouldBeFound(String filter) throws Exception {
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interfaceBoard.getId().intValue())))
            .andExpect(jsonPath("$.[*].isAssigned").value(hasItem(DEFAULT_IS_ASSIGNED.booleanValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)));

        // Check, that the count call also returns 1
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterfaceBoardShouldNotBeFound(String filter) throws Exception {
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterfaceBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInterfaceBoard() throws Exception {
        // Get the interfaceBoard
        restInterfaceBoardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInterfaceBoard() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();

        // Update the interfaceBoard
        InterfaceBoard updatedInterfaceBoard = interfaceBoardRepository.findById(interfaceBoard.getId()).get();
        // Disconnect from session so that the updates on updatedInterfaceBoard are not directly saved in db
        em.detach(updatedInterfaceBoard);
        updatedInterfaceBoard
            .isAssigned(UPDATED_IS_ASSIGNED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .hash(UPDATED_HASH)
            .serial(UPDATED_SERIAL)
            .version(UPDATED_VERSION)
            .port(UPDATED_PORT);
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(updatedInterfaceBoard);

        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interfaceBoardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isOk());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIsAssigned()).isEqualTo(UPDATED_IS_ASSIGNED);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testInterfaceBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testInterfaceBoard.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testInterfaceBoard.getPort()).isEqualTo(UPDATED_PORT);
    }

    @Test
    @Transactional
    void putNonExistingInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interfaceBoardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterfaceBoardWithPatch() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();

        // Update the interfaceBoard using partial update
        InterfaceBoard partialUpdatedInterfaceBoard = new InterfaceBoard();
        partialUpdatedInterfaceBoard.setId(interfaceBoard.getId());

        partialUpdatedInterfaceBoard
            .isAssigned(UPDATED_IS_ASSIGNED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .serial(UPDATED_SERIAL)
            .version(UPDATED_VERSION)
            .port(UPDATED_PORT);

        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterfaceBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterfaceBoard))
            )
            .andExpect(status().isOk());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIsAssigned()).isEqualTo(UPDATED_IS_ASSIGNED);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testInterfaceBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testInterfaceBoard.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testInterfaceBoard.getPort()).isEqualTo(UPDATED_PORT);
    }

    @Test
    @Transactional
    void fullUpdateInterfaceBoardWithPatch() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();

        // Update the interfaceBoard using partial update
        InterfaceBoard partialUpdatedInterfaceBoard = new InterfaceBoard();
        partialUpdatedInterfaceBoard.setId(interfaceBoard.getId());

        partialUpdatedInterfaceBoard
            .isAssigned(UPDATED_IS_ASSIGNED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .hash(UPDATED_HASH)
            .serial(UPDATED_SERIAL)
            .version(UPDATED_VERSION)
            .port(UPDATED_PORT);

        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterfaceBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterfaceBoard))
            )
            .andExpect(status().isOk());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
        InterfaceBoard testInterfaceBoard = interfaceBoardList.get(interfaceBoardList.size() - 1);
        assertThat(testInterfaceBoard.getIsAssigned()).isEqualTo(UPDATED_IS_ASSIGNED);
        assertThat(testInterfaceBoard.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testInterfaceBoard.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testInterfaceBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testInterfaceBoard.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testInterfaceBoard.getPort()).isEqualTo(UPDATED_PORT);
    }

    @Test
    @Transactional
    void patchNonExistingInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interfaceBoardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterfaceBoard() throws Exception {
        int databaseSizeBeforeUpdate = interfaceBoardRepository.findAll().size();
        interfaceBoard.setId(count.incrementAndGet());

        // Create the InterfaceBoard
        InterfaceBoardDTO interfaceBoardDTO = interfaceBoardMapper.toDto(interfaceBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterfaceBoardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interfaceBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterfaceBoard in the database
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInterfaceBoard() throws Exception {
        // Initialize the database
        interfaceBoardRepository.saveAndFlush(interfaceBoard);

        int databaseSizeBeforeDelete = interfaceBoardRepository.findAll().size();

        // Delete the interfaceBoard
        restInterfaceBoardMockMvc
            .perform(delete(ENTITY_API_URL_ID, interfaceBoard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterfaceBoard> interfaceBoardList = interfaceBoardRepository.findAll();
        assertThat(interfaceBoardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
