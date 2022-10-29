package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.InterfaceBoard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterfaceBoardDTO implements Serializable {

    private Long id;

    private Boolean isAssigned;

    @Size(max = 25)
    private String ipAddress;

    private String hash;

    @Size(max = 50)
    private String serial;

    @Size(max = 10)
    private String version;

    @Size(max = 10)
    private String port;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(Boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterfaceBoardDTO)) {
            return false;
        }

        InterfaceBoardDTO interfaceBoardDTO = (InterfaceBoardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interfaceBoardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterfaceBoardDTO{" +
            "id=" + getId() +
            ", isAssigned='" + getIsAssigned() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", hash='" + getHash() + "'" +
            ", serial='" + getSerial() + "'" +
            ", version='" + getVersion() + "'" +
            ", port='" + getPort() + "'" +
            "}";
    }
}
