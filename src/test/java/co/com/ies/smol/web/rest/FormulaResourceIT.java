package co.com.ies.smol.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.smol.IntegrationTest;
import co.com.ies.smol.domain.Formula;
import co.com.ies.smol.repository.FormulaRepository;
import co.com.ies.smol.service.criteria.FormulaCriteria;
import co.com.ies.smol.service.dto.FormulaDTO;
import co.com.ies.smol.service.mapper.FormulaMapper;
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
 * Integration tests for the {@link FormulaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormulaResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EXPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_EXPRESSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/formulas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private FormulaMapper formulaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormulaMockMvc;

    private Formula formula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createEntity(EntityManager em) {
        Formula formula = new Formula().description(DEFAULT_DESCRIPTION).expression(DEFAULT_EXPRESSION);
        return formula;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createUpdatedEntity(EntityManager em) {
        Formula formula = new Formula().description(UPDATED_DESCRIPTION).expression(UPDATED_EXPRESSION);
        return formula;
    }

    @BeforeEach
    public void initTest() {
        formula = createEntity(em);
    }

    @Test
    @Transactional
    void createFormula() throws Exception {
        int databaseSizeBeforeCreate = formulaRepository.findAll().size();
        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);
        restFormulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isCreated());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate + 1);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormula.getExpression()).isEqualTo(DEFAULT_EXPRESSION);
    }

    @Test
    @Transactional
    void createFormulaWithExistingId() throws Exception {
        // Create the Formula with an existing ID
        formula.setId(1L);
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        int databaseSizeBeforeCreate = formulaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormulas() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].expression").value(hasItem(DEFAULT_EXPRESSION)));
    }

    @Test
    @Transactional
    void getFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get the formula
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL_ID, formula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formula.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.expression").value(DEFAULT_EXPRESSION));
    }

    @Test
    @Transactional
    void getFormulasByIdFiltering() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        Long id = formula.getId();

        defaultFormulaShouldBeFound("id.equals=" + id);
        defaultFormulaShouldNotBeFound("id.notEquals=" + id);

        defaultFormulaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormulaShouldNotBeFound("id.greaterThan=" + id);

        defaultFormulaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormulaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormulasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description equals to DEFAULT_DESCRIPTION
        defaultFormulaShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the formulaList where description equals to UPDATED_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormulasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFormulaShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the formulaList where description equals to UPDATED_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormulasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description is not null
        defaultFormulaShouldBeFound("description.specified=true");

        // Get all the formulaList where description is null
        defaultFormulaShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFormulasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description contains DEFAULT_DESCRIPTION
        defaultFormulaShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the formulaList where description contains UPDATED_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormulasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description does not contain DEFAULT_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the formulaList where description does not contain UPDATED_DESCRIPTION
        defaultFormulaShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormulasByExpressionIsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where expression equals to DEFAULT_EXPRESSION
        defaultFormulaShouldBeFound("expression.equals=" + DEFAULT_EXPRESSION);

        // Get all the formulaList where expression equals to UPDATED_EXPRESSION
        defaultFormulaShouldNotBeFound("expression.equals=" + UPDATED_EXPRESSION);
    }

    @Test
    @Transactional
    void getAllFormulasByExpressionIsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where expression in DEFAULT_EXPRESSION or UPDATED_EXPRESSION
        defaultFormulaShouldBeFound("expression.in=" + DEFAULT_EXPRESSION + "," + UPDATED_EXPRESSION);

        // Get all the formulaList where expression equals to UPDATED_EXPRESSION
        defaultFormulaShouldNotBeFound("expression.in=" + UPDATED_EXPRESSION);
    }

    @Test
    @Transactional
    void getAllFormulasByExpressionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where expression is not null
        defaultFormulaShouldBeFound("expression.specified=true");

        // Get all the formulaList where expression is null
        defaultFormulaShouldNotBeFound("expression.specified=false");
    }

    @Test
    @Transactional
    void getAllFormulasByExpressionContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where expression contains DEFAULT_EXPRESSION
        defaultFormulaShouldBeFound("expression.contains=" + DEFAULT_EXPRESSION);

        // Get all the formulaList where expression contains UPDATED_EXPRESSION
        defaultFormulaShouldNotBeFound("expression.contains=" + UPDATED_EXPRESSION);
    }

    @Test
    @Transactional
    void getAllFormulasByExpressionNotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where expression does not contain DEFAULT_EXPRESSION
        defaultFormulaShouldNotBeFound("expression.doesNotContain=" + DEFAULT_EXPRESSION);

        // Get all the formulaList where expression does not contain UPDATED_EXPRESSION
        defaultFormulaShouldBeFound("expression.doesNotContain=" + UPDATED_EXPRESSION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormulaShouldBeFound(String filter) throws Exception {
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].expression").value(hasItem(DEFAULT_EXPRESSION)));

        // Check, that the count call also returns 1
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormulaShouldNotBeFound(String filter) throws Exception {
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormula() throws Exception {
        // Get the formula
        restFormulaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula
        Formula updatedFormula = formulaRepository.findById(formula.getId()).get();
        // Disconnect from session so that the updates on updatedFormula are not directly saved in db
        em.detach(updatedFormula);
        updatedFormula.description(UPDATED_DESCRIPTION).expression(UPDATED_EXPRESSION);
        FormulaDTO formulaDTO = formulaMapper.toDto(updatedFormula);

        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formulaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormula.getExpression()).isEqualTo(UPDATED_EXPRESSION);
    }

    @Test
    @Transactional
    void putNonExistingFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formulaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormulaWithPatch() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula using partial update
        Formula partialUpdatedFormula = new Formula();
        partialUpdatedFormula.setId(formula.getId());

        partialUpdatedFormula.description(UPDATED_DESCRIPTION);

        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormula))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormula.getExpression()).isEqualTo(DEFAULT_EXPRESSION);
    }

    @Test
    @Transactional
    void fullUpdateFormulaWithPatch() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula using partial update
        Formula partialUpdatedFormula = new Formula();
        partialUpdatedFormula.setId(formula.getId());

        partialUpdatedFormula.description(UPDATED_DESCRIPTION).expression(UPDATED_EXPRESSION);

        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormula))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormula.getExpression()).isEqualTo(UPDATED_EXPRESSION);
    }

    @Test
    @Transactional
    void patchNonExistingFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formulaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeDelete = formulaRepository.findAll().size();

        // Delete the formula
        restFormulaMockMvc
            .perform(delete(ENTITY_API_URL_ID, formula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
