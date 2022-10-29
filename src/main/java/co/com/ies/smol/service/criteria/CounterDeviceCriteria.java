package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.CounterDevice} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.CounterDeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counter-devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CounterDeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private BigDecimalFilter value;

    private BigDecimalFilter rolloverValue;

    private BigDecimalFilter creditSale;

    private BooleanFilter manualCounter;

    private BigDecimalFilter manualMultiplier;

    private BooleanFilter decimalsManualCounter;

    private UUIDFilter deviceId;

    private Boolean distinct;

    public CounterDeviceCriteria() {}

    public CounterDeviceCriteria(CounterDeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.rolloverValue = other.rolloverValue == null ? null : other.rolloverValue.copy();
        this.creditSale = other.creditSale == null ? null : other.creditSale.copy();
        this.manualCounter = other.manualCounter == null ? null : other.manualCounter.copy();
        this.manualMultiplier = other.manualMultiplier == null ? null : other.manualMultiplier.copy();
        this.decimalsManualCounter = other.decimalsManualCounter == null ? null : other.decimalsManualCounter.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CounterDeviceCriteria copy() {
        return new CounterDeviceCriteria(this);
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

    public BigDecimalFilter getValue() {
        return value;
    }

    public BigDecimalFilter value() {
        if (value == null) {
            value = new BigDecimalFilter();
        }
        return value;
    }

    public void setValue(BigDecimalFilter value) {
        this.value = value;
    }

    public BigDecimalFilter getRolloverValue() {
        return rolloverValue;
    }

    public BigDecimalFilter rolloverValue() {
        if (rolloverValue == null) {
            rolloverValue = new BigDecimalFilter();
        }
        return rolloverValue;
    }

    public void setRolloverValue(BigDecimalFilter rolloverValue) {
        this.rolloverValue = rolloverValue;
    }

    public BigDecimalFilter getCreditSale() {
        return creditSale;
    }

    public BigDecimalFilter creditSale() {
        if (creditSale == null) {
            creditSale = new BigDecimalFilter();
        }
        return creditSale;
    }

    public void setCreditSale(BigDecimalFilter creditSale) {
        this.creditSale = creditSale;
    }

    public BooleanFilter getManualCounter() {
        return manualCounter;
    }

    public BooleanFilter manualCounter() {
        if (manualCounter == null) {
            manualCounter = new BooleanFilter();
        }
        return manualCounter;
    }

    public void setManualCounter(BooleanFilter manualCounter) {
        this.manualCounter = manualCounter;
    }

    public BigDecimalFilter getManualMultiplier() {
        return manualMultiplier;
    }

    public BigDecimalFilter manualMultiplier() {
        if (manualMultiplier == null) {
            manualMultiplier = new BigDecimalFilter();
        }
        return manualMultiplier;
    }

    public void setManualMultiplier(BigDecimalFilter manualMultiplier) {
        this.manualMultiplier = manualMultiplier;
    }

    public BooleanFilter getDecimalsManualCounter() {
        return decimalsManualCounter;
    }

    public BooleanFilter decimalsManualCounter() {
        if (decimalsManualCounter == null) {
            decimalsManualCounter = new BooleanFilter();
        }
        return decimalsManualCounter;
    }

    public void setDecimalsManualCounter(BooleanFilter decimalsManualCounter) {
        this.decimalsManualCounter = decimalsManualCounter;
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
        final CounterDeviceCriteria that = (CounterDeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(value, that.value) &&
            Objects.equals(rolloverValue, that.rolloverValue) &&
            Objects.equals(creditSale, that.creditSale) &&
            Objects.equals(manualCounter, that.manualCounter) &&
            Objects.equals(manualMultiplier, that.manualMultiplier) &&
            Objects.equals(decimalsManualCounter, that.decimalsManualCounter) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            value,
            rolloverValue,
            creditSale,
            manualCounter,
            manualMultiplier,
            decimalsManualCounter,
            deviceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterDeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (rolloverValue != null ? "rolloverValue=" + rolloverValue + ", " : "") +
            (creditSale != null ? "creditSale=" + creditSale + ", " : "") +
            (manualCounter != null ? "manualCounter=" + manualCounter + ", " : "") +
            (manualMultiplier != null ? "manualMultiplier=" + manualMultiplier + ", " : "") +
            (decimalsManualCounter != null ? "decimalsManualCounter=" + decimalsManualCounter + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
