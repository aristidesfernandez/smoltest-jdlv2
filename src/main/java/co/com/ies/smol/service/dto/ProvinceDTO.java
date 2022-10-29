package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.Province} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProvinceDTO implements Serializable {

    private Long id;

    @Size(max = 25)
    private String code;

    @Size(max = 50)
    private String name;

    @Size(max = 25)
    private String daneCode;

    @Size(max = 15)
    private String phoneId;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDaneCode() {
        return daneCode;
    }

    public void setDaneCode(String daneCode) {
        this.daneCode = daneCode;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProvinceDTO)) {
            return false;
        }

        ProvinceDTO provinceDTO = (ProvinceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, provinceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinceDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", daneCode='" + getDaneCode() + "'" +
            ", phoneId='" + getPhoneId() + "'" +
            ", country=" + getCountry() +
            "}";
    }
}
