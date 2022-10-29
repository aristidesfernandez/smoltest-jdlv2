package co.com.ies.smol.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InterfaceBoard.
 */
@Entity
@Table(name = "interface_board")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterfaceBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_assigned")
    private Boolean isAssigned;

    @Size(max = 25)
    @Column(name = "ip_address", length = 25)
    private String ipAddress;

    @Column(name = "hash")
    private String hash;

    @Size(max = 50)
    @Column(name = "serial", length = 50)
    private String serial;

    @Size(max = 10)
    @Column(name = "version", length = 10)
    private String version;

    @Size(max = 10)
    @Column(name = "port", length = 10)
    private String port;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InterfaceBoard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsAssigned() {
        return this.isAssigned;
    }

    public InterfaceBoard isAssigned(Boolean isAssigned) {
        this.setIsAssigned(isAssigned);
        return this;
    }

    public void setIsAssigned(Boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public InterfaceBoard ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHash() {
        return this.hash;
    }

    public InterfaceBoard hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSerial() {
        return this.serial;
    }

    public InterfaceBoard serial(String serial) {
        this.setSerial(serial);
        return this;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getVersion() {
        return this.version;
    }

    public InterfaceBoard version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPort() {
        return this.port;
    }

    public InterfaceBoard port(String port) {
        this.setPort(port);
        return this;
    }

    public void setPort(String port) {
        this.port = port;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterfaceBoard)) {
            return false;
        }
        return id != null && id.equals(((InterfaceBoard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterfaceBoard{" +
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
