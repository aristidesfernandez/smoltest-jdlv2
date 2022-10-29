package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KeyOperatingPropertyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyOperatingProperty.class);
        KeyOperatingProperty keyOperatingProperty1 = new KeyOperatingProperty();
        keyOperatingProperty1.setId(1L);
        KeyOperatingProperty keyOperatingProperty2 = new KeyOperatingProperty();
        keyOperatingProperty2.setId(keyOperatingProperty1.getId());
        assertThat(keyOperatingProperty1).isEqualTo(keyOperatingProperty2);
        keyOperatingProperty2.setId(2L);
        assertThat(keyOperatingProperty1).isNotEqualTo(keyOperatingProperty2);
        keyOperatingProperty1.setId(null);
        assertThat(keyOperatingProperty1).isNotEqualTo(keyOperatingProperty2);
    }
}
