package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.Formula} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormulaDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String description;

    @Size(max = 50)
    private String expression;

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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormulaDTO)) {
            return false;
        }

        FormulaDTO formulaDTO = (FormulaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formulaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormulaDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", expression='" + getExpression() + "'" +
            "}";
    }
}
