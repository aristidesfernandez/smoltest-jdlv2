import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEstablishment, NewEstablishment } from '../establishment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEstablishment for edit and NewEstablishmentFormGroupInput for create.
 */
type EstablishmentFormGroupInput = IEstablishment | PartialWithRequiredKeyOf<NewEstablishment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEstablishment | NewEstablishment> = Omit<T, 'liquidationTime' | 'startTime' | 'closeTime'> & {
  liquidationTime?: string | null;
  startTime?: string | null;
  closeTime?: string | null;
};

type EstablishmentFormRawValue = FormValueOf<IEstablishment>;

type NewEstablishmentFormRawValue = FormValueOf<NewEstablishment>;

type EstablishmentFormDefaults = Pick<NewEstablishment, 'id' | 'liquidationTime' | 'startTime' | 'closeTime'>;

type EstablishmentFormGroupContent = {
  id: FormControl<EstablishmentFormRawValue['id'] | NewEstablishment['id']>;
  liquidationTime: FormControl<EstablishmentFormRawValue['liquidationTime']>;
  name: FormControl<EstablishmentFormRawValue['name']>;
  type: FormControl<EstablishmentFormRawValue['type']>;
  neighborhood: FormControl<EstablishmentFormRawValue['neighborhood']>;
  address: FormControl<EstablishmentFormRawValue['address']>;
  coljuegosCode: FormControl<EstablishmentFormRawValue['coljuegosCode']>;
  startTime: FormControl<EstablishmentFormRawValue['startTime']>;
  closeTime: FormControl<EstablishmentFormRawValue['closeTime']>;
  longitude: FormControl<EstablishmentFormRawValue['longitude']>;
  latitude: FormControl<EstablishmentFormRawValue['latitude']>;
  mercantileRegistration: FormControl<EstablishmentFormRawValue['mercantileRegistration']>;
  operator: FormControl<EstablishmentFormRawValue['operator']>;
  municipality: FormControl<EstablishmentFormRawValue['municipality']>;
};

export type EstablishmentFormGroup = FormGroup<EstablishmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EstablishmentFormService {
  createEstablishmentFormGroup(establishment: EstablishmentFormGroupInput = { id: null }): EstablishmentFormGroup {
    const establishmentRawValue = this.convertEstablishmentToEstablishmentRawValue({
      ...this.getFormDefaults(),
      ...establishment,
    });
    return new FormGroup<EstablishmentFormGroupContent>({
      id: new FormControl(
        { value: establishmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      liquidationTime: new FormControl(establishmentRawValue.liquidationTime),
      name: new FormControl(establishmentRawValue.name, {
        validators: [Validators.maxLength(100)],
      }),
      type: new FormControl(establishmentRawValue.type),
      neighborhood: new FormControl(establishmentRawValue.neighborhood),
      address: new FormControl(establishmentRawValue.address),
      coljuegosCode: new FormControl(establishmentRawValue.coljuegosCode, {
        validators: [Validators.maxLength(100)],
      }),
      startTime: new FormControl(establishmentRawValue.startTime),
      closeTime: new FormControl(establishmentRawValue.closeTime),
      longitude: new FormControl(establishmentRawValue.longitude),
      latitude: new FormControl(establishmentRawValue.latitude),
      mercantileRegistration: new FormControl(establishmentRawValue.mercantileRegistration, {
        validators: [Validators.maxLength(100)],
      }),
      operator: new FormControl(establishmentRawValue.operator, {
        validators: [Validators.required],
      }),
      municipality: new FormControl(establishmentRawValue.municipality, {
        validators: [Validators.required],
      }),
    });
  }

  getEstablishment(form: EstablishmentFormGroup): IEstablishment | NewEstablishment {
    return this.convertEstablishmentRawValueToEstablishment(form.getRawValue() as EstablishmentFormRawValue | NewEstablishmentFormRawValue);
  }

  resetForm(form: EstablishmentFormGroup, establishment: EstablishmentFormGroupInput): void {
    const establishmentRawValue = this.convertEstablishmentToEstablishmentRawValue({ ...this.getFormDefaults(), ...establishment });
    form.reset(
      {
        ...establishmentRawValue,
        id: { value: establishmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EstablishmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      liquidationTime: currentTime,
      startTime: currentTime,
      closeTime: currentTime,
    };
  }

  private convertEstablishmentRawValueToEstablishment(
    rawEstablishment: EstablishmentFormRawValue | NewEstablishmentFormRawValue
  ): IEstablishment | NewEstablishment {
    return {
      ...rawEstablishment,
      liquidationTime: dayjs(rawEstablishment.liquidationTime, DATE_TIME_FORMAT),
      startTime: dayjs(rawEstablishment.startTime, DATE_TIME_FORMAT),
      closeTime: dayjs(rawEstablishment.closeTime, DATE_TIME_FORMAT),
    };
  }

  private convertEstablishmentToEstablishmentRawValue(
    establishment: IEstablishment | (Partial<NewEstablishment> & EstablishmentFormDefaults)
  ): EstablishmentFormRawValue | PartialWithRequiredKeyOf<NewEstablishmentFormRawValue> {
    return {
      ...establishment,
      liquidationTime: establishment.liquidationTime ? establishment.liquidationTime.format(DATE_TIME_FORMAT) : undefined,
      startTime: establishment.startTime ? establishment.startTime.format(DATE_TIME_FORMAT) : undefined,
      closeTime: establishment.closeTime ? establishment.closeTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
