package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Isle.class);
        Isle isle1 = new Isle();
        isle1.setId(1L);
        Isle isle2 = new Isle();
        isle2.setId(isle1.getId());
        assertThat(isle1).isEqualTo(isle2);
        isle2.setId(2L);
        assertThat(isle1).isNotEqualTo(isle2);
        isle1.setId(null);
        assertThat(isle1).isNotEqualTo(isle2);
    }
}
