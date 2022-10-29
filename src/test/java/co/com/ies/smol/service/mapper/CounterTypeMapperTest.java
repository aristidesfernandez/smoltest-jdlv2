package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CounterTypeMapperTest {

    private CounterTypeMapper counterTypeMapper;

    @BeforeEach
    public void setUp() {
        counterTypeMapper = new CounterTypeMapperImpl();
    }
}
