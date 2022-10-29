package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.CounterEvent} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.CounterEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counter-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CounterEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private LongFilter valueCounter;

    private FloatFilter denominationSale;

    private StringFilter counterCode;

    private UUIDFilter eventDeviceId;

    private Boolean distinct;

    public CounterEventCriteria() {}

    public CounterEventCriteria(CounterEventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.valueCounter = other.valueCounter == null ? null : other.valueCounter.copy();
        this.denominationSale = other.denominationSale == null ? null : other.denominationSale.copy();
        this.counterCode = other.counterCode == null ? null : other.counterCode.copy();
        this.eventDeviceId = other.eventDeviceId == null ? null : other.eventDeviceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CounterEventCriteria copy() {
        return new CounterEventCriteria(this);
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

    public LongFilter getValueCounter() {
        return valueCounter;
    }

    public LongFilter valueCounter() {
        if (valueCounter == null) {
            valueCounter = new LongFilter();
        }
        return valueCounter;
    }

    public void setValueCounter(LongFilter valueCounter) {
        this.valueCounter = valueCounter;
    }

    public FloatFilter getDenominationSale() {
        return denominationSale;
    }

    public FloatFilter denominationSale() {
        if (denominationSale == null) {
            denominationSale = new FloatFilter();
        }
        return denominationSale;
    }

    public void setDenominationSale(FloatFilter denominationSale) {
        this.denominationSale = denominationSale;
    }

    public StringFilter getCounterCode() {
        return counterCode;
    }

    public StringFilter counterCode() {
        if (counterCode == null) {
            counterCode = new StringFilter();
        }
        return counterCode;
    }

    public void setCounterCode(StringFilter counterCode) {
        this.counterCode = counterCode;
    }

    public UUIDFilter getEventDeviceId() {
        return eventDeviceId;
    }

    public UUIDFilter eventDeviceId() {
        if (eventDeviceId == null) {
            eventDeviceId = new UUIDFilter();
        }
        return eventDeviceId;
    }

    public void setEventDeviceId(UUIDFilter eventDeviceId) {
        this.eventDeviceId = eventDeviceId;
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
        final CounterEventCriteria that = (CounterEventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valueCounter, that.valueCounter) &&
            Objects.equals(denominationSale, that.denominationSale) &&
            Objects.equals(counterCode, that.counterCode) &&
            Objects.equals(eventDeviceId, that.eventDeviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valueCounter, denominationSale, counterCode, eventDeviceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterEventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (valueCounter != null ? "valueCounter=" + valueCounter + ", " : "") +
            (denominationSale != null ? "denominationSale=" + denominationSale + ", " : "") +
            (counterCode != null ? "counterCode=" + counterCode + ", " : "") +
            (eventDeviceId != null ? "eventDeviceId=" + eventDeviceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
