import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOperationalPropertiesEstablishment, NewOperationalPropertiesEstablishment } from '../operational-properties-establishment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperationalPropertiesEstablishment for edit and NewOperationalPropertiesEstablishmentFormGroupInput for create.
 */
type OperationalPropertiesEstablishmentFormGroupInput =
  | IOperationalPropertiesEstablishment
  | PartialWithRequiredKeyOf<NewOperationalPropertiesEstablishment>;

type OperationalPropertiesEstablishmentFormDefaults = Pick<NewOperationalPropertiesEstablishment, 'id'>;

type OperationalPropertiesEstablishmentFormGroupContent = {
  id: FormControl<IOperationalPropertiesEstablishment['id'] | NewOperationalPropertiesEstablishment['id']>;
  value: FormControl<IOperationalPropertiesEstablishment['value']>;
  keyOperatingProperty: FormControl<IOperationalPropertiesEstablishment['keyOperatingProperty']>;
  establishment: FormControl<IOperationalPropertiesEstablishment['establishment']>;
};

export type OperationalPropertiesEstablishmentFormGroup = FormGroup<OperationalPropertiesEstablishmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperationalPropertiesEstablishmentFormService {
  createOperationalPropertiesEstablishmentFormGroup(
    operationalPropertiesEstablishment: OperationalPropertiesEstablishmentFormGroupInput = { id: null }
  ): OperationalPropertiesEstablishmentFormGroup {
    const operationalPropertiesEstablishmentRawValue = {
      ...this.getFormDefaults(),
      ...operationalPropertiesEstablishment,
    };
    return new FormGroup<OperationalPropertiesEstablishmentFormGroupContent>({
      id: new FormControl(
        { value: operationalPropertiesEstablishmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      value: new FormControl(operationalPropertiesEstablishmentRawValue.value),
      keyOperatingProperty: new FormControl(operationalPropertiesEstablishmentRawValue.keyOperatingProperty, {
        validators: [Validators.required],
      }),
      establishment: new FormControl(operationalPropertiesEstablishmentRawValue.establishment, {
        validators: [Validators.required],
      }),
    });
  }

  getOperationalPropertiesEstablishment(
    form: OperationalPropertiesEstablishmentFormGroup
  ): IOperationalPropertiesEstablishment | NewOperationalPropertiesEstablishment {
    return form.getRawValue() as IOperationalPropertiesEstablishment | NewOperationalPropertiesEstablishment;
  }

  resetForm(
    form: OperationalPropertiesEstablishmentFormGroup,
    operationalPropertiesEstablishment: OperationalPropertiesEstablishmentFormGroupInput
  ): void {
    const operationalPropertiesEstablishmentRawValue = { ...this.getFormDefaults(), ...operationalPropertiesEstablishment };
    form.reset(
      {
        ...operationalPropertiesEstablishmentRawValue,
        id: { value: operationalPropertiesEstablishmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OperationalPropertiesEstablishmentFormDefaults {
    return {
      id: null,
    };
  }
}
