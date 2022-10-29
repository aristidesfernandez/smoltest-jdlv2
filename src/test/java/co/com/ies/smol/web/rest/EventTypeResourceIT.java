package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.EventType;
import co.com.ies.smol.repository.EventTypeRepository;
import co.com.ies.smol.service.criteria.EventTypeCriteria;
import co.com.ies.smol.service.dto.EventTypeDTO;
import co.com.ies.smol.service.mapper.EventTypeMapper;
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
 * Integration tests for the {@link EventTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventTypeResourceIT {

    private static final String DEFAULT_EVENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_STORABLE = false;
    private static final Boolean UPDATED_IS_STORABLE = true;

    private static final Boolean DEFAULT_IS_PRIORITY = false;
    private static final Boolean UPDATED_IS_PRIORITY = true;

    private static final String DEFAULT_PROCESADOR = "AAAAAAAAAA";
    private static final String UPDATED_PROCESADOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ALARM = false;
    private static final Boolean UPDATED_IS_ALARM = true;

    private static final String ENTITY_API_URL = "/api/event-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventTypeMapper eventTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventTypeMockMvc;

    private EventType eventType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createEntity(EntityManager em) {
        EventType eventType = new EventType()
            .eventCode(DEFAULT_EVENT_CODE)
            .sasCode(DEFAULT_SAS_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isStorable(DEFAULT_IS_STORABLE)
            .isPriority(DEFAULT_IS_PRIORITY)
            .procesador(DEFAULT_PROCESADOR)
            .isAlarm(DEFAULT_IS_ALARM);
        return eventType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createUpdatedEntity(EntityManager em) {
        EventType eventType = new EventType()
            .eventCode(UPDATED_EVENT_CODE)
            .sasCode(UPDATED_SAS_CODE)
            .description(UPDATED_DESCRIPTION)
            .isStorable(UPDATED_IS_STORABLE)
            .isPriority(UPDATED_IS_PRIORITY)
            .procesador(UPDATED_PROCESADOR)
            .isAlarm(UPDATED_IS_ALARM);
        return eventType;
    }

    @BeforeEach
    public void initTest() {
        eventType = createEntity(em);
    }

    @Test
    @Transactional
    void createEventType() throws Exception {
        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();
        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);
        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getEventCode()).isEqualTo(DEFAULT_EVENT_CODE);
        assertThat(testEventType.getSasCode()).isEqualTo(DEFAULT_SAS_CODE);
        assertThat(testEventType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventType.getIsStorable()).isEqualTo(DEFAULT_IS_STORABLE);
        assertThat(testEventType.getIsPriority()).isEqualTo(DEFAULT_IS_PRIORITY);
        assertThat(testEventType.getProcesador()).isEqualTo(DEFAULT_PROCESADOR);
        assertThat(testEventType.getIsAlarm()).isEqualTo(DEFAULT_IS_ALARM);
    }

    @Test
    @Transactional
    void createEventTypeWithExistingId() throws Exception {
        // Create the EventType with an existing ID
        eventType.setId(1L);
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeRepository.findAll().size();
        // set the field null
        eventType.setEventCode(null);

        // Create the EventType, which fails.
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventTypes() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventCode").value(hasItem(DEFAULT_EVENT_CODE)))
            .andExpect(jsonPath("$.[*].sasCode").value(hasItem(DEFAULT_SAS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isStorable").value(hasItem(DEFAULT_IS_STORABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isPriority").value(hasItem(DEFAULT_IS_PRIORITY.booleanValue())))
            .andExpect(jsonPath("$.[*].procesador").value(hasItem(DEFAULT_PROCESADOR)))
            .andExpect(jsonPath("$.[*].isAlarm").value(hasItem(DEFAULT_IS_ALARM.booleanValue())));
    }

    @Test
    @Transactional
    void getEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get the eventType
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, eventType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventType.getId().intValue()))
            .andExpect(jsonPath("$.eventCode").value(DEFAULT_EVENT_CODE))
            .andExpect(jsonPath("$.sasCode").value(DEFAULT_SAS_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isStorable").value(DEFAULT_IS_STORABLE.booleanValue()))
            .andExpect(jsonPath("$.isPriority").value(DEFAULT_IS_PRIORITY.booleanValue()))
            .andExpect(jsonPath("$.procesador").value(DEFAULT_PROCESADOR))
            .andExpect(jsonPath("$.isAlarm").value(DEFAULT_IS_ALARM.booleanValue()));
    }

    @Test
    @Transactional
    void getEventTypesByIdFiltering() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        Long id = eventType.getId();

        defaultEventTypeShouldBeFound("id.equals=" + id);
        defaultEventTypeShouldNotBeFound("id.notEquals=" + id);

        defaultEventTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultEventTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventTypesByEventCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where eventCode equals to DEFAULT_EVENT_CODE
        defaultEventTypeShouldBeFound("eventCode.equals=" + DEFAULT_EVENT_CODE);

        // Get all the eventTypeList where eventCode equals to UPDATED_EVENT_CODE
        defaultEventTypeShouldNotBeFound("eventCode.equals=" + UPDATED_EVENT_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesByEventCodeIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where eventCode in DEFAULT_EVENT_CODE or UPDATED_EVENT_CODE
        defaultEventTypeShouldBeFound("eventCode.in=" + DEFAULT_EVENT_CODE + "," + UPDATED_EVENT_CODE);

        // Get all the eventTypeList where eventCode equals to UPDATED_EVENT_CODE
        defaultEventTypeShouldNotBeFound("eventCode.in=" + UPDATED_EVENT_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesByEventCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where eventCode is not null
        defaultEventTypeShouldBeFound("eventCode.specified=true");

        // Get all the eventTypeList where eventCode is null
        defaultEventTypeShouldNotBeFound("eventCode.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByEventCodeContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where eventCode contains DEFAULT_EVENT_CODE
        defaultEventTypeShouldBeFound("eventCode.contains=" + DEFAULT_EVENT_CODE);

        // Get all the eventTypeList where eventCode contains UPDATED_EVENT_CODE
        defaultEventTypeShouldNotBeFound("eventCode.contains=" + UPDATED_EVENT_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesByEventCodeNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where eventCode does not contain DEFAULT_EVENT_CODE
        defaultEventTypeShouldNotBeFound("eventCode.doesNotContain=" + DEFAULT_EVENT_CODE);

        // Get all the eventTypeList where eventCode does not contain UPDATED_EVENT_CODE
        defaultEventTypeShouldBeFound("eventCode.doesNotContain=" + UPDATED_EVENT_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesBySasCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where sasCode equals to DEFAULT_SAS_CODE
        defaultEventTypeShouldBeFound("sasCode.equals=" + DEFAULT_SAS_CODE);

        // Get all the eventTypeList where sasCode equals to UPDATED_SAS_CODE
        defaultEventTypeShouldNotBeFound("sasCode.equals=" + UPDATED_SAS_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesBySasCodeIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where sasCode in DEFAULT_SAS_CODE or UPDATED_SAS_CODE
        defaultEventTypeShouldBeFound("sasCode.in=" + DEFAULT_SAS_CODE + "," + UPDATED_SAS_CODE);

        // Get all the eventTypeList where sasCode equals to UPDATED_SAS_CODE
        defaultEventTypeShouldNotBeFound("sasCode.in=" + UPDATED_SAS_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesBySasCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where sasCode is not null
        defaultEventTypeShouldBeFound("sasCode.specified=true");

        // Get all the eventTypeList where sasCode is null
        defaultEventTypeShouldNotBeFound("sasCode.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesBySasCodeContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where sasCode contains DEFAULT_SAS_CODE
        defaultEventTypeShouldBeFound("sasCode.contains=" + DEFAULT_SAS_CODE);

        // Get all the eventTypeList where sasCode contains UPDATED_SAS_CODE
        defaultEventTypeShouldNotBeFound("sasCode.contains=" + UPDATED_SAS_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesBySasCodeNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where sasCode does not contain DEFAULT_SAS_CODE
        defaultEventTypeShouldNotBeFound("sasCode.doesNotContain=" + DEFAULT_SAS_CODE);

        // Get all the eventTypeList where sasCode does not contain UPDATED_SAS_CODE
        defaultEventTypeShouldBeFound("sasCode.doesNotContain=" + UPDATED_SAS_CODE);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description equals to DEFAULT_DESCRIPTION
        defaultEventTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeList where description equals to UPDATED_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventTypeList where description equals to UPDATED_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description is not null
        defaultEventTypeShouldBeFound("description.specified=true");

        // Get all the eventTypeList where description is null
        defaultEventTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description contains DEFAULT_DESCRIPTION
        defaultEventTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeList where description contains UPDATED_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeList where description does not contain UPDATED_DESCRIPTION
        defaultEventTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsStorableIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isStorable equals to DEFAULT_IS_STORABLE
        defaultEventTypeShouldBeFound("isStorable.equals=" + DEFAULT_IS_STORABLE);

        // Get all the eventTypeList where isStorable equals to UPDATED_IS_STORABLE
        defaultEventTypeShouldNotBeFound("isStorable.equals=" + UPDATED_IS_STORABLE);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsStorableIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isStorable in DEFAULT_IS_STORABLE or UPDATED_IS_STORABLE
        defaultEventTypeShouldBeFound("isStorable.in=" + DEFAULT_IS_STORABLE + "," + UPDATED_IS_STORABLE);

        // Get all the eventTypeList where isStorable equals to UPDATED_IS_STORABLE
        defaultEventTypeShouldNotBeFound("isStorable.in=" + UPDATED_IS_STORABLE);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsStorableIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isStorable is not null
        defaultEventTypeShouldBeFound("isStorable.specified=true");

        // Get all the eventTypeList where isStorable is null
        defaultEventTypeShouldNotBeFound("isStorable.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByIsPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isPriority equals to DEFAULT_IS_PRIORITY
        defaultEventTypeShouldBeFound("isPriority.equals=" + DEFAULT_IS_PRIORITY);

        // Get all the eventTypeList where isPriority equals to UPDATED_IS_PRIORITY
        defaultEventTypeShouldNotBeFound("isPriority.equals=" + UPDATED_IS_PRIORITY);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isPriority in DEFAULT_IS_PRIORITY or UPDATED_IS_PRIORITY
        defaultEventTypeShouldBeFound("isPriority.in=" + DEFAULT_IS_PRIORITY + "," + UPDATED_IS_PRIORITY);

        // Get all the eventTypeList where isPriority equals to UPDATED_IS_PRIORITY
        defaultEventTypeShouldNotBeFound("isPriority.in=" + UPDATED_IS_PRIORITY);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isPriority is not null
        defaultEventTypeShouldBeFound("isPriority.specified=true");

        // Get all the eventTypeList where isPriority is null
        defaultEventTypeShouldNotBeFound("isPriority.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByProcesadorIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where procesador equals to DEFAULT_PROCESADOR
        defaultEventTypeShouldBeFound("procesador.equals=" + DEFAULT_PROCESADOR);

        // Get all the eventTypeList where procesador equals to UPDATED_PROCESADOR
        defaultEventTypeShouldNotBeFound("procesador.equals=" + UPDATED_PROCESADOR);
    }

    @Test
    @Transactional
    void getAllEventTypesByProcesadorIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where procesador in DEFAULT_PROCESADOR or UPDATED_PROCESADOR
        defaultEventTypeShouldBeFound("procesador.in=" + DEFAULT_PROCESADOR + "," + UPDATED_PROCESADOR);

        // Get all the eventTypeList where procesador equals to UPDATED_PROCESADOR
        defaultEventTypeShouldNotBeFound("procesador.in=" + UPDATED_PROCESADOR);
    }

    @Test
    @Transactional
    void getAllEventTypesByProcesadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where procesador is not null
        defaultEventTypeShouldBeFound("procesador.specified=true");

        // Get all the eventTypeList where procesador is null
        defaultEventTypeShouldNotBeFound("procesador.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByProcesadorContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where procesador contains DEFAULT_PROCESADOR
        defaultEventTypeShouldBeFound("procesador.contains=" + DEFAULT_PROCESADOR);

        // Get all the eventTypeList where procesador contains UPDATED_PROCESADOR
        defaultEventTypeShouldNotBeFound("procesador.contains=" + UPDATED_PROCESADOR);
    }

    @Test
    @Transactional
    void getAllEventTypesByProcesadorNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where procesador does not contain DEFAULT_PROCESADOR
        defaultEventTypeShouldNotBeFound("procesador.doesNotContain=" + DEFAULT_PROCESADOR);

        // Get all the eventTypeList where procesador does not contain UPDATED_PROCESADOR
        defaultEventTypeShouldBeFound("procesador.doesNotContain=" + UPDATED_PROCESADOR);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsAlarmIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isAlarm equals to DEFAULT_IS_ALARM
        defaultEventTypeShouldBeFound("isAlarm.equals=" + DEFAULT_IS_ALARM);

        // Get all the eventTypeList where isAlarm equals to UPDATED_IS_ALARM
        defaultEventTypeShouldNotBeFound("isAlarm.equals=" + UPDATED_IS_ALARM);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsAlarmIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isAlarm in DEFAULT_IS_ALARM or UPDATED_IS_ALARM
        defaultEventTypeShouldBeFound("isAlarm.in=" + DEFAULT_IS_ALARM + "," + UPDATED_IS_ALARM);

        // Get all the eventTypeList where isAlarm equals to UPDATED_IS_ALARM
        defaultEventTypeShouldNotBeFound("isAlarm.in=" + UPDATED_IS_ALARM);
    }

    @Test
    @Transactional
    void getAllEventTypesByIsAlarmIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where isAlarm is not null
        defaultEventTypeShouldBeFound("isAlarm.specified=true");

        // Get all the eventTypeList where isAlarm is null
        defaultEventTypeShouldNotBeFound("isAlarm.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventTypeShouldBeFound(String filter) throws Exception {
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventCode").value(hasItem(DEFAULT_EVENT_CODE)))
            .andExpect(jsonPath("$.[*].sasCode").value(hasItem(DEFAULT_SAS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isStorable").value(hasItem(DEFAULT_IS_STORABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isPriority").value(hasItem(DEFAULT_IS_PRIORITY.booleanValue())))
            .andExpect(jsonPath("$.[*].procesador").value(hasItem(DEFAULT_PROCESADOR)))
            .andExpect(jsonPath("$.[*].isAlarm").value(hasItem(DEFAULT_IS_ALARM.booleanValue())));

        // Check, that the count call also returns 1
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventTypeShouldNotBeFound(String filter) throws Exception {
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventType() throws Exception {
        // Get the eventType
        restEventTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType
        EventType updatedEventType = eventTypeRepository.findById(eventType.getId()).get();
        // Disconnect from session so that the updates on updatedEventType are not directly saved in db
        em.detach(updatedEventType);
        updatedEventType
            .eventCode(UPDATED_EVENT_CODE)
            .sasCode(UPDATED_SAS_CODE)
            .description(UPDATED_DESCRIPTION)
            .isStorable(UPDATED_IS_STORABLE)
            .isPriority(UPDATED_IS_PRIORITY)
            .procesador(UPDATED_PROCESADOR)
            .isAlarm(UPDATED_IS_ALARM);
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(updatedEventType);

        restEventTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getEventCode()).isEqualTo(UPDATED_EVENT_CODE);
        assertThat(testEventType.getSasCode()).isEqualTo(UPDATED_SAS_CODE);
        assertThat(testEventType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventType.getIsStorable()).isEqualTo(UPDATED_IS_STORABLE);
        assertThat(testEventType.getIsPriority()).isEqualTo(UPDATED_IS_PRIORITY);
        assertThat(testEventType.getProcesador()).isEqualTo(UPDATED_PROCESADOR);
        assertThat(testEventType.getIsAlarm()).isEqualTo(UPDATED_IS_ALARM);
    }

    @Test
    @Transactional
    void putNonExistingEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(count.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(count.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(count.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventTypeWithPatch() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType using partial update
        EventType partialUpdatedEventType = new EventType();
        partialUpdatedEventType.setId(eventType.getId());

        partialUpdatedEventType.eventCode(UPDATED_EVENT_CODE).sasCode(UPDATED_SAS_CODE);

        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventType))
            )
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getEventCode()).isEqualTo(UPDATED_EVENT_CODE);
        assertThat(testEventType.getSasCode()).isEqualTo(UPDATED_SAS_CODE);
        assertThat(testEventType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventType.getIsStorable()).isEqualTo(DEFAULT_IS_STORABLE);
        assertThat(testEventType.getIsPriority()).isEqualTo(DEFAULT_IS_PRIORITY);
        assertThat(testEventType.getProcesador()).isEqualTo(DEFAULT_PROCESADOR);
        assertThat(testEventType.getIsAlarm()).isEqualTo(DEFAULT_IS_ALARM);
    }

    @Test
    @Transactional
    void fullUpdateEventTypeWithPatch() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType using partial update
        EventType partialUpdatedEventType = new EventType();
        partialUpdatedEventType.setId(eventType.getId());

        partialUpdatedEventType
            .eventCode(UPDATED_EVENT_CODE)
            .sasCode(UPDATED_SAS_CODE)
            .description(UPDATED_DESCRIPTION)
            .isStorable(UPDATED_IS_STORABLE)
            .isPriority(UPDATED_IS_PRIORITY)
            .procesador(UPDATED_PROCESADOR)
            .isAlarm(UPDATED_IS_ALARM);

        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventType))
            )
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getEventCode()).isEqualTo(UPDATED_EVENT_CODE);
        assertThat(testEventType.getSasCode()).isEqualTo(UPDATED_SAS_CODE);
        assertThat(testEventType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventType.getIsStorable()).isEqualTo(UPDATED_IS_STORABLE);
        assertThat(testEventType.getIsPriority()).isEqualTo(UPDATED_IS_PRIORITY);
        assertThat(testEventType.getProcesador()).isEqualTo(UPDATED_PROCESADOR);
        assertThat(testEventType.getIsAlarm()).isEqualTo(UPDATED_IS_ALARM);
    }

    @Test
    @Transactional
    void patchNonExistingEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(count.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(count.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(count.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeDelete = eventTypeRepository.findAll().size();

        // Delete the eventType
        restEventTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
