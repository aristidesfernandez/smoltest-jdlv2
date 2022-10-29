package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KeyOperatingPropertyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyOperatingPropertyDTO.class);
        KeyOperatingPropertyDTO keyOperatingPropertyDTO1 = new KeyOperatingPropertyDTO();
        keyOperatingPropertyDTO1.setId(1L);
        KeyOperatingPropertyDTO keyOperatingPropertyDTO2 = new KeyOperatingPropertyDTO();
        assertThat(keyOperatingPropertyDTO1).isNotEqualTo(keyOperatingPropertyDTO2);
        keyOperatingPropertyDTO2.setId(keyOperatingPropertyDTO1.getId());
        assertThat(keyOperatingPropertyDTO1).isEqualTo(keyOperatingPropertyDTO2);
        keyOperatingPropertyDTO2.setId(2L);
        assertThat(keyOperatingPropertyDTO1).isNotEqualTo(keyOperatingPropertyDTO2);
        keyOperatingPropertyDTO1.setId(null);
        assertThat(keyOperatingPropertyDTO1).isNotEqualTo(keyOperatingPropertyDTO2);
    }
}
