import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../establishment.test-samples';

import { EstablishmentFormService } from './establishment-form.service';

describe('Establishment Form Service', () => {
  let service: EstablishmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EstablishmentFormService);
  });

  describe('Service methods', () => {
    describe('createEstablishmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEstablishmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            liquidationTime: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            neighborhood: expect.any(Object),
            address: expect.any(Object),
            coljuegosCode: expect.any(Object),
            startTime: expect.any(Object),
            closeTime: expect.any(Object),
            longitude: expect.any(Object),
            latitude: expect.any(Object),
            mercantileRegistration: expect.any(Object),
            operator: expect.any(Object),
            municipality: expect.any(Object),
          })
        );
      });

      it('passing IEstablishment should create a new form with FormGroup', () => {
        const formGroup = service.createEstablishmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            liquidationTime: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            neighborhood: expect.any(Object),
            address: expect.any(Object),
            coljuegosCode: expect.any(Object),
            startTime: expect.any(Object),
            closeTime: expect.any(Object),
            longitude: expect.any(Object),
            latitude: expect.any(Object),
            mercantileRegistration: expect.any(Object),
            operator: expect.any(Object),
            municipality: expect.any(Object),
          })
        );
      });
    });

    describe('getEstablishment', () => {
      it('should return NewEstablishment for default Establishment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEstablishmentFormGroup(sampleWithNewData);

        const establishment = service.getEstablishment(formGroup) as any;

        expect(establishment).toMatchObject(sampleWithNewData);
      });

      it('should return NewEstablishment for empty Establishment initial value', () => {
        const formGroup = service.createEstablishmentFormGroup();

        const establishment = service.getEstablishment(formGroup) as any;

        expect(establishment).toMatchObject({});
      });

      it('should return IEstablishment', () => {
        const formGroup = service.createEstablishmentFormGroup(sampleWithRequiredData);

        const establishment = service.getEstablishment(formGroup) as any;

        expect(establishment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEstablishment should not enable id FormControl', () => {
        const formGroup = service.createEstablishmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEstablishment should disable id FormControl', () => {
        const formGroup = service.createEstablishmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
