package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeviceEstablishmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceEstablishment.class);
        DeviceEstablishment deviceEstablishment1 = new DeviceEstablishment();
        deviceEstablishment1.setId(UUID.randomUUID());
        DeviceEstablishment deviceEstablishment2 = new DeviceEstablishment();
        deviceEstablishment2.setId(deviceEstablishment1.getId());
        assertThat(deviceEstablishment1).isEqualTo(deviceEstablishment2);
        deviceEstablishment2.setId(UUID.randomUUID());
        assertThat(deviceEstablishment1).isNotEqualTo(deviceEstablishment2);
        deviceEstablishment1.setId(null);
        assertThat(deviceEstablishment1).isNotEqualTo(deviceEstablishment2);
    }
}
