import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../isle.test-samples';

import { IsleFormService } from './isle-form.service';

describe('Isle Form Service', () => {
  let service: IsleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IsleFormService);
  });

  describe('Service methods', () => {
    describe('createIsleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIsleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IIsle should create a new form with FormGroup', () => {
        const formGroup = service.createIsleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getIsle', () => {
      it('should return NewIsle for default Isle initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIsleFormGroup(sampleWithNewData);

        const isle = service.getIsle(formGroup) as any;

        expect(isle).toMatchObject(sampleWithNewData);
      });

      it('should return NewIsle for empty Isle initial value', () => {
        const formGroup = service.createIsleFormGroup();

        const isle = service.getIsle(formGroup) as any;

        expect(isle).toMatchObject({});
      });

      it('should return IIsle', () => {
        const formGroup = service.createIsleFormGroup(sampleWithRequiredData);

        const isle = service.getIsle(formGroup) as any;

        expect(isle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIsle should not enable id FormControl', () => {
        const formGroup = service.createIsleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIsle should disable id FormControl', () => {
        const formGroup = service.createIsleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
