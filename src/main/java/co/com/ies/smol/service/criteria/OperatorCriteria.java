package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.Operator} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.OperatorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /operators?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperatorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter permitDescription;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private StringFilter nit;

    private StringFilter contractNumber;

    private StringFilter companyName;

    private StringFilter brand;

    private LongFilter municipalityId;

    private Boolean distinct;

    public OperatorCriteria() {}

    public OperatorCriteria(OperatorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.permitDescription = other.permitDescription == null ? null : other.permitDescription.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.nit = other.nit == null ? null : other.nit.copy();
        this.contractNumber = other.contractNumber == null ? null : other.contractNumber.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.brand = other.brand == null ? null : other.brand.copy();
        this.municipalityId = other.municipalityId == null ? null : other.municipalityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OperatorCriteria copy() {
        return new OperatorCriteria(this);
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

    public StringFilter getPermitDescription() {
        return permitDescription;
    }

    public StringFilter permitDescription() {
        if (permitDescription == null) {
            permitDescription = new StringFilter();
        }
        return permitDescription;
    }

    public void setPermitDescription(StringFilter permitDescription) {
        this.permitDescription = permitDescription;
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

    public StringFilter getNit() {
        return nit;
    }

    public StringFilter nit() {
        if (nit == null) {
            nit = new StringFilter();
        }
        return nit;
    }

    public void setNit(StringFilter nit) {
        this.nit = nit;
    }

    public StringFilter getContractNumber() {
        return contractNumber;
    }

    public StringFilter contractNumber() {
        if (contractNumber == null) {
            contractNumber = new StringFilter();
        }
        return contractNumber;
    }

    public void setContractNumber(StringFilter contractNumber) {
        this.contractNumber = contractNumber;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public StringFilter companyName() {
        if (companyName == null) {
            companyName = new StringFilter();
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public StringFilter brand() {
        if (brand == null) {
            brand = new StringFilter();
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
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
        final OperatorCriteria that = (OperatorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(permitDescription, that.permitDescription) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(nit, that.nit) &&
            Objects.equals(contractNumber, that.contractNumber) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(municipalityId, that.municipalityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permitDescription, startDate, endDate, nit, contractNumber, companyName, brand, municipalityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperatorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (permitDescription != null ? "permitDescription=" + permitDescription + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (nit != null ? "nit=" + nit + ", " : "") +
            (contractNumber != null ? "contractNumber=" + contractNumber + ", " : "") +
            (companyName != null ? "companyName=" + companyName + ", " : "") +
            (brand != null ? "brand=" + brand + ", " : "") +
            (municipalityId != null ? "municipalityId=" + municipalityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
