import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../key-operating-property.test-samples';

import { KeyOperatingPropertyFormService } from './key-operating-property-form.service';

describe('KeyOperatingProperty Form Service', () => {
  let service: KeyOperatingPropertyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KeyOperatingPropertyFormService);
  });

  describe('Service methods', () => {
    describe('createKeyOperatingPropertyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKeyOperatingPropertyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            property: expect.any(Object),
          })
        );
      });

      it('passing IKeyOperatingProperty should create a new form with FormGroup', () => {
        const formGroup = service.createKeyOperatingPropertyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            property: expect.any(Object),
          })
        );
      });
    });

    describe('getKeyOperatingProperty', () => {
      it('should return NewKeyOperatingProperty for default KeyOperatingProperty initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createKeyOperatingPropertyFormGroup(sampleWithNewData);

        const keyOperatingProperty = service.getKeyOperatingProperty(formGroup) as any;

        expect(keyOperatingProperty).toMatchObject(sampleWithNewData);
      });

      it('should return NewKeyOperatingProperty for empty KeyOperatingProperty initial value', () => {
        const formGroup = service.createKeyOperatingPropertyFormGroup();

        const keyOperatingProperty = service.getKeyOperatingProperty(formGroup) as any;

        expect(keyOperatingProperty).toMatchObject({});
      });

      it('should return IKeyOperatingProperty', () => {
        const formGroup = service.createKeyOperatingPropertyFormGroup(sampleWithRequiredData);

        const keyOperatingProperty = service.getKeyOperatingProperty(formGroup) as any;

        expect(keyOperatingProperty).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKeyOperatingProperty should not enable id FormControl', () => {
        const formGroup = service.createKeyOperatingPropertyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKeyOperatingProperty should disable id FormControl', () => {
        const formGroup = service.createKeyOperatingPropertyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
