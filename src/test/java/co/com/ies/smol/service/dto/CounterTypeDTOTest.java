package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CounterTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounterTypeDTO.class);
        CounterTypeDTO counterTypeDTO1 = new CounterTypeDTO();
        counterTypeDTO1.setCounterCode("id1");
        CounterTypeDTO counterTypeDTO2 = new CounterTypeDTO();
        assertThat(counterTypeDTO1).isNotEqualTo(counterTypeDTO2);
        counterTypeDTO2.setCounterCode(counterTypeDTO1.getCounterCode());
        assertThat(counterTypeDTO1).isEqualTo(counterTypeDTO2);
        counterTypeDTO2.setCounterCode("id2");
        assertThat(counterTypeDTO1).isNotEqualTo(counterTypeDTO2);
        counterTypeDTO1.setCounterCode(null);
        assertThat(counterTypeDTO1).isNotEqualTo(counterTypeDTO2);
    }
}
