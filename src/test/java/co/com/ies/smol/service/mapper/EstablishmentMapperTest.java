package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstablishmentMapperTest {

    private EstablishmentMapper establishmentMapper;

    @BeforeEach
    public void setUp() {
        establishmentMapper = new EstablishmentMapperImpl();
    }
}
