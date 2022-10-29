package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceInterfaceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceInterfaceDTO.class);
        DeviceInterfaceDTO deviceInterfaceDTO1 = new DeviceInterfaceDTO();
        deviceInterfaceDTO1.setId(1L);
        DeviceInterfaceDTO deviceInterfaceDTO2 = new DeviceInterfaceDTO();
        assertThat(deviceInterfaceDTO1).isNotEqualTo(deviceInterfaceDTO2);
        deviceInterfaceDTO2.setId(deviceInterfaceDTO1.getId());
        assertThat(deviceInterfaceDTO1).isEqualTo(deviceInterfaceDTO2);
        deviceInterfaceDTO2.setId(2L);
        assertThat(deviceInterfaceDTO1).isNotEqualTo(deviceInterfaceDTO2);
        deviceInterfaceDTO1.setId(null);
        assertThat(deviceInterfaceDTO1).isNotEqualTo(deviceInterfaceDTO2);
    }
}
