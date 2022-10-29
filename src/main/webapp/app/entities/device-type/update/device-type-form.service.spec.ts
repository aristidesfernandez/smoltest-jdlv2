import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../device-type.test-samples';

import { DeviceTypeFormService } from './device-type-form.service';

describe('DeviceType Form Service', () => {
  let service: DeviceTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceTypeFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IDeviceType should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getDeviceType', () => {
      it('should return NewDeviceType for default DeviceType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeviceTypeFormGroup(sampleWithNewData);

        const deviceType = service.getDeviceType(formGroup) as any;

        expect(deviceType).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeviceType for empty DeviceType initial value', () => {
        const formGroup = service.createDeviceTypeFormGroup();

        const deviceType = service.getDeviceType(formGroup) as any;

        expect(deviceType).toMatchObject({});
      });

      it('should return IDeviceType', () => {
        const formGroup = service.createDeviceTypeFormGroup(sampleWithRequiredData);

        const deviceType = service.getDeviceType(formGroup) as any;

        expect(deviceType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeviceType should not enable id FormControl', () => {
        const formGroup = service.createDeviceTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeviceType should disable id FormControl', () => {
        const formGroup = service.createDeviceTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
