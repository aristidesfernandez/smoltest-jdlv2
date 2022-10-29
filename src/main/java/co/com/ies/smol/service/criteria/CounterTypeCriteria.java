package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.CounterType} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.CounterTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counter-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CounterTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter counterCode;

    private StringFilter name;

    private StringFilter description;

    private BooleanFilter includedInFormula;

    private BooleanFilter prize;

    private StringFilter category;

    private IntegerFilter udteWaitTime;

    private Boolean distinct;

    public CounterTypeCriteria() {}

    public CounterTypeCriteria(CounterTypeCriteria other) {
        this.counterCode = other.counterCode == null ? null : other.counterCode.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.includedInFormula = other.includedInFormula == null ? null : other.includedInFormula.copy();
        this.prize = other.prize == null ? null : other.prize.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.udteWaitTime = other.udteWaitTime == null ? null : other.udteWaitTime.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CounterTypeCriteria copy() {
        return new CounterTypeCriteria(this);
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

    public BooleanFilter getIncludedInFormula() {
        return includedInFormula;
    }

    public BooleanFilter includedInFormula() {
        if (includedInFormula == null) {
            includedInFormula = new BooleanFilter();
        }
        return includedInFormula;
    }

    public void setIncludedInFormula(BooleanFilter includedInFormula) {
        this.includedInFormula = includedInFormula;
    }

    public BooleanFilter getPrize() {
        return prize;
    }

    public BooleanFilter prize() {
        if (prize == null) {
            prize = new BooleanFilter();
        }
        return prize;
    }

    public void setPrize(BooleanFilter prize) {
        this.prize = prize;
    }

    public StringFilter getCategory() {
        return category;
    }

    public StringFilter category() {
        if (category == null) {
            category = new StringFilter();
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public IntegerFilter getUdteWaitTime() {
        return udteWaitTime;
    }

    public IntegerFilter udteWaitTime() {
        if (udteWaitTime == null) {
            udteWaitTime = new IntegerFilter();
        }
        return udteWaitTime;
    }

    public void setUdteWaitTime(IntegerFilter udteWaitTime) {
        this.udteWaitTime = udteWaitTime;
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
        final CounterTypeCriteria that = (CounterTypeCriteria) o;
        return (
            Objects.equals(counterCode, that.counterCode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(includedInFormula, that.includedInFormula) &&
            Objects.equals(prize, that.prize) &&
            Objects.equals(category, that.category) &&
            Objects.equals(udteWaitTime, that.udteWaitTime) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterCode, name, description, includedInFormula, prize, category, udteWaitTime, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterTypeCriteria{" +
            (counterCode != null ? "counterCode=" + counterCode + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (includedInFormula != null ? "includedInFormula=" + includedInFormula + ", " : "") +
            (prize != null ? "prize=" + prize + ", " : "") +
            (category != null ? "category=" + category + ", " : "") +
            (udteWaitTime != null ? "udteWaitTime=" + udteWaitTime + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
