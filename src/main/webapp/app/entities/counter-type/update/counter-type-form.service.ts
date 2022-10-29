import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICounterType, NewCounterType } from '../counter-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { counterCode: unknown }> = Partial<Omit<T, 'counterCode'>> & { counterCode: T['counterCode'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICounterType for edit and NewCounterTypeFormGroupInput for create.
 */
type CounterTypeFormGroupInput = ICounterType | PartialWithRequiredKeyOf<NewCounterType>;

type CounterTypeFormDefaults = Pick<NewCounterType, 'counterCode' | 'includedInFormula' | 'prize'>;

type CounterTypeFormGroupContent = {
  counterCode: FormControl<ICounterType['counterCode'] | NewCounterType['counterCode']>;
  name: FormControl<ICounterType['name']>;
  description: FormControl<ICounterType['description']>;
  includedInFormula: FormControl<ICounterType['includedInFormula']>;
  prize: FormControl<ICounterType['prize']>;
  category: FormControl<ICounterType['category']>;
  udteWaitTime: FormControl<ICounterType['udteWaitTime']>;
};

export type CounterTypeFormGroup = FormGroup<CounterTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CounterTypeFormService {
  createCounterTypeFormGroup(counterType: CounterTypeFormGroupInput = { counterCode: null }): CounterTypeFormGroup {
    const counterTypeRawValue = {
      ...this.getFormDefaults(),
      ...counterType,
    };
    return new FormGroup<CounterTypeFormGroupContent>({
      counterCode: new FormControl(
        { value: counterTypeRawValue.counterCode, disabled: counterTypeRawValue.counterCode !== null },
        {
          nonNullable: true,
          validators: [Validators.required, Validators.maxLength(2)],
        }
      ),
      name: new FormControl(counterTypeRawValue.name, {
        validators: [Validators.maxLength(50)],
      }),
      description: new FormControl(counterTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      includedInFormula: new FormControl(counterTypeRawValue.includedInFormula),
      prize: new FormControl(counterTypeRawValue.prize),
      category: new FormControl(counterTypeRawValue.category, {
        validators: [Validators.maxLength(100)],
      }),
      udteWaitTime: new FormControl(counterTypeRawValue.udteWaitTime),
    });
  }

  getCounterType(form: CounterTypeFormGroup): ICounterType | NewCounterType {
    return form.getRawValue() as ICounterType | NewCounterType;
  }

  resetForm(form: CounterTypeFormGroup, counterType: CounterTypeFormGroupInput): void {
    const counterTypeRawValue = { ...this.getFormDefaults(), ...counterType };
    form.reset(
      {
        ...counterTypeRawValue,
        counterCode: { value: counterTypeRawValue.counterCode, disabled: counterTypeRawValue.counterCode !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CounterTypeFormDefaults {
    return {
      counterCode: null,
      includedInFormula: false,
      prize: false,
    };
  }
}
