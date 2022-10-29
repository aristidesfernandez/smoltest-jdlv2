import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IKeyOperatingProperty, NewKeyOperatingProperty } from '../key-operating-property.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKeyOperatingProperty for edit and NewKeyOperatingPropertyFormGroupInput for create.
 */
type KeyOperatingPropertyFormGroupInput = IKeyOperatingProperty | PartialWithRequiredKeyOf<NewKeyOperatingProperty>;

type KeyOperatingPropertyFormDefaults = Pick<NewKeyOperatingProperty, 'id'>;

type KeyOperatingPropertyFormGroupContent = {
  id: FormControl<IKeyOperatingProperty['id'] | NewKeyOperatingProperty['id']>;
  description: FormControl<IKeyOperatingProperty['description']>;
  property: FormControl<IKeyOperatingProperty['property']>;
};

export type KeyOperatingPropertyFormGroup = FormGroup<KeyOperatingPropertyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KeyOperatingPropertyFormService {
  createKeyOperatingPropertyFormGroup(
    keyOperatingProperty: KeyOperatingPropertyFormGroupInput = { id: null }
  ): KeyOperatingPropertyFormGroup {
    const keyOperatingPropertyRawValue = {
      ...this.getFormDefaults(),
      ...keyOperatingProperty,
    };
    return new FormGroup<KeyOperatingPropertyFormGroupContent>({
      id: new FormControl(
        { value: keyOperatingPropertyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(keyOperatingPropertyRawValue.description),
      property: new FormControl(keyOperatingPropertyRawValue.property, {
        validators: [Validators.maxLength(32)],
      }),
    });
  }

  getKeyOperatingProperty(form: KeyOperatingPropertyFormGroup): IKeyOperatingProperty | NewKeyOperatingProperty {
    return form.getRawValue() as IKeyOperatingProperty | NewKeyOperatingProperty;
  }

  resetForm(form: KeyOperatingPropertyFormGroup, keyOperatingProperty: KeyOperatingPropertyFormGroupInput): void {
    const keyOperatingPropertyRawValue = { ...this.getFormDefaults(), ...keyOperatingProperty };
    form.reset(
      {
        ...keyOperatingPropertyRawValue,
        id: { value: keyOperatingPropertyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): KeyOperatingPropertyFormDefaults {
    return {
      id: null,
    };
  }
}
