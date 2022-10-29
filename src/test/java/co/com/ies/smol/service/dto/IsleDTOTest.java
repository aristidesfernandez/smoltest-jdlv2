package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsleDTO.class);
        IsleDTO isleDTO1 = new IsleDTO();
        isleDTO1.setId(1L);
        IsleDTO isleDTO2 = new IsleDTO();
        assertThat(isleDTO1).isNotEqualTo(isleDTO2);
        isleDTO2.setId(isleDTO1.getId());
        assertThat(isleDTO1).isEqualTo(isleDTO2);
        isleDTO2.setId(2L);
        assertThat(isleDTO1).isNotEqualTo(isleDTO2);
        isleDTO1.setId(null);
        assertThat(isleDTO1).isNotEqualTo(isleDTO2);
    }
}
