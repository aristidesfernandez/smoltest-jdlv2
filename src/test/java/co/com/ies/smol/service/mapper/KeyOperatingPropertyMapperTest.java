package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeyOperatingPropertyMapperTest {

    private KeyOperatingPropertyMapper keyOperatingPropertyMapper;

    @BeforeEach
    public void setUp() {
        keyOperatingPropertyMapper = new KeyOperatingPropertyMapperImpl();
    }
}
