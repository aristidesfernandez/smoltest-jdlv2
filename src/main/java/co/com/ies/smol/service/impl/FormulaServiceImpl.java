package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.Formula;
import co.com.ies.smol.repository.FormulaRepository;
import co.com.ies.smol.service.FormulaService;
import co.com.ies.smol.service.dto.FormulaDTO;
import co.com.ies.smol.service.mapper.FormulaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Formula}.
 */
@Service
@Transactional
public class FormulaServiceImpl implements FormulaService {

    private final Logger log = LoggerFactory.getLogger(FormulaServiceImpl.class);

    private final FormulaRepository formulaRepository;

    private final FormulaMapper formulaMapper;

    public FormulaServiceImpl(FormulaRepository formulaRepository, FormulaMapper formulaMapper) {
        this.formulaRepository = formulaRepository;
        this.formulaMapper = formulaMapper;
    }

    @Override
    public FormulaDTO save(FormulaDTO formulaDTO) {
        log.debug("Request to save Formula : {}", formulaDTO);
        Formula formula = formulaMapper.toEntity(formulaDTO);
        formula = formulaRepository.save(formula);
        return formulaMapper.toDto(formula);
    }

    @Override
    public FormulaDTO update(FormulaDTO formulaDTO) {
        log.debug("Request to update Formula : {}", formulaDTO);
        Formula formula = formulaMapper.toEntity(formulaDTO);
        formula = formulaRepository.save(formula);
        return formulaMapper.toDto(formula);
    }

    @Override
    public Optional<FormulaDTO> partialUpdate(FormulaDTO formulaDTO) {
        log.debug("Request to partially update Formula : {}", formulaDTO);

        return formulaRepository
            .findById(formulaDTO.getId())
            .map(existingFormula -> {
                formulaMapper.partialUpdate(existingFormula, formulaDTO);

                return existingFormula;
            })
            .map(formulaRepository::save)
            .map(formulaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormulaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formulas");
        return formulaRepository.findAll(pageable).map(formulaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormulaDTO> findOne(Long id) {
        log.debug("Request to get Formula : {}", id);
        return formulaRepository.findById(id).map(formulaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formula : {}", id);
        formulaRepository.deleteById(id);
    }
}
