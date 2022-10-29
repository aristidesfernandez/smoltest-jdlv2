package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Establishment;
import co.com.ies.smol.domain.KeyOperatingProperty;
import co.com.ies.smol.domain.OperationalPropertiesEstablishment;
import co.com.ies.smol.repository.OperationalPropertiesEstablishmentRepository;
import co.com.ies.smol.service.criteria.OperationalPropertiesEstablishmentCriteria;
import co.com.ies.smol.service.dto.OperationalPropertiesEstablishmentDTO;
import co.com.ies.smol.service.mapper.OperationalPropertiesEstablishmentMapper;
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
 * Integration tests for the {@link OperationalPropertiesEstablishmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperationalPropertiesEstablishmentResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operational-properties-establishments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperationalPropertiesEstablishmentRepository operationalPropertiesEstablishmentRepository;

    @Autowired
    private OperationalPropertiesEstablishmentMapper operationalPropertiesEstablishmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperationalPropertiesEstablishmentMockMvc;

    private OperationalPropertiesEstablishment operationalPropertiesEstablishment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperationalPropertiesEstablishment createEntity(EntityManager em) {
        OperationalPropertiesEstablishment operationalPropertiesEstablishment = new OperationalPropertiesEstablishment()
            .value(DEFAULT_VALUE);
        // Add required entity
        KeyOperatingProperty keyOperatingProperty;
        if (TestUtil.findAll(em, KeyOperatingProperty.class).isEmpty()) {
            keyOperatingProperty = KeyOperatingPropertyResourceIT.createEntity(em);
            em.persist(keyOperatingProperty);
            em.flush();
        } else {
            keyOperatingProperty = TestUtil.findAll(em, KeyOperatingProperty.class).get(0);
        }
        operationalPropertiesEstablishment.setKeyOperatingProperty(keyOperatingProperty);
        // Add required entity
        Establishment establishment;
        if (TestUtil.findAll(em, Establishment.class).isEmpty()) {
            establishment = EstablishmentResourceIT.createEntity(em);
            em.persist(establishment);
            em.flush();
        } else {
            establishment = TestUtil.findAll(em, Establishment.class).get(0);
        }
        operationalPropertiesEstablishment.setEstablishment(establishment);
        return operationalPropertiesEstablishment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperationalPropertiesEstablishment createUpdatedEntity(EntityManager em) {
        OperationalPropertiesEstablishment operationalPropertiesEstablishment = new OperationalPropertiesEstablishment()
            .value(UPDATED_VALUE);
        // Add required entity
        KeyOperatingProperty keyOperatingProperty;
        if (TestUtil.findAll(em, KeyOperatingProperty.class).isEmpty()) {
            keyOperatingProperty = KeyOperatingPropertyResourceIT.createUpdatedEntity(em);
            em.persist(keyOperatingProperty);
            em.flush();
        } else {
            keyOperatingProperty = TestUtil.findAll(em, KeyOperatingProperty.class).get(0);
        }
        operationalPropertiesEstablishment.setKeyOperatingProperty(keyOperatingProperty);
        // Add required entity
        Establishment establishment;
        if (TestUtil.findAll(em, Establishment.class).isEmpty()) {
            establishment = EstablishmentResourceIT.createUpdatedEntity(em);
            em.persist(establishment);
            em.flush();
        } else {
            establishment = TestUtil.findAll(em, Establishment.class).get(0);
        }
        operationalPropertiesEstablishment.setEstablishment(establishment);
        return operationalPropertiesEstablishment;
    }

    @BeforeEach
    public void initTest() {
        operationalPropertiesEstablishment = createEntity(em);
    }

    @Test
    @Transactional
    void createOperationalPropertiesEstablishment() throws Exception {
        int databaseSizeBeforeCreate = operationalPropertiesEstablishmentRepository.findAll().size();
        // Create the OperationalPropertiesEstablishment
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeCreate + 1);
        OperationalPropertiesEstablishment testOperationalPropertiesEstablishment = operationalPropertiesEstablishmentList.get(
            operationalPropertiesEstablishmentList.size() - 1
        );
        assertThat(testOperationalPropertiesEstablishment.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createOperationalPropertiesEstablishmentWithExistingId() throws Exception {
        // Create the OperationalPropertiesEstablishment with an existing ID
        operationalPropertiesEstablishment.setId(1L);
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );

        int databaseSizeBeforeCreate = operationalPropertiesEstablishmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishments() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        // Get all the operationalPropertiesEstablishmentList
        restOperationalPropertiesEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operationalPropertiesEstablishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getOperationalPropertiesEstablishment() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        // Get the operationalPropertiesEstablishment
        restOperationalPropertiesEstablishmentMockMvc
            .perform(get(ENTITY_API_URL_ID, operationalPropertiesEstablishment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operationalPropertiesEstablishment.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getOperationalPropertiesEstablishmentsByIdFiltering() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        Long id = operationalPropertiesEstablishment.getId();

        defaultOperationalPropertiesEstablishmentShouldBeFound("id.equals=" + id);
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("id.notEquals=" + id);

        defaultOperationalPropertiesEstablishmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("id.greaterThan=" + id);

        defaultOperationalPropertiesEstablishmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishmentsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        // Get all the operationalPropertiesEstablishmentList where value equals to DEFAULT_VALUE
        defaultOperationalPropertiesEstablishmentShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the operationalPropertiesEstablishmentList where value equals to UPDATED_VALUE
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishmentsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        // Get all the operationalPropertiesEstablishmentList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultOperationalPropertiesEstablishmentShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the operationalPropertiesEstablishmentList where value equals to UPDATED_VALUE
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishmentsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        // Get all the operationalPropertiesEstablishmentList where value is not null
        defaultOperationalPropertiesEstablishmentShouldBeFound("value.specified=true");

        // Get all the operationalPropertiesEstablishmentList where value is null
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishmentsByValueContainsSomething() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        // Get all the operationalPropertiesEstablishmentList where value contains DEFAULT_VALUE
        defaultOperationalPropertiesEstablishmentShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the operationalPropertiesEstablishmentList where value contains UPDATED_VALUE
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishmentsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        // Get all the operationalPropertiesEstablishmentList where value does not contain DEFAULT_VALUE
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the operationalPropertiesEstablishmentList where value does not contain UPDATED_VALUE
        defaultOperationalPropertiesEstablishmentShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishmentsByKeyOperatingPropertyIsEqualToSomething() throws Exception {
        KeyOperatingProperty keyOperatingProperty;
        if (TestUtil.findAll(em, KeyOperatingProperty.class).isEmpty()) {
            operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);
            keyOperatingProperty = KeyOperatingPropertyResourceIT.createEntity(em);
        } else {
            keyOperatingProperty = TestUtil.findAll(em, KeyOperatingProperty.class).get(0);
        }
        em.persist(keyOperatingProperty);
        em.flush();
        operationalPropertiesEstablishment.setKeyOperatingProperty(keyOperatingProperty);
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);
        Long keyOperatingPropertyId = keyOperatingProperty.getId();

        // Get all the operationalPropertiesEstablishmentList where keyOperatingProperty equals to keyOperatingPropertyId
        defaultOperationalPropertiesEstablishmentShouldBeFound("keyOperatingPropertyId.equals=" + keyOperatingPropertyId);

        // Get all the operationalPropertiesEstablishmentList where keyOperatingProperty equals to (keyOperatingPropertyId + 1)
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("keyOperatingPropertyId.equals=" + (keyOperatingPropertyId + 1));
    }

    @Test
    @Transactional
    void getAllOperationalPropertiesEstablishmentsByEstablishmentIsEqualToSomething() throws Exception {
        Establishment establishment;
        if (TestUtil.findAll(em, Establishment.class).isEmpty()) {
            operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);
            establishment = EstablishmentResourceIT.createEntity(em);
        } else {
            establishment = TestUtil.findAll(em, Establishment.class).get(0);
        }
        em.persist(establishment);
        em.flush();
        operationalPropertiesEstablishment.setEstablishment(establishment);
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);
        Long establishmentId = establishment.getId();

        // Get all the operationalPropertiesEstablishmentList where establishment equals to establishmentId
        defaultOperationalPropertiesEstablishmentShouldBeFound("establishmentId.equals=" + establishmentId);

        // Get all the operationalPropertiesEstablishmentList where establishment equals to (establishmentId + 1)
        defaultOperationalPropertiesEstablishmentShouldNotBeFound("establishmentId.equals=" + (establishmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperationalPropertiesEstablishmentShouldBeFound(String filter) throws Exception {
        restOperationalPropertiesEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operationalPropertiesEstablishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restOperationalPropertiesEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperationalPropertiesEstablishmentShouldNotBeFound(String filter) throws Exception {
        restOperationalPropertiesEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperationalPropertiesEstablishmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOperationalPropertiesEstablishment() throws Exception {
        // Get the operationalPropertiesEstablishment
        restOperationalPropertiesEstablishmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperationalPropertiesEstablishment() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();

        // Update the operationalPropertiesEstablishment
        OperationalPropertiesEstablishment updatedOperationalPropertiesEstablishment = operationalPropertiesEstablishmentRepository
            .findById(operationalPropertiesEstablishment.getId())
            .get();
        // Disconnect from session so that the updates on updatedOperationalPropertiesEstablishment are not directly saved in db
        em.detach(updatedOperationalPropertiesEstablishment);
        updatedOperationalPropertiesEstablishment.value(UPDATED_VALUE);
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            updatedOperationalPropertiesEstablishment
        );

        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationalPropertiesEstablishmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
        OperationalPropertiesEstablishment testOperationalPropertiesEstablishment = operationalPropertiesEstablishmentList.get(
            operationalPropertiesEstablishmentList.size() - 1
        );
        assertThat(testOperationalPropertiesEstablishment.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingOperationalPropertiesEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();
        operationalPropertiesEstablishment.setId(count.incrementAndGet());

        // Create the OperationalPropertiesEstablishment
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationalPropertiesEstablishmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperationalPropertiesEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();
        operationalPropertiesEstablishment.setId(count.incrementAndGet());

        // Create the OperationalPropertiesEstablishment
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperationalPropertiesEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();
        operationalPropertiesEstablishment.setId(count.incrementAndGet());

        // Create the OperationalPropertiesEstablishment
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperationalPropertiesEstablishmentWithPatch() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();

        // Update the operationalPropertiesEstablishment using partial update
        OperationalPropertiesEstablishment partialUpdatedOperationalPropertiesEstablishment = new OperationalPropertiesEstablishment();
        partialUpdatedOperationalPropertiesEstablishment.setId(operationalPropertiesEstablishment.getId());

        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperationalPropertiesEstablishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperationalPropertiesEstablishment))
            )
            .andExpect(status().isOk());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
        OperationalPropertiesEstablishment testOperationalPropertiesEstablishment = operationalPropertiesEstablishmentList.get(
            operationalPropertiesEstablishmentList.size() - 1
        );
        assertThat(testOperationalPropertiesEstablishment.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateOperationalPropertiesEstablishmentWithPatch() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();

        // Update the operationalPropertiesEstablishment using partial update
        OperationalPropertiesEstablishment partialUpdatedOperationalPropertiesEstablishment = new OperationalPropertiesEstablishment();
        partialUpdatedOperationalPropertiesEstablishment.setId(operationalPropertiesEstablishment.getId());

        partialUpdatedOperationalPropertiesEstablishment.value(UPDATED_VALUE);

        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperationalPropertiesEstablishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperationalPropertiesEstablishment))
            )
            .andExpect(status().isOk());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
        OperationalPropertiesEstablishment testOperationalPropertiesEstablishment = operationalPropertiesEstablishmentList.get(
            operationalPropertiesEstablishmentList.size() - 1
        );
        assertThat(testOperationalPropertiesEstablishment.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingOperationalPropertiesEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();
        operationalPropertiesEstablishment.setId(count.incrementAndGet());

        // Create the OperationalPropertiesEstablishment
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operationalPropertiesEstablishmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperationalPropertiesEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();
        operationalPropertiesEstablishment.setId(count.incrementAndGet());

        // Create the OperationalPropertiesEstablishment
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperationalPropertiesEstablishment() throws Exception {
        int databaseSizeBeforeUpdate = operationalPropertiesEstablishmentRepository.findAll().size();
        operationalPropertiesEstablishment.setId(count.incrementAndGet());

        // Create the OperationalPropertiesEstablishment
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentMapper.toDto(
            operationalPropertiesEstablishment
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalPropertiesEstablishmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationalPropertiesEstablishmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperationalPropertiesEstablishment in the database
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperationalPropertiesEstablishment() throws Exception {
        // Initialize the database
        operationalPropertiesEstablishmentRepository.saveAndFlush(operationalPropertiesEstablishment);

        int databaseSizeBeforeDelete = operationalPropertiesEstablishmentRepository.findAll().size();

        // Delete the operationalPropertiesEstablishment
        restOperationalPropertiesEstablishmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, operationalPropertiesEstablishment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OperationalPropertiesEstablishment> operationalPropertiesEstablishmentList = operationalPropertiesEstablishmentRepository.findAll();
        assertThat(operationalPropertiesEstablishmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
