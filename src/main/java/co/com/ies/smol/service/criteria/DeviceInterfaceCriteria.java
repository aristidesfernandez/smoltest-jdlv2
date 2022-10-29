package co.com.ies.smol.service.criteria;

import co.com.ies.smol.domain.enumeration.DeviceInterfaceStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.DeviceInterface} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.DeviceInterfaceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /device-interfaces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceInterfaceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DeviceInterfaceStatus
     */
    public static class DeviceInterfaceStatusFilter extends Filter<DeviceInterfaceStatus> {

        public DeviceInterfaceStatusFilter() {}

        public DeviceInterfaceStatusFilter(DeviceInterfaceStatusFilter filter) {
            super(filter);
        }

        @Override
        public DeviceInterfaceStatusFilter copy() {
            return new DeviceInterfaceStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private IntegerFilter establishmentId;

    private DeviceInterfaceStatusFilter state;

    private UUIDFilter deviceId;

    private LongFilter interfaceBoardId;

    private Boolean distinct;

    public DeviceInterfaceCriteria() {}

    public DeviceInterfaceCriteria(DeviceInterfaceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.establishmentId = other.establishmentId == null ? null : other.establishmentId.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.interfaceBoardId = other.interfaceBoardId == null ? null : other.interfaceBoardId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DeviceInterfaceCriteria copy() {
        return new DeviceInterfaceCriteria(this);
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

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            startDate = new ZonedDateTimeFilter();
        }
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTimeFilter getEndDate() {
        return endDate;
    }

    public ZonedDateTimeFilter endDate() {
        if (endDate == null) {
            endDate = new ZonedDateTimeFilter();
        }
        return endDate;
    }

    public void setEndDate(ZonedDateTimeFilter endDate) {
        this.endDate = endDate;
    }

    public IntegerFilter getEstablishmentId() {
        return establishmentId;
    }

    public IntegerFilter establishmentId() {
        if (establishmentId == null) {
            establishmentId = new IntegerFilter();
        }
        return establishmentId;
    }

    public void setEstablishmentId(IntegerFilter establishmentId) {
        this.establishmentId = establishmentId;
    }

    public DeviceInterfaceStatusFilter getState() {
        return state;
    }

    public DeviceInterfaceStatusFilter state() {
        if (state == null) {
            state = new DeviceInterfaceStatusFilter();
        }
        return state;
    }

    public void setState(DeviceInterfaceStatusFilter state) {
        this.state = state;
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

    public LongFilter getInterfaceBoardId() {
        return interfaceBoardId;
    }

    public LongFilter interfaceBoardId() {
        if (interfaceBoardId == null) {
            interfaceBoardId = new LongFilter();
        }
        return interfaceBoardId;
    }

    public void setInterfaceBoardId(LongFilter interfaceBoardId) {
        this.interfaceBoardId = interfaceBoardId;
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
        final DeviceInterfaceCriteria that = (DeviceInterfaceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(establishmentId, that.establishmentId) &&
            Objects.equals(state, that.state) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(interfaceBoardId, that.interfaceBoardId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, establishmentId, state, deviceId, interfaceBoardId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceInterfaceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (establishmentId != null ? "establishmentId=" + establishmentId + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (interfaceBoardId != null ? "interfaceBoardId=" + interfaceBoardId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
