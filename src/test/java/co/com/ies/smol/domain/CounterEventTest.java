package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CounterEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounterEvent.class);
        CounterEvent counterEvent1 = new CounterEvent();
        counterEvent1.setId(UUID.randomUUID());
        CounterEvent counterEvent2 = new CounterEvent();
        counterEvent2.setId(counterEvent1.getId());
        assertThat(counterEvent1).isEqualTo(counterEvent2);
        counterEvent2.setId(UUID.randomUUID());
        assertThat(counterEvent1).isNotEqualTo(counterEvent2);
        counterEvent1.setId(null);
        assertThat(counterEvent1).isNotEqualTo(counterEvent2);
    }
}
