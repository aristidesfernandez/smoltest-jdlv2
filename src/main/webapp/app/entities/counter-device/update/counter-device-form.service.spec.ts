import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../counter-device.test-samples';

import { CounterDeviceFormService } from './counter-device-form.service';

describe('CounterDevice Form Service', () => {
  let service: CounterDeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CounterDeviceFormService);
  });

  describe('Service methods', () => {
    describe('createCounterDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCounterDeviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            rolloverValue: expect.any(Object),
            creditSale: expect.any(Object),
            manualCounter: expect.any(Object),
            manualMultiplier: expect.any(Object),
            decimalsManualCounter: expect.any(Object),
            device: expect.any(Object),
          })
        );
      });

      it('passing ICounterDevice should create a new form with FormGroup', () => {
        const formGroup = service.createCounterDeviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            rolloverValue: expect.any(Object),
            creditSale: expect.any(Object),
            manualCounter: expect.any(Object),
            manualMultiplier: expect.any(Object),
            decimalsManualCounter: expect.any(Object),
            device: expect.any(Object),
          })
        );
      });
    });

    describe('getCounterDevice', () => {
      it('should return NewCounterDevice for default CounterDevice initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCounterDeviceFormGroup(sampleWithNewData);

        const counterDevice = service.getCounterDevice(formGroup) as any;

        expect(counterDevice).toMatchObject(sampleWithNewData);
      });

      it('should return NewCounterDevice for empty CounterDevice initial value', () => {
        const formGroup = service.createCounterDeviceFormGroup();

        const counterDevice = service.getCounterDevice(formGroup) as any;

        expect(counterDevice).toMatchObject({});
      });

      it('should return ICounterDevice', () => {
        const formGroup = service.createCounterDeviceFormGroup(sampleWithRequiredData);

        const counterDevice = service.getCounterDevice(formGroup) as any;

        expect(counterDevice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICounterDevice should not enable id FormControl', () => {
        const formGroup = service.createCounterDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCounterDevice should disable id FormControl', () => {
        const formGroup = service.createCounterDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
