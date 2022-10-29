package co.com.ies.smol.service.dto;

import co.com.ies.smol.domain.enumeration.EstablishmentType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.Establishment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstablishmentDTO implements Serializable {

    private Long id;

    private ZonedDateTime liquidationTime;

    @Size(max = 100)
    private String name;

    private EstablishmentType type;

    private String neighborhood;

    private String address;

    @Size(max = 100)
    private String coljuegosCode;

    private ZonedDateTime startTime;

    private ZonedDateTime closeTime;

    private Float longitude;

    private Float latitude;

    @Size(max = 100)
    private String mercantileRegistration;

    private OperatorDTO operator;

    private MunicipalityDTO municipality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLiquidationTime() {
        return liquidationTime;
    }

    public void setLiquidationTime(ZonedDateTime liquidationTime) {
        this.liquidationTime = liquidationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EstablishmentType getType() {
        return type;
    }

    public void setType(EstablishmentType type) {
        this.type = type;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getColjuegosCode() {
        return coljuegosCode;
    }

    public void setColjuegosCode(String coljuegosCode) {
        this.coljuegosCode = coljuegosCode;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(ZonedDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getMercantileRegistration() {
        return mercantileRegistration;
    }

    public void setMercantileRegistration(String mercantileRegistration) {
        this.mercantileRegistration = mercantileRegistration;
    }

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    public MunicipalityDTO getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityDTO municipality) {
        this.municipality = municipality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EstablishmentDTO)) {
            return false;
        }

        EstablishmentDTO establishmentDTO = (EstablishmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, establishmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstablishmentDTO{" +
            "id=" + getId() +
            ", liquidationTime='" + getLiquidationTime() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", neighborhood='" + getNeighborhood() + "'" +
            ", address='" + getAddress() + "'" +
            ", coljuegosCode='" + getColjuegosCode() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", closeTime='" + getCloseTime() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", mercantileRegistration='" + getMercantileRegistration() + "'" +
            ", operator=" + getOperator() +
            ", municipality=" + getMunicipality() +
            "}";
    }
}
