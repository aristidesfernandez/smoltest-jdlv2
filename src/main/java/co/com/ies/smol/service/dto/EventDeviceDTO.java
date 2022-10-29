package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.EventDevice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDeviceDTO implements Serializable {

    private UUID id;

    private ZonedDateTime createdAt;

    private Boolean theoreticalPercentage;

    private Double moneyDenomination;

    private DeviceEstablishmentDTO deviceEstablishment;

    private EventTypeDTO eventType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getTheoreticalPercentage() {
        return theoreticalPercentage;
    }

    public void setTheoreticalPercentage(Boolean theoreticalPercentage) {
        this.theoreticalPercentage = theoreticalPercentage;
    }

    public Double getMoneyDenomination() {
        return moneyDenomination;
    }

    public void setMoneyDenomination(Double moneyDenomination) {
        this.moneyDenomination = moneyDenomination;
    }

    public DeviceEstablishmentDTO getDeviceEstablishment() {
        return deviceEstablishment;
    }

    public void setDeviceEstablishment(DeviceEstablishmentDTO deviceEstablishment) {
        this.deviceEstablishment = deviceEstablishment;
    }

    public EventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeDTO eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDeviceDTO)) {
            return false;
        }

        EventDeviceDTO eventDeviceDTO = (EventDeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventDeviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDeviceDTO{" +
            "id='" + getId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", theoreticalPercentage='" + getTheoreticalPercentage() + "'" +
            ", moneyDenomination=" + getMoneyDenomination() +
            ", deviceEstablishment=" + getDeviceEstablishment() +
            ", eventType=" + getEventType() +
            "}";
    }
}
