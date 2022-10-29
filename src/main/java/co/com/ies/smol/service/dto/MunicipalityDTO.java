package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.Municipality} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MunicipalityDTO implements Serializable {

    private Long id;

    @Size(max = 25)
    private String code;

    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 25)
    private String daneCode;

    private ProvinceDTO province;

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

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MunicipalityDTO)) {
            return false;
        }

        MunicipalityDTO municipalityDTO = (MunicipalityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, municipalityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MunicipalityDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", daneCode='" + getDaneCode() + "'" +
            ", province=" + getProvince() +
            "}";
    }
}
