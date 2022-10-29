package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.InterfaceBoard} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.InterfaceBoardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interface-boards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterfaceBoardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter isAssigned;

    private StringFilter ipAddress;

    private StringFilter hash;

    private StringFilter serial;

    private StringFilter version;

    private StringFilter port;

    private Boolean distinct;

    public InterfaceBoardCriteria() {}

    public InterfaceBoardCriteria(InterfaceBoardCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.isAssigned = other.isAssigned == null ? null : other.isAssigned.copy();
        this.ipAddress = other.ipAddress == null ? null : other.ipAddress.copy();
        this.hash = other.hash == null ? null : other.hash.copy();
        this.serial = other.serial == null ? null : other.serial.copy();
        this.version = other.version == null ? null : other.version.copy();
        this.port = other.port == null ? null : other.port.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InterfaceBoardCriteria copy() {
        return new InterfaceBoardCriteria(this);
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

    public BooleanFilter getIsAssigned() {
        return isAssigned;
    }

    public BooleanFilter isAssigned() {
        if (isAssigned == null) {
            isAssigned = new BooleanFilter();
        }
        return isAssigned;
    }

    public void setIsAssigned(BooleanFilter isAssigned) {
        this.isAssigned = isAssigned;
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

    public StringFilter getHash() {
        return hash;
    }

    public StringFilter hash() {
        if (hash == null) {
            hash = new StringFilter();
        }
        return hash;
    }

    public void setHash(StringFilter hash) {
        this.hash = hash;
    }

    public StringFilter getSerial() {
        return serial;
    }

    public StringFilter serial() {
        if (serial == null) {
            serial = new StringFilter();
        }
        return serial;
    }

    public void setSerial(StringFilter serial) {
        this.serial = serial;
    }

    public StringFilter getVersion() {
        return version;
    }

    public StringFilter version() {
        if (version == null) {
            version = new StringFilter();
        }
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public StringFilter getPort() {
        return port;
    }

    public StringFilter port() {
        if (port == null) {
            port = new StringFilter();
        }
        return port;
    }

    public void setPort(StringFilter port) {
        this.port = port;
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
        final InterfaceBoardCriteria that = (InterfaceBoardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(isAssigned, that.isAssigned) &&
            Objects.equals(ipAddress, that.ipAddress) &&
            Objects.equals(hash, that.hash) &&
            Objects.equals(serial, that.serial) &&
            Objects.equals(version, that.version) &&
            Objects.equals(port, that.port) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isAssigned, ipAddress, hash, serial, version, port, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterfaceBoardCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (isAssigned != null ? "isAssigned=" + isAssigned + ", " : "") +
            (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "") +
            (hash != null ? "hash=" + hash + ", " : "") +
            (serial != null ? "serial=" + serial + ", " : "") +
            (version != null ? "version=" + version + ", " : "") +
            (port != null ? "port=" + port + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
