package co.com.ies.smol.web.rest;

import static co.com.ies.smol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.UserAccess;
import co.com.ies.smol.repository.UserAccessRepository;
import co.com.ies.smol.service.criteria.UserAccessCriteria;
import co.com.ies.smol.service.dto.UserAccessDTO;
import co.com.ies.smol.service.mapper.UserAccessMapper;
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
 * Integration tests for the {@link UserAccessResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAccessResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REGISTRATION_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGISTRATION_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REGISTRATION_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/user-accesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAccessRepository userAccessRepository;

    @Autowired
    private UserAccessMapper userAccessMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAccessMockMvc;

    private UserAccess userAccess;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccess createEntity(EntityManager em) {
        UserAccess userAccess = new UserAccess()
            .username(DEFAULT_USERNAME)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .registrationAt(DEFAULT_REGISTRATION_AT);
        return userAccess;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccess createUpdatedEntity(EntityManager em) {
        UserAccess userAccess = new UserAccess()
            .username(UPDATED_USERNAME)
            .ipAddress(UPDATED_IP_ADDRESS)
            .registrationAt(UPDATED_REGISTRATION_AT);
        return userAccess;
    }

    @BeforeEach
    public void initTest() {
        userAccess = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAccess() throws Exception {
        int databaseSizeBeforeCreate = userAccessRepository.findAll().size();
        // Create the UserAccess
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);
        restUserAccessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccessDTO)))
            .andExpect(status().isCreated());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeCreate + 1);
        UserAccess testUserAccess = userAccessList.get(userAccessList.size() - 1);
        assertThat(testUserAccess.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserAccess.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testUserAccess.getRegistrationAt()).isEqualTo(DEFAULT_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void createUserAccessWithExistingId() throws Exception {
        // Create the UserAccess with an existing ID
        userAccess.setId(1L);
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        int databaseSizeBeforeCreate = userAccessRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAccessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccessRepository.findAll().size();
        // set the field null
        userAccess.setUsername(null);

        // Create the UserAccess, which fails.
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        restUserAccessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccessDTO)))
            .andExpect(status().isBadRequest());

        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIpAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccessRepository.findAll().size();
        // set the field null
        userAccess.setIpAddress(null);

        // Create the UserAccess, which fails.
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        restUserAccessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccessDTO)))
            .andExpect(status().isBadRequest());

        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegistrationAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccessRepository.findAll().size();
        // set the field null
        userAccess.setRegistrationAt(null);

        // Create the UserAccess, which fails.
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        restUserAccessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccessDTO)))
            .andExpect(status().isBadRequest());

        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAccesses() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccess.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].registrationAt").value(hasItem(sameInstant(DEFAULT_REGISTRATION_AT))));
    }

    @Test
    @Transactional
    void getUserAccess() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get the userAccess
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL_ID, userAccess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAccess.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.registrationAt").value(sameInstant(DEFAULT_REGISTRATION_AT)));
    }

    @Test
    @Transactional
    void getUserAccessesByIdFiltering() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        Long id = userAccess.getId();

        defaultUserAccessShouldBeFound("id.equals=" + id);
        defaultUserAccessShouldNotBeFound("id.notEquals=" + id);

        defaultUserAccessShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserAccessShouldNotBeFound("id.greaterThan=" + id);

        defaultUserAccessShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserAccessShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserAccessesByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where username equals to DEFAULT_USERNAME
        defaultUserAccessShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the userAccessList where username equals to UPDATED_USERNAME
        defaultUserAccessShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUserAccessesByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultUserAccessShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the userAccessList where username equals to UPDATED_USERNAME
        defaultUserAccessShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUserAccessesByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where username is not null
        defaultUserAccessShouldBeFound("username.specified=true");

        // Get all the userAccessList where username is null
        defaultUserAccessShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAccessesByUsernameContainsSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where username contains DEFAULT_USERNAME
        defaultUserAccessShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the userAccessList where username contains UPDATED_USERNAME
        defaultUserAccessShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUserAccessesByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where username does not contain DEFAULT_USERNAME
        defaultUserAccessShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the userAccessList where username does not contain UPDATED_USERNAME
        defaultUserAccessShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress equals to DEFAULT_IP_ADDRESS
        defaultUserAccessShouldBeFound("ipAddress.equals=" + DEFAULT_IP_ADDRESS);

        // Get all the userAccessList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultUserAccessShouldNotBeFound("ipAddress.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress in DEFAULT_IP_ADDRESS or UPDATED_IP_ADDRESS
        defaultUserAccessShouldBeFound("ipAddress.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS);

        // Get all the userAccessList where ipAddress equals to UPDATED_IP_ADDRESS
        defaultUserAccessShouldNotBeFound("ipAddress.in=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress is not null
        defaultUserAccessShouldBeFound("ipAddress.specified=true");

        // Get all the userAccessList where ipAddress is null
        defaultUserAccessShouldNotBeFound("ipAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressContainsSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress contains DEFAULT_IP_ADDRESS
        defaultUserAccessShouldBeFound("ipAddress.contains=" + DEFAULT_IP_ADDRESS);

        // Get all the userAccessList where ipAddress contains UPDATED_IP_ADDRESS
        defaultUserAccessShouldNotBeFound("ipAddress.contains=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressNotContainsSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress does not contain DEFAULT_IP_ADDRESS
        defaultUserAccessShouldNotBeFound("ipAddress.doesNotContain=" + DEFAULT_IP_ADDRESS);

        // Get all the userAccessList where ipAddress does not contain UPDATED_IP_ADDRESS
        defaultUserAccessShouldBeFound("ipAddress.doesNotContain=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByRegistrationAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where registrationAt equals to DEFAULT_REGISTRATION_AT
        defaultUserAccessShouldBeFound("registrationAt.equals=" + DEFAULT_REGISTRATION_AT);

        // Get all the userAccessList where registrationAt equals to UPDATED_REGISTRATION_AT
        defaultUserAccessShouldNotBeFound("registrationAt.equals=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllUserAccessesByRegistrationAtIsInShouldWork() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where registrationAt in DEFAULT_REGISTRATION_AT or UPDATED_REGISTRATION_AT
        defaultUserAccessShouldBeFound("registrationAt.in=" + DEFAULT_REGISTRATION_AT + "," + UPDATED_REGISTRATION_AT);

        // Get all the userAccessList where registrationAt equals to UPDATED_REGISTRATION_AT
        defaultUserAccessShouldNotBeFound("registrationAt.in=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllUserAccessesByRegistrationAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where registrationAt is not null
        defaultUserAccessShouldBeFound("registrationAt.specified=true");

        // Get all the userAccessList where registrationAt is null
        defaultUserAccessShouldNotBeFound("registrationAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAccessesByRegistrationAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where registrationAt is greater than or equal to DEFAULT_REGISTRATION_AT
        defaultUserAccessShouldBeFound("registrationAt.greaterThanOrEqual=" + DEFAULT_REGISTRATION_AT);

        // Get all the userAccessList where registrationAt is greater than or equal to UPDATED_REGISTRATION_AT
        defaultUserAccessShouldNotBeFound("registrationAt.greaterThanOrEqual=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllUserAccessesByRegistrationAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where registrationAt is less than or equal to DEFAULT_REGISTRATION_AT
        defaultUserAccessShouldBeFound("registrationAt.lessThanOrEqual=" + DEFAULT_REGISTRATION_AT);

        // Get all the userAccessList where registrationAt is less than or equal to SMALLER_REGISTRATION_AT
        defaultUserAccessShouldNotBeFound("registrationAt.lessThanOrEqual=" + SMALLER_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllUserAccessesByRegistrationAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where registrationAt is less than DEFAULT_REGISTRATION_AT
        defaultUserAccessShouldNotBeFound("registrationAt.lessThan=" + DEFAULT_REGISTRATION_AT);

        // Get all the userAccessList where registrationAt is less than UPDATED_REGISTRATION_AT
        defaultUserAccessShouldBeFound("registrationAt.lessThan=" + UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void getAllUserAccessesByRegistrationAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where registrationAt is greater than DEFAULT_REGISTRATION_AT
        defaultUserAccessShouldNotBeFound("registrationAt.greaterThan=" + DEFAULT_REGISTRATION_AT);

        // Get all the userAccessList where registrationAt is greater than SMALLER_REGISTRATION_AT
        defaultUserAccessShouldBeFound("registrationAt.greaterThan=" + SMALLER_REGISTRATION_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserAccessShouldBeFound(String filter) throws Exception {
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccess.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].registrationAt").value(hasItem(sameInstant(DEFAULT_REGISTRATION_AT))));

        // Check, that the count call also returns 1
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserAccessShouldNotBeFound(String filter) throws Exception {
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserAccess() throws Exception {
        // Get the userAccess
        restUserAccessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAccess() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();

        // Update the userAccess
        UserAccess updatedUserAccess = userAccessRepository.findById(userAccess.getId()).get();
        // Disconnect from session so that the updates on updatedUserAccess are not directly saved in db
        em.detach(updatedUserAccess);
        updatedUserAccess.username(UPDATED_USERNAME).ipAddress(UPDATED_IP_ADDRESS).registrationAt(UPDATED_REGISTRATION_AT);
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(updatedUserAccess);

        restUserAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
        UserAccess testUserAccess = userAccessList.get(userAccessList.size() - 1);
        assertThat(testUserAccess.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAccess.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testUserAccess.getRegistrationAt()).isEqualTo(UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void putNonExistingUserAccess() throws Exception {
        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();
        userAccess.setId(count.incrementAndGet());

        // Create the UserAccess
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAccess() throws Exception {
        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();
        userAccess.setId(count.incrementAndGet());

        // Create the UserAccess
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAccess() throws Exception {
        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();
        userAccess.setId(count.incrementAndGet());

        // Create the UserAccess
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccessDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAccessWithPatch() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();

        // Update the userAccess using partial update
        UserAccess partialUpdatedUserAccess = new UserAccess();
        partialUpdatedUserAccess.setId(userAccess.getId());

        partialUpdatedUserAccess.ipAddress(UPDATED_IP_ADDRESS);

        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccess.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccess))
            )
            .andExpect(status().isOk());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
        UserAccess testUserAccess = userAccessList.get(userAccessList.size() - 1);
        assertThat(testUserAccess.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserAccess.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testUserAccess.getRegistrationAt()).isEqualTo(DEFAULT_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void fullUpdateUserAccessWithPatch() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();

        // Update the userAccess using partial update
        UserAccess partialUpdatedUserAccess = new UserAccess();
        partialUpdatedUserAccess.setId(userAccess.getId());

        partialUpdatedUserAccess.username(UPDATED_USERNAME).ipAddress(UPDATED_IP_ADDRESS).registrationAt(UPDATED_REGISTRATION_AT);

        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccess.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccess))
            )
            .andExpect(status().isOk());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
        UserAccess testUserAccess = userAccessList.get(userAccessList.size() - 1);
        assertThat(testUserAccess.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAccess.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testUserAccess.getRegistrationAt()).isEqualTo(UPDATED_REGISTRATION_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUserAccess() throws Exception {
        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();
        userAccess.setId(count.incrementAndGet());

        // Create the UserAccess
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAccessDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAccessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAccess() throws Exception {
        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();
        userAccess.setId(count.incrementAndGet());

        // Create the UserAccess
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAccessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAccess() throws Exception {
        int databaseSizeBeforeUpdate = userAccessRepository.findAll().size();
        userAccess.setId(count.incrementAndGet());

        // Create the UserAccess
        UserAccessDTO userAccessDTO = userAccessMapper.toDto(userAccess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userAccessDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccess in the database
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAccess() throws Exception {
        // Initialize the database
        userAccessRepository.saveAndFlush(userAccess);

        int databaseSizeBeforeDelete = userAccessRepository.findAll().size();

        // Delete the userAccess
        restUserAccessMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAccess.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAccess> userAccessList = userAccessRepository.findAll();
        assertThat(userAccessList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
