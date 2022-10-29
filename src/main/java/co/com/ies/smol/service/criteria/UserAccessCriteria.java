package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.UserAccess} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.UserAccessResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-accesses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccessCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter username;

    private StringFilter ipAddress;

    private ZonedDateTimeFilter registrationAt;

    private Boolean distinct;

    public UserAccessCriteria() {}

    public UserAccessCriteria(UserAccessCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.ipAddress = other.ipAddress == null ? null : other.ipAddress.copy();
        this.registrationAt = other.registrationAt == null ? null : other.registrationAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserAccessCriteria copy() {
        return new UserAccessCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getIpAddress() {
        return ipAddress;
    }

    public StringFilter ipAddress() {
        if (ipAddress == null) {
            ipAddress = new StringFilter();
        }
        return ipAddress;
    }

    public void setIpAddress(StringFilter ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ZonedDateTimeFilter getRegistrationAt() {
        return registrationAt;
    }

    public ZonedDateTimeFilter registrationAt() {
        if (registrationAt == null) {
            registrationAt = new ZonedDateTimeFilter();
        }
        return registrationAt;
    }

    public void setRegistrationAt(ZonedDateTimeFilter registrationAt) {
        this.registrationAt = registrationAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserAccessCriteria that = (UserAccessCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(ipAddress, that.ipAddress) &&
            Objects.equals(registrationAt, that.registrationAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, ipAddress, registrationAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccessCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "") +
            (registrationAt != null ? "registrationAt=" + registrationAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
