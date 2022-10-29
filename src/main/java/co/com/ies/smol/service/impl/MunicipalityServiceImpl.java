package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.repository.MunicipalityRepository;
import co.com.ies.smol.service.MunicipalityService;
import co.com.ies.smol.service.dto.MunicipalityDTO;
import co.com.ies.smol.service.mapper.MunicipalityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Municipality}.
 */
@Service
@Transactional
public class MunicipalityServiceImpl implements MunicipalityService {

    private final Logger log = LoggerFactory.getLogger(MunicipalityServiceImpl.class);

    private final MunicipalityRepository municipalityRepository;

    private final MunicipalityMapper municipalityMapper;

    public MunicipalityServiceImpl(MunicipalityRepository municipalityRepository, MunicipalityMapper municipalityMapper) {
        this.municipalityRepository = municipalityRepository;
        this.municipalityMapper = municipalityMapper;
    }

    @Override
    public MunicipalityDTO save(MunicipalityDTO municipalityDTO) {
        log.debug("Request to save Municipality : {}", municipalityDTO);
        Municipality municipality = municipalityMapper.toEntity(municipalityDTO);
        municipality = municipalityRepository.save(municipality);
        return municipalityMapper.toDto(municipality);
    }

    @Override
    public MunicipalityDTO update(MunicipalityDTO municipalityDTO) {
        log.debug("Request to update Municipality : {}", municipalityDTO);
        Municipality municipality = municipalityMapper.toEntity(municipalityDTO);
        municipality = municipalityRepository.save(municipality);
        return municipalityMapper.toDto(municipality);
    }

    @Override
    public Optional<MunicipalityDTO> partialUpdate(MunicipalityDTO municipalityDTO) {
        log.debug("Request to partially update Municipality : {}", municipalityDTO);

        return municipalityRepository
            .findById(municipalityDTO.getId())
            .map(existingMunicipality -> {
                municipalityMapper.partialUpdate(existingMunicipality, municipalityDTO);

                return existingMunicipality;
            })
            .map(municipalityRepository::save)
            .map(municipalityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MunicipalityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Municipalities");
        return municipalityRepository.findAll(pageable).map(municipalityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MunicipalityDTO> findOne(Long id) {
        log.debug("Request to get Municipality : {}", id);
        return municipalityRepository.findById(id).map(municipalityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Municipality : {}", id);
        municipalityRepository.deleteById(id);
    }
}
