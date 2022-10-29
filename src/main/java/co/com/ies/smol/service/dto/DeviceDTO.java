package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.Device} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(max = 50)
    private String serial;

    private Boolean isProtocolEsdcs;

    private Integer numberPlayedReport;

    private BigDecimal sasDenomination;

    private Boolean isMultigame;

    private Boolean isMultiDenomination;

    private Boolean isRetanqueo;

    @Size(max = 30)
    private String state;

    private BigDecimal theoreticalHold;

    private Integer sasIdentifier;

    private Long creditLimit;

    private Boolean hasHooper;

    @Size(max = 50)
    private String coljuegosCode;

    private LocalDate fabricationDate;

    private BigDecimal currentToken;

    private BigDecimal denominationTito;

    private ZonedDateTime endLostCommunication;

    private ZonedDateTime startLostCommunication;

    private BigDecimal reportMultiplier;

    private String nuid;

    private Boolean payManualPrize;

    private Boolean manualHandpay;

    private Boolean manualJackpot;

    private Boolean manualGameEvent;

    private String betCode;

    private Boolean homologationIndicator;

    @Size(max = 50)
    private String coljuegosModel;

    private Boolean reportable;

    private BigDecimal aftDenomination;

    private ZonedDateTime lastUpdateDate;

    private Boolean enableRollover;

    private ZonedDateTime lastCorruptionDate;

    private BigDecimal daftDenomination;

    private Boolean prizesEnabled;

    private Integer currencyTypeId;

    private Integer isleId;

    private ModelDTO model;

    private DeviceCategoryDTO deviceCategory;

    private DeviceTypeDTO deviceType;

    private FormulaDTO formulaHandpay;

    private FormulaDTO formulaJackpot;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Boolean getIsProtocolEsdcs() {
        return isProtocolEsdcs;
    }

    public void setIsProtocolEsdcs(Boolean isProtocolEsdcs) {
        this.isProtocolEsdcs = isProtocolEsdcs;
    }

    public Integer getNumberPlayedReport() {
        return numberPlayedReport;
    }

    public void setNumberPlayedReport(Integer numberPlayedReport) {
        this.numberPlayedReport = numberPlayedReport;
    }

    public BigDecimal getSasDenomination() {
        return sasDenomination;
    }

    public void setSasDenomination(BigDecimal sasDenomination) {
        this.sasDenomination = sasDenomination;
    }

    public Boolean getIsMultigame() {
        return isMultigame;
    }

    public void setIsMultigame(Boolean isMultigame) {
        this.isMultigame = isMultigame;
    }

    public Boolean getIsMultiDenomination() {
        return isMultiDenomination;
    }

    public void setIsMultiDenomination(Boolean isMultiDenomination) {
        this.isMultiDenomination = isMultiDenomination;
    }

    public Boolean getIsRetanqueo() {
        return isRetanqueo;
    }

    public void setIsRetanqueo(Boolean isRetanqueo) {
        this.isRetanqueo = isRetanqueo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTheoreticalHold() {
        return theoreticalHold;
    }

    public void setTheoreticalHold(BigDecimal theoreticalHold) {
        this.theoreticalHold = theoreticalHold;
    }

    public Integer getSasIdentifier() {
        return sasIdentifier;
    }

    public void setSasIdentifier(Integer sasIdentifier) {
        this.sasIdentifier = sasIdentifier;
    }

    public Long getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Long creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean getHasHooper() {
        return hasHooper;
    }

    public void setHasHooper(Boolean hasHooper) {
        this.hasHooper = hasHooper;
    }

    public String getColjuegosCode() {
        return coljuegosCode;
    }

    public void setColjuegosCode(String coljuegosCode) {
        this.coljuegosCode = coljuegosCode;
    }

    public LocalDate getFabricationDate() {
        return fabricationDate;
    }

    public void setFabricationDate(LocalDate fabricationDate) {
        this.fabricationDate = fabricationDate;
    }

    public BigDecimal getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(BigDecimal currentToken) {
        this.currentToken = currentToken;
    }

    public BigDecimal getDenominationTito() {
        return denominationTito;
    }

    public void setDenominationTito(BigDecimal denominationTito) {
        this.denominationTito = denominationTito;
    }

    public ZonedDateTime getEndLostCommunication() {
        return endLostCommunication;
    }

    public void setEndLostCommunication(ZonedDateTime endLostCommunication) {
        this.endLostCommunication = endLostCommunication;
    }

    public ZonedDateTime getStartLostCommunication() {
        return startLostCommunication;
    }

    public void setStartLostCommunication(ZonedDateTime startLostCommunication) {
        this.startLostCommunication = startLostCommunication;
    }

    public BigDecimal getReportMultiplier() {
        return reportMultiplier;
    }

    public void setReportMultiplier(BigDecimal reportMultiplier) {
        this.reportMultiplier = reportMultiplier;
    }

    public String getNuid() {
        return nuid;
    }

    public void setNuid(String nuid) {
        this.nuid = nuid;
    }

    public Boolean getPayManualPrize() {
        return payManualPrize;
    }

    public void setPayManualPrize(Boolean payManualPrize) {
        this.payManualPrize = payManualPrize;
    }

    public Boolean getManualHandpay() {
        return manualHandpay;
    }

    public void setManualHandpay(Boolean manualHandpay) {
        this.manualHandpay = manualHandpay;
    }

    public Boolean getManualJackpot() {
        return manualJackpot;
    }

    public void setManualJackpot(Boolean manualJackpot) {
        this.manualJackpot = manualJackpot;
    }

    public Boolean getManualGameEvent() {
        return manualGameEvent;
    }

    public void setManualGameEvent(Boolean manualGameEvent) {
        this.manualGameEvent = manualGameEvent;
    }

    public String getBetCode() {
        return betCode;
    }

    public void setBetCode(String betCode) {
        this.betCode = betCode;
    }

    public Boolean getHomologationIndicator() {
        return homologationIndicator;
    }

    public void setHomologationIndicator(Boolean homologationIndicator) {
        this.homologationIndicator = homologationIndicator;
    }

    public String getColjuegosModel() {
        return coljuegosModel;
    }

    public void setColjuegosModel(String coljuegosModel) {
        this.coljuegosModel = coljuegosModel;
    }

    public Boolean getReportable() {
        return reportable;
    }

    public void setReportable(Boolean reportable) {
        this.reportable = reportable;
    }

    public BigDecimal getAftDenomination() {
        return aftDenomination;
    }

    public void setAftDenomination(BigDecimal aftDenomination) {
        this.aftDenomination = aftDenomination;
    }

    public ZonedDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Boolean getEnableRollover() {
        return enableRollover;
    }

    public void setEnableRollover(Boolean enableRollover) {
        this.enableRollover = enableRollover;
    }

    public ZonedDateTime getLastCorruptionDate() {
        return lastCorruptionDate;
    }

    public void setLastCorruptionDate(ZonedDateTime lastCorruptionDate) {
        this.lastCorruptionDate = lastCorruptionDate;
    }

    public BigDecimal getDaftDenomination() {
        return daftDenomination;
    }

    public void setDaftDenomination(BigDecimal daftDenomination) {
        this.daftDenomination = daftDenomination;
    }

    public Boolean getPrizesEnabled() {
        return prizesEnabled;
    }

    public void setPrizesEnabled(Boolean prizesEnabled) {
        this.prizesEnabled = prizesEnabled;
    }

    public Integer getCurrencyTypeId() {
        return currencyTypeId;
    }

    public void setCurrencyTypeId(Integer currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    public Integer getIsleId() {
        return isleId;
    }

    public void setIsleId(Integer isleId) {
        this.isleId = isleId;
    }

    public ModelDTO getModel() {
        return model;
    }

    public void setModel(ModelDTO model) {
        this.model = model;
    }

    public DeviceCategoryDTO getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(DeviceCategoryDTO deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public DeviceTypeDTO getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeDTO deviceType) {
        this.deviceType = deviceType;
    }

    public FormulaDTO getFormulaHandpay() {
        return formulaHandpay;
    }

    public void setFormulaHandpay(FormulaDTO formulaHandpay) {
        this.formulaHandpay = formulaHandpay;
    }

    public FormulaDTO getFormulaJackpot() {
        return formulaJackpot;
    }

    public void setFormulaJackpot(FormulaDTO formulaJackpot) {
        this.formulaJackpot = formulaJackpot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id='" + getId() + "'" +
            ", serial='" + getSerial() + "'" +
            ", isProtocolEsdcs='" + getIsProtocolEsdcs() + "'" +
            ", numberPlayedReport=" + getNumberPlayedReport() +
            ", sasDenomination=" + getSasDenomination() +
            ", isMultigame='" + getIsMultigame() + "'" +
            ", isMultiDenomination='" + getIsMultiDenomination() + "'" +
            ", isRetanqueo='" + getIsRetanqueo() + "'" +
            ", state='" + getState() + "'" +
            ", theoreticalHold=" + getTheoreticalHold() +
            ", sasIdentifier=" + getSasIdentifier() +
            ", creditLimit=" + getCreditLimit() +
            ", hasHooper='" + getHasHooper() + "'" +
            ", coljuegosCode='" + getColjuegosCode() + "'" +
            ", fabricationDate='" + getFabricationDate() + "'" +
            ", currentToken=" + getCurrentToken() +
            ", denominationTito=" + getDenominationTito() +
            ", endLostCommunication='" + getEndLostCommunication() + "'" +
            ", startLostCommunication='" + getStartLostCommunication() + "'" +
            ", reportMultiplier=" + getReportMultiplier() +
            ", nuid='" + getNuid() + "'" +
            ", payManualPrize='" + getPayManualPrize() + "'" +
            ", manualHandpay='" + getManualHandpay() + "'" +
            ", manualJackpot='" + getManualJackpot() + "'" +
            ", manualGameEvent='" + getManualGameEvent() + "'" +
            ", betCode='" + getBetCode() + "'" +
            ", homologationIndicator='" + getHomologationIndicator() + "'" +
            ", coljuegosModel='" + getColjuegosModel() + "'" +
            ", reportable='" + getReportable() + "'" +
            ", aftDenomination=" + getAftDenomination() +
            ", lastUpdateDate='" + getLastUpdateDate() + "'" +
            ", enableRollover='" + getEnableRollover() + "'" +
            ", lastCorruptionDate='" + getLastCorruptionDate() + "'" +
            ", daftDenomination=" + getDaftDenomination() +
            ", prizesEnabled='" + getPrizesEnabled() + "'" +
            ", currencyTypeId=" + getCurrencyTypeId() +
            ", isleId=" + getIsleId() +
            ", model=" + getModel() +
            ", deviceCategory=" + getDeviceCategory() +
            ", deviceType=" + getDeviceType() +
            ", formulaHandpay=" + getFormulaHandpay() +
            ", formulaJackpot=" + getFormulaJackpot() +
            "}";
    }
}
