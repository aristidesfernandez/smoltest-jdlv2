package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.KeyOperatingProperty} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KeyOperatingPropertyDTO implements Serializable {

    private Long id;

    private String description;

    @Size(max = 32)
    private String property;

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

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeyOperatingPropertyDTO)) {
            return false;
        }

        KeyOperatingPropertyDTO keyOperatingPropertyDTO = (KeyOperatingPropertyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, keyOperatingPropertyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KeyOperatingPropertyDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", property='" + getProperty() + "'" +
            "}";
    }
}
