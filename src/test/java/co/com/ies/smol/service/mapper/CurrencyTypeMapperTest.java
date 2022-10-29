package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CurrencyTypeMapperTest {

    private CurrencyTypeMapper currencyTypeMapper;

    @BeforeEach
    public void setUp() {
        currencyTypeMapper = new CurrencyTypeMapperImpl();
    }
}
