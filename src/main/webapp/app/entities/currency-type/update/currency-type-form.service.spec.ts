import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../currency-type.test-samples';

import { CurrencyTypeFormService } from './currency-type-form.service';

describe('CurrencyType Form Service', () => {
  let service: CurrencyTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurrencyTypeFormService);
  });

  describe('Service methods', () => {
    describe('createCurrencyTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCurrencyTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
            isPriority: expect.any(Object),
            location: expect.any(Object),
            exchangeRate: expect.any(Object),
          })
        );
      });

      it('passing ICurrencyType should create a new form with FormGroup', () => {
        const formGroup = service.createCurrencyTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
            isPriority: expect.any(Object),
            location: expect.any(Object),
            exchangeRate: expect.any(Object),
          })
        );
      });
    });

    describe('getCurrencyType', () => {
      it('should return NewCurrencyType for default CurrencyType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCurrencyTypeFormGroup(sampleWithNewData);

        const currencyType = service.getCurrencyType(formGroup) as any;

        expect(currencyType).toMatchObject(sampleWithNewData);
      });

      it('should return NewCurrencyType for empty CurrencyType initial value', () => {
        const formGroup = service.createCurrencyTypeFormGroup();

        const currencyType = service.getCurrencyType(formGroup) as any;

        expect(currencyType).toMatchObject({});
      });

      it('should return ICurrencyType', () => {
        const formGroup = service.createCurrencyTypeFormGroup(sampleWithRequiredData);

        const currencyType = service.getCurrencyType(formGroup) as any;

        expect(currencyType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICurrencyType should not enable id FormControl', () => {
        const formGroup = service.createCurrencyTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCurrencyType should disable id FormControl', () => {
        const formGroup = service.createCurrencyTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
