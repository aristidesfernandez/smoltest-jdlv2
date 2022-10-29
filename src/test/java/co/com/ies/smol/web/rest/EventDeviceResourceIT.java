package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.DeviceEstablishment;
import co.com.ies.smol.domain.EventDevice;
import co.com.ies.smol.domain.EventType;
import co.com.ies.smol.repository.EventDeviceRepository;
import co.com.ies.smol.service.criteria.EventDeviceCriteria;
import co.com.ies.smol.service.dto.EventDeviceDTO;
import co.com.ies.smol.service.mapper.EventDeviceMapper;
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
 * Integration tests for the {@link EventDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventDeviceResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_THEORETICAL_PERCENTAGE = false;
    private static final Boolean UPDATED_THEORETICAL_PERCENTAGE = true;

    private static final Double DEFAULT_MONEY_DENOMINATION = 1D;
    private static final Double UPDATED_MONEY_DENOMINATION = 2D;
    private static final Double SMALLER_MONEY_DENOMINATION = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/event-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EventDeviceRepository eventDeviceRepository;

    @Autowired
    private EventDeviceMapper eventDeviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventDeviceMockMvc;

    private EventDevice eventDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventDevice createEntity(EntityManager em) {
        EventDevice eventDevice = new EventDevice()
            .createdAt(DEFAULT_CREATED_AT)
            .theoreticalPercentage(DEFAULT_THEORETICAL_PERCENTAGE)
            .moneyDenomination(DEFAULT_MONEY_DENOMINATION);
        // Add required entity
        DeviceEstablishment deviceEstablishment;
        if (TestUtil.findAll(em, DeviceEstablishment.class).isEmpty()) {
            deviceEstablishment = DeviceEstablishmentResourceIT.createEntity(em);
            em.persist(deviceEstablishment);
            em.flush();
        } else {
            deviceEstablishment = TestUtil.findAll(em, DeviceEstablishment.class).get(0);
        }
        eventDevice.setDeviceEstablishment(deviceEstablishment);
        // Add required entity
        EventType eventType;
        if (TestUtil.findAll(em, EventType.class).isEmpty()) {
            eventType = EventTypeResourceIT.createEntity(em);
            em.persist(eventType);
            em.flush();
        } else {
            eventType = TestUtil.findAll(em, EventType.class).get(0);
        }
        eventDevice.setEventType(eventType);
        return eventDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventDevice createUpdatedEntity(EntityManager em) {
        EventDevice eventDevice = new EventDevice()
            .createdAt(UPDATED_CREATED_AT)
            .theoreticalPercentage(UPDATED_THEORETICAL_PERCENTAGE)
            .moneyDenomination(UPDATED_MONEY_DENOMINATION);
        // Add required entity
        DeviceEstablishment deviceEstablishment;
        if (TestUtil.findAll(em, DeviceEstablishment.class).isEmpty()) {
            deviceEstablishment = DeviceEstablishmentResourceIT.createUpdatedEntity(em);
            em.persist(deviceEstablishment);
            em.flush();
        } else {
            deviceEstablishment = TestUtil.findAll(em, DeviceEstablishment.class).get(0);
        }
        eventDevice.setDeviceEstablishment(deviceEstablishment);
        // Add required entity
        EventType eventType;
        if (TestUtil.findAll(em, EventType.class).isEmpty()) {
            eventType = EventTypeResourceIT.createUpdatedEntity(em);
            em.persist(eventType);
            em.flush();
        } else {
            eventType = TestUtil.findAll(em, EventType.class).get(0);
        }
        eventDevice.setEventType(eventType);
        return eventDevice;
    }

    @BeforeEach
    public void initTest() {
        eventDevice = createEntity(em);
    }

    @Test
    @Transactional
    void createEventDevice() throws Exception {
        int databaseSizeBeforeCreate = eventDeviceRepository.findAll().size();
        // Create the EventDevice
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);
        restEventDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        EventDevice testEventDevice = eventDeviceList.get(eventDeviceList.size() - 1);
        assertThat(testEventDevice.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventDevice.getTheoreticalPercentage()).isEqualTo(DEFAULT_THEORETICAL_PERCENTAGE);
        assertThat(testEventDevice.getMoneyDenomination()).isEqualTo(DEFAULT_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void createEventDeviceWithExistingId() throws Exception {
        // Create the EventDevice with an existing ID
        eventDeviceRepository.saveAndFlush(eventDevice);
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);

        int databaseSizeBeforeCreate = eventDeviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventDevices() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList
        restEventDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventDevice.getId().toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].theoreticalPercentage").value(hasItem(DEFAULT_THEORETICAL_PERCENTAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].moneyDenomination").value(hasItem(DEFAULT_MONEY_DENOMINATION.doubleValue())));
    }

    @Test
    @Transactional
    void getEventDevice() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get the eventDevice
        restEventDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, eventDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventDevice.getId().toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.theoreticalPercentage").value(DEFAULT_THEORETICAL_PERCENTAGE.booleanValue()))
            .andExpect(jsonPath("$.moneyDenomination").value(DEFAULT_MONEY_DENOMINATION.doubleValue()));
    }

    @Test
    @Transactional
    void getEventDevicesByIdFiltering() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        UUID id = eventDevice.getId();

        defaultEventDeviceShouldBeFound("id.equals=" + id);
        defaultEventDeviceShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllEventDevicesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventDeviceShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventDeviceList where createdAt equals to UPDATED_CREATED_AT
        defaultEventDeviceShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDevicesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventDeviceShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventDeviceList where createdAt equals to UPDATED_CREATED_AT
        defaultEventDeviceShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDevicesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where createdAt is not null
        defaultEventDeviceShouldBeFound("createdAt.specified=true");

        // Get all the eventDeviceList where createdAt is null
        defaultEventDeviceShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDevicesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventDeviceShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventDeviceList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventDeviceShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDevicesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventDeviceShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventDeviceList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventDeviceShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDevicesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventDeviceShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventDeviceList where createdAt is less than UPDATED_CREATED_AT
        defaultEventDeviceShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDevicesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventDeviceShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventDeviceList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventDeviceShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDevicesByTheoreticalPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where theoreticalPercentage equals to DEFAULT_THEORETICAL_PERCENTAGE
        defaultEventDeviceShouldBeFound("theoreticalPercentage.equals=" + DEFAULT_THEORETICAL_PERCENTAGE);

        // Get all the eventDeviceList where theoreticalPercentage equals to UPDATED_THEORETICAL_PERCENTAGE
        defaultEventDeviceShouldNotBeFound("theoreticalPercentage.equals=" + UPDATED_THEORETICAL_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllEventDevicesByTheoreticalPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where theoreticalPercentage in DEFAULT_THEORETICAL_PERCENTAGE or UPDATED_THEORETICAL_PERCENTAGE
        defaultEventDeviceShouldBeFound(
            "theoreticalPercentage.in=" + DEFAULT_THEORETICAL_PERCENTAGE + "," + UPDATED_THEORETICAL_PERCENTAGE
        );

        // Get all the eventDeviceList where theoreticalPercentage equals to UPDATED_THEORETICAL_PERCENTAGE
        defaultEventDeviceShouldNotBeFound("theoreticalPercentage.in=" + UPDATED_THEORETICAL_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllEventDevicesByTheoreticalPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where theoreticalPercentage is not null
        defaultEventDeviceShouldBeFound("theoreticalPercentage.specified=true");

        // Get all the eventDeviceList where theoreticalPercentage is null
        defaultEventDeviceShouldNotBeFound("theoreticalPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDevicesByMoneyDenominationIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where moneyDenomination equals to DEFAULT_MONEY_DENOMINATION
        defaultEventDeviceShouldBeFound("moneyDenomination.equals=" + DEFAULT_MONEY_DENOMINATION);

        // Get all the eventDeviceList where moneyDenomination equals to UPDATED_MONEY_DENOMINATION
        defaultEventDeviceShouldNotBeFound("moneyDenomination.equals=" + UPDATED_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllEventDevicesByMoneyDenominationIsInShouldWork() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where moneyDenomination in DEFAULT_MONEY_DENOMINATION or UPDATED_MONEY_DENOMINATION
        defaultEventDeviceShouldBeFound("moneyDenomination.in=" + DEFAULT_MONEY_DENOMINATION + "," + UPDATED_MONEY_DENOMINATION);

        // Get all the eventDeviceList where moneyDenomination equals to UPDATED_MONEY_DENOMINATION
        defaultEventDeviceShouldNotBeFound("moneyDenomination.in=" + UPDATED_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllEventDevicesByMoneyDenominationIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where moneyDenomination is not null
        defaultEventDeviceShouldBeFound("moneyDenomination.specified=true");

        // Get all the eventDeviceList where moneyDenomination is null
        defaultEventDeviceShouldNotBeFound("moneyDenomination.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDevicesByMoneyDenominationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where moneyDenomination is greater than or equal to DEFAULT_MONEY_DENOMINATION
        defaultEventDeviceShouldBeFound("moneyDenomination.greaterThanOrEqual=" + DEFAULT_MONEY_DENOMINATION);

        // Get all the eventDeviceList where moneyDenomination is greater than or equal to UPDATED_MONEY_DENOMINATION
        defaultEventDeviceShouldNotBeFound("moneyDenomination.greaterThanOrEqual=" + UPDATED_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllEventDevicesByMoneyDenominationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where moneyDenomination is less than or equal to DEFAULT_MONEY_DENOMINATION
        defaultEventDeviceShouldBeFound("moneyDenomination.lessThanOrEqual=" + DEFAULT_MONEY_DENOMINATION);

        // Get all the eventDeviceList where moneyDenomination is less than or equal to SMALLER_MONEY_DENOMINATION
        defaultEventDeviceShouldNotBeFound("moneyDenomination.lessThanOrEqual=" + SMALLER_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllEventDevicesByMoneyDenominationIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where moneyDenomination is less than DEFAULT_MONEY_DENOMINATION
        defaultEventDeviceShouldNotBeFound("moneyDenomination.lessThan=" + DEFAULT_MONEY_DENOMINATION);

        // Get all the eventDeviceList where moneyDenomination is less than UPDATED_MONEY_DENOMINATION
        defaultEventDeviceShouldBeFound("moneyDenomination.lessThan=" + UPDATED_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllEventDevicesByMoneyDenominationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        // Get all the eventDeviceList where moneyDenomination is greater than DEFAULT_MONEY_DENOMINATION
        defaultEventDeviceShouldNotBeFound("moneyDenomination.greaterThan=" + DEFAULT_MONEY_DENOMINATION);

        // Get all the eventDeviceList where moneyDenomination is greater than SMALLER_MONEY_DENOMINATION
        defaultEventDeviceShouldBeFound("moneyDenomination.greaterThan=" + SMALLER_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllEventDevicesByDeviceEstablishmentIsEqualToSomething() throws Exception {
        DeviceEstablishment deviceEstablishment;
        if (TestUtil.findAll(em, DeviceEstablishment.class).isEmpty()) {
            eventDeviceRepository.saveAndFlush(eventDevice);
            deviceEstablishment = DeviceEstablishmentResourceIT.createEntity(em);
        } else {
            deviceEstablishment = TestUtil.findAll(em, DeviceEstablishment.class).get(0);
        }
        em.persist(deviceEstablishment);
        em.flush();
        eventDevice.setDeviceEstablishment(deviceEstablishment);
        eventDeviceRepository.saveAndFlush(eventDevice);
        UUID deviceEstablishmentId = deviceEstablishment.getId();

        // Get all the eventDeviceList where deviceEstablishment equals to deviceEstablishmentId
        defaultEventDeviceShouldBeFound("deviceEstablishmentId.equals=" + deviceEstablishmentId);

        // Get all the eventDeviceList where deviceEstablishment equals to UUID.randomUUID()
        defaultEventDeviceShouldNotBeFound("deviceEstablishmentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllEventDevicesByEventTypeIsEqualToSomething() throws Exception {
        EventType eventType;
        if (TestUtil.findAll(em, EventType.class).isEmpty()) {
            eventDeviceRepository.saveAndFlush(eventDevice);
            eventType = EventTypeResourceIT.createEntity(em);
        } else {
            eventType = TestUtil.findAll(em, EventType.class).get(0);
        }
        em.persist(eventType);
        em.flush();
        eventDevice.setEventType(eventType);
        eventDeviceRepository.saveAndFlush(eventDevice);
        Long eventTypeId = eventType.getId();

        // Get all the eventDeviceList where eventType equals to eventTypeId
        defaultEventDeviceShouldBeFound("eventTypeId.equals=" + eventTypeId);

        // Get all the eventDeviceList where eventType equals to (eventTypeId + 1)
        defaultEventDeviceShouldNotBeFound("eventTypeId.equals=" + (eventTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventDeviceShouldBeFound(String filter) throws Exception {
        restEventDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventDevice.getId().toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].theoreticalPercentage").value(hasItem(DEFAULT_THEORETICAL_PERCENTAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].moneyDenomination").value(hasItem(DEFAULT_MONEY_DENOMINATION.doubleValue())));

        // Check, that the count call also returns 1
        restEventDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventDeviceShouldNotBeFound(String filter) throws Exception {
        restEventDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventDevice() throws Exception {
        // Get the eventDevice
        restEventDeviceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventDevice() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();

        // Update the eventDevice
        EventDevice updatedEventDevice = eventDeviceRepository.findById(eventDevice.getId()).get();
        // Disconnect from session so that the updates on updatedEventDevice are not directly saved in db
        em.detach(updatedEventDevice);
        updatedEventDevice
            .createdAt(UPDATED_CREATED_AT)
            .theoreticalPercentage(UPDATED_THEORETICAL_PERCENTAGE)
            .moneyDenomination(UPDATED_MONEY_DENOMINATION);
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(updatedEventDevice);

        restEventDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
        EventDevice testEventDevice = eventDeviceList.get(eventDeviceList.size() - 1);
        assertThat(testEventDevice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventDevice.getTheoreticalPercentage()).isEqualTo(UPDATED_THEORETICAL_PERCENTAGE);
        assertThat(testEventDevice.getMoneyDenomination()).isEqualTo(UPDATED_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void putNonExistingEventDevice() throws Exception {
        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();
        eventDevice.setId(UUID.randomUUID());

        // Create the EventDevice
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventDevice() throws Exception {
        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();
        eventDevice.setId(UUID.randomUUID());

        // Create the EventDevice
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventDevice() throws Exception {
        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();
        eventDevice.setId(UUID.randomUUID());

        // Create the EventDevice
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventDeviceWithPatch() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();

        // Update the eventDevice using partial update
        EventDevice partialUpdatedEventDevice = new EventDevice();
        partialUpdatedEventDevice.setId(eventDevice.getId());

        partialUpdatedEventDevice.theoreticalPercentage(UPDATED_THEORETICAL_PERCENTAGE).moneyDenomination(UPDATED_MONEY_DENOMINATION);

        restEventDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventDevice))
            )
            .andExpect(status().isOk());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
        EventDevice testEventDevice = eventDeviceList.get(eventDeviceList.size() - 1);
        assertThat(testEventDevice.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventDevice.getTheoreticalPercentage()).isEqualTo(UPDATED_THEORETICAL_PERCENTAGE);
        assertThat(testEventDevice.getMoneyDenomination()).isEqualTo(UPDATED_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void fullUpdateEventDeviceWithPatch() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();

        // Update the eventDevice using partial update
        EventDevice partialUpdatedEventDevice = new EventDevice();
        partialUpdatedEventDevice.setId(eventDevice.getId());

        partialUpdatedEventDevice
            .createdAt(UPDATED_CREATED_AT)
            .theoreticalPercentage(UPDATED_THEORETICAL_PERCENTAGE)
            .moneyDenomination(UPDATED_MONEY_DENOMINATION);

        restEventDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventDevice))
            )
            .andExpect(status().isOk());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
        EventDevice testEventDevice = eventDeviceList.get(eventDeviceList.size() - 1);
        assertThat(testEventDevice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventDevice.getTheoreticalPercentage()).isEqualTo(UPDATED_THEORETICAL_PERCENTAGE);
        assertThat(testEventDevice.getMoneyDenomination()).isEqualTo(UPDATED_MONEY_DENOMINATION);
    }

    @Test
    @Transactional
    void patchNonExistingEventDevice() throws Exception {
        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();
        eventDevice.setId(UUID.randomUUID());

        // Create the EventDevice
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventDevice() throws Exception {
        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();
        eventDevice.setId(UUID.randomUUID());

        // Create the EventDevice
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventDevice() throws Exception {
        int databaseSizeBeforeUpdate = eventDeviceRepository.findAll().size();
        eventDevice.setId(UUID.randomUUID());

        // Create the EventDevice
        EventDeviceDTO eventDeviceDTO = eventDeviceMapper.toDto(eventDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventDevice in the database
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventDevice() throws Exception {
        // Initialize the database
        eventDeviceRepository.saveAndFlush(eventDevice);

        int databaseSizeBeforeDelete = eventDeviceRepository.findAll().size();

        // Delete the eventDevice
        restEventDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventDevice.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventDevice> eventDeviceList = eventDeviceRepository.findAll();
        assertThat(eventDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
