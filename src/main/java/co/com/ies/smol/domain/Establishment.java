package co.com.ies.smol.domain;

import co.com.ies.smol.domain.enumeration.EstablishmentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Establishment.
 */
@Entity
@Table(name = "establishment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Establishment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "liquidation_time")
    private ZonedDateTime liquidationTime;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EstablishmentType type;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "address")
    private String address;

    @Size(max = 100)
    @Column(name = "coljuegos_code", length = 100)
    private String coljuegosCode;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "close_time")
    private ZonedDateTime closeTime;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "latitude")
    private Float latitude;

    @Size(max = 100)
    @Column(name = "mercantile_registration", length = 100)
    private String mercantileRegistration;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "municipality" }, allowSetters = true)
    private Operator operator;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "province" }, allowSetters = true)
    private Municipality municipality;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Establishment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLiquidationTime() {
        return this.liquidationTime;
    }

    public Establishment liquidationTime(ZonedDateTime liquidationTime) {
        this.setLiquidationTime(liquidationTime);
        return this;
    }

    public void setLiquidationTime(ZonedDateTime liquidationTime) {
        this.liquidationTime = liquidationTime;
    }

    public String getName() {
        return this.name;
    }

    public Establishment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EstablishmentType getType() {
        return this.type;
    }

    public Establishment type(EstablishmentType type) {
        this.setType(type);
        return this;
    }

    public void setType(EstablishmentType type) {
        this.type = type;
    }

    public String getNeighborhood() {
        return this.neighborhood;
    }

    public Establishment neighborhood(String neighborhood) {
        this.setNeighborhood(neighborhood);
        return this;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getAddress() {
        return this.address;
    }

    public Establishment address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getColjuegosCode() {
        return this.coljuegosCode;
    }

    public Establishment coljuegosCode(String coljuegosCode) {
        this.setColjuegosCode(coljuegosCode);
        return this;
    }

    public void setColjuegosCode(String coljuegosCode) {
        this.coljuegosCode = coljuegosCode;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public Establishment startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getCloseTime() {
        return this.closeTime;
    }

    public Establishment closeTime(ZonedDateTime closeTime) {
        this.setCloseTime(closeTime);
        return this;
    }

    public void setCloseTime(ZonedDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public Establishment longitude(Float longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Establishment latitude(Float latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getMercantileRegistration() {
        return this.mercantileRegistration;
    }

    public Establishment mercantileRegistration(String mercantileRegistration) {
        this.setMercantileRegistration(mercantileRegistration);
        return this;
    }

    public void setMercantileRegistration(String mercantileRegistration) {
        this.mercantileRegistration = mercantileRegistration;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Establishment operator(Operator operator) {
        this.setOperator(operator);
        return this;
    }

    public Municipality getMunicipality() {
        return this.municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Establishment municipality(Municipality municipality) {
        this.setMunicipality(municipality);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Establishment)) {
            return false;
        }
        return id != null && id.equals(((Establishment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Establishment{" +
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
            "}";
    }
}
