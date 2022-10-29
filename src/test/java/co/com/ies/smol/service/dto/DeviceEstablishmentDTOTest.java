package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeviceEstablishmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceEstablishmentDTO.class);
        DeviceEstablishmentDTO deviceEstablishmentDTO1 = new DeviceEstablishmentDTO();
        deviceEstablishmentDTO1.setId(UUID.randomUUID());
        DeviceEstablishmentDTO deviceEstablishmentDTO2 = new DeviceEstablishmentDTO();
        assertThat(deviceEstablishmentDTO1).isNotEqualTo(deviceEstablishmentDTO2);
        deviceEstablishmentDTO2.setId(deviceEstablishmentDTO1.getId());
        assertThat(deviceEstablishmentDTO1).isEqualTo(deviceEstablishmentDTO2);
        deviceEstablishmentDTO2.setId(UUID.randomUUID());
        assertThat(deviceEstablishmentDTO1).isNotEqualTo(deviceEstablishmentDTO2);
        deviceEstablishmentDTO1.setId(null);
        assertThat(deviceEstablishmentDTO1).isNotEqualTo(deviceEstablishmentDTO2);
    }
}
