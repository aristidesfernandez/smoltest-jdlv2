import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../device-establishment.test-samples';

import { DeviceEstablishmentFormService } from './device-establishment-form.service';

describe('DeviceEstablishment Form Service', () => {
  let service: DeviceEstablishmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceEstablishmentFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceEstablishmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceEstablishmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            registrationAt: expect.any(Object),
            departureAt: expect.any(Object),
            deviceNumber: expect.any(Object),
            consecutiveDevice: expect.any(Object),
            establishmentId: expect.any(Object),
            negativeAward: expect.any(Object),
            device: expect.any(Object),
          })
        );
      });

      it('passing IDeviceEstablishment should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceEstablishmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            registrationAt: expect.any(Object),
            departureAt: expect.any(Object),
            deviceNumber: expect.any(Object),
            consecutiveDevice: expect.any(Object),
            establishmentId: expect.any(Object),
            negativeAward: expect.any(Object),
            device: expect.any(Object),
          })
        );
      });
    });

    describe('getDeviceEstablishment', () => {
      it('should return NewDeviceEstablishment for default DeviceEstablishment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeviceEstablishmentFormGroup(sampleWithNewData);

        const deviceEstablishment = service.getDeviceEstablishment(formGroup) as any;

        expect(deviceEstablishment).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeviceEstablishment for empty DeviceEstablishment initial value', () => {
        const formGroup = service.createDeviceEstablishmentFormGroup();

        const deviceEstablishment = service.getDeviceEstablishment(formGroup) as any;

        expect(deviceEstablishment).toMatchObject({});
      });

      it('should return IDeviceEstablishment', () => {
        const formGroup = service.createDeviceEstablishmentFormGroup(sampleWithRequiredData);

        const deviceEstablishment = service.getDeviceEstablishment(formGroup) as any;

        expect(deviceEstablishment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeviceEstablishment should not enable id FormControl', () => {
        const formGroup = service.createDeviceEstablishmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeviceEstablishment should disable id FormControl', () => {
        const formGroup = service.createDeviceEstablishmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
