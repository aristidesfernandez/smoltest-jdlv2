package co.com.ies.smol.domain;

import co.com.ies.smol.domain.enumeration.DeviceInterfaceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeviceInterface.
 */
@Entity
@Table(name = "device_interface")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceInterface implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "establishment_id")
    private Integer establishmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private DeviceInterfaceStatus state;

    @ManyToOne
    @JsonIgnoreProperties(value = { "model", "deviceCategory", "deviceType", "formulaHandpay", "formulaJackpot" }, allowSetters = true)
    private Device device;

    @ManyToOne
    private InterfaceBoard interfaceBoard;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeviceInterface id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public DeviceInterface startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public DeviceInterface endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getEstablishmentId() {
        return this.establishmentId;
    }

    public DeviceInterface establishmentId(Integer establishmentId) {
        this.setEstablishmentId(establishmentId);
        return this;
    }

    public void setEstablishmentId(Integer establishmentId) {
        this.establishmentId = establishmentId;
    }

    public DeviceInterfaceStatus getState() {
        return this.state;
    }

    public DeviceInterface state(DeviceInterfaceStatus state) {
        this.setState(state);
        return this;
    }

    public void setState(DeviceInterfaceStatus state) {
        this.state = state;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public DeviceInterface device(Device device) {
        this.setDevice(device);
        return this;
    }

    public InterfaceBoard getInterfaceBoard() {
        return this.interfaceBoard;
    }

    public void setInterfaceBoard(InterfaceBoard interfaceBoard) {
        this.interfaceBoard = interfaceBoard;
    }

    public DeviceInterface interfaceBoard(InterfaceBoard interfaceBoard) {
        this.setInterfaceBoard(interfaceBoard);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceInterface)) {
            return false;
        }
        return id != null && id.equals(((DeviceInterface) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceInterface{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", establishmentId=" + getEstablishmentId() +
            ", state='" + getState() + "'" +
            "}";
    }
}
