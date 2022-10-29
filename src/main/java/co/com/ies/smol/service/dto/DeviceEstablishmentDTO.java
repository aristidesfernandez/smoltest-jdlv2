package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.DeviceEstablishment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceEstablishmentDTO implements Serializable {

    private UUID id;

    @NotNull
    private ZonedDateTime registrationAt;

    private ZonedDateTime departureAt;

    private Integer deviceNumber;

    private Integer consecutiveDevice;

    private Long establishmentId;

    private Float negativeAward;

    private DeviceDTO device;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getRegistrationAt() {
        return registrationAt;
    }

    public void setRegistrationAt(ZonedDateTime registrationAt) {
        this.registrationAt = registrationAt;
    }

    public ZonedDateTime getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(ZonedDateTime departureAt) {
        this.departureAt = departureAt;
    }

    public Integer getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(Integer deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public Integer getConsecutiveDevice() {
        return consecutiveDevice;
    }

    public void setConsecutiveDevice(Integer consecutiveDevice) {
        this.consecutiveDevice = consecutiveDevice;
    }

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }

    public Float getNegativeAward() {
        return negativeAward;
    }

    public void setNegativeAward(Float negativeAward) {
        this.negativeAward = negativeAward;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceEstablishmentDTO)) {
            return false;
        }

        DeviceEstablishmentDTO deviceEstablishmentDTO = (DeviceEstablishmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceEstablishmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceEstablishmentDTO{" +
            "id='" + getId() + "'" +
            ", registrationAt='" + getRegistrationAt() + "'" +
            ", departureAt='" + getDepartureAt() + "'" +
            ", deviceNumber=" + getDeviceNumber() +
            ", consecutiveDevice=" + getConsecutiveDevice() +
            ", establishmentId=" + getEstablishmentId() +
            ", negativeAward=" + getNegativeAward() +
            ", device=" + getDevice() +
            "}";
    }
}
