import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../counter-event.test-samples';

import { CounterEventFormService } from './counter-event-form.service';

describe('CounterEvent Form Service', () => {
  let service: CounterEventFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CounterEventFormService);
  });

  describe('Service methods', () => {
    describe('createCounterEventFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCounterEventFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valueCounter: expect.any(Object),
            denominationSale: expect.any(Object),
            counterCode: expect.any(Object),
            eventDevice: expect.any(Object),
          })
        );
      });

      it('passing ICounterEvent should create a new form with FormGroup', () => {
        const formGroup = service.createCounterEventFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valueCounter: expect.any(Object),
            denominationSale: expect.any(Object),
            counterCode: expect.any(Object),
            eventDevice: expect.any(Object),
          })
        );
      });
    });

    describe('getCounterEvent', () => {
      it('should return NewCounterEvent for default CounterEvent initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCounterEventFormGroup(sampleWithNewData);

        const counterEvent = service.getCounterEvent(formGroup) as any;

        expect(counterEvent).toMatchObject(sampleWithNewData);
      });

      it('should return NewCounterEvent for empty CounterEvent initial value', () => {
        const formGroup = service.createCounterEventFormGroup();

        const counterEvent = service.getCounterEvent(formGroup) as any;

        expect(counterEvent).toMatchObject({});
      });

      it('should return ICounterEvent', () => {
        const formGroup = service.createCounterEventFormGroup(sampleWithRequiredData);

        const counterEvent = service.getCounterEvent(formGroup) as any;

        expect(counterEvent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICounterEvent should not enable id FormControl', () => {
        const formGroup = service.createCounterEventFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCounterEvent should disable id FormControl', () => {
        const formGroup = service.createCounterEventFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
