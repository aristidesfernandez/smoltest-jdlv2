import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../operational-properties-establishment.test-samples';

import { OperationalPropertiesEstablishmentFormService } from './operational-properties-establishment-form.service';

describe('OperationalPropertiesEstablishment Form Service', () => {
  let service: OperationalPropertiesEstablishmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OperationalPropertiesEstablishmentFormService);
  });

  describe('Service methods', () => {
    describe('createOperationalPropertiesEstablishmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOperationalPropertiesEstablishmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            keyOperatingProperty: expect.any(Object),
            establishment: expect.any(Object),
          })
        );
      });

      it('passing IOperationalPropertiesEstablishment should create a new form with FormGroup', () => {
        const formGroup = service.createOperationalPropertiesEstablishmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            keyOperatingProperty: expect.any(Object),
            establishment: expect.any(Object),
          })
        );
      });
    });

    describe('getOperationalPropertiesEstablishment', () => {
      it('should return NewOperationalPropertiesEstablishment for default OperationalPropertiesEstablishment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOperationalPropertiesEstablishmentFormGroup(sampleWithNewData);

        const operationalPropertiesEstablishment = service.getOperationalPropertiesEstablishment(formGroup) as any;

        expect(operationalPropertiesEstablishment).toMatchObject(sampleWithNewData);
      });

      it('should return NewOperationalPropertiesEstablishment for empty OperationalPropertiesEstablishment initial value', () => {
        const formGroup = service.createOperationalPropertiesEstablishmentFormGroup();

        const operationalPropertiesEstablishment = service.getOperationalPropertiesEstablishment(formGroup) as any;

        expect(operationalPropertiesEstablishment).toMatchObject({});
      });

      it('should return IOperationalPropertiesEstablishment', () => {
        const formGroup = service.createOperationalPropertiesEstablishmentFormGroup(sampleWithRequiredData);

        const operationalPropertiesEstablishment = service.getOperationalPropertiesEstablishment(formGroup) as any;

        expect(operationalPropertiesEstablishment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOperationalPropertiesEstablishment should not enable id FormControl', () => {
        const formGroup = service.createOperationalPropertiesEstablishmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOperationalPropertiesEstablishment should disable id FormControl', () => {
        const formGroup = service.createOperationalPropertiesEstablishmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
