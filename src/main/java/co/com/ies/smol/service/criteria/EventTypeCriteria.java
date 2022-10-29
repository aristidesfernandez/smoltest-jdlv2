package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.EventType} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.EventTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventCode;

    private StringFilter sasCode;

    private StringFilter description;

    private BooleanFilter isStorable;

    private BooleanFilter isPriority;

    private StringFilter procesador;

    private BooleanFilter isAlarm;

    private Boolean distinct;

    public EventTypeCriteria() {}

    public EventTypeCriteria(EventTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventCode = other.eventCode == null ? null : other.eventCode.copy();
        this.sasCode = other.sasCode == null ? null : other.sasCode.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isStorable = other.isStorable == null ? null : other.isStorable.copy();
        this.isPriority = other.isPriority == null ? null : other.isPriority.copy();
        this.procesador = other.procesador == null ? null : other.procesador.copy();
        this.isAlarm = other.isAlarm == null ? null : other.isAlarm.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventTypeCriteria copy() {
        return new EventTypeCriteria(this);
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

    public StringFilter getEventCode() {
        return eventCode;
    }

    public StringFilter eventCode() {
        if (eventCode == null) {
            eventCode = new StringFilter();
        }
        return eventCode;
    }

    public void setEventCode(StringFilter eventCode) {
        this.eventCode = eventCode;
    }

    public StringFilter getSasCode() {
        return sasCode;
    }

    public StringFilter sasCode() {
        if (sasCode == null) {
            sasCode = new StringFilter();
        }
        return sasCode;
    }

    public void setSasCode(StringFilter sasCode) {
        this.sasCode = sasCode;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getIsStorable() {
        return isStorable;
    }

    public BooleanFilter isStorable() {
        if (isStorable == null) {
            isStorable = new BooleanFilter();
        }
        return isStorable;
    }

    public void setIsStorable(BooleanFilter isStorable) {
        this.isStorable = isStorable;
    }

    public BooleanFilter getIsPriority() {
        return isPriority;
    }

    public BooleanFilter isPriority() {
        if (isPriority == null) {
            isPriority = new BooleanFilter();
        }
        return isPriority;
    }

    public void setIsPriority(BooleanFilter isPriority) {
        this.isPriority = isPriority;
    }

    public StringFilter getProcesador() {
        return procesador;
    }

    public StringFilter procesador() {
        if (procesador == null) {
            procesador = new StringFilter();
        }
        return procesador;
    }

    public void setProcesador(StringFilter procesador) {
        this.procesador = procesador;
    }

    public BooleanFilter getIsAlarm() {
        return isAlarm;
    }

    public BooleanFilter isAlarm() {
        if (isAlarm == null) {
            isAlarm = new BooleanFilter();
        }
        return isAlarm;
    }

    public void setIsAlarm(BooleanFilter isAlarm) {
        this.isAlarm = isAlarm;
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
        final EventTypeCriteria that = (EventTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventCode, that.eventCode) &&
            Objects.equals(sasCode, that.sasCode) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isStorable, that.isStorable) &&
            Objects.equals(isPriority, that.isPriority) &&
            Objects.equals(procesador, that.procesador) &&
            Objects.equals(isAlarm, that.isAlarm) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventCode, sasCode, description, isStorable, isPriority, procesador, isAlarm, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventCode != null ? "eventCode=" + eventCode + ", " : "") +
            (sasCode != null ? "sasCode=" + sasCode + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isStorable != null ? "isStorable=" + isStorable + ", " : "") +
            (isPriority != null ? "isPriority=" + isPriority + ", " : "") +
            (procesador != null ? "procesador=" + procesador + ", " : "") +
            (isAlarm != null ? "isAlarm=" + isAlarm + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
