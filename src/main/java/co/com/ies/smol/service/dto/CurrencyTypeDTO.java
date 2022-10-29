package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.CurrencyType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurrencyTypeDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String description;

    @Size(max = 50)
    private String name;

    private Boolean isPriority;

    @Size(max = 50)
    private String location;

    private Float exchangeRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPriority() {
        return isPriority;
    }

    public void setIsPriority(Boolean isPriority) {
        this.isPriority = isPriority;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyTypeDTO)) {
            return false;
        }

        CurrencyTypeDTO currencyTypeDTO = (CurrencyTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currencyTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyTypeDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            ", isPriority='" + getIsPriority() + "'" +
            ", location='" + getLocation() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            "}";
    }
}
