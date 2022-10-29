import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../event-device.test-samples';

import { EventDeviceFormService } from './event-device-form.service';

describe('EventDevice Form Service', () => {
  let service: EventDeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventDeviceFormService);
  });

  describe('Service methods', () => {
    describe('createEventDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventDeviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            theoreticalPercentage: expect.any(Object),
            moneyDenomination: expect.any(Object),
            deviceEstablishment: expect.any(Object),
            eventType: expect.any(Object),
          })
        );
      });

      it('passing IEventDevice should create a new form with FormGroup', () => {
        const formGroup = service.createEventDeviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            theoreticalPercentage: expect.any(Object),
            moneyDenomination: expect.any(Object),
            deviceEstablishment: expect.any(Object),
            eventType: expect.any(Object),
          })
        );
      });
    });

    describe('getEventDevice', () => {
      it('should return NewEventDevice for default EventDevice initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEventDeviceFormGroup(sampleWithNewData);

        const eventDevice = service.getEventDevice(formGroup) as any;

        expect(eventDevice).toMatchObject(sampleWithNewData);
      });

      it('should return NewEventDevice for empty EventDevice initial value', () => {
        const formGroup = service.createEventDeviceFormGroup();

        const eventDevice = service.getEventDevice(formGroup) as any;

        expect(eventDevice).toMatchObject({});
      });

      it('should return IEventDevice', () => {
        const formGroup = service.createEventDeviceFormGroup(sampleWithRequiredData);

        const eventDevice = service.getEventDevice(formGroup) as any;

        expect(eventDevice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEventDevice should not enable id FormControl', () => {
        const formGroup = service.createEventDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEventDevice should disable id FormControl', () => {
        const formGroup = service.createEventDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
