import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../device-interface.test-samples';

import { DeviceInterfaceFormService } from './device-interface-form.service';

describe('DeviceInterface Form Service', () => {
  let service: DeviceInterfaceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceInterfaceFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceInterfaceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceInterfaceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            establishmentId: expect.any(Object),
            state: expect.any(Object),
            device: expect.any(Object),
            interfaceBoard: expect.any(Object),
          })
        );
      });

      it('passing IDeviceInterface should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceInterfaceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            establishmentId: expect.any(Object),
            state: expect.any(Object),
            device: expect.any(Object),
            interfaceBoard: expect.any(Object),
          })
        );
      });
    });

    describe('getDeviceInterface', () => {
      it('should return NewDeviceInterface for default DeviceInterface initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeviceInterfaceFormGroup(sampleWithNewData);

        const deviceInterface = service.getDeviceInterface(formGroup) as any;

        expect(deviceInterface).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeviceInterface for empty DeviceInterface initial value', () => {
        const formGroup = service.createDeviceInterfaceFormGroup();

        const deviceInterface = service.getDeviceInterface(formGroup) as any;

        expect(deviceInterface).toMatchObject({});
      });

      it('should return IDeviceInterface', () => {
        const formGroup = service.createDeviceInterfaceFormGroup(sampleWithRequiredData);

        const deviceInterface = service.getDeviceInterface(formGroup) as any;

        expect(deviceInterface).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeviceInterface should not enable id FormControl', () => {
        const formGroup = service.createDeviceInterfaceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeviceInterface should disable id FormControl', () => {
        const formGroup = service.createDeviceInterfaceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
