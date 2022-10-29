import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOperator, NewOperator } from '../operator.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperator for edit and NewOperatorFormGroupInput for create.
 */
type OperatorFormGroupInput = IOperator | PartialWithRequiredKeyOf<NewOperator>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOperator | NewOperator> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type OperatorFormRawValue = FormValueOf<IOperator>;

type NewOperatorFormRawValue = FormValueOf<NewOperator>;

type OperatorFormDefaults = Pick<NewOperator, 'id' | 'startDate' | 'endDate'>;

type OperatorFormGroupContent = {
  id: FormControl<OperatorFormRawValue['id'] | NewOperator['id']>;
  permitDescription: FormControl<OperatorFormRawValue['permitDescription']>;
  startDate: FormControl<OperatorFormRawValue['startDate']>;
  endDate: FormControl<OperatorFormRawValue['endDate']>;
  nit: FormControl<OperatorFormRawValue['nit']>;
  contractNumber: FormControl<OperatorFormRawValue['contractNumber']>;
  companyName: FormControl<OperatorFormRawValue['companyName']>;
  brand: FormControl<OperatorFormRawValue['brand']>;
  municipality: FormControl<OperatorFormRawValue['municipality']>;
};

export type OperatorFormGroup = FormGroup<OperatorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperatorFormService {
  createOperatorFormGroup(operator: OperatorFormGroupInput = { id: null }): OperatorFormGroup {
    const operatorRawValue = this.convertOperatorToOperatorRawValue({
      ...this.getFormDefaults(),
      ...operator,
    });
    return new FormGroup<OperatorFormGroupContent>({
      id: new FormControl(
        { value: operatorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      permitDescription: new FormControl(operatorRawValue.permitDescription, {
        validators: [Validators.maxLength(50)],
      }),
      startDate: new FormControl(operatorRawValue.startDate),
      endDate: new FormControl(operatorRawValue.endDate),
      nit: new FormControl(operatorRawValue.nit, {
        validators: [Validators.maxLength(50)],
      }),
      contractNumber: new FormControl(operatorRawValue.contractNumber, {
        validators: [Validators.maxLength(50)],
      }),
      companyName: new FormControl(operatorRawValue.companyName),
      brand: new FormControl(operatorRawValue.brand, {
        validators: [Validators.maxLength(50)],
      }),
      municipality: new FormControl(operatorRawValue.municipality, {
        validators: [Validators.required],
      }),
    });
  }

  getOperator(form: OperatorFormGroup): IOperator | NewOperator {
    return this.convertOperatorRawValueToOperator(form.getRawValue() as OperatorFormRawValue | NewOperatorFormRawValue);
  }

  resetForm(form: OperatorFormGroup, operator: OperatorFormGroupInput): void {
    const operatorRawValue = this.convertOperatorToOperatorRawValue({ ...this.getFormDefaults(), ...operator });
    form.reset(
      {
        ...operatorRawValue,
        id: { value: operatorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OperatorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertOperatorRawValueToOperator(rawOperator: OperatorFormRawValue | NewOperatorFormRawValue): IOperator | NewOperator {
    return {
      ...rawOperator,
      startDate: dayjs(rawOperator.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawOperator.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertOperatorToOperatorRawValue(
    operator: IOperator | (Partial<NewOperator> & OperatorFormDefaults)
  ): OperatorFormRawValue | PartialWithRequiredKeyOf<NewOperatorFormRawValue> {
    return {
      ...operator,
      startDate: operator.startDate ? operator.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: operator.endDate ? operator.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
