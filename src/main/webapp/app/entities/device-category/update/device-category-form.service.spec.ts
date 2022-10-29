import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../device-category.test-samples';

import { DeviceCategoryFormService } from './device-category-form.service';

describe('DeviceCategory Form Service', () => {
  let service: DeviceCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IDeviceCategory should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getDeviceCategory', () => {
      it('should return NewDeviceCategory for default DeviceCategory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeviceCategoryFormGroup(sampleWithNewData);

        const deviceCategory = service.getDeviceCategory(formGroup) as any;

        expect(deviceCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeviceCategory for empty DeviceCategory initial value', () => {
        const formGroup = service.createDeviceCategoryFormGroup();

        const deviceCategory = service.getDeviceCategory(formGroup) as any;

        expect(deviceCategory).toMatchObject({});
      });

      it('should return IDeviceCategory', () => {
        const formGroup = service.createDeviceCategoryFormGroup(sampleWithRequiredData);

        const deviceCategory = service.getDeviceCategory(formGroup) as any;

        expect(deviceCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeviceCategory should not enable id FormControl', () => {
        const formGroup = service.createDeviceCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeviceCategory should disable id FormControl', () => {
        const formGroup = service.createDeviceCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
