package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.EventType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String eventCode;

    @Size(max = 50)
    private String sasCode;

    @Size(max = 100)
    private String description;

    private Boolean isStorable;

    private Boolean isPriority;

    @Size(max = 100)
    private String procesador;

    private Boolean isAlarm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getSasCode() {
        return sasCode;
    }

    public void setSasCode(String sasCode) {
        this.sasCode = sasCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsStorable() {
        return isStorable;
    }

    public void setIsStorable(Boolean isStorable) {
        this.isStorable = isStorable;
    }

    public Boolean getIsPriority() {
        return isPriority;
    }

    public void setIsPriority(Boolean isPriority) {
        this.isPriority = isPriority;
    }

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public Boolean getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(Boolean isAlarm) {
        this.isAlarm = isAlarm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTypeDTO)) {
            return false;
        }

        EventTypeDTO eventTypeDTO = (EventTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTypeDTO{" +
            "id=" + getId() +
            ", eventCode='" + getEventCode() + "'" +
            ", sasCode='" + getSasCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", isStorable='" + getIsStorable() + "'" +
            ", isPriority='" + getIsPriority() + "'" +
            ", procesador='" + getProcesador() + "'" +
            ", isAlarm='" + getIsAlarm() + "'" +
            "}";
    }
}
