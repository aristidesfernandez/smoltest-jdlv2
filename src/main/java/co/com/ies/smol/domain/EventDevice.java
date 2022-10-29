package co.com.ies.smol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventDevice.
 */
@Entity
@Table(name = "event_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "theoretical_percentage")
    private Boolean theoreticalPercentage;

    @Column(name = "money_denomination")
    private Double moneyDenomination;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "device" }, allowSetters = true)
    private DeviceEstablishment deviceEstablishment;

    @ManyToOne(optional = false)
    @NotNull
    private EventType eventType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public EventDevice id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventDevice createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getTheoreticalPercentage() {
        return this.theoreticalPercentage;
    }

    public EventDevice theoreticalPercentage(Boolean theoreticalPercentage) {
        this.setTheoreticalPercentage(theoreticalPercentage);
        return this;
    }

    public void setTheoreticalPercentage(Boolean theoreticalPercentage) {
        this.theoreticalPercentage = theoreticalPercentage;
    }

    public Double getMoneyDenomination() {
        return this.moneyDenomination;
    }

    public EventDevice moneyDenomination(Double moneyDenomination) {
        this.setMoneyDenomination(moneyDenomination);
        return this;
    }

    public void setMoneyDenomination(Double moneyDenomination) {
        this.moneyDenomination = moneyDenomination;
    }

    public DeviceEstablishment getDeviceEstablishment() {
        return this.deviceEstablishment;
    }

    public void setDeviceEstablishment(DeviceEstablishment deviceEstablishment) {
        this.deviceEstablishment = deviceEstablishment;
    }

    public EventDevice deviceEstablishment(DeviceEstablishment deviceEstablishment) {
        this.setDeviceEstablishment(deviceEstablishment);
        return this;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventDevice eventType(EventType eventType) {
        this.setEventType(eventType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDevice)) {
            return false;
        }
        return id != null && id.equals(((EventDevice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDevice{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", theoreticalPercentage='" + getTheoreticalPercentage() + "'" +
            ", moneyDenomination=" + getMoneyDenomination() +
            "}";
    }
}
