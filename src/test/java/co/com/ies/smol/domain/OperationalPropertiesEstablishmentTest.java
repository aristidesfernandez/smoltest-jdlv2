package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperationalPropertiesEstablishmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationalPropertiesEstablishment.class);
        OperationalPropertiesEstablishment operationalPropertiesEstablishment1 = new OperationalPropertiesEstablishment();
        operationalPropertiesEstablishment1.setId(1L);
        OperationalPropertiesEstablishment operationalPropertiesEstablishment2 = new OperationalPropertiesEstablishment();
        operationalPropertiesEstablishment2.setId(operationalPropertiesEstablishment1.getId());
        assertThat(operationalPropertiesEstablishment1).isEqualTo(operationalPropertiesEstablishment2);
        operationalPropertiesEstablishment2.setId(2L);
        assertThat(operationalPropertiesEstablishment1).isNotEqualTo(operationalPropertiesEstablishment2);
        operationalPropertiesEstablishment1.setId(null);
        assertThat(operationalPropertiesEstablishment1).isNotEqualTo(operationalPropertiesEstablishment2);
    }
}
