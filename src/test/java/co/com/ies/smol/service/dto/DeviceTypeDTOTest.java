package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceTypeDTO.class);
        DeviceTypeDTO deviceTypeDTO1 = new DeviceTypeDTO();
        deviceTypeDTO1.setId(1L);
        DeviceTypeDTO deviceTypeDTO2 = new DeviceTypeDTO();
        assertThat(deviceTypeDTO1).isNotEqualTo(deviceTypeDTO2);
        deviceTypeDTO2.setId(deviceTypeDTO1.getId());
        assertThat(deviceTypeDTO1).isEqualTo(deviceTypeDTO2);
        deviceTypeDTO2.setId(2L);
        assertThat(deviceTypeDTO1).isNotEqualTo(deviceTypeDTO2);
        deviceTypeDTO1.setId(null);
        assertThat(deviceTypeDTO1).isNotEqualTo(deviceTypeDTO2);
    }
}
