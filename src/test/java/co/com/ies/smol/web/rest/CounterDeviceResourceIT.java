package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.CounterDevice;
import co.com.ies.smol.domain.Device;
import co.com.ies.smol.repository.CounterDeviceRepository;
import co.com.ies.smol.service.criteria.CounterDeviceCriteria;
import co.com.ies.smol.service.dto.CounterDeviceDTO;
import co.com.ies.smol.service.mapper.CounterDeviceMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CounterDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CounterDeviceResourceIT {

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ROLLOVER_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ROLLOVER_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ROLLOVER_VALUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CREDIT_SALE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_SALE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CREDIT_SALE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_MANUAL_COUNTER = false;
    private static final Boolean UPDATED_MANUAL_COUNTER = true;

    private static final BigDecimal DEFAULT_MANUAL_MULTIPLIER = new BigDecimal(1);
    private static final BigDecimal UPDATED_MANUAL_MULTIPLIER = new BigDecimal(2);
    private static final BigDecimal SMALLER_MANUAL_MULTIPLIER = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_DECIMALS_MANUAL_COUNTER = false;
    private static final Boolean UPDATED_DECIMALS_MANUAL_COUNTER = true;

    private static final String ENTITY_API_URL = "/api/counter-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CounterDeviceRepository counterDeviceRepository;

    @Autowired
    private CounterDeviceMapper counterDeviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCounterDeviceMockMvc;

    private CounterDevice counterDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterDevice createEntity(EntityManager em) {
        CounterDevice counterDevice = new CounterDevice()
            .value(DEFAULT_VALUE)
            .rolloverValue(DEFAULT_ROLLOVER_VALUE)
            .creditSale(DEFAULT_CREDIT_SALE)
            .manualCounter(DEFAULT_MANUAL_COUNTER)
            .manualMultiplier(DEFAULT_MANUAL_MULTIPLIER)
            .decimalsManualCounter(DEFAULT_DECIMALS_MANUAL_COUNTER);
        // Add required entity
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            device = DeviceResourceIT.createEntity(em);
            em.persist(device);
            em.flush();
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        counterDevice.setDevice(device);
        return counterDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterDevice createUpdatedEntity(EntityManager em) {
        CounterDevice counterDevice = new CounterDevice()
            .value(UPDATED_VALUE)
            .rolloverValue(UPDATED_ROLLOVER_VALUE)
            .creditSale(UPDATED_CREDIT_SALE)
            .manualCounter(UPDATED_MANUAL_COUNTER)
            .manualMultiplier(UPDATED_MANUAL_MULTIPLIER)
            .decimalsManualCounter(UPDATED_DECIMALS_MANUAL_COUNTER);
        // Add required entity
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            device = DeviceResourceIT.createUpdatedEntity(em);
            em.persist(device);
            em.flush();
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        counterDevice.setDevice(device);
        return counterDevice;
    }

    @BeforeEach
    public void initTest() {
        counterDevice = createEntity(em);
    }

    @Test
    @Transactional
    void createCounterDevice() throws Exception {
        int databaseSizeBeforeCreate = counterDeviceRepository.findAll().size();
        // Create the CounterDevice
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);
        restCounterDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        CounterDevice testCounterDevice = counterDeviceList.get(counterDeviceList.size() - 1);
        assertThat(testCounterDevice.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testCounterDevice.getRolloverValue()).isEqualByComparingTo(DEFAULT_ROLLOVER_VALUE);
        assertThat(testCounterDevice.getCreditSale()).isEqualByComparingTo(DEFAULT_CREDIT_SALE);
        assertThat(testCounterDevice.getManualCounter()).isEqualTo(DEFAULT_MANUAL_COUNTER);
        assertThat(testCounterDevice.getManualMultiplier()).isEqualByComparingTo(DEFAULT_MANUAL_MULTIPLIER);
        assertThat(testCounterDevice.getDecimalsManualCounter()).isEqualTo(DEFAULT_DECIMALS_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void createCounterDeviceWithExistingId() throws Exception {
        // Create the CounterDevice with an existing ID
        counterDeviceRepository.saveAndFlush(counterDevice);
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);

        int databaseSizeBeforeCreate = counterDeviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounterDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCounterDevices() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList
        restCounterDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterDevice.getId().toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].rolloverValue").value(hasItem(sameNumber(DEFAULT_ROLLOVER_VALUE))))
            .andExpect(jsonPath("$.[*].creditSale").value(hasItem(sameNumber(DEFAULT_CREDIT_SALE))))
            .andExpect(jsonPath("$.[*].manualCounter").value(hasItem(DEFAULT_MANUAL_COUNTER.booleanValue())))
            .andExpect(jsonPath("$.[*].manualMultiplier").value(hasItem(sameNumber(DEFAULT_MANUAL_MULTIPLIER))))
            .andExpect(jsonPath("$.[*].decimalsManualCounter").value(hasItem(DEFAULT_DECIMALS_MANUAL_COUNTER.booleanValue())));
    }

    @Test
    @Transactional
    void getCounterDevice() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get the counterDevice
        restCounterDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, counterDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(counterDevice.getId().toString()))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.rolloverValue").value(sameNumber(DEFAULT_ROLLOVER_VALUE)))
            .andExpect(jsonPath("$.creditSale").value(sameNumber(DEFAULT_CREDIT_SALE)))
            .andExpect(jsonPath("$.manualCounter").value(DEFAULT_MANUAL_COUNTER.booleanValue()))
            .andExpect(jsonPath("$.manualMultiplier").value(sameNumber(DEFAULT_MANUAL_MULTIPLIER)))
            .andExpect(jsonPath("$.decimalsManualCounter").value(DEFAULT_DECIMALS_MANUAL_COUNTER.booleanValue()));
    }

    @Test
    @Transactional
    void getCounterDevicesByIdFiltering() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        UUID id = counterDevice.getId();

        defaultCounterDeviceShouldBeFound("id.equals=" + id);
        defaultCounterDeviceShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where value equals to DEFAULT_VALUE
        defaultCounterDeviceShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the counterDeviceList where value equals to UPDATED_VALUE
        defaultCounterDeviceShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultCounterDeviceShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the counterDeviceList where value equals to UPDATED_VALUE
        defaultCounterDeviceShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where value is not null
        defaultCounterDeviceShouldBeFound("value.specified=true");

        // Get all the counterDeviceList where value is null
        defaultCounterDeviceShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterDevicesByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where value is greater than or equal to DEFAULT_VALUE
        defaultCounterDeviceShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the counterDeviceList where value is greater than or equal to UPDATED_VALUE
        defaultCounterDeviceShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where value is less than or equal to DEFAULT_VALUE
        defaultCounterDeviceShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the counterDeviceList where value is less than or equal to SMALLER_VALUE
        defaultCounterDeviceShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where value is less than DEFAULT_VALUE
        defaultCounterDeviceShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the counterDeviceList where value is less than UPDATED_VALUE
        defaultCounterDeviceShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where value is greater than DEFAULT_VALUE
        defaultCounterDeviceShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the counterDeviceList where value is greater than SMALLER_VALUE
        defaultCounterDeviceShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByRolloverValueIsEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where rolloverValue equals to DEFAULT_ROLLOVER_VALUE
        defaultCounterDeviceShouldBeFound("rolloverValue.equals=" + DEFAULT_ROLLOVER_VALUE);

        // Get all the counterDeviceList where rolloverValue equals to UPDATED_ROLLOVER_VALUE
        defaultCounterDeviceShouldNotBeFound("rolloverValue.equals=" + UPDATED_ROLLOVER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByRolloverValueIsInShouldWork() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where rolloverValue in DEFAULT_ROLLOVER_VALUE or UPDATED_ROLLOVER_VALUE
        defaultCounterDeviceShouldBeFound("rolloverValue.in=" + DEFAULT_ROLLOVER_VALUE + "," + UPDATED_ROLLOVER_VALUE);

        // Get all the counterDeviceList where rolloverValue equals to UPDATED_ROLLOVER_VALUE
        defaultCounterDeviceShouldNotBeFound("rolloverValue.in=" + UPDATED_ROLLOVER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByRolloverValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where rolloverValue is not null
        defaultCounterDeviceShouldBeFound("rolloverValue.specified=true");

        // Get all the counterDeviceList where rolloverValue is null
        defaultCounterDeviceShouldNotBeFound("rolloverValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterDevicesByRolloverValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where rolloverValue is greater than or equal to DEFAULT_ROLLOVER_VALUE
        defaultCounterDeviceShouldBeFound("rolloverValue.greaterThanOrEqual=" + DEFAULT_ROLLOVER_VALUE);

        // Get all the counterDeviceList where rolloverValue is greater than or equal to UPDATED_ROLLOVER_VALUE
        defaultCounterDeviceShouldNotBeFound("rolloverValue.greaterThanOrEqual=" + UPDATED_ROLLOVER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByRolloverValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where rolloverValue is less than or equal to DEFAULT_ROLLOVER_VALUE
        defaultCounterDeviceShouldBeFound("rolloverValue.lessThanOrEqual=" + DEFAULT_ROLLOVER_VALUE);

        // Get all the counterDeviceList where rolloverValue is less than or equal to SMALLER_ROLLOVER_VALUE
        defaultCounterDeviceShouldNotBeFound("rolloverValue.lessThanOrEqual=" + SMALLER_ROLLOVER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByRolloverValueIsLessThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where rolloverValue is less than DEFAULT_ROLLOVER_VALUE
        defaultCounterDeviceShouldNotBeFound("rolloverValue.lessThan=" + DEFAULT_ROLLOVER_VALUE);

        // Get all the counterDeviceList where rolloverValue is less than UPDATED_ROLLOVER_VALUE
        defaultCounterDeviceShouldBeFound("rolloverValue.lessThan=" + UPDATED_ROLLOVER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByRolloverValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where rolloverValue is greater than DEFAULT_ROLLOVER_VALUE
        defaultCounterDeviceShouldNotBeFound("rolloverValue.greaterThan=" + DEFAULT_ROLLOVER_VALUE);

        // Get all the counterDeviceList where rolloverValue is greater than SMALLER_ROLLOVER_VALUE
        defaultCounterDeviceShouldBeFound("rolloverValue.greaterThan=" + SMALLER_ROLLOVER_VALUE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByCreditSaleIsEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where creditSale equals to DEFAULT_CREDIT_SALE
        defaultCounterDeviceShouldBeFound("creditSale.equals=" + DEFAULT_CREDIT_SALE);

        // Get all the counterDeviceList where creditSale equals to UPDATED_CREDIT_SALE
        defaultCounterDeviceShouldNotBeFound("creditSale.equals=" + UPDATED_CREDIT_SALE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByCreditSaleIsInShouldWork() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where creditSale in DEFAULT_CREDIT_SALE or UPDATED_CREDIT_SALE
        defaultCounterDeviceShouldBeFound("creditSale.in=" + DEFAULT_CREDIT_SALE + "," + UPDATED_CREDIT_SALE);

        // Get all the counterDeviceList where creditSale equals to UPDATED_CREDIT_SALE
        defaultCounterDeviceShouldNotBeFound("creditSale.in=" + UPDATED_CREDIT_SALE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByCreditSaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where creditSale is not null
        defaultCounterDeviceShouldBeFound("creditSale.specified=true");

        // Get all the counterDeviceList where creditSale is null
        defaultCounterDeviceShouldNotBeFound("creditSale.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterDevicesByCreditSaleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where creditSale is greater than or equal to DEFAULT_CREDIT_SALE
        defaultCounterDeviceShouldBeFound("creditSale.greaterThanOrEqual=" + DEFAULT_CREDIT_SALE);

        // Get all the counterDeviceList where creditSale is greater than or equal to UPDATED_CREDIT_SALE
        defaultCounterDeviceShouldNotBeFound("creditSale.greaterThanOrEqual=" + UPDATED_CREDIT_SALE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByCreditSaleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where creditSale is less than or equal to DEFAULT_CREDIT_SALE
        defaultCounterDeviceShouldBeFound("creditSale.lessThanOrEqual=" + DEFAULT_CREDIT_SALE);

        // Get all the counterDeviceList where creditSale is less than or equal to SMALLER_CREDIT_SALE
        defaultCounterDeviceShouldNotBeFound("creditSale.lessThanOrEqual=" + SMALLER_CREDIT_SALE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByCreditSaleIsLessThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where creditSale is less than DEFAULT_CREDIT_SALE
        defaultCounterDeviceShouldNotBeFound("creditSale.lessThan=" + DEFAULT_CREDIT_SALE);

        // Get all the counterDeviceList where creditSale is less than UPDATED_CREDIT_SALE
        defaultCounterDeviceShouldBeFound("creditSale.lessThan=" + UPDATED_CREDIT_SALE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByCreditSaleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where creditSale is greater than DEFAULT_CREDIT_SALE
        defaultCounterDeviceShouldNotBeFound("creditSale.greaterThan=" + DEFAULT_CREDIT_SALE);

        // Get all the counterDeviceList where creditSale is greater than SMALLER_CREDIT_SALE
        defaultCounterDeviceShouldBeFound("creditSale.greaterThan=" + SMALLER_CREDIT_SALE);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualCounterIsEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualCounter equals to DEFAULT_MANUAL_COUNTER
        defaultCounterDeviceShouldBeFound("manualCounter.equals=" + DEFAULT_MANUAL_COUNTER);

        // Get all the counterDeviceList where manualCounter equals to UPDATED_MANUAL_COUNTER
        defaultCounterDeviceShouldNotBeFound("manualCounter.equals=" + UPDATED_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualCounterIsInShouldWork() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualCounter in DEFAULT_MANUAL_COUNTER or UPDATED_MANUAL_COUNTER
        defaultCounterDeviceShouldBeFound("manualCounter.in=" + DEFAULT_MANUAL_COUNTER + "," + UPDATED_MANUAL_COUNTER);

        // Get all the counterDeviceList where manualCounter equals to UPDATED_MANUAL_COUNTER
        defaultCounterDeviceShouldNotBeFound("manualCounter.in=" + UPDATED_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualCounterIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualCounter is not null
        defaultCounterDeviceShouldBeFound("manualCounter.specified=true");

        // Get all the counterDeviceList where manualCounter is null
        defaultCounterDeviceShouldNotBeFound("manualCounter.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualMultiplierIsEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualMultiplier equals to DEFAULT_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldBeFound("manualMultiplier.equals=" + DEFAULT_MANUAL_MULTIPLIER);

        // Get all the counterDeviceList where manualMultiplier equals to UPDATED_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldNotBeFound("manualMultiplier.equals=" + UPDATED_MANUAL_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualMultiplierIsInShouldWork() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualMultiplier in DEFAULT_MANUAL_MULTIPLIER or UPDATED_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldBeFound("manualMultiplier.in=" + DEFAULT_MANUAL_MULTIPLIER + "," + UPDATED_MANUAL_MULTIPLIER);

        // Get all the counterDeviceList where manualMultiplier equals to UPDATED_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldNotBeFound("manualMultiplier.in=" + UPDATED_MANUAL_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualMultiplierIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualMultiplier is not null
        defaultCounterDeviceShouldBeFound("manualMultiplier.specified=true");

        // Get all the counterDeviceList where manualMultiplier is null
        defaultCounterDeviceShouldNotBeFound("manualMultiplier.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualMultiplierIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualMultiplier is greater than or equal to DEFAULT_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldBeFound("manualMultiplier.greaterThanOrEqual=" + DEFAULT_MANUAL_MULTIPLIER);

        // Get all the counterDeviceList where manualMultiplier is greater than or equal to UPDATED_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldNotBeFound("manualMultiplier.greaterThanOrEqual=" + UPDATED_MANUAL_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualMultiplierIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualMultiplier is less than or equal to DEFAULT_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldBeFound("manualMultiplier.lessThanOrEqual=" + DEFAULT_MANUAL_MULTIPLIER);

        // Get all the counterDeviceList where manualMultiplier is less than or equal to SMALLER_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldNotBeFound("manualMultiplier.lessThanOrEqual=" + SMALLER_MANUAL_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualMultiplierIsLessThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualMultiplier is less than DEFAULT_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldNotBeFound("manualMultiplier.lessThan=" + DEFAULT_MANUAL_MULTIPLIER);

        // Get all the counterDeviceList where manualMultiplier is less than UPDATED_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldBeFound("manualMultiplier.lessThan=" + UPDATED_MANUAL_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByManualMultiplierIsGreaterThanSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where manualMultiplier is greater than DEFAULT_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldNotBeFound("manualMultiplier.greaterThan=" + DEFAULT_MANUAL_MULTIPLIER);

        // Get all the counterDeviceList where manualMultiplier is greater than SMALLER_MANUAL_MULTIPLIER
        defaultCounterDeviceShouldBeFound("manualMultiplier.greaterThan=" + SMALLER_MANUAL_MULTIPLIER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByDecimalsManualCounterIsEqualToSomething() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where decimalsManualCounter equals to DEFAULT_DECIMALS_MANUAL_COUNTER
        defaultCounterDeviceShouldBeFound("decimalsManualCounter.equals=" + DEFAULT_DECIMALS_MANUAL_COUNTER);

        // Get all the counterDeviceList where decimalsManualCounter equals to UPDATED_DECIMALS_MANUAL_COUNTER
        defaultCounterDeviceShouldNotBeFound("decimalsManualCounter.equals=" + UPDATED_DECIMALS_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByDecimalsManualCounterIsInShouldWork() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where decimalsManualCounter in DEFAULT_DECIMALS_MANUAL_COUNTER or UPDATED_DECIMALS_MANUAL_COUNTER
        defaultCounterDeviceShouldBeFound(
            "decimalsManualCounter.in=" + DEFAULT_DECIMALS_MANUAL_COUNTER + "," + UPDATED_DECIMALS_MANUAL_COUNTER
        );

        // Get all the counterDeviceList where decimalsManualCounter equals to UPDATED_DECIMALS_MANUAL_COUNTER
        defaultCounterDeviceShouldNotBeFound("decimalsManualCounter.in=" + UPDATED_DECIMALS_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void getAllCounterDevicesByDecimalsManualCounterIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        // Get all the counterDeviceList where decimalsManualCounter is not null
        defaultCounterDeviceShouldBeFound("decimalsManualCounter.specified=true");

        // Get all the counterDeviceList where decimalsManualCounter is null
        defaultCounterDeviceShouldNotBeFound("decimalsManualCounter.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterDevicesByDeviceIsEqualToSomething() throws Exception {
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            counterDeviceRepository.saveAndFlush(counterDevice);
            device = DeviceResourceIT.createEntity(em);
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        em.persist(device);
        em.flush();
        counterDevice.setDevice(device);
        counterDeviceRepository.saveAndFlush(counterDevice);
        UUID deviceId = device.getId();

        // Get all the counterDeviceList where device equals to deviceId
        defaultCounterDeviceShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the counterDeviceList where device equals to UUID.randomUUID()
        defaultCounterDeviceShouldNotBeFound("deviceId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCounterDeviceShouldBeFound(String filter) throws Exception {
        restCounterDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterDevice.getId().toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].rolloverValue").value(hasItem(sameNumber(DEFAULT_ROLLOVER_VALUE))))
            .andExpect(jsonPath("$.[*].creditSale").value(hasItem(sameNumber(DEFAULT_CREDIT_SALE))))
            .andExpect(jsonPath("$.[*].manualCounter").value(hasItem(DEFAULT_MANUAL_COUNTER.booleanValue())))
            .andExpect(jsonPath("$.[*].manualMultiplier").value(hasItem(sameNumber(DEFAULT_MANUAL_MULTIPLIER))))
            .andExpect(jsonPath("$.[*].decimalsManualCounter").value(hasItem(DEFAULT_DECIMALS_MANUAL_COUNTER.booleanValue())));

        // Check, that the count call also returns 1
        restCounterDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCounterDeviceShouldNotBeFound(String filter) throws Exception {
        restCounterDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCounterDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCounterDevice() throws Exception {
        // Get the counterDevice
        restCounterDeviceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCounterDevice() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();

        // Update the counterDevice
        CounterDevice updatedCounterDevice = counterDeviceRepository.findById(counterDevice.getId()).get();
        // Disconnect from session so that the updates on updatedCounterDevice are not directly saved in db
        em.detach(updatedCounterDevice);
        updatedCounterDevice
            .value(UPDATED_VALUE)
            .rolloverValue(UPDATED_ROLLOVER_VALUE)
            .creditSale(UPDATED_CREDIT_SALE)
            .manualCounter(UPDATED_MANUAL_COUNTER)
            .manualMultiplier(UPDATED_MANUAL_MULTIPLIER)
            .decimalsManualCounter(UPDATED_DECIMALS_MANUAL_COUNTER);
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(updatedCounterDevice);

        restCounterDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
        CounterDevice testCounterDevice = counterDeviceList.get(counterDeviceList.size() - 1);
        assertThat(testCounterDevice.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testCounterDevice.getRolloverValue()).isEqualByComparingTo(UPDATED_ROLLOVER_VALUE);
        assertThat(testCounterDevice.getCreditSale()).isEqualByComparingTo(UPDATED_CREDIT_SALE);
        assertThat(testCounterDevice.getManualCounter()).isEqualTo(UPDATED_MANUAL_COUNTER);
        assertThat(testCounterDevice.getManualMultiplier()).isEqualByComparingTo(UPDATED_MANUAL_MULTIPLIER);
        assertThat(testCounterDevice.getDecimalsManualCounter()).isEqualTo(UPDATED_DECIMALS_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void putNonExistingCounterDevice() throws Exception {
        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();
        counterDevice.setId(UUID.randomUUID());

        // Create the CounterDevice
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounterDevice() throws Exception {
        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();
        counterDevice.setId(UUID.randomUUID());

        // Create the CounterDevice
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounterDevice() throws Exception {
        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();
        counterDevice.setId(UUID.randomUUID());

        // Create the CounterDevice
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterDeviceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCounterDeviceWithPatch() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();

        // Update the counterDevice using partial update
        CounterDevice partialUpdatedCounterDevice = new CounterDevice();
        partialUpdatedCounterDevice.setId(counterDevice.getId());

        partialUpdatedCounterDevice.rolloverValue(UPDATED_ROLLOVER_VALUE).decimalsManualCounter(UPDATED_DECIMALS_MANUAL_COUNTER);

        restCounterDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterDevice))
            )
            .andExpect(status().isOk());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
        CounterDevice testCounterDevice = counterDeviceList.get(counterDeviceList.size() - 1);
        assertThat(testCounterDevice.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testCounterDevice.getRolloverValue()).isEqualByComparingTo(UPDATED_ROLLOVER_VALUE);
        assertThat(testCounterDevice.getCreditSale()).isEqualByComparingTo(DEFAULT_CREDIT_SALE);
        assertThat(testCounterDevice.getManualCounter()).isEqualTo(DEFAULT_MANUAL_COUNTER);
        assertThat(testCounterDevice.getManualMultiplier()).isEqualByComparingTo(DEFAULT_MANUAL_MULTIPLIER);
        assertThat(testCounterDevice.getDecimalsManualCounter()).isEqualTo(UPDATED_DECIMALS_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void fullUpdateCounterDeviceWithPatch() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();

        // Update the counterDevice using partial update
        CounterDevice partialUpdatedCounterDevice = new CounterDevice();
        partialUpdatedCounterDevice.setId(counterDevice.getId());

        partialUpdatedCounterDevice
            .value(UPDATED_VALUE)
            .rolloverValue(UPDATED_ROLLOVER_VALUE)
            .creditSale(UPDATED_CREDIT_SALE)
            .manualCounter(UPDATED_MANUAL_COUNTER)
            .manualMultiplier(UPDATED_MANUAL_MULTIPLIER)
            .decimalsManualCounter(UPDATED_DECIMALS_MANUAL_COUNTER);

        restCounterDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterDevice))
            )
            .andExpect(status().isOk());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
        CounterDevice testCounterDevice = counterDeviceList.get(counterDeviceList.size() - 1);
        assertThat(testCounterDevice.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testCounterDevice.getRolloverValue()).isEqualByComparingTo(UPDATED_ROLLOVER_VALUE);
        assertThat(testCounterDevice.getCreditSale()).isEqualByComparingTo(UPDATED_CREDIT_SALE);
        assertThat(testCounterDevice.getManualCounter()).isEqualTo(UPDATED_MANUAL_COUNTER);
        assertThat(testCounterDevice.getManualMultiplier()).isEqualByComparingTo(UPDATED_MANUAL_MULTIPLIER);
        assertThat(testCounterDevice.getDecimalsManualCounter()).isEqualTo(UPDATED_DECIMALS_MANUAL_COUNTER);
    }

    @Test
    @Transactional
    void patchNonExistingCounterDevice() throws Exception {
        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();
        counterDevice.setId(UUID.randomUUID());

        // Create the CounterDevice
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, counterDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounterDevice() throws Exception {
        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();
        counterDevice.setId(UUID.randomUUID());

        // Create the CounterDevice
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounterDevice() throws Exception {
        int databaseSizeBeforeUpdate = counterDeviceRepository.findAll().size();
        counterDevice.setId(UUID.randomUUID());

        // Create the CounterDevice
        CounterDeviceDTO counterDeviceDTO = counterDeviceMapper.toDto(counterDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterDevice in the database
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCounterDevice() throws Exception {
        // Initialize the database
        counterDeviceRepository.saveAndFlush(counterDevice);

        int databaseSizeBeforeDelete = counterDeviceRepository.findAll().size();

        // Delete the counterDevice
        restCounterDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, counterDevice.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CounterDevice> counterDeviceList = counterDeviceRepository.findAll();
        assertThat(counterDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
