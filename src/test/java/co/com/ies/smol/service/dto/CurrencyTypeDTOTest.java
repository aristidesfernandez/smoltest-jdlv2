package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CurrencyTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyTypeDTO.class);
        CurrencyTypeDTO currencyTypeDTO1 = new CurrencyTypeDTO();
        currencyTypeDTO1.setId(1L);
        CurrencyTypeDTO currencyTypeDTO2 = new CurrencyTypeDTO();
        assertThat(currencyTypeDTO1).isNotEqualTo(currencyTypeDTO2);
        currencyTypeDTO2.setId(currencyTypeDTO1.getId());
        assertThat(currencyTypeDTO1).isEqualTo(currencyTypeDTO2);
        currencyTypeDTO2.setId(2L);
        assertThat(currencyTypeDTO1).isNotEqualTo(currencyTypeDTO2);
        currencyTypeDTO1.setId(null);
        assertThat(currencyTypeDTO1).isNotEqualTo(currencyTypeDTO2);
    }
}
