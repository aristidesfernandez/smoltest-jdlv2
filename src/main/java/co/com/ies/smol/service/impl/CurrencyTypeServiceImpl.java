package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.CurrencyType;
import co.com.ies.smol.repository.CurrencyTypeRepository;
import co.com.ies.smol.service.CurrencyTypeService;
import co.com.ies.smol.service.dto.CurrencyTypeDTO;
import co.com.ies.smol.service.mapper.CurrencyTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CurrencyType}.
 */
@Service
@Transactional
public class CurrencyTypeServiceImpl implements CurrencyTypeService {

    private final Logger log = LoggerFactory.getLogger(CurrencyTypeServiceImpl.class);

    private final CurrencyTypeRepository currencyTypeRepository;

    private final CurrencyTypeMapper currencyTypeMapper;

    public CurrencyTypeServiceImpl(CurrencyTypeRepository currencyTypeRepository, CurrencyTypeMapper currencyTypeMapper) {
        this.currencyTypeRepository = currencyTypeRepository;
        this.currencyTypeMapper = currencyTypeMapper;
    }

    @Override
    public CurrencyTypeDTO save(CurrencyTypeDTO currencyTypeDTO) {
        log.debug("Request to save CurrencyType : {}", currencyTypeDTO);
        CurrencyType currencyType = currencyTypeMapper.toEntity(currencyTypeDTO);
        currencyType = currencyTypeRepository.save(currencyType);
        return currencyTypeMapper.toDto(currencyType);
    }

    @Override
    public CurrencyTypeDTO update(CurrencyTypeDTO currencyTypeDTO) {
        log.debug("Request to update CurrencyType : {}", currencyTypeDTO);
        CurrencyType currencyType = currencyTypeMapper.toEntity(currencyTypeDTO);
        currencyType = currencyTypeRepository.save(currencyType);
        return currencyTypeMapper.toDto(currencyType);
    }

    @Override
    public Optional<CurrencyTypeDTO> partialUpdate(CurrencyTypeDTO currencyTypeDTO) {
        log.debug("Request to partially update CurrencyType : {}", currencyTypeDTO);

        return currencyTypeRepository
            .findById(currencyTypeDTO.getId())
            .map(existingCurrencyType -> {
                currencyTypeMapper.partialUpdate(existingCurrencyType, currencyTypeDTO);

                return existingCurrencyType;
            })
            .map(currencyTypeRepository::save)
            .map(currencyTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurrencyTypes");
        return currencyTypeRepository.findAll(pageable).map(currencyTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyTypeDTO> findOne(Long id) {
        log.debug("Request to get CurrencyType : {}", id);
        return currencyTypeRepository.findById(id).map(currencyTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CurrencyType : {}", id);
        currencyTypeRepository.deleteById(id);
    }
}
