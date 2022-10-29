package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MunicipalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Municipality.class);
        Municipality municipality1 = new Municipality();
        municipality1.setId(1L);
        Municipality municipality2 = new Municipality();
        municipality2.setId(municipality1.getId());
        assertThat(municipality1).isEqualTo(municipality2);
        municipality2.setId(2L);
        assertThat(municipality1).isNotEqualTo(municipality2);
        municipality1.setId(null);
        assertThat(municipality1).isNotEqualTo(municipality2);
    }
}
