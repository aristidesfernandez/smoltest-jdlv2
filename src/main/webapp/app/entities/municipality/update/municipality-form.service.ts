import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMunicipality, NewMunicipality } from '../municipality.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMunicipality for edit and NewMunicipalityFormGroupInput for create.
 */
type MunicipalityFormGroupInput = IMunicipality | PartialWithRequiredKeyOf<NewMunicipality>;

type MunicipalityFormDefaults = Pick<NewMunicipality, 'id'>;

type MunicipalityFormGroupContent = {
  id: FormControl<IMunicipality['id'] | NewMunicipality['id']>;
  code: FormControl<IMunicipality['code']>;
  name: FormControl<IMunicipality['name']>;
  daneCode: FormControl<IMunicipality['daneCode']>;
  province: FormControl<IMunicipality['province']>;
};

export type MunicipalityFormGroup = FormGroup<MunicipalityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MunicipalityFormService {
  createMunicipalityFormGroup(municipality: MunicipalityFormGroupInput = { id: null }): MunicipalityFormGroup {
    const municipalityRawValue = {
      ...this.getFormDefaults(),
      ...municipality,
    };
    return new FormGroup<MunicipalityFormGroupContent>({
      id: new FormControl(
        { value: municipalityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(municipalityRawValue.code, {
        validators: [Validators.maxLength(25)],
      }),
      name: new FormControl(municipalityRawValue.name, {
        validators: [Validators.maxLength(50)],
      }),
      daneCode: new FormControl(municipalityRawValue.daneCode, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      province: new FormControl(municipalityRawValue.province, {
        validators: [Validators.required],
      }),
    });
  }

  getMunicipality(form: MunicipalityFormGroup): IMunicipality | NewMunicipality {
    return form.getRawValue() as IMunicipality | NewMunicipality;
  }

  resetForm(form: MunicipalityFormGroup, municipality: MunicipalityFormGroupInput): void {
    const municipalityRawValue = { ...this.getFormDefaults(), ...municipality };
    form.reset(
      {
        ...municipalityRawValue,
        id: { value: municipalityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MunicipalityFormDefaults {
    return {
      id: null,
    };
  }
}
