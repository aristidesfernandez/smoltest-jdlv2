package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceInterfaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceInterface.class);
        DeviceInterface deviceInterface1 = new DeviceInterface();
        deviceInterface1.setId(1L);
        DeviceInterface deviceInterface2 = new DeviceInterface();
        deviceInterface2.setId(deviceInterface1.getId());
        assertThat(deviceInterface1).isEqualTo(deviceInterface2);
        deviceInterface2.setId(2L);
        assertThat(deviceInterface1).isNotEqualTo(deviceInterface2);
        deviceInterface1.setId(null);
        assertThat(deviceInterface1).isNotEqualTo(deviceInterface2);
    }
}
