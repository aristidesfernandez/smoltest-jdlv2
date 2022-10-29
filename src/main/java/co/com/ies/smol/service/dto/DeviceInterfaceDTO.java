package co.com.ies.smol.service.dto;

import co.com.ies.smol.domain.enumeration.DeviceInterfaceStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link co.com.ies.smol.domain.DeviceInterface} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceInterfaceDTO implements Serializable {

    private Long id;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Integer establishmentId;

    private DeviceInterfaceStatus state;

    private DeviceDTO device;

    private InterfaceBoardDTO interfaceBoard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Integer establishmentId) {
        this.establishmentId = establishmentId;
    }

    public DeviceInterfaceStatus getState() {
        return state;
    }

    public void setState(DeviceInterfaceStatus state) {
        this.state = state;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    public InterfaceBoardDTO getInterfaceBoard() {
        return interfaceBoard;
    }

    public void setInterfaceBoard(InterfaceBoardDTO interfaceBoard) {
        this.interfaceBoard = interfaceBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceInterfaceDTO)) {
            return false;
        }

        DeviceInterfaceDTO deviceInterfaceDTO = (DeviceInterfaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceInterfaceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceInterfaceDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", establishmentId=" + getEstablishmentId() +
            ", state='" + getState() + "'" +
            ", device=" + getDevice() +
            ", interfaceBoard=" + getInterfaceBoard() +
            "}";
    }
}
