package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperationalPropertiesEstablishmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationalPropertiesEstablishmentDTO.class);
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO1 = new OperationalPropertiesEstablishmentDTO();
        operationalPropertiesEstablishmentDTO1.setId(1L);
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO2 = new OperationalPropertiesEstablishmentDTO();
        assertThat(operationalPropertiesEstablishmentDTO1).isNotEqualTo(operationalPropertiesEstablishmentDTO2);
        operationalPropertiesEstablishmentDTO2.setId(operationalPropertiesEstablishmentDTO1.getId());
        assertThat(operationalPropertiesEstablishmentDTO1).isEqualTo(operationalPropertiesEstablishmentDTO2);
        operationalPropertiesEstablishmentDTO2.setId(2L);
        assertThat(operationalPropertiesEstablishmentDTO1).isNotEqualTo(operationalPropertiesEstablishmentDTO2);
        operationalPropertiesEstablishmentDTO1.setId(null);
        assertThat(operationalPropertiesEstablishmentDTO1).isNotEqualTo(operationalPropertiesEstablishmentDTO2);
    }
}
