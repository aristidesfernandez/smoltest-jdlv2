package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.Manufacturer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManufacturerDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String code;

    @Size(max = 50)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManufacturerDTO)) {
            return false;
        }

        ManufacturerDTO manufacturerDTO = (ManufacturerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manufacturerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManufacturerDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
