package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationalPropertiesEstablishmentMapperTest {

    private OperationalPropertiesEstablishmentMapper operationalPropertiesEstablishmentMapper;

    @BeforeEach
    public void setUp() {
        operationalPropertiesEstablishmentMapper = new OperationalPropertiesEstablishmentMapperImpl();
    }
}
