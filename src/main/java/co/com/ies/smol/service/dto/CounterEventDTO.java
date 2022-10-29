package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.CounterEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CounterEventDTO implements Serializable {

    private UUID id;

    private Long valueCounter;

    private Float denominationSale;

    @NotNull
    @Size(max = 2)
    private String counterCode;

    private EventDeviceDTO eventDevice;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getValueCounter() {
        return valueCounter;
    }

    public void setValueCounter(Long valueCounter) {
        this.valueCounter = valueCounter;
    }

    public Float getDenominationSale() {
        return denominationSale;
    }

    public void setDenominationSale(Float denominationSale) {
        this.denominationSale = denominationSale;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public EventDeviceDTO getEventDevice() {
        return eventDevice;
    }

    public void setEventDevice(EventDeviceDTO eventDevice) {
        this.eventDevice = eventDevice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CounterEventDTO)) {
            return false;
        }

        CounterEventDTO counterEventDTO = (CounterEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, counterEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterEventDTO{" +
            "id='" + getId() + "'" +
            ", valueCounter=" + getValueCounter() +
            ", denominationSale=" + getDenominationSale() +
            ", counterCode='" + getCounterCode() + "'" +
            ", eventDevice=" + getEventDevice() +
            "}";
    }
}
