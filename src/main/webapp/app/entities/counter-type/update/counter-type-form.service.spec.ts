import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../counter-type.test-samples';

import { CounterTypeFormService } from './counter-type-form.service';

describe('CounterType Form Service', () => {
  let service: CounterTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CounterTypeFormService);
  });

  describe('Service methods', () => {
    describe('createCounterTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCounterTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            counterCode: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            includedInFormula: expect.any(Object),
            prize: expect.any(Object),
            category: expect.any(Object),
            udteWaitTime: expect.any(Object),
          })
        );
      });

      it('passing ICounterType should create a new form with FormGroup', () => {
        const formGroup = service.createCounterTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            counterCode: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            includedInFormula: expect.any(Object),
            prize: expect.any(Object),
            category: expect.any(Object),
            udteWaitTime: expect.any(Object),
          })
        );
      });
    });

    describe('getCounterType', () => {
      it('should return NewCounterType for default CounterType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCounterTypeFormGroup(sampleWithNewData);

        const counterType = service.getCounterType(formGroup) as any;

        expect(counterType).toMatchObject(sampleWithNewData);
      });

      it('should return NewCounterType for empty CounterType initial value', () => {
        const formGroup = service.createCounterTypeFormGroup();

        const counterType = service.getCounterType(formGroup) as any;

        expect(counterType).toMatchObject({});
      });

      it('should return ICounterType', () => {
        const formGroup = service.createCounterTypeFormGroup(sampleWithRequiredData);

        const counterType = service.getCounterType(formGroup) as any;

        expect(counterType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICounterType should not enable counterCode FormControl', () => {
        const formGroup = service.createCounterTypeFormGroup();
        expect(formGroup.controls.counterCode.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.counterCode.disabled).toBe(true);
      });

      it('passing NewCounterType should disable counterCode FormControl', () => {
        const formGroup = service.createCounterTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.counterCode.disabled).toBe(true);

        service.resetForm(formGroup, { counterCode: null });

        expect(formGroup.controls.counterCode.disabled).toBe(true);
      });
    });
  });
});
