package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static co.com.ies.smol.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Device;
import co.com.ies.smol.domain.DeviceCategory;
import co.com.ies.smol.domain.DeviceType;
import co.com.ies.smol.domain.Formula;
import co.com.ies.smol.domain.Model;
import co.com.ies.smol.repository.DeviceRepository;
import co.com.ies.smol.service.criteria.DeviceCriteria;
import co.com.ies.smol.service.dto.DeviceDTO;
import co.com.ies.smol.service.mapper.DeviceMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final String DEFAULT_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PROTOCOL_ESDCS = false;
    private static final Boolean UPDATED_IS_PROTOCOL_ESDCS = true;

    private static final Integer DEFAULT_NUMBER_PLAYED_REPORT = 1;
    private static final Integer UPDATED_NUMBER_PLAYED_REPORT = 2;
    private static final Integer SMALLER_NUMBER_PLAYED_REPORT = 1 - 1;

    private static final BigDecimal DEFAULT_SAS_DENOMINATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_SAS_DENOMINATION = new BigDecimal(2);
    private static final BigDecimal SMALLER_SAS_DENOMINATION = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_MULTIGAME = false;
    private static final Boolean UPDATED_IS_MULTIGAME = true;

    private static final Boolean DEFAULT_IS_MULTI_DENOMINATION = false;
    private static final Boolean UPDATED_IS_MULTI_DENOMINATION = true;

    private static final Boolean DEFAULT_IS_RETANQUEO = false;
    private static final Boolean UPDATED_IS_RETANQUEO = true;

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_THEORETICAL_HOLD = new BigDecimal(1);
    private static final BigDecimal UPDATED_THEORETICAL_HOLD = new BigDecimal(2);
    private static final BigDecimal SMALLER_THEORETICAL_HOLD = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_SAS_IDENTIFIER = 1;
    private static final Integer UPDATED_SAS_IDENTIFIER = 2;
    private static final Integer SMALLER_SAS_IDENTIFIER = 1 - 1;

    private static final Long DEFAULT_CREDIT_LIMIT = 1L;
    private static final Long UPDATED_CREDIT_LIMIT = 2L;
    private static final Long SMALLER_CREDIT_LIMIT = 1L - 1L;

    private static final Boolean DEFAULT_HAS_HOOPER = false;
    private static final Boolean UPDATED_HAS_HOOPER = true;

    private static final String DEFAULT_COLJUEGOS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COLJUEGOS_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FABRICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FABRICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FABRICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_CURRENT_TOKEN = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_TOKEN = new BigDecimal(2);
    private static final BigDecimal SMALLER_CURRENT_TOKEN = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DENOMINATION_TITO = new BigDecimal(1);
    private static final BigDecimal UPDATED_DENOMINATION_TITO = new BigDecimal(2);
    private static final BigDecimal SMALLER_DENOMINATION_TITO = new BigDecimal(1 - 1);

    private static final ZonedDateTime DEFAULT_END_LOST_COMMUNICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_LOST_COMMUNICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_LOST_COMMUNICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_START_LOST_COMMUNICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_LOST_COMMUNICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_LOST_COMMUNICATION = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final BigDecimal DEFAULT_REPORT_MULTIPLIER = new BigDecimal(1);
    private static final BigDecimal UPDATED_REPORT_MULTIPLIER = new BigDecimal(2);
    private static final BigDecimal SMALLER_REPORT_MULTIPLIER = new BigDecimal(1 - 1);

    private static final String DEFAULT_NUID = "AAAAAAAAAA";
    private static final String UPDATED_NUID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAY_MANUAL_PRIZE = false;
    private static final Boolean UPDATED_PAY_MANUAL_PRIZE = true;

    private static final Boolean DEFAULT_MANUAL_HANDPAY = false;
    private static final Boolean UPDATED_MANUAL_HANDPAY = true;

    private static final Boolean DEFAULT_MANUAL_JACKPOT = false;
    private static final Boolean UPDATED_MANUAL_JACKPOT = true;

    private static final Boolean DEFAULT_MANUAL_GAME_EVENT = false;
    private static final Boolean UPDATED_MANUAL_GAME_EVENT = true;

    private static final String DEFAULT_BET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BET_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HOMOLOGATION_INDICATOR = false;
    private static final Boolean UPDATED_HOMOLOGATION_INDICATOR = true;

    private static final String DEFAULT_COLJUEGOS_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_COLJUEGOS_MODEL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REPORTABLE = false;
    private static final Boolean UPDATED_REPORTABLE = true;

    private static final BigDecimal DEFAULT_AFT_DENOMINATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_AFT_DENOMINATION = new BigDecimal(2);
    private static final BigDecimal SMALLER_AFT_DENOMINATION = new BigDecimal(1 - 1);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ENABLE_ROLLOVER = false;
    private static final Boolean UPDATED_ENABLE_ROLLOVER = true;

    private static final ZonedDateTime DEFAULT_LAST_CORRUPTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_CORRUPTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_CORRUPTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_DAFT_DENOMINATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_DAFT_DENOMINATION = new BigDecimal(2);
    private static final BigDecimal SMALLER_DAFT_DENOMINATION = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_PRIZES_ENABLED = false;
    private static final Boolean UPDATED_PRIZES_ENABLED = true;

    private static final Integer DEFAULT_CURRENCY_TYPE_ID = 1;
    private static final Integer UPDATED_CURRENCY_TYPE_ID = 2;
    private static final Integer SMALLER_CURRENCY_TYPE_ID = 1 - 1;

    private static final Integer DEFAULT_ISLE_ID = 1;
    private static final Integer UPDATED_ISLE_ID = 2;
    private static final Integer SMALLER_ISLE_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device()
            .serial(DEFAULT_SERIAL)
            .isProtocolEsdcs(DEFAULT_IS_PROTOCOL_ESDCS)
            .numberPlayedReport(DEFAULT_NUMBER_PLAYED_REPORT)
            .sasDenomination(DEFAULT_SAS_DENOMINATION)
            .isMultigame(DEFAULT_IS_MULTIGAME)
            .isMultiDenomination(DEFAULT_IS_MULTI_DENOMINATION)
            .isRetanqueo(DEFAULT_IS_RETANQUEO)
            .state(DEFAULT_STATE)
            .theoreticalHold(DEFAULT_THEORETICAL_HOLD)
            .sasIdentifier(DEFAULT_SAS_IDENTIFIER)
            .creditLimit(DEFAULT_CREDIT_LIMIT)
            .hasHooper(DEFAULT_HAS_HOOPER)
            .coljuegosCode(DEFAULT_COLJUEGOS_CODE)
            .fabricationDate(DEFAULT_FABRICATION_DATE)
            .currentToken(DEFAULT_CURRENT_TOKEN)
            .denominationTito(DEFAULT_DENOMINATION_TITO)
            .endLostCommunication(DEFAULT_END_LOST_COMMUNICATION)
            .startLostCommunication(DEFAULT_START_LOST_COMMUNICATION)
            .reportMultiplier(DEFAULT_REPORT_MULTIPLIER)
            .nuid(DEFAULT_NUID)
            .payManualPrize(DEFAULT_PAY_MANUAL_PRIZE)
            .manualHandpay(DEFAULT_MANUAL_HANDPAY)
            .manualJackpot(DEFAULT_MANUAL_JACKPOT)
            .manualGameEvent(DEFAULT_MANUAL_GAME_EVENT)
            .betCode(DEFAULT_BET_CODE)
            .homologationIndicator(DEFAULT_HOMOLOGATION_INDICATOR)
            .coljuegosModel(DEFAULT_COLJUEGOS_MODEL)
            .reportable(DEFAULT_REPORTABLE)
            .aftDenomination(DEFAULT_AFT_DENOMINATION)
            .lastUpdateDate(DEFAULT_LAST_UPDATE_DATE)
            .enableRollover(DEFAULT_ENABLE_ROLLOVER)
            .lastCorruptionDate(DEFAULT_LAST_CORRUPTION_DATE)
            .daftDenomination(DEFAULT_DAFT_DENOMINATION)
            .prizesEnabled(DEFAULT_PRIZES_ENABLED)
            .currencyTypeId(DEFAULT_CURRENCY_TYPE_ID)
            .isleId(DEFAULT_ISLE_ID);
        // Add required entity
        Model model;
        if (TestUtil.findAll(em, Model.class).isEmpty()) {
            model = ModelResourceIT.createEntity(em);
            em.persist(model);
            em.flush();
        } else {
            model = TestUtil.findAll(em, Model.class).get(0);
        }
        device.setModel(model);
        // Add required entity
        DeviceCategory deviceCategory;
        if (TestUtil.findAll(em, DeviceCategory.class).isEmpty()) {
            deviceCategory = DeviceCategoryResourceIT.createEntity(em);
            em.persist(deviceCategory);
            em.flush();
        } else {
            deviceCategory = TestUtil.findAll(em, DeviceCategory.class).get(0);
        }
        device.setDeviceCategory(deviceCategory);
        // Add required entity
        DeviceType deviceType;
        if (TestUtil.findAll(em, DeviceType.class).isEmpty()) {
            deviceType = DeviceTypeResourceIT.createEntity(em);
            em.persist(deviceType);
            em.flush();
        } else {
            deviceType = TestUtil.findAll(em, DeviceType.class).get(0);
        }
        device.setDeviceType(deviceType);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity(EntityManager em) {
        Device device = new Device()
            .serial(UPDATED_SERIAL)
            .isProtocolEsdcs(UPDATED_IS_PROTOCOL_ESDCS)
            .numberPlayedReport(UPDATED_NUMBER_PLAYED_REPORT)
            .sasDenomination(UPDATED_SAS_DENOMINATION)
            .isMultigame(UPDATED_IS_MULTIGAME)
            .isMultiDenomination(UPDATED_IS_MULTI_DENOMINATION)
            .isRetanqueo(UPDATED_IS_RETANQUEO)
            .state(UPDATED_STATE)
            .theoreticalHold(UPDATED_THEORETICAL_HOLD)
            .sasIdentifier(UPDATED_SAS_IDENTIFIER)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .hasHooper(UPDATED_HAS_HOOPER)
            .coljuegosCode(UPDATED_COLJUEGOS_CODE)
            .fabricationDate(UPDATED_FABRICATION_DATE)
            .currentToken(UPDATED_CURRENT_TOKEN)
            .denominationTito(UPDATED_DENOMINATION_TITO)
            .endLostCommunication(UPDATED_END_LOST_COMMUNICATION)
            .startLostCommunication(UPDATED_START_LOST_COMMUNICATION)
            .reportMultiplier(UPDATED_REPORT_MULTIPLIER)
            .nuid(UPDATED_NUID)
            .payManualPrize(UPDATED_PAY_MANUAL_PRIZE)
            .manualHandpay(UPDATED_MANUAL_HANDPAY)
            .manualJackpot(UPDATED_MANUAL_JACKPOT)
            .manualGameEvent(UPDATED_MANUAL_GAME_EVENT)
            .betCode(UPDATED_BET_CODE)
            .homologationIndicator(UPDATED_HOMOLOGATION_INDICATOR)
            .coljuegosModel(UPDATED_COLJUEGOS_MODEL)
            .reportable(UPDATED_REPORTABLE)
            .aftDenomination(UPDATED_AFT_DENOMINATION)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE)
            .enableRollover(UPDATED_ENABLE_ROLLOVER)
            .lastCorruptionDate(UPDATED_LAST_CORRUPTION_DATE)
            .daftDenomination(UPDATED_DAFT_DENOMINATION)
            .prizesEnabled(UPDATED_PRIZES_ENABLED)
            .currencyTypeId(UPDATED_CURRENCY_TYPE_ID)
            .isleId(UPDATED_ISLE_ID);
        // Add required entity
        Model model;
        if (TestUtil.findAll(em, Model.class).isEmpty()) {
            model = ModelResourceIT.createUpdatedEntity(em);
            em.persist(model);
            em.flush();
        } else {
            model = TestUtil.findAll(em, Model.class).get(0);
        }
        device.setModel(model);
        // Add required entity
        DeviceCategory deviceCategory;
        if (TestUtil.findAll(em, DeviceCategory.class).isEmpty()) {
            deviceCategory = DeviceCategoryResourceIT.createUpdatedEntity(em);
            em.persist(deviceCategory);
            em.flush();
        } else {
            deviceCategory = TestUtil.findAll(em, DeviceCategory.class).get(0);
        }
        device.setDeviceCategory(deviceCategory);
        // Add required entity
        DeviceType deviceType;
        if (TestUtil.findAll(em, DeviceType.class).isEmpty()) {
            deviceType = DeviceTypeResourceIT.createUpdatedEntity(em);
            em.persist(deviceType);
            em.flush();
        } else {
            deviceType = TestUtil.findAll(em, DeviceType.class).get(0);
        }
        device.setDeviceType(deviceType);
        return device;
    }

    @BeforeEach
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();
        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerial()).isEqualTo(DEFAULT_SERIAL);
        assertThat(testDevice.getIsProtocolEsdcs()).isEqualTo(DEFAULT_IS_PROTOCOL_ESDCS);
        assertThat(testDevice.getNumberPlayedReport()).isEqualTo(DEFAULT_NUMBER_PLAYED_REPORT);
        assertThat(testDevice.getSasDenomination()).isEqualByComparingTo(DEFAULT_SAS_DENOMINATION);
        assertThat(testDevice.getIsMultigame()).isEqualTo(DEFAULT_IS_MULTIGAME);
        assertThat(testDevice.getIsMultiDenomination()).isEqualTo(DEFAULT_IS_MULTI_DENOMINATION);
        assertThat(testDevice.getIsRetanqueo()).isEqualTo(DEFAULT_IS_RETANQUEO);
        assertThat(testDevice.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testDevice.getTheoreticalHold()).isEqualByComparingTo(DEFAULT_THEORETICAL_HOLD);
        assertThat(testDevice.getSasIdentifier()).isEqualTo(DEFAULT_SAS_IDENTIFIER);
        assertThat(testDevice.getCreditLimit()).isEqualTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testDevice.getHasHooper()).isEqualTo(DEFAULT_HAS_HOOPER);
        assertThat(testDevice.getColjuegosCode()).isEqualTo(DEFAULT_COLJUEGOS_CODE);
        assertThat(testDevice.getFabricationDate()).isEqualTo(DEFAULT_FABRICATION_DATE);
        assertThat(testDevice.getCurrentToken()).isEqualByComparingTo(DEFAULT_CURRENT_TOKEN);
        assertThat(testDevice.getDenominationTito()).isEqualByComparingTo(DEFAULT_DENOMINATION_TITO);
        assertThat(testDevice.getEndLostCommunication()).isEqualTo(DEFAULT_END_LOST_COMMUNICATION);
        assertThat(testDevice.getStartLostCommunication()).isEqualTo(DEFAULT_START_LOST_COMMUNICATION);
        assertThat(testDevice.getReportMultiplier()).isEqualByComparingTo(DEFAULT_REPORT_MULTIPLIER);
        assertThat(testDevice.getNuid()).isEqualTo(DEFAULT_NUID);
        assertThat(testDevice.getPayManualPrize()).isEqualTo(DEFAULT_PAY_MANUAL_PRIZE);
        assertThat(testDevice.getManualHandpay()).isEqualTo(DEFAULT_MANUAL_HANDPAY);
        assertThat(testDevice.getManualJackpot()).isEqualTo(DEFAULT_MANUAL_JACKPOT);
        assertThat(testDevice.getManualGameEvent()).isEqualTo(DEFAULT_MANUAL_GAME_EVENT);
        assertThat(testDevice.getBetCode()).isEqualTo(DEFAULT_BET_CODE);
        assertThat(testDevice.getHomologationIndicator()).isEqualTo(DEFAULT_HOMOLOGATION_INDICATOR);
        assertThat(testDevice.getColjuegosModel()).isEqualTo(DEFAULT_COLJUEGOS_MODEL);
        assertThat(testDevice.getReportable()).isEqualTo(DEFAULT_REPORTABLE);
        assertThat(testDevice.getAftDenomination()).isEqualByComparingTo(DEFAULT_AFT_DENOMINATION);
        assertThat(testDevice.getLastUpdateDate()).isEqualTo(DEFAULT_LAST_UPDATE_DATE);
        assertThat(testDevice.getEnableRollover()).isEqualTo(DEFAULT_ENABLE_ROLLOVER);
        assertThat(testDevice.getLastCorruptionDate()).isEqualTo(DEFAULT_LAST_CORRUPTION_DATE);
        assertThat(testDevice.getDaftDenomination()).isEqualByComparingTo(DEFAULT_DAFT_DENOMINATION);
        assertThat(testDevice.getPrizesEnabled()).isEqualTo(DEFAULT_PRIZES_ENABLED);
        assertThat(testDevice.getCurrencyTypeId()).isEqualTo(DEFAULT_CURRENCY_TYPE_ID);
        assertThat(testDevice.getIsleId()).isEqualTo(DEFAULT_ISLE_ID);
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        deviceRepository.saveAndFlush(device);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setSerial(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().toString())))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)))
            .andExpect(jsonPath("$.[*].isProtocolEsdcs").value(hasItem(DEFAULT_IS_PROTOCOL_ESDCS.booleanValue())))
            .andExpect(jsonPath("$.[*].numberPlayedReport").value(hasItem(DEFAULT_NUMBER_PLAYED_REPORT)))
            .andExpect(jsonPath("$.[*].sasDenomination").value(hasItem(sameNumber(DEFAULT_SAS_DENOMINATION))))
            .andExpect(jsonPath("$.[*].isMultigame").value(hasItem(DEFAULT_IS_MULTIGAME.booleanValue())))
            .andExpect(jsonPath("$.[*].isMultiDenomination").value(hasItem(DEFAULT_IS_MULTI_DENOMINATION.booleanValue())))
            .andExpect(jsonPath("$.[*].isRetanqueo").value(hasItem(DEFAULT_IS_RETANQUEO.booleanValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].theoreticalHold").value(hasItem(sameNumber(DEFAULT_THEORETICAL_HOLD))))
            .andExpect(jsonPath("$.[*].sasIdentifier").value(hasItem(DEFAULT_SAS_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(DEFAULT_CREDIT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].hasHooper").value(hasItem(DEFAULT_HAS_HOOPER.booleanValue())))
            .andExpect(jsonPath("$.[*].coljuegosCode").value(hasItem(DEFAULT_COLJUEGOS_CODE)))
            .andExpect(jsonPath("$.[*].fabricationDate").value(hasItem(DEFAULT_FABRICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentToken").value(hasItem(sameNumber(DEFAULT_CURRENT_TOKEN))))
            .andExpect(jsonPath("$.[*].denominationTito").value(hasItem(sameNumber(DEFAULT_DENOMINATION_TITO))))
            .andExpect(jsonPath("$.[*].endLostCommunication").value(hasItem(sameInstant(DEFAULT_END_LOST_COMMUNICATION))))
            .andExpect(jsonPath("$.[*].startLostCommunication").value(hasItem(sameInstant(DEFAULT_START_LOST_COMMUNICATION))))
            .andExpect(jsonPath("$.[*].reportMultiplier").value(hasItem(sameNumber(DEFAULT_REPORT_MULTIPLIER))))
            .andExpect(jsonPath("$.[*].nuid").value(hasItem(DEFAULT_NUID)))
            .andExpect(jsonPath("$.[*].payManualPrize").value(hasItem(DEFAULT_PAY_MANUAL_PRIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].manualHandpay").value(hasItem(DEFAULT_MANUAL_HANDPAY.booleanValue())))
            .andExpect(jsonPath("$.[*].manualJackpot").value(hasItem(DEFAULT_MANUAL_JACKPOT.booleanValue())))
            .andExpect(jsonPath("$.[*].manualGameEvent").value(hasItem(DEFAULT_MANUAL_GAME_EVENT.booleanValue())))
            .andExpect(jsonPath("$.[*].betCode").value(hasItem(DEFAULT_BET_CODE)))
            .andExpect(jsonPath("$.[*].homologationIndicator").value(hasItem(DEFAULT_HOMOLOGATION_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].coljuegosModel").value(hasItem(DEFAULT_COLJUEGOS_MODEL)))
            .andExpect(jsonPath("$.[*].reportable").value(hasItem(DEFAULT_REPORTABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].aftDenomination").value(hasItem(sameNumber(DEFAULT_AFT_DENOMINATION))))
            .andExpect(jsonPath("$.[*].lastUpdateDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE_DATE))))
            .andExpect(jsonPath("$.[*].enableRollover").value(hasItem(DEFAULT_ENABLE_ROLLOVER.booleanValue())))
            .andExpect(jsonPath("$.[*].lastCorruptionDate").value(hasItem(sameInstant(DEFAULT_LAST_CORRUPTION_DATE))))
            .andExpect(jsonPath("$.[*].daftDenomination").value(hasItem(sameNumber(DEFAULT_DAFT_DENOMINATION))))
            .andExpect(jsonPath("$.[*].prizesEnabled").value(hasItem(DEFAULT_PRIZES_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].currencyTypeId").value(hasItem(DEFAULT_CURRENCY_TYPE_ID)))
            .andExpect(jsonPath("$.[*].isleId").value(hasItem(DEFAULT_ISLE_ID)));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().toString()))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL))
            .andExpect(jsonPath("$.isProtocolEsdcs").value(DEFAULT_IS_PROTOCOL_ESDCS.booleanValue()))
            .andExpect(jsonPath("$.numberPlayedReport").value(DEFAULT_NUMBER_PLAYED_REPORT))
            .andExpect(jsonPath("$.sasDenomination").value(sameNumber(DEFAULT_SAS_DENOMINATION)))
            .andExpect(jsonPath("$.isMultigame").value(DEFAULT_IS_MULTIGAME.booleanValue()))
            .andExpect(jsonPath("$.isMultiDenomination").value(DEFAULT_IS_MULTI_DENOMINATION.booleanValue()))
            .andExpect(jsonPath("$.isRetanqueo").value(DEFAULT_IS_RETANQUEO.booleanValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.theoreticalHold").value(sameNumber(DEFAULT_THEORETICAL_HOLD)))
            .andExpect(jsonPath("$.sasIdentifier").value(DEFAULT_SAS_IDENTIFIER))
            .andExpect(jsonPath("$.creditLimit").value(DEFAULT_CREDIT_LIMIT.intValue()))
            .andExpect(jsonPath("$.hasHooper").value(DEFAULT_HAS_HOOPER.booleanValue()))
            .andExpect(jsonPath("$.coljuegosCode").value(DEFAULT_COLJUEGOS_CODE))
            .andExpect(jsonPath("$.fabricationDate").value(DEFAULT_FABRICATION_DATE.toString()))
            .andExpect(jsonPath("$.currentToken").value(sameNumber(DEFAULT_CURRENT_TOKEN)))
            .andExpect(jsonPath("$.denominationTito").value(sameNumber(DEFAULT_DENOMINATION_TITO)))
            .andExpect(jsonPath("$.endLostCommunication").value(sameInstant(DEFAULT_END_LOST_COMMUNICATION)))
            .andExpect(jsonPath("$.startLostCommunication").value(sameInstant(DEFAULT_START_LOST_COMMUNICATION)))
            .andExpect(jsonPath("$.reportMultiplier").value(sameNumber(DEFAULT_REPORT_MULTIPLIER)))
            .andExpect(jsonPath("$.nuid").value(DEFAULT_NUID))
            .andExpect(jsonPath("$.payManualPrize").value(DEFAULT_PAY_MANUAL_PRIZE.booleanValue()))
            .andExpect(jsonPath("$.manualHandpay").value(DEFAULT_MANUAL_HANDPAY.booleanValue()))
            .andExpect(jsonPath("$.manualJackpot").value(DEFAULT_MANUAL_JACKPOT.booleanValue()))
            .andExpect(jsonPath("$.manualGameEvent").value(DEFAULT_MANUAL_GAME_EVENT.booleanValue()))
            .andExpect(jsonPath("$.betCode").value(DEFAULT_BET_CODE))
            .andExpect(jsonPath("$.homologationIndicator").value(DEFAULT_HOMOLOGATION_INDICATOR.booleanValue()))
            .andExpect(jsonPath("$.coljuegosModel").value(DEFAULT_COLJUEGOS_MODEL))
            .andExpect(jsonPath("$.reportable").value(DEFAULT_REPORTABLE.booleanValue()))
            .andExpect(jsonPath("$.aftDenomination").value(sameNumber(DEFAULT_AFT_DENOMINATION)))
            .andExpect(jsonPath("$.lastUpdateDate").value(sameInstant(DEFAULT_LAST_UPDATE_DATE)))
            .andExpect(jsonPath("$.enableRollover").value(DEFAULT_ENABLE_ROLLOVER.booleanValue()))
            .andExpect(jsonPath("$.lastCorruptionDate").value(sameInstant(DEFAULT_LAST_CORRUPTION_DATE)))
            .andExpect(jsonPath("$.daftDenomination").value(sameNumber(DEFAULT_DAFT_DENOMINATION)))
            .andExpect(jsonPath("$.prizesEnabled").value(DEFAULT_PRIZES_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.currencyTypeId").value(DEFAULT_CURRENCY_TYPE_ID))
            .andExpect(jsonPath("$.isleId").value(DEFAULT_ISLE_ID));
    }

    @Test
    @Transactional
    void getDevicesByIdFiltering() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        UUID id = device.getId();

        defaultDeviceShouldBeFound("id.equals=" + id);
        defaultDeviceShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serial equals to DEFAULT_SERIAL
        defaultDeviceShouldBeFound("serial.equals=" + DEFAULT_SERIAL);

        // Get all the deviceList where serial equals to UPDATED_SERIAL
        defaultDeviceShouldNotBeFound("serial.equals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serial in DEFAULT_SERIAL or UPDATED_SERIAL
        defaultDeviceShouldBeFound("serial.in=" + DEFAULT_SERIAL + "," + UPDATED_SERIAL);

        // Get all the deviceList where serial equals to UPDATED_SERIAL
        defaultDeviceShouldNotBeFound("serial.in=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serial is not null
        defaultDeviceShouldBeFound("serial.specified=true");

        // Get all the deviceList where serial is null
        defaultDeviceShouldNotBeFound("serial.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesBySerialContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serial contains DEFAULT_SERIAL
        defaultDeviceShouldBeFound("serial.contains=" + DEFAULT_SERIAL);

        // Get all the deviceList where serial contains UPDATED_SERIAL
        defaultDeviceShouldNotBeFound("serial.contains=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serial does not contain DEFAULT_SERIAL
        defaultDeviceShouldNotBeFound("serial.doesNotContain=" + DEFAULT_SERIAL);

        // Get all the deviceList where serial does not contain UPDATED_SERIAL
        defaultDeviceShouldBeFound("serial.doesNotContain=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllDevicesByIsProtocolEsdcsIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isProtocolEsdcs equals to DEFAULT_IS_PROTOCOL_ESDCS
        defaultDeviceShouldBeFound("isProtocolEsdcs.equals=" + DEFAULT_IS_PROTOCOL_ESDCS);

        // Get all the deviceList where isProtocolEsdcs equals to UPDATED_IS_PROTOCOL_ESDCS
        defaultDeviceShouldNotBeFound("isProtocolEsdcs.equals=" + UPDATED_IS_PROTOCOL_ESDCS);
    }

    @Test
    @Transactional
    void getAllDevicesByIsProtocolEsdcsIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isProtocolEsdcs in DEFAULT_IS_PROTOCOL_ESDCS or UPDATED_IS_PROTOCOL_ESDCS
        defaultDeviceShouldBeFound("isProtocolEsdcs.in=" + DEFAULT_IS_PROTOCOL_ESDCS + "," + UPDATED_IS_PROTOCOL_ESDCS);

        // Get all the deviceList where isProtocolEsdcs equals to UPDATED_IS_PROTOCOL_ESDCS
        defaultDeviceShouldNotBeFound("isProtocolEsdcs.in=" + UPDATED_IS_PROTOCOL_ESDCS);
    }

    @Test
    @Transactional
    void getAllDevicesByIsProtocolEsdcsIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isProtocolEsdcs is not null
        defaultDeviceShouldBeFound("isProtocolEsdcs.specified=true");

        // Get all the deviceList where isProtocolEsdcs is null
        defaultDeviceShouldNotBeFound("isProtocolEsdcs.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByNumberPlayedReportIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where numberPlayedReport equals to DEFAULT_NUMBER_PLAYED_REPORT
        defaultDeviceShouldBeFound("numberPlayedReport.equals=" + DEFAULT_NUMBER_PLAYED_REPORT);

        // Get all the deviceList where numberPlayedReport equals to UPDATED_NUMBER_PLAYED_REPORT
        defaultDeviceShouldNotBeFound("numberPlayedReport.equals=" + UPDATED_NUMBER_PLAYED_REPORT);
    }

    @Test
    @Transactional
    void getAllDevicesByNumberPlayedReportIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where numberPlayedReport in DEFAULT_NUMBER_PLAYED_REPORT or UPDATED_NUMBER_PLAYED_REPORT
        defaultDeviceShouldBeFound("numberPlayedReport.in=" + DEFAULT_NUMBER_PLAYED_REPORT + "," + UPDATED_NUMBER_PLAYED_REPORT);

        // Get all the deviceList where numberPlayedReport equals to UPDATED_NUMBER_PLAYED_REPORT
        defaultDeviceShouldNotBeFound("numberPlayedReport.in=" + UPDATED_NUMBER_PLAYED_REPORT);
    }

    @Test
    @Transactional
    void getAllDevicesByNumberPlayedReportIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where numberPlayedReport is not null
        defaultDeviceShouldBeFound("numberPlayedReport.specified=true");

        // Get all the deviceList where numberPlayedReport is null
        defaultDeviceShouldNotBeFound("numberPlayedReport.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByNumberPlayedReportIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where numberPlayedReport is greater than or equal to DEFAULT_NUMBER_PLAYED_REPORT
        defaultDeviceShouldBeFound("numberPlayedReport.greaterThanOrEqual=" + DEFAULT_NUMBER_PLAYED_REPORT);

        // Get all the deviceList where numberPlayedReport is greater than or equal to UPDATED_NUMBER_PLAYED_REPORT
        defaultDeviceShouldNotBeFound("numberPlayedReport.greaterThanOrEqual=" + UPDATED_NUMBER_PLAYED_REPORT);
    }

    @Test
    @Transactional
    void getAllDevicesByNumberPlayedReportIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where numberPlayedReport is less than or equal to DEFAULT_NUMBER_PLAYED_REPORT
        defaultDeviceShouldBeFound("numberPlayedReport.lessThanOrEqual=" + DEFAULT_NUMBER_PLAYED_REPORT);

        // Get all the deviceList where numberPlayedReport is less than or equal to SMALLER_NUMBER_PLAYED_REPORT
        defaultDeviceShouldNotBeFound("numberPlayedReport.lessThanOrEqual=" + SMALLER_NUMBER_PLAYED_REPORT);
    }

    @Test
    @Transactional
    void getAllDevicesByNumberPlayedReportIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where numberPlayedReport is less than DEFAULT_NUMBER_PLAYED_REPORT
        defaultDeviceShouldNotBeFound("numberPlayedReport.lessThan=" + DEFAULT_NUMBER_PLAYED_REPORT);

        // Get all the deviceList where numberPlayedReport is less than UPDATED_NUMBER_PLAYED_REPORT
        defaultDeviceShouldBeFound("numberPlayedReport.lessThan=" + UPDATED_NUMBER_PLAYED_REPORT);
    }

    @Test
    @Transactional
    void getAllDevicesByNumberPlayedReportIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where numberPlayedReport is greater than DEFAULT_NUMBER_PLAYED_REPORT
        defaultDeviceShouldNotBeFound("numberPlayedReport.greaterThan=" + DEFAULT_NUMBER_PLAYED_REPORT);

        // Get all the deviceList where numberPlayedReport is greater than SMALLER_NUMBER_PLAYED_REPORT
        defaultDeviceShouldBeFound("numberPlayedReport.greaterThan=" + SMALLER_NUMBER_PLAYED_REPORT);
    }

    @Test
    @Transactional
    void getAllDevicesBySasDenominationIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasDenomination equals to DEFAULT_SAS_DENOMINATION
        defaultDeviceShouldBeFound("sasDenomination.equals=" + DEFAULT_SAS_DENOMINATION);

        // Get all the deviceList where sasDenomination equals to UPDATED_SAS_DENOMINATION
        defaultDeviceShouldNotBeFound("sasDenomination.equals=" + UPDATED_SAS_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesBySasDenominationIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasDenomination in DEFAULT_SAS_DENOMINATION or UPDATED_SAS_DENOMINATION
        defaultDeviceShouldBeFound("sasDenomination.in=" + DEFAULT_SAS_DENOMINATION + "," + UPDATED_SAS_DENOMINATION);

        // Get all the deviceList where sasDenomination equals to UPDATED_SAS_DENOMINATION
        defaultDeviceShouldNotBeFound("sasDenomination.in=" + UPDATED_SAS_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesBySasDenominationIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasDenomination is not null
        defaultDeviceShouldBeFound("sasDenomination.specified=true");

        // Get all the deviceList where sasDenomination is null
        defaultDeviceShouldNotBeFound("sasDenomination.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesBySasDenominationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasDenomination is greater than or equal to DEFAULT_SAS_DENOMINATION
        defaultDeviceShouldBeFound("sasDenomination.greaterThanOrEqual=" + DEFAULT_SAS_DENOMINATION);

        // Get all the deviceList where sasDenomination is greater than or equal to UPDATED_SAS_DENOMINATION
        defaultDeviceShouldNotBeFound("sasDenomination.greaterThanOrEqual=" + UPDATED_SAS_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesBySasDenominationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasDenomination is less than or equal to DEFAULT_SAS_DENOMINATION
        defaultDeviceShouldBeFound("sasDenomination.lessThanOrEqual=" + DEFAULT_SAS_DENOMINATION);

        // Get all the deviceList where sasDenomination is less than or equal to SMALLER_SAS_DENOMINATION
        defaultDeviceShouldNotBeFound("sasDenomination.lessThanOrEqual=" + SMALLER_SAS_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesBySasDenominationIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasDenomination is less than DEFAULT_SAS_DENOMINATION
        defaultDeviceShouldNotBeFound("sasDenomination.lessThan=" + DEFAULT_SAS_DENOMINATION);

        // Get all the deviceList where sasDenomination is less than UPDATED_SAS_DENOMINATION
        defaultDeviceShouldBeFound("sasDenomination.lessThan=" + UPDATED_SAS_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesBySasDenominationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasDenomination is greater than DEFAULT_SAS_DENOMINATION
        defaultDeviceShouldNotBeFound("sasDenomination.greaterThan=" + DEFAULT_SAS_DENOMINATION);

        // Get all the deviceList where sasDenomination is greater than SMALLER_SAS_DENOMINATION
        defaultDeviceShouldBeFound("sasDenomination.greaterThan=" + SMALLER_SAS_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByIsMultigameIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isMultigame equals to DEFAULT_IS_MULTIGAME
        defaultDeviceShouldBeFound("isMultigame.equals=" + DEFAULT_IS_MULTIGAME);

        // Get all the deviceList where isMultigame equals to UPDATED_IS_MULTIGAME
        defaultDeviceShouldNotBeFound("isMultigame.equals=" + UPDATED_IS_MULTIGAME);
    }

    @Test
    @Transactional
    void getAllDevicesByIsMultigameIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isMultigame in DEFAULT_IS_MULTIGAME or UPDATED_IS_MULTIGAME
        defaultDeviceShouldBeFound("isMultigame.in=" + DEFAULT_IS_MULTIGAME + "," + UPDATED_IS_MULTIGAME);

        // Get all the deviceList where isMultigame equals to UPDATED_IS_MULTIGAME
        defaultDeviceShouldNotBeFound("isMultigame.in=" + UPDATED_IS_MULTIGAME);
    }

    @Test
    @Transactional
    void getAllDevicesByIsMultigameIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isMultigame is not null
        defaultDeviceShouldBeFound("isMultigame.specified=true");

        // Get all the deviceList where isMultigame is null
        defaultDeviceShouldNotBeFound("isMultigame.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByIsMultiDenominationIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isMultiDenomination equals to DEFAULT_IS_MULTI_DENOMINATION
        defaultDeviceShouldBeFound("isMultiDenomination.equals=" + DEFAULT_IS_MULTI_DENOMINATION);

        // Get all the deviceList where isMultiDenomination equals to UPDATED_IS_MULTI_DENOMINATION
        defaultDeviceShouldNotBeFound("isMultiDenomination.equals=" + UPDATED_IS_MULTI_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByIsMultiDenominationIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isMultiDenomination in DEFAULT_IS_MULTI_DENOMINATION or UPDATED_IS_MULTI_DENOMINATION
        defaultDeviceShouldBeFound("isMultiDenomination.in=" + DEFAULT_IS_MULTI_DENOMINATION + "," + UPDATED_IS_MULTI_DENOMINATION);

        // Get all the deviceList where isMultiDenomination equals to UPDATED_IS_MULTI_DENOMINATION
        defaultDeviceShouldNotBeFound("isMultiDenomination.in=" + UPDATED_IS_MULTI_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByIsMultiDenominationIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isMultiDenomination is not null
        defaultDeviceShouldBeFound("isMultiDenomination.specified=true");

        // Get all the deviceList where isMultiDenomination is null
        defaultDeviceShouldNotBeFound("isMultiDenomination.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByIsRetanqueoIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isRetanqueo equals to DEFAULT_IS_RETANQUEO
        defaultDeviceShouldBeFound("isRetanqueo.equals=" + DEFAULT_IS_RETANQUEO);

        // Get all the deviceList where isRetanqueo equals to UPDATED_IS_RETANQUEO
        defaultDeviceShouldNotBeFound("isRetanqueo.equals=" + UPDATED_IS_RETANQUEO);
    }

    @Test
    @Transactional
    void getAllDevicesByIsRetanqueoIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isRetanqueo in DEFAULT_IS_RETANQUEO or UPDATED_IS_RETANQUEO
        defaultDeviceShouldBeFound("isRetanqueo.in=" + DEFAULT_IS_RETANQUEO + "," + UPDATED_IS_RETANQUEO);

        // Get all the deviceList where isRetanqueo equals to UPDATED_IS_RETANQUEO
        defaultDeviceShouldNotBeFound("isRetanqueo.in=" + UPDATED_IS_RETANQUEO);
    }

    @Test
    @Transactional
    void getAllDevicesByIsRetanqueoIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isRetanqueo is not null
        defaultDeviceShouldBeFound("isRetanqueo.specified=true");

        // Get all the deviceList where isRetanqueo is null
        defaultDeviceShouldNotBeFound("isRetanqueo.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where state equals to DEFAULT_STATE
        defaultDeviceShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the deviceList where state equals to UPDATED_STATE
        defaultDeviceShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDevicesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where state in DEFAULT_STATE or UPDATED_STATE
        defaultDeviceShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the deviceList where state equals to UPDATED_STATE
        defaultDeviceShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDevicesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where state is not null
        defaultDeviceShouldBeFound("state.specified=true");

        // Get all the deviceList where state is null
        defaultDeviceShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByStateContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where state contains DEFAULT_STATE
        defaultDeviceShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the deviceList where state contains UPDATED_STATE
        defaultDeviceShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDevicesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where state does not contain DEFAULT_STATE
        defaultDeviceShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the deviceList where state does not contain UPDATED_STATE
        defaultDeviceShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDevicesByTheoreticalHoldIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where theoreticalHold equals to DEFAULT_THEORETICAL_HOLD
        defaultDeviceShouldBeFound("theoreticalHold.equals=" + DEFAULT_THEORETICAL_HOLD);

        // Get all the deviceList where theoreticalHold equals to UPDATED_THEORETICAL_HOLD
        defaultDeviceShouldNotBeFound("theoreticalHold.equals=" + UPDATED_THEORETICAL_HOLD);
    }

    @Test
    @Transactional
    void getAllDevicesByTheoreticalHoldIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where theoreticalHold in DEFAULT_THEORETICAL_HOLD or UPDATED_THEORETICAL_HOLD
        defaultDeviceShouldBeFound("theoreticalHold.in=" + DEFAULT_THEORETICAL_HOLD + "," + UPDATED_THEORETICAL_HOLD);

        // Get all the deviceList where theoreticalHold equals to UPDATED_THEORETICAL_HOLD
        defaultDeviceShouldNotBeFound("theoreticalHold.in=" + UPDATED_THEORETICAL_HOLD);
    }

    @Test
    @Transactional
    void getAllDevicesByTheoreticalHoldIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where theoreticalHold is not null
        defaultDeviceShouldBeFound("theoreticalHold.specified=true");

        // Get all the deviceList where theoreticalHold is null
        defaultDeviceShouldNotBeFound("theoreticalHold.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByTheoreticalHoldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where theoreticalHold is greater than or equal to DEFAULT_THEORETICAL_HOLD
        defaultDeviceShouldBeFound("theoreticalHold.greaterThanOrEqual=" + DEFAULT_THEORETICAL_HOLD);

        // Get all the deviceList where theoreticalHold is greater than or equal to UPDATED_THEORETICAL_HOLD
        defaultDeviceShouldNotBeFound("theoreticalHold.greaterThanOrEqual=" + UPDATED_THEORETICAL_HOLD);
    }

    @Test
    @Transactional
    void getAllDevicesByTheoreticalHoldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where theoreticalHold is less than or equal to DEFAULT_THEORETICAL_HOLD
        defaultDeviceShouldBeFound("theoreticalHold.lessThanOrEqual=" + DEFAULT_THEORETICAL_HOLD);

        // Get all the deviceList where theoreticalHold is less than or equal to SMALLER_THEORETICAL_HOLD
        defaultDeviceShouldNotBeFound("theoreticalHold.lessThanOrEqual=" + SMALLER_THEORETICAL_HOLD);
    }

    @Test
    @Transactional
    void getAllDevicesByTheoreticalHoldIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where theoreticalHold is less than DEFAULT_THEORETICAL_HOLD
        defaultDeviceShouldNotBeFound("theoreticalHold.lessThan=" + DEFAULT_THEORETICAL_HOLD);

        // Get all the deviceList where theoreticalHold is less than UPDATED_THEORETICAL_HOLD
        defaultDeviceShouldBeFound("theoreticalHold.lessThan=" + UPDATED_THEORETICAL_HOLD);
    }

    @Test
    @Transactional
    void getAllDevicesByTheoreticalHoldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where theoreticalHold is greater than DEFAULT_THEORETICAL_HOLD
        defaultDeviceShouldNotBeFound("theoreticalHold.greaterThan=" + DEFAULT_THEORETICAL_HOLD);

        // Get all the deviceList where theoreticalHold is greater than SMALLER_THEORETICAL_HOLD
        defaultDeviceShouldBeFound("theoreticalHold.greaterThan=" + SMALLER_THEORETICAL_HOLD);
    }

    @Test
    @Transactional
    void getAllDevicesBySasIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasIdentifier equals to DEFAULT_SAS_IDENTIFIER
        defaultDeviceShouldBeFound("sasIdentifier.equals=" + DEFAULT_SAS_IDENTIFIER);

        // Get all the deviceList where sasIdentifier equals to UPDATED_SAS_IDENTIFIER
        defaultDeviceShouldNotBeFound("sasIdentifier.equals=" + UPDATED_SAS_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDevicesBySasIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasIdentifier in DEFAULT_SAS_IDENTIFIER or UPDATED_SAS_IDENTIFIER
        defaultDeviceShouldBeFound("sasIdentifier.in=" + DEFAULT_SAS_IDENTIFIER + "," + UPDATED_SAS_IDENTIFIER);

        // Get all the deviceList where sasIdentifier equals to UPDATED_SAS_IDENTIFIER
        defaultDeviceShouldNotBeFound("sasIdentifier.in=" + UPDATED_SAS_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDevicesBySasIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasIdentifier is not null
        defaultDeviceShouldBeFound("sasIdentifier.specified=true");

        // Get all the deviceList where sasIdentifier is null
        defaultDeviceShouldNotBeFound("sasIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesBySasIdentifierIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasIdentifier is greater than or equal to DEFAULT_SAS_IDENTIFIER
        defaultDeviceShouldBeFound("sasIdentifier.greaterThanOrEqual=" + DEFAULT_SAS_IDENTIFIER);

        // Get all the deviceList where sasIdentifier is greater than or equal to UPDATED_SAS_IDENTIFIER
        defaultDeviceShouldNotBeFound("sasIdentifier.greaterThanOrEqual=" + UPDATED_SAS_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDevicesBySasIdentifierIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasIdentifier is less than or equal to DEFAULT_SAS_IDENTIFIER
        defaultDeviceShouldBeFound("sasIdentifier.lessThanOrEqual=" + DEFAULT_SAS_IDENTIFIER);

        // Get all the deviceList where sasIdentifier is less than or equal to SMALLER_SAS_IDENTIFIER
        defaultDeviceShouldNotBeFound("sasIdentifier.lessThanOrEqual=" + SMALLER_SAS_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDevicesBySasIdentifierIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasIdentifier is less than DEFAULT_SAS_IDENTIFIER
        defaultDeviceShouldNotBeFound("sasIdentifier.lessThan=" + DEFAULT_SAS_IDENTIFIER);

        // Get all the deviceList where sasIdentifier is less than UPDATED_SAS_IDENTIFIER
        defaultDeviceShouldBeFound("sasIdentifier.lessThan=" + UPDATED_SAS_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDevicesBySasIdentifierIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where sasIdentifier is greater than DEFAULT_SAS_IDENTIFIER
        defaultDeviceShouldNotBeFound("sasIdentifier.greaterThan=" + DEFAULT_SAS_IDENTIFIER);

        // Get all the deviceList where sasIdentifier is greater than SMALLER_SAS_IDENTIFIER
        defaultDeviceShouldBeFound("sasIdentifier.greaterThan=" + SMALLER_SAS_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDevicesByCreditLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where creditLimit equals to DEFAULT_CREDIT_LIMIT
        defaultDeviceShouldBeFound("creditLimit.equals=" + DEFAULT_CREDIT_LIMIT);

        // Get all the deviceList where creditLimit equals to UPDATED_CREDIT_LIMIT
        defaultDeviceShouldNotBeFound("creditLimit.equals=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllDevicesByCreditLimitIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where creditLimit in DEFAULT_CREDIT_LIMIT or UPDATED_CREDIT_LIMIT
        defaultDeviceShouldBeFound("creditLimit.in=" + DEFAULT_CREDIT_LIMIT + "," + UPDATED_CREDIT_LIMIT);

        // Get all the deviceList where creditLimit equals to UPDATED_CREDIT_LIMIT
        defaultDeviceShouldNotBeFound("creditLimit.in=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllDevicesByCreditLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where creditLimit is not null
        defaultDeviceShouldBeFound("creditLimit.specified=true");

        // Get all the deviceList where creditLimit is null
        defaultDeviceShouldNotBeFound("creditLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByCreditLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where creditLimit is greater than or equal to DEFAULT_CREDIT_LIMIT
        defaultDeviceShouldBeFound("creditLimit.greaterThanOrEqual=" + DEFAULT_CREDIT_LIMIT);

        // Get all the deviceList where creditLimit is greater than or equal to UPDATED_CREDIT_LIMIT
        defaultDeviceShouldNotBeFound("creditLimit.greaterThanOrEqual=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllDevicesByCreditLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where creditLimit is less than or equal to DEFAULT_CREDIT_LIMIT
        defaultDeviceShouldBeFound("creditLimit.lessThanOrEqual=" + DEFAULT_CREDIT_LIMIT);

        // Get all the deviceList where creditLimit is less than or equal to SMALLER_CREDIT_LIMIT
        defaultDeviceShouldNotBeFound("creditLimit.lessThanOrEqual=" + SMALLER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllDevicesByCreditLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where creditLimit is less than DEFAULT_CREDIT_LIMIT
        defaultDeviceShouldNotBeFound("creditLimit.lessThan=" + DEFAULT_CREDIT_LIMIT);

        // Get all the deviceList where creditLimit is less than UPDATED_CREDIT_LIMIT
        defaultDeviceShouldBeFound("creditLimit.lessThan=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllDevicesByCreditLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where creditLimit is greater than DEFAULT_CREDIT_LIMIT
        defaultDeviceShouldNotBeFound("creditLimit.greaterThan=" + DEFAULT_CREDIT_LIMIT);

        // Get all the deviceList where creditLimit is greater than SMALLER_CREDIT_LIMIT
        defaultDeviceShouldBeFound("creditLimit.greaterThan=" + SMALLER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllDevicesByHasHooperIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where hasHooper equals to DEFAULT_HAS_HOOPER
        defaultDeviceShouldBeFound("hasHooper.equals=" + DEFAULT_HAS_HOOPER);

        // Get all the deviceList where hasHooper equals to UPDATED_HAS_HOOPER
        defaultDeviceShouldNotBeFound("hasHooper.equals=" + UPDATED_HAS_HOOPER);
    }

    @Test
    @Transactional
    void getAllDevicesByHasHooperIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where hasHooper in DEFAULT_HAS_HOOPER or UPDATED_HAS_HOOPER
        defaultDeviceShouldBeFound("hasHooper.in=" + DEFAULT_HAS_HOOPER + "," + UPDATED_HAS_HOOPER);

        // Get all the deviceList where hasHooper equals to UPDATED_HAS_HOOPER
        defaultDeviceShouldNotBeFound("hasHooper.in=" + UPDATED_HAS_HOOPER);
    }

    @Test
    @Transactional
    void getAllDevicesByHasHooperIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where hasHooper is not null
        defaultDeviceShouldBeFound("hasHooper.specified=true");

        // Get all the deviceList where hasHooper is null
        defaultDeviceShouldNotBeFound("hasHooper.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosCode equals to DEFAULT_COLJUEGOS_CODE
        defaultDeviceShouldBeFound("coljuegosCode.equals=" + DEFAULT_COLJUEGOS_CODE);

        // Get all the deviceList where coljuegosCode equals to UPDATED_COLJUEGOS_CODE
        defaultDeviceShouldNotBeFound("coljuegosCode.equals=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosCodeIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosCode in DEFAULT_COLJUEGOS_CODE or UPDATED_COLJUEGOS_CODE
        defaultDeviceShouldBeFound("coljuegosCode.in=" + DEFAULT_COLJUEGOS_CODE + "," + UPDATED_COLJUEGOS_CODE);

        // Get all the deviceList where coljuegosCode equals to UPDATED_COLJUEGOS_CODE
        defaultDeviceShouldNotBeFound("coljuegosCode.in=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosCode is not null
        defaultDeviceShouldBeFound("coljuegosCode.specified=true");

        // Get all the deviceList where coljuegosCode is null
        defaultDeviceShouldNotBeFound("coljuegosCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosCodeContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosCode contains DEFAULT_COLJUEGOS_CODE
        defaultDeviceShouldBeFound("coljuegosCode.contains=" + DEFAULT_COLJUEGOS_CODE);

        // Get all the deviceList where coljuegosCode contains UPDATED_COLJUEGOS_CODE
        defaultDeviceShouldNotBeFound("coljuegosCode.contains=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosCodeNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosCode does not contain DEFAULT_COLJUEGOS_CODE
        defaultDeviceShouldNotBeFound("coljuegosCode.doesNotContain=" + DEFAULT_COLJUEGOS_CODE);

        // Get all the deviceList where coljuegosCode does not contain UPDATED_COLJUEGOS_CODE
        defaultDeviceShouldBeFound("coljuegosCode.doesNotContain=" + UPDATED_COLJUEGOS_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByFabricationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where fabricationDate equals to DEFAULT_FABRICATION_DATE
        defaultDeviceShouldBeFound("fabricationDate.equals=" + DEFAULT_FABRICATION_DATE);

        // Get all the deviceList where fabricationDate equals to UPDATED_FABRICATION_DATE
        defaultDeviceShouldNotBeFound("fabricationDate.equals=" + UPDATED_FABRICATION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByFabricationDateIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where fabricationDate in DEFAULT_FABRICATION_DATE or UPDATED_FABRICATION_DATE
        defaultDeviceShouldBeFound("fabricationDate.in=" + DEFAULT_FABRICATION_DATE + "," + UPDATED_FABRICATION_DATE);

        // Get all the deviceList where fabricationDate equals to UPDATED_FABRICATION_DATE
        defaultDeviceShouldNotBeFound("fabricationDate.in=" + UPDATED_FABRICATION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByFabricationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where fabricationDate is not null
        defaultDeviceShouldBeFound("fabricationDate.specified=true");

        // Get all the deviceList where fabricationDate is null
        defaultDeviceShouldNotBeFound("fabricationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByFabricationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where fabricationDate is greater than or equal to DEFAULT_FABRICATION_DATE
        defaultDeviceShouldBeFound("fabricationDate.greaterThanOrEqual=" + DEFAULT_FABRICATION_DATE);

        // Get all the deviceList where fabricationDate is greater than or equal to UPDATED_FABRICATION_DATE
        defaultDeviceShouldNotBeFound("fabricationDate.greaterThanOrEqual=" + UPDATED_FABRICATION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByFabricationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where fabricationDate is less than or equal to DEFAULT_FABRICATION_DATE
        defaultDeviceShouldBeFound("fabricationDate.lessThanOrEqual=" + DEFAULT_FABRICATION_DATE);

        // Get all the deviceList where fabricationDate is less than or equal to SMALLER_FABRICATION_DATE
        defaultDeviceShouldNotBeFound("fabricationDate.lessThanOrEqual=" + SMALLER_FABRICATION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByFabricationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where fabricationDate is less than DEFAULT_FABRICATION_DATE
        defaultDeviceShouldNotBeFound("fabricationDate.lessThan=" + DEFAULT_FABRICATION_DATE);

        // Get all the deviceList where fabricationDate is less than UPDATED_FABRICATION_DATE
        defaultDeviceShouldBeFound("fabricationDate.lessThan=" + UPDATED_FABRICATION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByFabricationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where fabricationDate is greater than DEFAULT_FABRICATION_DATE
        defaultDeviceShouldNotBeFound("fabricationDate.greaterThan=" + DEFAULT_FABRICATION_DATE);

        // Get all the deviceList where fabricationDate is greater than SMALLER_FABRICATION_DATE
        defaultDeviceShouldBeFound("fabricationDate.greaterThan=" + SMALLER_FABRICATION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrentTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currentToken equals to DEFAULT_CURRENT_TOKEN
        defaultDeviceShouldBeFound("currentToken.equals=" + DEFAULT_CURRENT_TOKEN);

        // Get all the deviceList where currentToken equals to UPDATED_CURRENT_TOKEN
        defaultDeviceShouldNotBeFound("currentToken.equals=" + UPDATED_CURRENT_TOKEN);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrentTokenIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currentToken in DEFAULT_CURRENT_TOKEN or UPDATED_CURRENT_TOKEN
        defaultDeviceShouldBeFound("currentToken.in=" + DEFAULT_CURRENT_TOKEN + "," + UPDATED_CURRENT_TOKEN);

        // Get all the deviceList where currentToken equals to UPDATED_CURRENT_TOKEN
        defaultDeviceShouldNotBeFound("currentToken.in=" + UPDATED_CURRENT_TOKEN);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrentTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currentToken is not null
        defaultDeviceShouldBeFound("currentToken.specified=true");

        // Get all the deviceList where currentToken is null
        defaultDeviceShouldNotBeFound("currentToken.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByCurrentTokenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currentToken is greater than or equal to DEFAULT_CURRENT_TOKEN
        defaultDeviceShouldBeFound("currentToken.greaterThanOrEqual=" + DEFAULT_CURRENT_TOKEN);

        // Get all the deviceList where currentToken is greater than or equal to UPDATED_CURRENT_TOKEN
        defaultDeviceShouldNotBeFound("currentToken.greaterThanOrEqual=" + UPDATED_CURRENT_TOKEN);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrentTokenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currentToken is less than or equal to DEFAULT_CURRENT_TOKEN
        defaultDeviceShouldBeFound("currentToken.lessThanOrEqual=" + DEFAULT_CURRENT_TOKEN);

        // Get all the deviceList where currentToken is less than or equal to SMALLER_CURRENT_TOKEN
        defaultDeviceShouldNotBeFound("currentToken.lessThanOrEqual=" + SMALLER_CURRENT_TOKEN);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrentTokenIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currentToken is less than DEFAULT_CURRENT_TOKEN
        defaultDeviceShouldNotBeFound("currentToken.lessThan=" + DEFAULT_CURRENT_TOKEN);

        // Get all the deviceList where currentToken is less than UPDATED_CURRENT_TOKEN
        defaultDeviceShouldBeFound("currentToken.lessThan=" + UPDATED_CURRENT_TOKEN);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrentTokenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currentToken is greater than DEFAULT_CURRENT_TOKEN
        defaultDeviceShouldNotBeFound("currentToken.greaterThan=" + DEFAULT_CURRENT_TOKEN);

        // Get all the deviceList where currentToken is greater than SMALLER_CURRENT_TOKEN
        defaultDeviceShouldBeFound("currentToken.greaterThan=" + SMALLER_CURRENT_TOKEN);
    }

    @Test
    @Transactional
    void getAllDevicesByDenominationTitoIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where denominationTito equals to DEFAULT_DENOMINATION_TITO
        defaultDeviceShouldBeFound("denominationTito.equals=" + DEFAULT_DENOMINATION_TITO);

        // Get all the deviceList where denominationTito equals to UPDATED_DENOMINATION_TITO
        defaultDeviceShouldNotBeFound("denominationTito.equals=" + UPDATED_DENOMINATION_TITO);
    }

    @Test
    @Transactional
    void getAllDevicesByDenominationTitoIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where denominationTito in DEFAULT_DENOMINATION_TITO or UPDATED_DENOMINATION_TITO
        defaultDeviceShouldBeFound("denominationTito.in=" + DEFAULT_DENOMINATION_TITO + "," + UPDATED_DENOMINATION_TITO);

        // Get all the deviceList where denominationTito equals to UPDATED_DENOMINATION_TITO
        defaultDeviceShouldNotBeFound("denominationTito.in=" + UPDATED_DENOMINATION_TITO);
    }

    @Test
    @Transactional
    void getAllDevicesByDenominationTitoIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where denominationTito is not null
        defaultDeviceShouldBeFound("denominationTito.specified=true");

        // Get all the deviceList where denominationTito is null
        defaultDeviceShouldNotBeFound("denominationTito.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByDenominationTitoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where denominationTito is greater than or equal to DEFAULT_DENOMINATION_TITO
        defaultDeviceShouldBeFound("denominationTito.greaterThanOrEqual=" + DEFAULT_DENOMINATION_TITO);

        // Get all the deviceList where denominationTito is greater than or equal to UPDATED_DENOMINATION_TITO
        defaultDeviceShouldNotBeFound("denominationTito.greaterThanOrEqual=" + UPDATED_DENOMINATION_TITO);
    }

    @Test
    @Transactional
    void getAllDevicesByDenominationTitoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where denominationTito is less than or equal to DEFAULT_DENOMINATION_TITO
        defaultDeviceShouldBeFound("denominationTito.lessThanOrEqual=" + DEFAULT_DENOMINATION_TITO);

        // Get all the deviceList where denominationTito is less than or equal to SMALLER_DENOMINATION_TITO
        defaultDeviceShouldNotBeFound("denominationTito.lessThanOrEqual=" + SMALLER_DENOMINATION_TITO);
    }

    @Test
    @Transactional
    void getAllDevicesByDenominationTitoIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where denominationTito is less than DEFAULT_DENOMINATION_TITO
        defaultDeviceShouldNotBeFound("denominationTito.lessThan=" + DEFAULT_DENOMINATION_TITO);

        // Get all the deviceList where denominationTito is less than UPDATED_DENOMINATION_TITO
        defaultDeviceShouldBeFound("denominationTito.lessThan=" + UPDATED_DENOMINATION_TITO);
    }

    @Test
    @Transactional
    void getAllDevicesByDenominationTitoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where denominationTito is greater than DEFAULT_DENOMINATION_TITO
        defaultDeviceShouldNotBeFound("denominationTito.greaterThan=" + DEFAULT_DENOMINATION_TITO);

        // Get all the deviceList where denominationTito is greater than SMALLER_DENOMINATION_TITO
        defaultDeviceShouldBeFound("denominationTito.greaterThan=" + SMALLER_DENOMINATION_TITO);
    }

    @Test
    @Transactional
    void getAllDevicesByEndLostCommunicationIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where endLostCommunication equals to DEFAULT_END_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("endLostCommunication.equals=" + DEFAULT_END_LOST_COMMUNICATION);

        // Get all the deviceList where endLostCommunication equals to UPDATED_END_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("endLostCommunication.equals=" + UPDATED_END_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByEndLostCommunicationIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where endLostCommunication in DEFAULT_END_LOST_COMMUNICATION or UPDATED_END_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("endLostCommunication.in=" + DEFAULT_END_LOST_COMMUNICATION + "," + UPDATED_END_LOST_COMMUNICATION);

        // Get all the deviceList where endLostCommunication equals to UPDATED_END_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("endLostCommunication.in=" + UPDATED_END_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByEndLostCommunicationIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where endLostCommunication is not null
        defaultDeviceShouldBeFound("endLostCommunication.specified=true");

        // Get all the deviceList where endLostCommunication is null
        defaultDeviceShouldNotBeFound("endLostCommunication.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByEndLostCommunicationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where endLostCommunication is greater than or equal to DEFAULT_END_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("endLostCommunication.greaterThanOrEqual=" + DEFAULT_END_LOST_COMMUNICATION);

        // Get all the deviceList where endLostCommunication is greater than or equal to UPDATED_END_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("endLostCommunication.greaterThanOrEqual=" + UPDATED_END_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByEndLostCommunicationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where endLostCommunication is less than or equal to DEFAULT_END_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("endLostCommunication.lessThanOrEqual=" + DEFAULT_END_LOST_COMMUNICATION);

        // Get all the deviceList where endLostCommunication is less than or equal to SMALLER_END_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("endLostCommunication.lessThanOrEqual=" + SMALLER_END_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByEndLostCommunicationIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where endLostCommunication is less than DEFAULT_END_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("endLostCommunication.lessThan=" + DEFAULT_END_LOST_COMMUNICATION);

        // Get all the deviceList where endLostCommunication is less than UPDATED_END_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("endLostCommunication.lessThan=" + UPDATED_END_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByEndLostCommunicationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where endLostCommunication is greater than DEFAULT_END_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("endLostCommunication.greaterThan=" + DEFAULT_END_LOST_COMMUNICATION);

        // Get all the deviceList where endLostCommunication is greater than SMALLER_END_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("endLostCommunication.greaterThan=" + SMALLER_END_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByStartLostCommunicationIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where startLostCommunication equals to DEFAULT_START_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("startLostCommunication.equals=" + DEFAULT_START_LOST_COMMUNICATION);

        // Get all the deviceList where startLostCommunication equals to UPDATED_START_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("startLostCommunication.equals=" + UPDATED_START_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByStartLostCommunicationIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where startLostCommunication in DEFAULT_START_LOST_COMMUNICATION or UPDATED_START_LOST_COMMUNICATION
        defaultDeviceShouldBeFound(
            "startLostCommunication.in=" + DEFAULT_START_LOST_COMMUNICATION + "," + UPDATED_START_LOST_COMMUNICATION
        );

        // Get all the deviceList where startLostCommunication equals to UPDATED_START_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("startLostCommunication.in=" + UPDATED_START_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByStartLostCommunicationIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where startLostCommunication is not null
        defaultDeviceShouldBeFound("startLostCommunication.specified=true");

        // Get all the deviceList where startLostCommunication is null
        defaultDeviceShouldNotBeFound("startLostCommunication.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByStartLostCommunicationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where startLostCommunication is greater than or equal to DEFAULT_START_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("startLostCommunication.greaterThanOrEqual=" + DEFAULT_START_LOST_COMMUNICATION);

        // Get all the deviceList where startLostCommunication is greater than or equal to UPDATED_START_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("startLostCommunication.greaterThanOrEqual=" + UPDATED_START_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByStartLostCommunicationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where startLostCommunication is less than or equal to DEFAULT_START_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("startLostCommunication.lessThanOrEqual=" + DEFAULT_START_LOST_COMMUNICATION);

        // Get all the deviceList where startLostCommunication is less than or equal to SMALLER_START_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("startLostCommunication.lessThanOrEqual=" + SMALLER_START_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByStartLostCommunicationIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where startLostCommunication is less than DEFAULT_START_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("startLostCommunication.lessThan=" + DEFAULT_START_LOST_COMMUNICATION);

        // Get all the deviceList where startLostCommunication is less than UPDATED_START_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("startLostCommunication.lessThan=" + UPDATED_START_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByStartLostCommunicationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where startLostCommunication is greater than DEFAULT_START_LOST_COMMUNICATION
        defaultDeviceShouldNotBeFound("startLostCommunication.greaterThan=" + DEFAULT_START_LOST_COMMUNICATION);

        // Get all the deviceList where startLostCommunication is greater than SMALLER_START_LOST_COMMUNICATION
        defaultDeviceShouldBeFound("startLostCommunication.greaterThan=" + SMALLER_START_LOST_COMMUNICATION);
    }

    @Test
    @Transactional
    void getAllDevicesByReportMultiplierIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportMultiplier equals to DEFAULT_REPORT_MULTIPLIER
        defaultDeviceShouldBeFound("reportMultiplier.equals=" + DEFAULT_REPORT_MULTIPLIER);

        // Get all the deviceList where reportMultiplier equals to UPDATED_REPORT_MULTIPLIER
        defaultDeviceShouldNotBeFound("reportMultiplier.equals=" + UPDATED_REPORT_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllDevicesByReportMultiplierIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportMultiplier in DEFAULT_REPORT_MULTIPLIER or UPDATED_REPORT_MULTIPLIER
        defaultDeviceShouldBeFound("reportMultiplier.in=" + DEFAULT_REPORT_MULTIPLIER + "," + UPDATED_REPORT_MULTIPLIER);

        // Get all the deviceList where reportMultiplier equals to UPDATED_REPORT_MULTIPLIER
        defaultDeviceShouldNotBeFound("reportMultiplier.in=" + UPDATED_REPORT_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllDevicesByReportMultiplierIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportMultiplier is not null
        defaultDeviceShouldBeFound("reportMultiplier.specified=true");

        // Get all the deviceList where reportMultiplier is null
        defaultDeviceShouldNotBeFound("reportMultiplier.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByReportMultiplierIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportMultiplier is greater than or equal to DEFAULT_REPORT_MULTIPLIER
        defaultDeviceShouldBeFound("reportMultiplier.greaterThanOrEqual=" + DEFAULT_REPORT_MULTIPLIER);

        // Get all the deviceList where reportMultiplier is greater than or equal to UPDATED_REPORT_MULTIPLIER
        defaultDeviceShouldNotBeFound("reportMultiplier.greaterThanOrEqual=" + UPDATED_REPORT_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllDevicesByReportMultiplierIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportMultiplier is less than or equal to DEFAULT_REPORT_MULTIPLIER
        defaultDeviceShouldBeFound("reportMultiplier.lessThanOrEqual=" + DEFAULT_REPORT_MULTIPLIER);

        // Get all the deviceList where reportMultiplier is less than or equal to SMALLER_REPORT_MULTIPLIER
        defaultDeviceShouldNotBeFound("reportMultiplier.lessThanOrEqual=" + SMALLER_REPORT_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllDevicesByReportMultiplierIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportMultiplier is less than DEFAULT_REPORT_MULTIPLIER
        defaultDeviceShouldNotBeFound("reportMultiplier.lessThan=" + DEFAULT_REPORT_MULTIPLIER);

        // Get all the deviceList where reportMultiplier is less than UPDATED_REPORT_MULTIPLIER
        defaultDeviceShouldBeFound("reportMultiplier.lessThan=" + UPDATED_REPORT_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllDevicesByReportMultiplierIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportMultiplier is greater than DEFAULT_REPORT_MULTIPLIER
        defaultDeviceShouldNotBeFound("reportMultiplier.greaterThan=" + DEFAULT_REPORT_MULTIPLIER);

        // Get all the deviceList where reportMultiplier is greater than SMALLER_REPORT_MULTIPLIER
        defaultDeviceShouldBeFound("reportMultiplier.greaterThan=" + SMALLER_REPORT_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllDevicesByNuidIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where nuid equals to DEFAULT_NUID
        defaultDeviceShouldBeFound("nuid.equals=" + DEFAULT_NUID);

        // Get all the deviceList where nuid equals to UPDATED_NUID
        defaultDeviceShouldNotBeFound("nuid.equals=" + UPDATED_NUID);
    }

    @Test
    @Transactional
    void getAllDevicesByNuidIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where nuid in DEFAULT_NUID or UPDATED_NUID
        defaultDeviceShouldBeFound("nuid.in=" + DEFAULT_NUID + "," + UPDATED_NUID);

        // Get all the deviceList where nuid equals to UPDATED_NUID
        defaultDeviceShouldNotBeFound("nuid.in=" + UPDATED_NUID);
    }

    @Test
    @Transactional
    void getAllDevicesByNuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where nuid is not null
        defaultDeviceShouldBeFound("nuid.specified=true");

        // Get all the deviceList where nuid is null
        defaultDeviceShouldNotBeFound("nuid.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByNuidContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where nuid contains DEFAULT_NUID
        defaultDeviceShouldBeFound("nuid.contains=" + DEFAULT_NUID);

        // Get all the deviceList where nuid contains UPDATED_NUID
        defaultDeviceShouldNotBeFound("nuid.contains=" + UPDATED_NUID);
    }

    @Test
    @Transactional
    void getAllDevicesByNuidNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where nuid does not contain DEFAULT_NUID
        defaultDeviceShouldNotBeFound("nuid.doesNotContain=" + DEFAULT_NUID);

        // Get all the deviceList where nuid does not contain UPDATED_NUID
        defaultDeviceShouldBeFound("nuid.doesNotContain=" + UPDATED_NUID);
    }

    @Test
    @Transactional
    void getAllDevicesByPayManualPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where payManualPrize equals to DEFAULT_PAY_MANUAL_PRIZE
        defaultDeviceShouldBeFound("payManualPrize.equals=" + DEFAULT_PAY_MANUAL_PRIZE);

        // Get all the deviceList where payManualPrize equals to UPDATED_PAY_MANUAL_PRIZE
        defaultDeviceShouldNotBeFound("payManualPrize.equals=" + UPDATED_PAY_MANUAL_PRIZE);
    }

    @Test
    @Transactional
    void getAllDevicesByPayManualPrizeIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where payManualPrize in DEFAULT_PAY_MANUAL_PRIZE or UPDATED_PAY_MANUAL_PRIZE
        defaultDeviceShouldBeFound("payManualPrize.in=" + DEFAULT_PAY_MANUAL_PRIZE + "," + UPDATED_PAY_MANUAL_PRIZE);

        // Get all the deviceList where payManualPrize equals to UPDATED_PAY_MANUAL_PRIZE
        defaultDeviceShouldNotBeFound("payManualPrize.in=" + UPDATED_PAY_MANUAL_PRIZE);
    }

    @Test
    @Transactional
    void getAllDevicesByPayManualPrizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where payManualPrize is not null
        defaultDeviceShouldBeFound("payManualPrize.specified=true");

        // Get all the deviceList where payManualPrize is null
        defaultDeviceShouldNotBeFound("payManualPrize.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByManualHandpayIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualHandpay equals to DEFAULT_MANUAL_HANDPAY
        defaultDeviceShouldBeFound("manualHandpay.equals=" + DEFAULT_MANUAL_HANDPAY);

        // Get all the deviceList where manualHandpay equals to UPDATED_MANUAL_HANDPAY
        defaultDeviceShouldNotBeFound("manualHandpay.equals=" + UPDATED_MANUAL_HANDPAY);
    }

    @Test
    @Transactional
    void getAllDevicesByManualHandpayIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualHandpay in DEFAULT_MANUAL_HANDPAY or UPDATED_MANUAL_HANDPAY
        defaultDeviceShouldBeFound("manualHandpay.in=" + DEFAULT_MANUAL_HANDPAY + "," + UPDATED_MANUAL_HANDPAY);

        // Get all the deviceList where manualHandpay equals to UPDATED_MANUAL_HANDPAY
        defaultDeviceShouldNotBeFound("manualHandpay.in=" + UPDATED_MANUAL_HANDPAY);
    }

    @Test
    @Transactional
    void getAllDevicesByManualHandpayIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualHandpay is not null
        defaultDeviceShouldBeFound("manualHandpay.specified=true");

        // Get all the deviceList where manualHandpay is null
        defaultDeviceShouldNotBeFound("manualHandpay.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByManualJackpotIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualJackpot equals to DEFAULT_MANUAL_JACKPOT
        defaultDeviceShouldBeFound("manualJackpot.equals=" + DEFAULT_MANUAL_JACKPOT);

        // Get all the deviceList where manualJackpot equals to UPDATED_MANUAL_JACKPOT
        defaultDeviceShouldNotBeFound("manualJackpot.equals=" + UPDATED_MANUAL_JACKPOT);
    }

    @Test
    @Transactional
    void getAllDevicesByManualJackpotIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualJackpot in DEFAULT_MANUAL_JACKPOT or UPDATED_MANUAL_JACKPOT
        defaultDeviceShouldBeFound("manualJackpot.in=" + DEFAULT_MANUAL_JACKPOT + "," + UPDATED_MANUAL_JACKPOT);

        // Get all the deviceList where manualJackpot equals to UPDATED_MANUAL_JACKPOT
        defaultDeviceShouldNotBeFound("manualJackpot.in=" + UPDATED_MANUAL_JACKPOT);
    }

    @Test
    @Transactional
    void getAllDevicesByManualJackpotIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualJackpot is not null
        defaultDeviceShouldBeFound("manualJackpot.specified=true");

        // Get all the deviceList where manualJackpot is null
        defaultDeviceShouldNotBeFound("manualJackpot.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByManualGameEventIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualGameEvent equals to DEFAULT_MANUAL_GAME_EVENT
        defaultDeviceShouldBeFound("manualGameEvent.equals=" + DEFAULT_MANUAL_GAME_EVENT);

        // Get all the deviceList where manualGameEvent equals to UPDATED_MANUAL_GAME_EVENT
        defaultDeviceShouldNotBeFound("manualGameEvent.equals=" + UPDATED_MANUAL_GAME_EVENT);
    }

    @Test
    @Transactional
    void getAllDevicesByManualGameEventIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualGameEvent in DEFAULT_MANUAL_GAME_EVENT or UPDATED_MANUAL_GAME_EVENT
        defaultDeviceShouldBeFound("manualGameEvent.in=" + DEFAULT_MANUAL_GAME_EVENT + "," + UPDATED_MANUAL_GAME_EVENT);

        // Get all the deviceList where manualGameEvent equals to UPDATED_MANUAL_GAME_EVENT
        defaultDeviceShouldNotBeFound("manualGameEvent.in=" + UPDATED_MANUAL_GAME_EVENT);
    }

    @Test
    @Transactional
    void getAllDevicesByManualGameEventIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where manualGameEvent is not null
        defaultDeviceShouldBeFound("manualGameEvent.specified=true");

        // Get all the deviceList where manualGameEvent is null
        defaultDeviceShouldNotBeFound("manualGameEvent.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByBetCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where betCode equals to DEFAULT_BET_CODE
        defaultDeviceShouldBeFound("betCode.equals=" + DEFAULT_BET_CODE);

        // Get all the deviceList where betCode equals to UPDATED_BET_CODE
        defaultDeviceShouldNotBeFound("betCode.equals=" + UPDATED_BET_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByBetCodeIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where betCode in DEFAULT_BET_CODE or UPDATED_BET_CODE
        defaultDeviceShouldBeFound("betCode.in=" + DEFAULT_BET_CODE + "," + UPDATED_BET_CODE);

        // Get all the deviceList where betCode equals to UPDATED_BET_CODE
        defaultDeviceShouldNotBeFound("betCode.in=" + UPDATED_BET_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByBetCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where betCode is not null
        defaultDeviceShouldBeFound("betCode.specified=true");

        // Get all the deviceList where betCode is null
        defaultDeviceShouldNotBeFound("betCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByBetCodeContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where betCode contains DEFAULT_BET_CODE
        defaultDeviceShouldBeFound("betCode.contains=" + DEFAULT_BET_CODE);

        // Get all the deviceList where betCode contains UPDATED_BET_CODE
        defaultDeviceShouldNotBeFound("betCode.contains=" + UPDATED_BET_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByBetCodeNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where betCode does not contain DEFAULT_BET_CODE
        defaultDeviceShouldNotBeFound("betCode.doesNotContain=" + DEFAULT_BET_CODE);

        // Get all the deviceList where betCode does not contain UPDATED_BET_CODE
        defaultDeviceShouldBeFound("betCode.doesNotContain=" + UPDATED_BET_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByHomologationIndicatorIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where homologationIndicator equals to DEFAULT_HOMOLOGATION_INDICATOR
        defaultDeviceShouldBeFound("homologationIndicator.equals=" + DEFAULT_HOMOLOGATION_INDICATOR);

        // Get all the deviceList where homologationIndicator equals to UPDATED_HOMOLOGATION_INDICATOR
        defaultDeviceShouldNotBeFound("homologationIndicator.equals=" + UPDATED_HOMOLOGATION_INDICATOR);
    }

    @Test
    @Transactional
    void getAllDevicesByHomologationIndicatorIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where homologationIndicator in DEFAULT_HOMOLOGATION_INDICATOR or UPDATED_HOMOLOGATION_INDICATOR
        defaultDeviceShouldBeFound("homologationIndicator.in=" + DEFAULT_HOMOLOGATION_INDICATOR + "," + UPDATED_HOMOLOGATION_INDICATOR);

        // Get all the deviceList where homologationIndicator equals to UPDATED_HOMOLOGATION_INDICATOR
        defaultDeviceShouldNotBeFound("homologationIndicator.in=" + UPDATED_HOMOLOGATION_INDICATOR);
    }

    @Test
    @Transactional
    void getAllDevicesByHomologationIndicatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where homologationIndicator is not null
        defaultDeviceShouldBeFound("homologationIndicator.specified=true");

        // Get all the deviceList where homologationIndicator is null
        defaultDeviceShouldNotBeFound("homologationIndicator.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosModelIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosModel equals to DEFAULT_COLJUEGOS_MODEL
        defaultDeviceShouldBeFound("coljuegosModel.equals=" + DEFAULT_COLJUEGOS_MODEL);

        // Get all the deviceList where coljuegosModel equals to UPDATED_COLJUEGOS_MODEL
        defaultDeviceShouldNotBeFound("coljuegosModel.equals=" + UPDATED_COLJUEGOS_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosModelIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosModel in DEFAULT_COLJUEGOS_MODEL or UPDATED_COLJUEGOS_MODEL
        defaultDeviceShouldBeFound("coljuegosModel.in=" + DEFAULT_COLJUEGOS_MODEL + "," + UPDATED_COLJUEGOS_MODEL);

        // Get all the deviceList where coljuegosModel equals to UPDATED_COLJUEGOS_MODEL
        defaultDeviceShouldNotBeFound("coljuegosModel.in=" + UPDATED_COLJUEGOS_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosModel is not null
        defaultDeviceShouldBeFound("coljuegosModel.specified=true");

        // Get all the deviceList where coljuegosModel is null
        defaultDeviceShouldNotBeFound("coljuegosModel.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosModelContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosModel contains DEFAULT_COLJUEGOS_MODEL
        defaultDeviceShouldBeFound("coljuegosModel.contains=" + DEFAULT_COLJUEGOS_MODEL);

        // Get all the deviceList where coljuegosModel contains UPDATED_COLJUEGOS_MODEL
        defaultDeviceShouldNotBeFound("coljuegosModel.contains=" + UPDATED_COLJUEGOS_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesByColjuegosModelNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where coljuegosModel does not contain DEFAULT_COLJUEGOS_MODEL
        defaultDeviceShouldNotBeFound("coljuegosModel.doesNotContain=" + DEFAULT_COLJUEGOS_MODEL);

        // Get all the deviceList where coljuegosModel does not contain UPDATED_COLJUEGOS_MODEL
        defaultDeviceShouldBeFound("coljuegosModel.doesNotContain=" + UPDATED_COLJUEGOS_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesByReportableIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportable equals to DEFAULT_REPORTABLE
        defaultDeviceShouldBeFound("reportable.equals=" + DEFAULT_REPORTABLE);

        // Get all the deviceList where reportable equals to UPDATED_REPORTABLE
        defaultDeviceShouldNotBeFound("reportable.equals=" + UPDATED_REPORTABLE);
    }

    @Test
    @Transactional
    void getAllDevicesByReportableIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportable in DEFAULT_REPORTABLE or UPDATED_REPORTABLE
        defaultDeviceShouldBeFound("reportable.in=" + DEFAULT_REPORTABLE + "," + UPDATED_REPORTABLE);

        // Get all the deviceList where reportable equals to UPDATED_REPORTABLE
        defaultDeviceShouldNotBeFound("reportable.in=" + UPDATED_REPORTABLE);
    }

    @Test
    @Transactional
    void getAllDevicesByReportableIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where reportable is not null
        defaultDeviceShouldBeFound("reportable.specified=true");

        // Get all the deviceList where reportable is null
        defaultDeviceShouldNotBeFound("reportable.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByAftDenominationIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where aftDenomination equals to DEFAULT_AFT_DENOMINATION
        defaultDeviceShouldBeFound("aftDenomination.equals=" + DEFAULT_AFT_DENOMINATION);

        // Get all the deviceList where aftDenomination equals to UPDATED_AFT_DENOMINATION
        defaultDeviceShouldNotBeFound("aftDenomination.equals=" + UPDATED_AFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByAftDenominationIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where aftDenomination in DEFAULT_AFT_DENOMINATION or UPDATED_AFT_DENOMINATION
        defaultDeviceShouldBeFound("aftDenomination.in=" + DEFAULT_AFT_DENOMINATION + "," + UPDATED_AFT_DENOMINATION);

        // Get all the deviceList where aftDenomination equals to UPDATED_AFT_DENOMINATION
        defaultDeviceShouldNotBeFound("aftDenomination.in=" + UPDATED_AFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByAftDenominationIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where aftDenomination is not null
        defaultDeviceShouldBeFound("aftDenomination.specified=true");

        // Get all the deviceList where aftDenomination is null
        defaultDeviceShouldNotBeFound("aftDenomination.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByAftDenominationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where aftDenomination is greater than or equal to DEFAULT_AFT_DENOMINATION
        defaultDeviceShouldBeFound("aftDenomination.greaterThanOrEqual=" + DEFAULT_AFT_DENOMINATION);

        // Get all the deviceList where aftDenomination is greater than or equal to UPDATED_AFT_DENOMINATION
        defaultDeviceShouldNotBeFound("aftDenomination.greaterThanOrEqual=" + UPDATED_AFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByAftDenominationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where aftDenomination is less than or equal to DEFAULT_AFT_DENOMINATION
        defaultDeviceShouldBeFound("aftDenomination.lessThanOrEqual=" + DEFAULT_AFT_DENOMINATION);

        // Get all the deviceList where aftDenomination is less than or equal to SMALLER_AFT_DENOMINATION
        defaultDeviceShouldNotBeFound("aftDenomination.lessThanOrEqual=" + SMALLER_AFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByAftDenominationIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where aftDenomination is less than DEFAULT_AFT_DENOMINATION
        defaultDeviceShouldNotBeFound("aftDenomination.lessThan=" + DEFAULT_AFT_DENOMINATION);

        // Get all the deviceList where aftDenomination is less than UPDATED_AFT_DENOMINATION
        defaultDeviceShouldBeFound("aftDenomination.lessThan=" + UPDATED_AFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByAftDenominationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where aftDenomination is greater than DEFAULT_AFT_DENOMINATION
        defaultDeviceShouldNotBeFound("aftDenomination.greaterThan=" + DEFAULT_AFT_DENOMINATION);

        // Get all the deviceList where aftDenomination is greater than SMALLER_AFT_DENOMINATION
        defaultDeviceShouldBeFound("aftDenomination.greaterThan=" + SMALLER_AFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByLastUpdateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastUpdateDate equals to DEFAULT_LAST_UPDATE_DATE
        defaultDeviceShouldBeFound("lastUpdateDate.equals=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the deviceList where lastUpdateDate equals to UPDATED_LAST_UPDATE_DATE
        defaultDeviceShouldNotBeFound("lastUpdateDate.equals=" + UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastUpdateDateIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastUpdateDate in DEFAULT_LAST_UPDATE_DATE or UPDATED_LAST_UPDATE_DATE
        defaultDeviceShouldBeFound("lastUpdateDate.in=" + DEFAULT_LAST_UPDATE_DATE + "," + UPDATED_LAST_UPDATE_DATE);

        // Get all the deviceList where lastUpdateDate equals to UPDATED_LAST_UPDATE_DATE
        defaultDeviceShouldNotBeFound("lastUpdateDate.in=" + UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastUpdateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastUpdateDate is not null
        defaultDeviceShouldBeFound("lastUpdateDate.specified=true");

        // Get all the deviceList where lastUpdateDate is null
        defaultDeviceShouldNotBeFound("lastUpdateDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByLastUpdateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastUpdateDate is greater than or equal to DEFAULT_LAST_UPDATE_DATE
        defaultDeviceShouldBeFound("lastUpdateDate.greaterThanOrEqual=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the deviceList where lastUpdateDate is greater than or equal to UPDATED_LAST_UPDATE_DATE
        defaultDeviceShouldNotBeFound("lastUpdateDate.greaterThanOrEqual=" + UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastUpdateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastUpdateDate is less than or equal to DEFAULT_LAST_UPDATE_DATE
        defaultDeviceShouldBeFound("lastUpdateDate.lessThanOrEqual=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the deviceList where lastUpdateDate is less than or equal to SMALLER_LAST_UPDATE_DATE
        defaultDeviceShouldNotBeFound("lastUpdateDate.lessThanOrEqual=" + SMALLER_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastUpdateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastUpdateDate is less than DEFAULT_LAST_UPDATE_DATE
        defaultDeviceShouldNotBeFound("lastUpdateDate.lessThan=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the deviceList where lastUpdateDate is less than UPDATED_LAST_UPDATE_DATE
        defaultDeviceShouldBeFound("lastUpdateDate.lessThan=" + UPDATED_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastUpdateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastUpdateDate is greater than DEFAULT_LAST_UPDATE_DATE
        defaultDeviceShouldNotBeFound("lastUpdateDate.greaterThan=" + DEFAULT_LAST_UPDATE_DATE);

        // Get all the deviceList where lastUpdateDate is greater than SMALLER_LAST_UPDATE_DATE
        defaultDeviceShouldBeFound("lastUpdateDate.greaterThan=" + SMALLER_LAST_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByEnableRolloverIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where enableRollover equals to DEFAULT_ENABLE_ROLLOVER
        defaultDeviceShouldBeFound("enableRollover.equals=" + DEFAULT_ENABLE_ROLLOVER);

        // Get all the deviceList where enableRollover equals to UPDATED_ENABLE_ROLLOVER
        defaultDeviceShouldNotBeFound("enableRollover.equals=" + UPDATED_ENABLE_ROLLOVER);
    }

    @Test
    @Transactional
    void getAllDevicesByEnableRolloverIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where enableRollover in DEFAULT_ENABLE_ROLLOVER or UPDATED_ENABLE_ROLLOVER
        defaultDeviceShouldBeFound("enableRollover.in=" + DEFAULT_ENABLE_ROLLOVER + "," + UPDATED_ENABLE_ROLLOVER);

        // Get all the deviceList where enableRollover equals to UPDATED_ENABLE_ROLLOVER
        defaultDeviceShouldNotBeFound("enableRollover.in=" + UPDATED_ENABLE_ROLLOVER);
    }

    @Test
    @Transactional
    void getAllDevicesByEnableRolloverIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where enableRollover is not null
        defaultDeviceShouldBeFound("enableRollover.specified=true");

        // Get all the deviceList where enableRollover is null
        defaultDeviceShouldNotBeFound("enableRollover.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByLastCorruptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastCorruptionDate equals to DEFAULT_LAST_CORRUPTION_DATE
        defaultDeviceShouldBeFound("lastCorruptionDate.equals=" + DEFAULT_LAST_CORRUPTION_DATE);

        // Get all the deviceList where lastCorruptionDate equals to UPDATED_LAST_CORRUPTION_DATE
        defaultDeviceShouldNotBeFound("lastCorruptionDate.equals=" + UPDATED_LAST_CORRUPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastCorruptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastCorruptionDate in DEFAULT_LAST_CORRUPTION_DATE or UPDATED_LAST_CORRUPTION_DATE
        defaultDeviceShouldBeFound("lastCorruptionDate.in=" + DEFAULT_LAST_CORRUPTION_DATE + "," + UPDATED_LAST_CORRUPTION_DATE);

        // Get all the deviceList where lastCorruptionDate equals to UPDATED_LAST_CORRUPTION_DATE
        defaultDeviceShouldNotBeFound("lastCorruptionDate.in=" + UPDATED_LAST_CORRUPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastCorruptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastCorruptionDate is not null
        defaultDeviceShouldBeFound("lastCorruptionDate.specified=true");

        // Get all the deviceList where lastCorruptionDate is null
        defaultDeviceShouldNotBeFound("lastCorruptionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByLastCorruptionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastCorruptionDate is greater than or equal to DEFAULT_LAST_CORRUPTION_DATE
        defaultDeviceShouldBeFound("lastCorruptionDate.greaterThanOrEqual=" + DEFAULT_LAST_CORRUPTION_DATE);

        // Get all the deviceList where lastCorruptionDate is greater than or equal to UPDATED_LAST_CORRUPTION_DATE
        defaultDeviceShouldNotBeFound("lastCorruptionDate.greaterThanOrEqual=" + UPDATED_LAST_CORRUPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastCorruptionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastCorruptionDate is less than or equal to DEFAULT_LAST_CORRUPTION_DATE
        defaultDeviceShouldBeFound("lastCorruptionDate.lessThanOrEqual=" + DEFAULT_LAST_CORRUPTION_DATE);

        // Get all the deviceList where lastCorruptionDate is less than or equal to SMALLER_LAST_CORRUPTION_DATE
        defaultDeviceShouldNotBeFound("lastCorruptionDate.lessThanOrEqual=" + SMALLER_LAST_CORRUPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastCorruptionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastCorruptionDate is less than DEFAULT_LAST_CORRUPTION_DATE
        defaultDeviceShouldNotBeFound("lastCorruptionDate.lessThan=" + DEFAULT_LAST_CORRUPTION_DATE);

        // Get all the deviceList where lastCorruptionDate is less than UPDATED_LAST_CORRUPTION_DATE
        defaultDeviceShouldBeFound("lastCorruptionDate.lessThan=" + UPDATED_LAST_CORRUPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByLastCorruptionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where lastCorruptionDate is greater than DEFAULT_LAST_CORRUPTION_DATE
        defaultDeviceShouldNotBeFound("lastCorruptionDate.greaterThan=" + DEFAULT_LAST_CORRUPTION_DATE);

        // Get all the deviceList where lastCorruptionDate is greater than SMALLER_LAST_CORRUPTION_DATE
        defaultDeviceShouldBeFound("lastCorruptionDate.greaterThan=" + SMALLER_LAST_CORRUPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDevicesByDaftDenominationIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where daftDenomination equals to DEFAULT_DAFT_DENOMINATION
        defaultDeviceShouldBeFound("daftDenomination.equals=" + DEFAULT_DAFT_DENOMINATION);

        // Get all the deviceList where daftDenomination equals to UPDATED_DAFT_DENOMINATION
        defaultDeviceShouldNotBeFound("daftDenomination.equals=" + UPDATED_DAFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDaftDenominationIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where daftDenomination in DEFAULT_DAFT_DENOMINATION or UPDATED_DAFT_DENOMINATION
        defaultDeviceShouldBeFound("daftDenomination.in=" + DEFAULT_DAFT_DENOMINATION + "," + UPDATED_DAFT_DENOMINATION);

        // Get all the deviceList where daftDenomination equals to UPDATED_DAFT_DENOMINATION
        defaultDeviceShouldNotBeFound("daftDenomination.in=" + UPDATED_DAFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDaftDenominationIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where daftDenomination is not null
        defaultDeviceShouldBeFound("daftDenomination.specified=true");

        // Get all the deviceList where daftDenomination is null
        defaultDeviceShouldNotBeFound("daftDenomination.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByDaftDenominationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where daftDenomination is greater than or equal to DEFAULT_DAFT_DENOMINATION
        defaultDeviceShouldBeFound("daftDenomination.greaterThanOrEqual=" + DEFAULT_DAFT_DENOMINATION);

        // Get all the deviceList where daftDenomination is greater than or equal to UPDATED_DAFT_DENOMINATION
        defaultDeviceShouldNotBeFound("daftDenomination.greaterThanOrEqual=" + UPDATED_DAFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDaftDenominationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where daftDenomination is less than or equal to DEFAULT_DAFT_DENOMINATION
        defaultDeviceShouldBeFound("daftDenomination.lessThanOrEqual=" + DEFAULT_DAFT_DENOMINATION);

        // Get all the deviceList where daftDenomination is less than or equal to SMALLER_DAFT_DENOMINATION
        defaultDeviceShouldNotBeFound("daftDenomination.lessThanOrEqual=" + SMALLER_DAFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDaftDenominationIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where daftDenomination is less than DEFAULT_DAFT_DENOMINATION
        defaultDeviceShouldNotBeFound("daftDenomination.lessThan=" + DEFAULT_DAFT_DENOMINATION);

        // Get all the deviceList where daftDenomination is less than UPDATED_DAFT_DENOMINATION
        defaultDeviceShouldBeFound("daftDenomination.lessThan=" + UPDATED_DAFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDaftDenominationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where daftDenomination is greater than DEFAULT_DAFT_DENOMINATION
        defaultDeviceShouldNotBeFound("daftDenomination.greaterThan=" + DEFAULT_DAFT_DENOMINATION);

        // Get all the deviceList where daftDenomination is greater than SMALLER_DAFT_DENOMINATION
        defaultDeviceShouldBeFound("daftDenomination.greaterThan=" + SMALLER_DAFT_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllDevicesByPrizesEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where prizesEnabled equals to DEFAULT_PRIZES_ENABLED
        defaultDeviceShouldBeFound("prizesEnabled.equals=" + DEFAULT_PRIZES_ENABLED);

        // Get all the deviceList where prizesEnabled equals to UPDATED_PRIZES_ENABLED
        defaultDeviceShouldNotBeFound("prizesEnabled.equals=" + UPDATED_PRIZES_ENABLED);
    }

    @Test
    @Transactional
    void getAllDevicesByPrizesEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where prizesEnabled in DEFAULT_PRIZES_ENABLED or UPDATED_PRIZES_ENABLED
        defaultDeviceShouldBeFound("prizesEnabled.in=" + DEFAULT_PRIZES_ENABLED + "," + UPDATED_PRIZES_ENABLED);

        // Get all the deviceList where prizesEnabled equals to UPDATED_PRIZES_ENABLED
        defaultDeviceShouldNotBeFound("prizesEnabled.in=" + UPDATED_PRIZES_ENABLED);
    }

    @Test
    @Transactional
    void getAllDevicesByPrizesEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where prizesEnabled is not null
        defaultDeviceShouldBeFound("prizesEnabled.specified=true");

        // Get all the deviceList where prizesEnabled is null
        defaultDeviceShouldNotBeFound("prizesEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByCurrencyTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currencyTypeId equals to DEFAULT_CURRENCY_TYPE_ID
        defaultDeviceShouldBeFound("currencyTypeId.equals=" + DEFAULT_CURRENCY_TYPE_ID);

        // Get all the deviceList where currencyTypeId equals to UPDATED_CURRENCY_TYPE_ID
        defaultDeviceShouldNotBeFound("currencyTypeId.equals=" + UPDATED_CURRENCY_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrencyTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currencyTypeId in DEFAULT_CURRENCY_TYPE_ID or UPDATED_CURRENCY_TYPE_ID
        defaultDeviceShouldBeFound("currencyTypeId.in=" + DEFAULT_CURRENCY_TYPE_ID + "," + UPDATED_CURRENCY_TYPE_ID);

        // Get all the deviceList where currencyTypeId equals to UPDATED_CURRENCY_TYPE_ID
        defaultDeviceShouldNotBeFound("currencyTypeId.in=" + UPDATED_CURRENCY_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrencyTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currencyTypeId is not null
        defaultDeviceShouldBeFound("currencyTypeId.specified=true");

        // Get all the deviceList where currencyTypeId is null
        defaultDeviceShouldNotBeFound("currencyTypeId.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByCurrencyTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currencyTypeId is greater than or equal to DEFAULT_CURRENCY_TYPE_ID
        defaultDeviceShouldBeFound("currencyTypeId.greaterThanOrEqual=" + DEFAULT_CURRENCY_TYPE_ID);

        // Get all the deviceList where currencyTypeId is greater than or equal to UPDATED_CURRENCY_TYPE_ID
        defaultDeviceShouldNotBeFound("currencyTypeId.greaterThanOrEqual=" + UPDATED_CURRENCY_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrencyTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currencyTypeId is less than or equal to DEFAULT_CURRENCY_TYPE_ID
        defaultDeviceShouldBeFound("currencyTypeId.lessThanOrEqual=" + DEFAULT_CURRENCY_TYPE_ID);

        // Get all the deviceList where currencyTypeId is less than or equal to SMALLER_CURRENCY_TYPE_ID
        defaultDeviceShouldNotBeFound("currencyTypeId.lessThanOrEqual=" + SMALLER_CURRENCY_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrencyTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currencyTypeId is less than DEFAULT_CURRENCY_TYPE_ID
        defaultDeviceShouldNotBeFound("currencyTypeId.lessThan=" + DEFAULT_CURRENCY_TYPE_ID);

        // Get all the deviceList where currencyTypeId is less than UPDATED_CURRENCY_TYPE_ID
        defaultDeviceShouldBeFound("currencyTypeId.lessThan=" + UPDATED_CURRENCY_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByCurrencyTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where currencyTypeId is greater than DEFAULT_CURRENCY_TYPE_ID
        defaultDeviceShouldNotBeFound("currencyTypeId.greaterThan=" + DEFAULT_CURRENCY_TYPE_ID);

        // Get all the deviceList where currencyTypeId is greater than SMALLER_CURRENCY_TYPE_ID
        defaultDeviceShouldBeFound("currencyTypeId.greaterThan=" + SMALLER_CURRENCY_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByIsleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isleId equals to DEFAULT_ISLE_ID
        defaultDeviceShouldBeFound("isleId.equals=" + DEFAULT_ISLE_ID);

        // Get all the deviceList where isleId equals to UPDATED_ISLE_ID
        defaultDeviceShouldNotBeFound("isleId.equals=" + UPDATED_ISLE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByIsleIdIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isleId in DEFAULT_ISLE_ID or UPDATED_ISLE_ID
        defaultDeviceShouldBeFound("isleId.in=" + DEFAULT_ISLE_ID + "," + UPDATED_ISLE_ID);

        // Get all the deviceList where isleId equals to UPDATED_ISLE_ID
        defaultDeviceShouldNotBeFound("isleId.in=" + UPDATED_ISLE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByIsleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isleId is not null
        defaultDeviceShouldBeFound("isleId.specified=true");

        // Get all the deviceList where isleId is null
        defaultDeviceShouldNotBeFound("isleId.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByIsleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isleId is greater than or equal to DEFAULT_ISLE_ID
        defaultDeviceShouldBeFound("isleId.greaterThanOrEqual=" + DEFAULT_ISLE_ID);

        // Get all the deviceList where isleId is greater than or equal to UPDATED_ISLE_ID
        defaultDeviceShouldNotBeFound("isleId.greaterThanOrEqual=" + UPDATED_ISLE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByIsleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isleId is less than or equal to DEFAULT_ISLE_ID
        defaultDeviceShouldBeFound("isleId.lessThanOrEqual=" + DEFAULT_ISLE_ID);

        // Get all the deviceList where isleId is less than or equal to SMALLER_ISLE_ID
        defaultDeviceShouldNotBeFound("isleId.lessThanOrEqual=" + SMALLER_ISLE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByIsleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isleId is less than DEFAULT_ISLE_ID
        defaultDeviceShouldNotBeFound("isleId.lessThan=" + DEFAULT_ISLE_ID);

        // Get all the deviceList where isleId is less than UPDATED_ISLE_ID
        defaultDeviceShouldBeFound("isleId.lessThan=" + UPDATED_ISLE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByIsleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where isleId is greater than DEFAULT_ISLE_ID
        defaultDeviceShouldNotBeFound("isleId.greaterThan=" + DEFAULT_ISLE_ID);

        // Get all the deviceList where isleId is greater than SMALLER_ISLE_ID
        defaultDeviceShouldBeFound("isleId.greaterThan=" + SMALLER_ISLE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByModelIsEqualToSomething() throws Exception {
        Model model;
        if (TestUtil.findAll(em, Model.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            model = ModelResourceIT.createEntity(em);
        } else {
            model = TestUtil.findAll(em, Model.class).get(0);
        }
        em.persist(model);
        em.flush();
        device.setModel(model);
        deviceRepository.saveAndFlush(device);
        Long modelId = model.getId();

        // Get all the deviceList where model equals to modelId
        defaultDeviceShouldBeFound("modelId.equals=" + modelId);

        // Get all the deviceList where model equals to (modelId + 1)
        defaultDeviceShouldNotBeFound("modelId.equals=" + (modelId + 1));
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceCategoryIsEqualToSomething() throws Exception {
        DeviceCategory deviceCategory;
        if (TestUtil.findAll(em, DeviceCategory.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            deviceCategory = DeviceCategoryResourceIT.createEntity(em);
        } else {
            deviceCategory = TestUtil.findAll(em, DeviceCategory.class).get(0);
        }
        em.persist(deviceCategory);
        em.flush();
        device.setDeviceCategory(deviceCategory);
        deviceRepository.saveAndFlush(device);
        Long deviceCategoryId = deviceCategory.getId();

        // Get all the deviceList where deviceCategory equals to deviceCategoryId
        defaultDeviceShouldBeFound("deviceCategoryId.equals=" + deviceCategoryId);

        // Get all the deviceList where deviceCategory equals to (deviceCategoryId + 1)
        defaultDeviceShouldNotBeFound("deviceCategoryId.equals=" + (deviceCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceTypeIsEqualToSomething() throws Exception {
        DeviceType deviceType;
        if (TestUtil.findAll(em, DeviceType.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            deviceType = DeviceTypeResourceIT.createEntity(em);
        } else {
            deviceType = TestUtil.findAll(em, DeviceType.class).get(0);
        }
        em.persist(deviceType);
        em.flush();
        device.setDeviceType(deviceType);
        deviceRepository.saveAndFlush(device);
        Long deviceTypeId = deviceType.getId();

        // Get all the deviceList where deviceType equals to deviceTypeId
        defaultDeviceShouldBeFound("deviceTypeId.equals=" + deviceTypeId);

        // Get all the deviceList where deviceType equals to (deviceTypeId + 1)
        defaultDeviceShouldNotBeFound("deviceTypeId.equals=" + (deviceTypeId + 1));
    }

    @Test
    @Transactional
    void getAllDevicesByFormulaHandpayIsEqualToSomething() throws Exception {
        Formula formulaHandpay;
        if (TestUtil.findAll(em, Formula.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            formulaHandpay = FormulaResourceIT.createEntity(em);
        } else {
            formulaHandpay = TestUtil.findAll(em, Formula.class).get(0);
        }
        em.persist(formulaHandpay);
        em.flush();
        device.setFormulaHandpay(formulaHandpay);
        deviceRepository.saveAndFlush(device);
        Long formulaHandpayId = formulaHandpay.getId();

        // Get all the deviceList where formulaHandpay equals to formulaHandpayId
        defaultDeviceShouldBeFound("formulaHandpayId.equals=" + formulaHandpayId);

        // Get all the deviceList where formulaHandpay equals to (formulaHandpayId + 1)
        defaultDeviceShouldNotBeFound("formulaHandpayId.equals=" + (formulaHandpayId + 1));
    }

    @Test
    @Transactional
    void getAllDevicesByFormulaJackpotIsEqualToSomething() throws Exception {
        Formula formulaJackpot;
        if (TestUtil.findAll(em, Formula.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            formulaJackpot = FormulaResourceIT.createEntity(em);
        } else {
            formulaJackpot = TestUtil.findAll(em, Formula.class).get(0);
        }
        em.persist(formulaJackpot);
        em.flush();
        device.setFormulaJackpot(formulaJackpot);
        deviceRepository.saveAndFlush(device);
        Long formulaJackpotId = formulaJackpot.getId();

        // Get all the deviceList where formulaJackpot equals to formulaJackpotId
        defaultDeviceShouldBeFound("formulaJackpotId.equals=" + formulaJackpotId);

        // Get all the deviceList where formulaJackpot equals to (formulaJackpotId + 1)
        defaultDeviceShouldNotBeFound("formulaJackpotId.equals=" + (formulaJackpotId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceShouldBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().toString())))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)))
            .andExpect(jsonPath("$.[*].isProtocolEsdcs").value(hasItem(DEFAULT_IS_PROTOCOL_ESDCS.booleanValue())))
            .andExpect(jsonPath("$.[*].numberPlayedReport").value(hasItem(DEFAULT_NUMBER_PLAYED_REPORT)))
            .andExpect(jsonPath("$.[*].sasDenomination").value(hasItem(sameNumber(DEFAULT_SAS_DENOMINATION))))
            .andExpect(jsonPath("$.[*].isMultigame").value(hasItem(DEFAULT_IS_MULTIGAME.booleanValue())))
            .andExpect(jsonPath("$.[*].isMultiDenomination").value(hasItem(DEFAULT_IS_MULTI_DENOMINATION.booleanValue())))
            .andExpect(jsonPath("$.[*].isRetanqueo").value(hasItem(DEFAULT_IS_RETANQUEO.booleanValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].theoreticalHold").value(hasItem(sameNumber(DEFAULT_THEORETICAL_HOLD))))
            .andExpect(jsonPath("$.[*].sasIdentifier").value(hasItem(DEFAULT_SAS_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(DEFAULT_CREDIT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].hasHooper").value(hasItem(DEFAULT_HAS_HOOPER.booleanValue())))
            .andExpect(jsonPath("$.[*].coljuegosCode").value(hasItem(DEFAULT_COLJUEGOS_CODE)))
            .andExpect(jsonPath("$.[*].fabricationDate").value(hasItem(DEFAULT_FABRICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentToken").value(hasItem(sameNumber(DEFAULT_CURRENT_TOKEN))))
            .andExpect(jsonPath("$.[*].denominationTito").value(hasItem(sameNumber(DEFAULT_DENOMINATION_TITO))))
            .andExpect(jsonPath("$.[*].endLostCommunication").value(hasItem(sameInstant(DEFAULT_END_LOST_COMMUNICATION))))
            .andExpect(jsonPath("$.[*].startLostCommunication").value(hasItem(sameInstant(DEFAULT_START_LOST_COMMUNICATION))))
            .andExpect(jsonPath("$.[*].reportMultiplier").value(hasItem(sameNumber(DEFAULT_REPORT_MULTIPLIER))))
            .andExpect(jsonPath("$.[*].nuid").value(hasItem(DEFAULT_NUID)))
            .andExpect(jsonPath("$.[*].payManualPrize").value(hasItem(DEFAULT_PAY_MANUAL_PRIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].manualHandpay").value(hasItem(DEFAULT_MANUAL_HANDPAY.booleanValue())))
            .andExpect(jsonPath("$.[*].manualJackpot").value(hasItem(DEFAULT_MANUAL_JACKPOT.booleanValue())))
            .andExpect(jsonPath("$.[*].manualGameEvent").value(hasItem(DEFAULT_MANUAL_GAME_EVENT.booleanValue())))
            .andExpect(jsonPath("$.[*].betCode").value(hasItem(DEFAULT_BET_CODE)))
            .andExpect(jsonPath("$.[*].homologationIndicator").value(hasItem(DEFAULT_HOMOLOGATION_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].coljuegosModel").value(hasItem(DEFAULT_COLJUEGOS_MODEL)))
            .andExpect(jsonPath("$.[*].reportable").value(hasItem(DEFAULT_REPORTABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].aftDenomination").value(hasItem(sameNumber(DEFAULT_AFT_DENOMINATION))))
            .andExpect(jsonPath("$.[*].lastUpdateDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE_DATE))))
            .andExpect(jsonPath("$.[*].enableRollover").value(hasItem(DEFAULT_ENABLE_ROLLOVER.booleanValue())))
            .andExpect(jsonPath("$.[*].lastCorruptionDate").value(hasItem(sameInstant(DEFAULT_LAST_CORRUPTION_DATE))))
            .andExpect(jsonPath("$.[*].daftDenomination").value(hasItem(sameNumber(DEFAULT_DAFT_DENOMINATION))))
            .andExpect(jsonPath("$.[*].prizesEnabled").value(hasItem(DEFAULT_PRIZES_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].currencyTypeId").value(hasItem(DEFAULT_CURRENCY_TYPE_ID)))
            .andExpect(jsonPath("$.[*].isleId").value(hasItem(DEFAULT_ISLE_ID)));

        // Check, that the count call also returns 1
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceShouldNotBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice
            .serial(UPDATED_SERIAL)
            .isProtocolEsdcs(UPDATED_IS_PROTOCOL_ESDCS)
            .numberPlayedReport(UPDATED_NUMBER_PLAYED_REPORT)
            .sasDenomination(UPDATED_SAS_DENOMINATION)
            .isMultigame(UPDATED_IS_MULTIGAME)
            .isMultiDenomination(UPDATED_IS_MULTI_DENOMINATION)
            .isRetanqueo(UPDATED_IS_RETANQUEO)
            .state(UPDATED_STATE)
            .theoreticalHold(UPDATED_THEORETICAL_HOLD)
            .sasIdentifier(UPDATED_SAS_IDENTIFIER)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .hasHooper(UPDATED_HAS_HOOPER)
            .coljuegosCode(UPDATED_COLJUEGOS_CODE)
            .fabricationDate(UPDATED_FABRICATION_DATE)
            .currentToken(UPDATED_CURRENT_TOKEN)
            .denominationTito(UPDATED_DENOMINATION_TITO)
            .endLostCommunication(UPDATED_END_LOST_COMMUNICATION)
            .startLostCommunication(UPDATED_START_LOST_COMMUNICATION)
            .reportMultiplier(UPDATED_REPORT_MULTIPLIER)
            .nuid(UPDATED_NUID)
            .payManualPrize(UPDATED_PAY_MANUAL_PRIZE)
            .manualHandpay(UPDATED_MANUAL_HANDPAY)
            .manualJackpot(UPDATED_MANUAL_JACKPOT)
            .manualGameEvent(UPDATED_MANUAL_GAME_EVENT)
            .betCode(UPDATED_BET_CODE)
            .homologationIndicator(UPDATED_HOMOLOGATION_INDICATOR)
            .coljuegosModel(UPDATED_COLJUEGOS_MODEL)
            .reportable(UPDATED_REPORTABLE)
            .aftDenomination(UPDATED_AFT_DENOMINATION)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE)
            .enableRollover(UPDATED_ENABLE_ROLLOVER)
            .lastCorruptionDate(UPDATED_LAST_CORRUPTION_DATE)
            .daftDenomination(UPDATED_DAFT_DENOMINATION)
            .prizesEnabled(UPDATED_PRIZES_ENABLED)
            .currencyTypeId(UPDATED_CURRENCY_TYPE_ID)
            .isleId(UPDATED_ISLE_ID);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testDevice.getIsProtocolEsdcs()).isEqualTo(UPDATED_IS_PROTOCOL_ESDCS);
        assertThat(testDevice.getNumberPlayedReport()).isEqualTo(UPDATED_NUMBER_PLAYED_REPORT);
        assertThat(testDevice.getSasDenomination()).isEqualByComparingTo(UPDATED_SAS_DENOMINATION);
        assertThat(testDevice.getIsMultigame()).isEqualTo(UPDATED_IS_MULTIGAME);
        assertThat(testDevice.getIsMultiDenomination()).isEqualTo(UPDATED_IS_MULTI_DENOMINATION);
        assertThat(testDevice.getIsRetanqueo()).isEqualTo(UPDATED_IS_RETANQUEO);
        assertThat(testDevice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDevice.getTheoreticalHold()).isEqualByComparingTo(UPDATED_THEORETICAL_HOLD);
        assertThat(testDevice.getSasIdentifier()).isEqualTo(UPDATED_SAS_IDENTIFIER);
        assertThat(testDevice.getCreditLimit()).isEqualTo(UPDATED_CREDIT_LIMIT);
        assertThat(testDevice.getHasHooper()).isEqualTo(UPDATED_HAS_HOOPER);
        assertThat(testDevice.getColjuegosCode()).isEqualTo(UPDATED_COLJUEGOS_CODE);
        assertThat(testDevice.getFabricationDate()).isEqualTo(UPDATED_FABRICATION_DATE);
        assertThat(testDevice.getCurrentToken()).isEqualByComparingTo(UPDATED_CURRENT_TOKEN);
        assertThat(testDevice.getDenominationTito()).isEqualByComparingTo(UPDATED_DENOMINATION_TITO);
        assertThat(testDevice.getEndLostCommunication()).isEqualTo(UPDATED_END_LOST_COMMUNICATION);
        assertThat(testDevice.getStartLostCommunication()).isEqualTo(UPDATED_START_LOST_COMMUNICATION);
        assertThat(testDevice.getReportMultiplier()).isEqualByComparingTo(UPDATED_REPORT_MULTIPLIER);
        assertThat(testDevice.getNuid()).isEqualTo(UPDATED_NUID);
        assertThat(testDevice.getPayManualPrize()).isEqualTo(UPDATED_PAY_MANUAL_PRIZE);
        assertThat(testDevice.getManualHandpay()).isEqualTo(UPDATED_MANUAL_HANDPAY);
        assertThat(testDevice.getManualJackpot()).isEqualTo(UPDATED_MANUAL_JACKPOT);
        assertThat(testDevice.getManualGameEvent()).isEqualTo(UPDATED_MANUAL_GAME_EVENT);
        assertThat(testDevice.getBetCode()).isEqualTo(UPDATED_BET_CODE);
        assertThat(testDevice.getHomologationIndicator()).isEqualTo(UPDATED_HOMOLOGATION_INDICATOR);
        assertThat(testDevice.getColjuegosModel()).isEqualTo(UPDATED_COLJUEGOS_MODEL);
        assertThat(testDevice.getReportable()).isEqualTo(UPDATED_REPORTABLE);
        assertThat(testDevice.getAftDenomination()).isEqualByComparingTo(UPDATED_AFT_DENOMINATION);
        assertThat(testDevice.getLastUpdateDate()).isEqualTo(UPDATED_LAST_UPDATE_DATE);
        assertThat(testDevice.getEnableRollover()).isEqualTo(UPDATED_ENABLE_ROLLOVER);
        assertThat(testDevice.getLastCorruptionDate()).isEqualTo(UPDATED_LAST_CORRUPTION_DATE);
        assertThat(testDevice.getDaftDenomination()).isEqualByComparingTo(UPDATED_DAFT_DENOMINATION);
        assertThat(testDevice.getPrizesEnabled()).isEqualTo(UPDATED_PRIZES_ENABLED);
        assertThat(testDevice.getCurrencyTypeId()).isEqualTo(UPDATED_CURRENCY_TYPE_ID);
        assertThat(testDevice.getIsleId()).isEqualTo(UPDATED_ISLE_ID);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(UUID.randomUUID());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(UUID.randomUUID());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(UUID.randomUUID());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .sasDenomination(UPDATED_SAS_DENOMINATION)
            .isMultigame(UPDATED_IS_MULTIGAME)
            .isMultiDenomination(UPDATED_IS_MULTI_DENOMINATION)
            .isRetanqueo(UPDATED_IS_RETANQUEO)
            .state(UPDATED_STATE)
            .theoreticalHold(UPDATED_THEORETICAL_HOLD)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .coljuegosCode(UPDATED_COLJUEGOS_CODE)
            .fabricationDate(UPDATED_FABRICATION_DATE)
            .currentToken(UPDATED_CURRENT_TOKEN)
            .denominationTito(UPDATED_DENOMINATION_TITO)
            .payManualPrize(UPDATED_PAY_MANUAL_PRIZE)
            .manualHandpay(UPDATED_MANUAL_HANDPAY)
            .manualJackpot(UPDATED_MANUAL_JACKPOT)
            .manualGameEvent(UPDATED_MANUAL_GAME_EVENT)
            .homologationIndicator(UPDATED_HOMOLOGATION_INDICATOR)
            .currencyTypeId(UPDATED_CURRENCY_TYPE_ID);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerial()).isEqualTo(DEFAULT_SERIAL);
        assertThat(testDevice.getIsProtocolEsdcs()).isEqualTo(DEFAULT_IS_PROTOCOL_ESDCS);
        assertThat(testDevice.getNumberPlayedReport()).isEqualTo(DEFAULT_NUMBER_PLAYED_REPORT);
        assertThat(testDevice.getSasDenomination()).isEqualByComparingTo(UPDATED_SAS_DENOMINATION);
        assertThat(testDevice.getIsMultigame()).isEqualTo(UPDATED_IS_MULTIGAME);
        assertThat(testDevice.getIsMultiDenomination()).isEqualTo(UPDATED_IS_MULTI_DENOMINATION);
        assertThat(testDevice.getIsRetanqueo()).isEqualTo(UPDATED_IS_RETANQUEO);
        assertThat(testDevice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDevice.getTheoreticalHold()).isEqualByComparingTo(UPDATED_THEORETICAL_HOLD);
        assertThat(testDevice.getSasIdentifier()).isEqualTo(DEFAULT_SAS_IDENTIFIER);
        assertThat(testDevice.getCreditLimit()).isEqualTo(UPDATED_CREDIT_LIMIT);
        assertThat(testDevice.getHasHooper()).isEqualTo(DEFAULT_HAS_HOOPER);
        assertThat(testDevice.getColjuegosCode()).isEqualTo(UPDATED_COLJUEGOS_CODE);
        assertThat(testDevice.getFabricationDate()).isEqualTo(UPDATED_FABRICATION_DATE);
        assertThat(testDevice.getCurrentToken()).isEqualByComparingTo(UPDATED_CURRENT_TOKEN);
        assertThat(testDevice.getDenominationTito()).isEqualByComparingTo(UPDATED_DENOMINATION_TITO);
        assertThat(testDevice.getEndLostCommunication()).isEqualTo(DEFAULT_END_LOST_COMMUNICATION);
        assertThat(testDevice.getStartLostCommunication()).isEqualTo(DEFAULT_START_LOST_COMMUNICATION);
        assertThat(testDevice.getReportMultiplier()).isEqualByComparingTo(DEFAULT_REPORT_MULTIPLIER);
        assertThat(testDevice.getNuid()).isEqualTo(DEFAULT_NUID);
        assertThat(testDevice.getPayManualPrize()).isEqualTo(UPDATED_PAY_MANUAL_PRIZE);
        assertThat(testDevice.getManualHandpay()).isEqualTo(UPDATED_MANUAL_HANDPAY);
        assertThat(testDevice.getManualJackpot()).isEqualTo(UPDATED_MANUAL_JACKPOT);
        assertThat(testDevice.getManualGameEvent()).isEqualTo(UPDATED_MANUAL_GAME_EVENT);
        assertThat(testDevice.getBetCode()).isEqualTo(DEFAULT_BET_CODE);
        assertThat(testDevice.getHomologationIndicator()).isEqualTo(UPDATED_HOMOLOGATION_INDICATOR);
        assertThat(testDevice.getColjuegosModel()).isEqualTo(DEFAULT_COLJUEGOS_MODEL);
        assertThat(testDevice.getReportable()).isEqualTo(DEFAULT_REPORTABLE);
        assertThat(testDevice.getAftDenomination()).isEqualByComparingTo(DEFAULT_AFT_DENOMINATION);
        assertThat(testDevice.getLastUpdateDate()).isEqualTo(DEFAULT_LAST_UPDATE_DATE);
        assertThat(testDevice.getEnableRollover()).isEqualTo(DEFAULT_ENABLE_ROLLOVER);
        assertThat(testDevice.getLastCorruptionDate()).isEqualTo(DEFAULT_LAST_CORRUPTION_DATE);
        assertThat(testDevice.getDaftDenomination()).isEqualByComparingTo(DEFAULT_DAFT_DENOMINATION);
        assertThat(testDevice.getPrizesEnabled()).isEqualTo(DEFAULT_PRIZES_ENABLED);
        assertThat(testDevice.getCurrencyTypeId()).isEqualTo(UPDATED_CURRENCY_TYPE_ID);
        assertThat(testDevice.getIsleId()).isEqualTo(DEFAULT_ISLE_ID);
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .serial(UPDATED_SERIAL)
            .isProtocolEsdcs(UPDATED_IS_PROTOCOL_ESDCS)
            .numberPlayedReport(UPDATED_NUMBER_PLAYED_REPORT)
            .sasDenomination(UPDATED_SAS_DENOMINATION)
            .isMultigame(UPDATED_IS_MULTIGAME)
            .isMultiDenomination(UPDATED_IS_MULTI_DENOMINATION)
            .isRetanqueo(UPDATED_IS_RETANQUEO)
            .state(UPDATED_STATE)
            .theoreticalHold(UPDATED_THEORETICAL_HOLD)
            .sasIdentifier(UPDATED_SAS_IDENTIFIER)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .hasHooper(UPDATED_HAS_HOOPER)
            .coljuegosCode(UPDATED_COLJUEGOS_CODE)
            .fabricationDate(UPDATED_FABRICATION_DATE)
            .currentToken(UPDATED_CURRENT_TOKEN)
            .denominationTito(UPDATED_DENOMINATION_TITO)
            .endLostCommunication(UPDATED_END_LOST_COMMUNICATION)
            .startLostCommunication(UPDATED_START_LOST_COMMUNICATION)
            .reportMultiplier(UPDATED_REPORT_MULTIPLIER)
            .nuid(UPDATED_NUID)
            .payManualPrize(UPDATED_PAY_MANUAL_PRIZE)
            .manualHandpay(UPDATED_MANUAL_HANDPAY)
            .manualJackpot(UPDATED_MANUAL_JACKPOT)
            .manualGameEvent(UPDATED_MANUAL_GAME_EVENT)
            .betCode(UPDATED_BET_CODE)
            .homologationIndicator(UPDATED_HOMOLOGATION_INDICATOR)
            .coljuegosModel(UPDATED_COLJUEGOS_MODEL)
            .reportable(UPDATED_REPORTABLE)
            .aftDenomination(UPDATED_AFT_DENOMINATION)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE)
            .enableRollover(UPDATED_ENABLE_ROLLOVER)
            .lastCorruptionDate(UPDATED_LAST_CORRUPTION_DATE)
            .daftDenomination(UPDATED_DAFT_DENOMINATION)
            .prizesEnabled(UPDATED_PRIZES_ENABLED)
            .currencyTypeId(UPDATED_CURRENCY_TYPE_ID)
            .isleId(UPDATED_ISLE_ID);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testDevice.getIsProtocolEsdcs()).isEqualTo(UPDATED_IS_PROTOCOL_ESDCS);
        assertThat(testDevice.getNumberPlayedReport()).isEqualTo(UPDATED_NUMBER_PLAYED_REPORT);
        assertThat(testDevice.getSasDenomination()).isEqualByComparingTo(UPDATED_SAS_DENOMINATION);
        assertThat(testDevice.getIsMultigame()).isEqualTo(UPDATED_IS_MULTIGAME);
        assertThat(testDevice.getIsMultiDenomination()).isEqualTo(UPDATED_IS_MULTI_DENOMINATION);
        assertThat(testDevice.getIsRetanqueo()).isEqualTo(UPDATED_IS_RETANQUEO);
        assertThat(testDevice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDevice.getTheoreticalHold()).isEqualByComparingTo(UPDATED_THEORETICAL_HOLD);
        assertThat(testDevice.getSasIdentifier()).isEqualTo(UPDATED_SAS_IDENTIFIER);
        assertThat(testDevice.getCreditLimit()).isEqualTo(UPDATED_CREDIT_LIMIT);
        assertThat(testDevice.getHasHooper()).isEqualTo(UPDATED_HAS_HOOPER);
        assertThat(testDevice.getColjuegosCode()).isEqualTo(UPDATED_COLJUEGOS_CODE);
        assertThat(testDevice.getFabricationDate()).isEqualTo(UPDATED_FABRICATION_DATE);
        assertThat(testDevice.getCurrentToken()).isEqualByComparingTo(UPDATED_CURRENT_TOKEN);
        assertThat(testDevice.getDenominationTito()).isEqualByComparingTo(UPDATED_DENOMINATION_TITO);
        assertThat(testDevice.getEndLostCommunication()).isEqualTo(UPDATED_END_LOST_COMMUNICATION);
        assertThat(testDevice.getStartLostCommunication()).isEqualTo(UPDATED_START_LOST_COMMUNICATION);
        assertThat(testDevice.getReportMultiplier()).isEqualByComparingTo(UPDATED_REPORT_MULTIPLIER);
        assertThat(testDevice.getNuid()).isEqualTo(UPDATED_NUID);
        assertThat(testDevice.getPayManualPrize()).isEqualTo(UPDATED_PAY_MANUAL_PRIZE);
        assertThat(testDevice.getManualHandpay()).isEqualTo(UPDATED_MANUAL_HANDPAY);
        assertThat(testDevice.getManualJackpot()).isEqualTo(UPDATED_MANUAL_JACKPOT);
        assertThat(testDevice.getManualGameEvent()).isEqualTo(UPDATED_MANUAL_GAME_EVENT);
        assertThat(testDevice.getBetCode()).isEqualTo(UPDATED_BET_CODE);
        assertThat(testDevice.getHomologationIndicator()).isEqualTo(UPDATED_HOMOLOGATION_INDICATOR);
        assertThat(testDevice.getColjuegosModel()).isEqualTo(UPDATED_COLJUEGOS_MODEL);
        assertThat(testDevice.getReportable()).isEqualTo(UPDATED_REPORTABLE);
        assertThat(testDevice.getAftDenomination()).isEqualByComparingTo(UPDATED_AFT_DENOMINATION);
        assertThat(testDevice.getLastUpdateDate()).isEqualTo(UPDATED_LAST_UPDATE_DATE);
        assertThat(testDevice.getEnableRollover()).isEqualTo(UPDATED_ENABLE_ROLLOVER);
        assertThat(testDevice.getLastCorruptionDate()).isEqualTo(UPDATED_LAST_CORRUPTION_DATE);
        assertThat(testDevice.getDaftDenomination()).isEqualByComparingTo(UPDATED_DAFT_DENOMINATION);
        assertThat(testDevice.getPrizesEnabled()).isEqualTo(UPDATED_PRIZES_ENABLED);
        assertThat(testDevice.getCurrencyTypeId()).isEqualTo(UPDATED_CURRENCY_TYPE_ID);
        assertThat(testDevice.getIsleId()).isEqualTo(UPDATED_ISLE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(UUID.randomUUID());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(UUID.randomUUID());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(UUID.randomUUID());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
