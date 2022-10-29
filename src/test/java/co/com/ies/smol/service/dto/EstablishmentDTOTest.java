package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstablishmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstablishmentDTO.class);
        EstablishmentDTO establishmentDTO1 = new EstablishmentDTO();
        establishmentDTO1.setId(1L);
        EstablishmentDTO establishmentDTO2 = new EstablishmentDTO();
        assertThat(establishmentDTO1).isNotEqualTo(establishmentDTO2);
        establishmentDTO2.setId(establishmentDTO1.getId());
        assertThat(establishmentDTO1).isEqualTo(establishmentDTO2);
        establishmentDTO2.setId(2L);
        assertThat(establishmentDTO1).isNotEqualTo(establishmentDTO2);
        establishmentDTO1.setId(null);
        assertThat(establishmentDTO1).isNotEqualTo(establishmentDTO2);
    }
}
