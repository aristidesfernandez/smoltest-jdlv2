package co.com.ies.smol.service.criteria;

import co.com.ies.smol.domain.enumeration.EstablishmentType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.Establishment} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.EstablishmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /establishments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstablishmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstablishmentType
     */
    public static class EstablishmentTypeFilter extends Filter<EstablishmentType> {

        public EstablishmentTypeFilter() {}

        public EstablishmentTypeFilter(EstablishmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public EstablishmentTypeFilter copy() {
            return new EstablishmentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter liquidationTime;

    private StringFilter name;

    private EstablishmentTypeFilter type;

    private StringFilter neighborhood;

    private StringFilter address;

    private StringFilter coljuegosCode;

    private ZonedDateTimeFilter startTime;

    private ZonedDateTimeFilter closeTime;

    private FloatFilter longitude;

    private FloatFilter latitude;

    private StringFilter mercantileRegistration;

    private LongFilter operatorId;

    private LongFilter municipalityId;

    private Boolean distinct;

    public EstablishmentCriteria() {}

    public EstablishmentCriteria(EstablishmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.liquidationTime = other.liquidationTime == null ? null : other.liquidationTime.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.neighborhood = other.neighborhood == null ? null : other.neighborhood.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.coljuegosCode = other.coljuegosCode == null ? null : other.coljuegosCode.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.closeTime = other.closeTime == null ? null : other.closeTime.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.mercantileRegistration = other.mercantileRegistration == null ? null : other.mercantileRegistration.copy();
        this.operatorId = other.operatorId == null ? null : other.operatorId.copy();
        this.municipalityId = other.municipalityId == null ? null : other.municipalityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EstablishmentCriteria copy() {
        return new EstablishmentCriteria(this);
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

    public ZonedDateTimeFilter getLiquidationTime() {
        return liquidationTime;
    }

    public ZonedDateTimeFilter liquidationTime() {
        if (liquidationTime == null) {
            liquidationTime = new ZonedDateTimeFilter();
        }
        return liquidationTime;
    }

    public void setLiquidationTime(ZonedDateTimeFilter liquidationTime) {
        this.liquidationTime = liquidationTime;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public EstablishmentTypeFilter getType() {
        return type;
    }

    public EstablishmentTypeFilter type() {
        if (type == null) {
            type = new EstablishmentTypeFilter();
        }
        return type;
    }

    public void setType(EstablishmentTypeFilter type) {
        this.type = type;
    }

    public StringFilter getNeighborhood() {
        return neighborhood;
    }

    public StringFilter neighborhood() {
        if (neighborhood == null) {
            neighborhood = new StringFilter();
        }
        return neighborhood;
    }

    public void setNeighborhood(StringFilter neighborhood) {
        this.neighborhood = neighborhood;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getColjuegosCode() {
        return coljuegosCode;
    }

    public StringFilter coljuegosCode() {
        if (coljuegosCode == null) {
            coljuegosCode = new StringFilter();
        }
        return coljuegosCode;
    }

    public void setColjuegosCode(StringFilter coljuegosCode) {
        this.coljuegosCode = coljuegosCode;
    }

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            startTime = new ZonedDateTimeFilter();
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTimeFilter getCloseTime() {
        return closeTime;
    }

    public ZonedDateTimeFilter closeTime() {
        if (closeTime == null) {
            closeTime = new ZonedDateTimeFilter();
        }
        return closeTime;
    }

    public void setCloseTime(ZonedDateTimeFilter closeTime) {
        this.closeTime = closeTime;
    }

    public FloatFilter getLongitude() {
        return longitude;
    }

    public FloatFilter longitude() {
        if (longitude == null) {
            longitude = new FloatFilter();
        }
        return longitude;
    }

    public void setLongitude(FloatFilter longitude) {
        this.longitude = longitude;
    }

    public FloatFilter getLatitude() {
        return latitude;
    }

    public FloatFilter latitude() {
        if (latitude == null) {
            latitude = new FloatFilter();
        }
        return latitude;
    }

    public void setLatitude(FloatFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getMercantileRegistration() {
        return mercantileRegistration;
    }

    public StringFilter mercantileRegistration() {
        if (mercantileRegistration == null) {
            mercantileRegistration = new StringFilter();
        }
        return mercantileRegistration;
    }

    public void setMercantileRegistration(StringFilter mercantileRegistration) {
        this.mercantileRegistration = mercantileRegistration;
    }

    public LongFilter getOperatorId() {
        return operatorId;
    }

    public LongFilter operatorId() {
        if (operatorId == null) {
            operatorId = new LongFilter();
        }
        return operatorId;
    }

    public void setOperatorId(LongFilter operatorId) {
        this.operatorId = operatorId;
    }

    public LongFilter getMunicipalityId() {
        return municipalityId;
    }

    public LongFilter municipalityId() {
        if (municipalityId == null) {
            municipalityId = new LongFilter();
        }
        return municipalityId;
    }

    public void setMunicipalityId(LongFilter municipalityId) {
        this.municipalityId = municipalityId;
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
        final EstablishmentCriteria that = (EstablishmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(liquidationTime, that.liquidationTime) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(neighborhood, that.neighborhood) &&
            Objects.equals(address, that.address) &&
            Objects.equals(coljuegosCode, that.coljuegosCode) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(closeTime, that.closeTime) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(mercantileRegistration, that.mercantileRegistration) &&
            Objects.equals(operatorId, that.operatorId) &&
            Objects.equals(municipalityId, that.municipalityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            liquidationTime,
            name,
            type,
            neighborhood,
            address,
            coljuegosCode,
            startTime,
            closeTime,
            longitude,
            latitude,
            mercantileRegistration,
            operatorId,
            municipalityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstablishmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (liquidationTime != null ? "liquidationTime=" + liquidationTime + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (neighborhood != null ? "neighborhood=" + neighborhood + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (coljuegosCode != null ? "coljuegosCode=" + coljuegosCode + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (closeTime != null ? "closeTime=" + closeTime + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (mercantileRegistration != null ? "mercantileRegistration=" + mercantileRegistration + ", " : "") +
            (operatorId != null ? "operatorId=" + operatorId + ", " : "") +
            (municipalityId != null ? "municipalityId=" + municipalityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
