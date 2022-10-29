package co.com.ies.smol.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAccess.
 */
@Entity
@Table(name = "user_access")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 25)
    @Column(name = "username", length = 25, nullable = false)
    private String username;

    @NotNull
    @Size(max = 20)
    @Column(name = "ip_address", length = 20, nullable = false)
    private String ipAddress;

    @NotNull
    @Column(name = "registration_at", nullable = false)
    private ZonedDateTime registrationAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAccess id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public UserAccess username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public UserAccess ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ZonedDateTime getRegistrationAt() {
        return this.registrationAt;
    }

    public UserAccess registrationAt(ZonedDateTime registrationAt) {
        this.setRegistrationAt(registrationAt);
        return this;
    }

    public void setRegistrationAt(ZonedDateTime registrationAt) {
        this.registrationAt = registrationAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccess)) {
            return false;
        }
        return id != null && id.equals(((UserAccess) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccess{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", registrationAt='" + getRegistrationAt() + "'" +
            "}";
    }
}
