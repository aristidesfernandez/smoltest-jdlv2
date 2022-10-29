import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIsle, NewIsle } from '../isle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIsle for edit and NewIsleFormGroupInput for create.
 */
type IsleFormGroupInput = IIsle | PartialWithRequiredKeyOf<NewIsle>;

type IsleFormDefaults = Pick<NewIsle, 'id'>;

type IsleFormGroupContent = {
  id: FormControl<IIsle['id'] | NewIsle['id']>;
  description: FormControl<IIsle['description']>;
  name: FormControl<IIsle['name']>;
};

export type IsleFormGroup = FormGroup<IsleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IsleFormService {
  createIsleFormGroup(isle: IsleFormGroupInput = { id: null }): IsleFormGroup {
    const isleRawValue = {
      ...this.getFormDefaults(),
      ...isle,
    };
    return new FormGroup<IsleFormGroupContent>({
      id: new FormControl(
        { value: isleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(isleRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      name: new FormControl(isleRawValue.name, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getIsle(form: IsleFormGroup): IIsle | NewIsle {
    return form.getRawValue() as IIsle | NewIsle;
  }

  resetForm(form: IsleFormGroup, isle: IsleFormGroupInput): void {
    const isleRawValue = { ...this.getFormDefaults(), ...isle };
    form.reset(
      {
        ...isleRawValue,
        id: { value: isleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IsleFormDefaults {
    return {
      id: null,
    };
  }
}
