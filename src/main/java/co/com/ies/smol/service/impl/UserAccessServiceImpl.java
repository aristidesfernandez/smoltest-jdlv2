package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.UserAccess;
import co.com.ies.smol.repository.UserAccessRepository;
import co.com.ies.smol.service.UserAccessService;
import co.com.ies.smol.service.dto.UserAccessDTO;
import co.com.ies.smol.service.mapper.UserAccessMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAccess}.
 */
@Service
@Transactional
public class UserAccessServiceImpl implements UserAccessService {

    private final Logger log = LoggerFactory.getLogger(UserAccessServiceImpl.class);

    private final UserAccessRepository userAccessRepository;

    private final UserAccessMapper userAccessMapper;

    public UserAccessServiceImpl(UserAccessRepository userAccessRepository, UserAccessMapper userAccessMapper) {
        this.userAccessRepository = userAccessRepository;
        this.userAccessMapper = userAccessMapper;
    }

    @Override
    public UserAccessDTO save(UserAccessDTO userAccessDTO) {
        log.debug("Request to save UserAccess : {}", userAccessDTO);
        UserAccess userAccess = userAccessMapper.toEntity(userAccessDTO);
        userAccess = userAccessRepository.save(userAccess);
        return userAccessMapper.toDto(userAccess);
    }

    @Override
    public UserAccessDTO update(UserAccessDTO userAccessDTO) {
        log.debug("Request to update UserAccess : {}", userAccessDTO);
        UserAccess userAccess = userAccessMapper.toEntity(userAccessDTO);
        userAccess = userAccessRepository.save(userAccess);
        return userAccessMapper.toDto(userAccess);
    }

    @Override
    public Optional<UserAccessDTO> partialUpdate(UserAccessDTO userAccessDTO) {
        log.debug("Request to partially update UserAccess : {}", userAccessDTO);

        return userAccessRepository
            .findById(userAccessDTO.getId())
            .map(existingUserAccess -> {
                userAccessMapper.partialUpdate(existingUserAccess, userAccessDTO);

                return existingUserAccess;
            })
            .map(userAccessRepository::save)
            .map(userAccessMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAccessDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAccesses");
        return userAccessRepository.findAll(pageable).map(userAccessMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccessDTO> findOne(Long id) {
        log.debug("Request to get UserAccess : {}", id);
        return userAccessRepository.findById(id).map(userAccessMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAccess : {}", id);
        userAccessRepository.deleteById(id);
    }
}
