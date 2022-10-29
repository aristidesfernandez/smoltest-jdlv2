package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.DeviceEstablishment} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.DeviceEstablishmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /device-establishments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceEstablishmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private ZonedDateTimeFilter registrationAt;

    private ZonedDateTimeFilter departureAt;

    private IntegerFilter deviceNumber;

    private IntegerFilter consecutiveDevice;

    private LongFilter establishmentId;

    private FloatFilter negativeAward;

    private UUIDFilter deviceId;

    private Boolean distinct;

    public DeviceEstablishmentCriteria() {}

    public DeviceEstablishmentCriteria(DeviceEstablishmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.registrationAt = other.registrationAt == null ? null : other.registrationAt.copy();
        this.departureAt = other.departureAt == null ? null : other.departureAt.copy();
        this.deviceNumber = other.deviceNumber == null ? null : other.deviceNumber.copy();
        this.consecutiveDevice = other.consecutiveDevice == null ? null : other.consecutiveDevice.copy();
        this.establishmentId = other.establishmentId == null ? null : other.establishmentId.copy();
        this.negativeAward = other.negativeAward == null ? null : other.negativeAward.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DeviceEstablishmentCriteria copy() {
        return new DeviceEstablishmentCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
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

    public ZonedDateTimeFilter getDepartureAt() {
        return departureAt;
    }

    public ZonedDateTimeFilter departureAt() {
        if (departureAt == null) {
            departureAt = new ZonedDateTimeFilter();
        }
        return departureAt;
    }

    public void setDepartureAt(ZonedDateTimeFilter departureAt) {
        this.departureAt = departureAt;
    }

    public IntegerFilter getDeviceNumber() {
        return deviceNumber;
    }

    public IntegerFilter deviceNumber() {
        if (deviceNumber == null) {
            deviceNumber = new IntegerFilter();
        }
        return deviceNumber;
    }

    public void setDeviceNumber(IntegerFilter deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public IntegerFilter getConsecutiveDevice() {
        return consecutiveDevice;
    }

    public IntegerFilter consecutiveDevice() {
        if (consecutiveDevice == null) {
            consecutiveDevice = new IntegerFilter();
        }
        return consecutiveDevice;
    }

    public void setConsecutiveDevice(IntegerFilter consecutiveDevice) {
        this.consecutiveDevice = consecutiveDevice;
    }

    public LongFilter getEstablishmentId() {
        return establishmentId;
    }

    public LongFilter establishmentId() {
        if (establishmentId == null) {
            establishmentId = new LongFilter();
        }
        return establishmentId;
    }

    public void setEstablishmentId(LongFilter establishmentId) {
        this.establishmentId = establishmentId;
    }

    public FloatFilter getNegativeAward() {
        return negativeAward;
    }

    public FloatFilter negativeAward() {
        if (negativeAward == null) {
            negativeAward = new FloatFilter();
        }
        return negativeAward;
    }

    public void setNegativeAward(FloatFilter negativeAward) {
        this.negativeAward = negativeAward;
    }

    public UUIDFilter getDeviceId() {
        return deviceId;
    }

    public UUIDFilter deviceId() {
        if (deviceId == null) {
            deviceId = new UUIDFilter();
        }
        return deviceId;
    }

    public void setDeviceId(UUIDFilter deviceId) {
        this.deviceId = deviceId;
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
        final DeviceEstablishmentCriteria that = (DeviceEstablishmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(registrationAt, that.registrationAt) &&
            Objects.equals(departureAt, that.departureAt) &&
            Objects.equals(deviceNumber, that.deviceNumber) &&
            Objects.equals(consecutiveDevice, that.consecutiveDevice) &&
            Objects.equals(establishmentId, that.establishmentId) &&
            Objects.equals(negativeAward, that.negativeAward) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            registrationAt,
            departureAt,
            deviceNumber,
            consecutiveDevice,
            establishmentId,
            negativeAward,
            deviceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceEstablishmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (registrationAt != null ? "registrationAt=" + registrationAt + ", " : "") +
            (departureAt != null ? "departureAt=" + departureAt + ", " : "") +
            (deviceNumber != null ? "deviceNumber=" + deviceNumber + ", " : "") +
            (consecutiveDevice != null ? "consecutiveDevice=" + consecutiveDevice + ", " : "") +
            (establishmentId != null ? "establishmentId=" + establishmentId + ", " : "") +
            (negativeAward != null ? "negativeAward=" + negativeAward + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
