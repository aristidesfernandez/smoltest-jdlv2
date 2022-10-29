package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.EventDevice} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.EventDeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private ZonedDateTimeFilter createdAt;

    private BooleanFilter theoreticalPercentage;

    private DoubleFilter moneyDenomination;

    private UUIDFilter deviceEstablishmentId;

    private LongFilter eventTypeId;

    private Boolean distinct;

    public EventDeviceCriteria() {}

    public EventDeviceCriteria(EventDeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.theoreticalPercentage = other.theoreticalPercentage == null ? null : other.theoreticalPercentage.copy();
        this.moneyDenomination = other.moneyDenomination == null ? null : other.moneyDenomination.copy();
        this.deviceEstablishmentId = other.deviceEstablishmentId == null ? null : other.deviceEstablishmentId.copy();
        this.eventTypeId = other.eventTypeId == null ? null : other.eventTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventDeviceCriteria copy() {
        return new EventDeviceCriteria(this);
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

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public BooleanFilter getTheoreticalPercentage() {
        return theoreticalPercentage;
    }

    public BooleanFilter theoreticalPercentage() {
        if (theoreticalPercentage == null) {
            theoreticalPercentage = new BooleanFilter();
        }
        return theoreticalPercentage;
    }

    public void setTheoreticalPercentage(BooleanFilter theoreticalPercentage) {
        this.theoreticalPercentage = theoreticalPercentage;
    }

    public DoubleFilter getMoneyDenomination() {
        return moneyDenomination;
    }

    public DoubleFilter moneyDenomination() {
        if (moneyDenomination == null) {
            moneyDenomination = new DoubleFilter();
        }
        return moneyDenomination;
    }

    public void setMoneyDenomination(DoubleFilter moneyDenomination) {
        this.moneyDenomination = moneyDenomination;
    }

    public UUIDFilter getDeviceEstablishmentId() {
        return deviceEstablishmentId;
    }

    public UUIDFilter deviceEstablishmentId() {
        if (deviceEstablishmentId == null) {
            deviceEstablishmentId = new UUIDFilter();
        }
        return deviceEstablishmentId;
    }

    public void setDeviceEstablishmentId(UUIDFilter deviceEstablishmentId) {
        this.deviceEstablishmentId = deviceEstablishmentId;
    }

    public LongFilter getEventTypeId() {
        return eventTypeId;
    }

    public LongFilter eventTypeId() {
        if (eventTypeId == null) {
            eventTypeId = new LongFilter();
        }
        return eventTypeId;
    }

    public void setEventTypeId(LongFilter eventTypeId) {
        this.eventTypeId = eventTypeId;
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
        final EventDeviceCriteria that = (EventDeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(theoreticalPercentage, that.theoreticalPercentage) &&
            Objects.equals(moneyDenomination, that.moneyDenomination) &&
            Objects.equals(deviceEstablishmentId, that.deviceEstablishmentId) &&
            Objects.equals(eventTypeId, that.eventTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, theoreticalPercentage, moneyDenomination, deviceEstablishmentId, eventTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (theoreticalPercentage != null ? "theoreticalPercentage=" + theoreticalPercentage + ", " : "") +
            (moneyDenomination != null ? "moneyDenomination=" + moneyDenomination + ", " : "") +
            (deviceEstablishmentId != null ? "deviceEstablishmentId=" + deviceEstablishmentId + ", " : "") +
            (eventTypeId != null ? "eventTypeId=" + eventTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
