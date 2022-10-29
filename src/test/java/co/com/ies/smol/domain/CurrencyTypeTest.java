package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CurrencyTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyType.class);
        CurrencyType currencyType1 = new CurrencyType();
        currencyType1.setId(1L);
        CurrencyType currencyType2 = new CurrencyType();
        currencyType2.setId(currencyType1.getId());
        assertThat(currencyType1).isEqualTo(currencyType2);
        currencyType2.setId(2L);
        assertThat(currencyType1).isNotEqualTo(currencyType2);
        currencyType1.setId(null);
        assertThat(currencyType1).isNotEqualTo(currencyType2);
    }
}
