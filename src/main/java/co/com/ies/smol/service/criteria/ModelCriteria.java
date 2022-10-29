package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.Model} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.ModelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /models?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private BooleanFilter subtractBonus;

    private BigDecimalFilter collectionCeil;

    private BigDecimalFilter rolloverLimit;

    private LongFilter manufacturerId;

    private LongFilter formulaId;

    private Boolean distinct;

    public ModelCriteria() {}

    public ModelCriteria(ModelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.subtractBonus = other.subtractBonus == null ? null : other.subtractBonus.copy();
        this.collectionCeil = other.collectionCeil == null ? null : other.collectionCeil.copy();
        this.rolloverLimit = other.rolloverLimit == null ? null : other.rolloverLimit.copy();
        this.manufacturerId = other.manufacturerId == null ? null : other.manufacturerId.copy();
        this.formulaId = other.formulaId == null ? null : other.formulaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ModelCriteria copy() {
        return new ModelCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public BooleanFilter getSubtractBonus() {
        return subtractBonus;
    }

    public BooleanFilter subtractBonus() {
        if (subtractBonus == null) {
            subtractBonus = new BooleanFilter();
        }
        return subtractBonus;
    }

    public void setSubtractBonus(BooleanFilter subtractBonus) {
        this.subtractBonus = subtractBonus;
    }

    public BigDecimalFilter getCollectionCeil() {
        return collectionCeil;
    }

    public BigDecimalFilter collectionCeil() {
        if (collectionCeil == null) {
            collectionCeil = new BigDecimalFilter();
        }
        return collectionCeil;
    }

    public void setCollectionCeil(BigDecimalFilter collectionCeil) {
        this.collectionCeil = collectionCeil;
    }

    public BigDecimalFilter getRolloverLimit() {
        return rolloverLimit;
    }

    public BigDecimalFilter rolloverLimit() {
        if (rolloverLimit == null) {
            rolloverLimit = new BigDecimalFilter();
        }
        return rolloverLimit;
    }

    public void setRolloverLimit(BigDecimalFilter rolloverLimit) {
        this.rolloverLimit = rolloverLimit;
    }

    public LongFilter getManufacturerId() {
        return manufacturerId;
    }

    public LongFilter manufacturerId() {
        if (manufacturerId == null) {
            manufacturerId = new LongFilter();
        }
        return manufacturerId;
    }

    public void setManufacturerId(LongFilter manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public LongFilter getFormulaId() {
        return formulaId;
    }

    public LongFilter formulaId() {
        if (formulaId == null) {
            formulaId = new LongFilter();
        }
        return formulaId;
    }

    public void setFormulaId(LongFilter formulaId) {
        this.formulaId = formulaId;
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
        final ModelCriteria that = (ModelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(subtractBonus, that.subtractBonus) &&
            Objects.equals(collectionCeil, that.collectionCeil) &&
            Objects.equals(rolloverLimit, that.rolloverLimit) &&
            Objects.equals(manufacturerId, that.manufacturerId) &&
            Objects.equals(formulaId, that.formulaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, subtractBonus, collectionCeil, rolloverLimit, manufacturerId, formulaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (subtractBonus != null ? "subtractBonus=" + subtractBonus + ", " : "") +
            (collectionCeil != null ? "collectionCeil=" + collectionCeil + ", " : "") +
            (rolloverLimit != null ? "rolloverLimit=" + rolloverLimit + ", " : "") +
            (manufacturerId != null ? "manufacturerId=" + manufacturerId + ", " : "") +
            (formulaId != null ? "formulaId=" + formulaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
