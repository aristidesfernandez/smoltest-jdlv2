import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../device.test-samples';

import { DeviceFormService } from './device-form.service';

describe('Device Form Service', () => {
  let service: DeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serial: expect.any(Object),
            isProtocolEsdcs: expect.any(Object),
            numberPlayedReport: expect.any(Object),
            sasDenomination: expect.any(Object),
            isMultigame: expect.any(Object),
            isMultiDenomination: expect.any(Object),
            isRetanqueo: expect.any(Object),
            state: expect.any(Object),
            theoreticalHold: expect.any(Object),
            sasIdentifier: expect.any(Object),
            creditLimit: expect.any(Object),
            hasHooper: expect.any(Object),
            coljuegosCode: expect.any(Object),
            fabricationDate: expect.any(Object),
            currentToken: expect.any(Object),
            denominationTito: expect.any(Object),
            endLostCommunication: expect.any(Object),
            startLostCommunication: expect.any(Object),
            reportMultiplier: expect.any(Object),
            nuid: expect.any(Object),
            payManualPrize: expect.any(Object),
            manualHandpay: expect.any(Object),
            manualJackpot: expect.any(Object),
            manualGameEvent: expect.any(Object),
            betCode: expect.any(Object),
            homologationIndicator: expect.any(Object),
            coljuegosModel: expect.any(Object),
            reportable: expect.any(Object),
            aftDenomination: expect.any(Object),
            lastUpdateDate: expect.any(Object),
            enableRollover: expect.any(Object),
            lastCorruptionDate: expect.any(Object),
            daftDenomination: expect.any(Object),
            prizesEnabled: expect.any(Object),
            currencyTypeId: expect.any(Object),
            isleId: expect.any(Object),
            model: expect.any(Object),
            deviceCategory: expect.any(Object),
            deviceType: expect.any(Object),
            formulaHandpay: expect.any(Object),
            formulaJackpot: expect.any(Object),
          })
        );
      });

      it('passing IDevice should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serial: expect.any(Object),
            isProtocolEsdcs: expect.any(Object),
            numberPlayedReport: expect.any(Object),
            sasDenomination: expect.any(Object),
            isMultigame: expect.any(Object),
            isMultiDenomination: expect.any(Object),
            isRetanqueo: expect.any(Object),
            state: expect.any(Object),
            theoreticalHold: expect.any(Object),
            sasIdentifier: expect.any(Object),
            creditLimit: expect.any(Object),
            hasHooper: expect.any(Object),
            coljuegosCode: expect.any(Object),
            fabricationDate: expect.any(Object),
            currentToken: expect.any(Object),
            denominationTito: expect.any(Object),
            endLostCommunication: expect.any(Object),
            startLostCommunication: expect.any(Object),
            reportMultiplier: expect.any(Object),
            nuid: expect.any(Object),
            payManualPrize: expect.any(Object),
            manualHandpay: expect.any(Object),
            manualJackpot: expect.any(Object),
            manualGameEvent: expect.any(Object),
            betCode: expect.any(Object),
            homologationIndicator: expect.any(Object),
            coljuegosModel: expect.any(Object),
            reportable: expect.any(Object),
            aftDenomination: expect.any(Object),
            lastUpdateDate: expect.any(Object),
            enableRollover: expect.any(Object),
            lastCorruptionDate: expect.any(Object),
            daftDenomination: expect.any(Object),
            prizesEnabled: expect.any(Object),
            currencyTypeId: expect.any(Object),
            isleId: expect.any(Object),
            model: expect.any(Object),
            deviceCategory: expect.any(Object),
            deviceType: expect.any(Object),
            formulaHandpay: expect.any(Object),
            formulaJackpot: expect.any(Object),
          })
        );
      });
    });

    describe('getDevice', () => {
      it('should return NewDevice for default Device initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeviceFormGroup(sampleWithNewData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithNewData);
      });

      it('should return NewDevice for empty Device initial value', () => {
        const formGroup = service.createDeviceFormGroup();

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject({});
      });

      it('should return IDevice', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDevice should not enable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDevice should disable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
