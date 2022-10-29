package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EventDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDevice.class);
        EventDevice eventDevice1 = new EventDevice();
        eventDevice1.setId(UUID.randomUUID());
        EventDevice eventDevice2 = new EventDevice();
        eventDevice2.setId(eventDevice1.getId());
        assertThat(eventDevice1).isEqualTo(eventDevice2);
        eventDevice2.setId(UUID.randomUUID());
        assertThat(eventDevice1).isNotEqualTo(eventDevice2);
        eventDevice1.setId(null);
        assertThat(eventDevice1).isNotEqualTo(eventDevice2);
    }
}
