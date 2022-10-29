import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserAccess, NewUserAccess } from '../user-access.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserAccess for edit and NewUserAccessFormGroupInput for create.
 */
type UserAccessFormGroupInput = IUserAccess | PartialWithRequiredKeyOf<NewUserAccess>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserAccess | NewUserAccess> = Omit<T, 'registrationAt'> & {
  registrationAt?: string | null;
};

type UserAccessFormRawValue = FormValueOf<IUserAccess>;

type NewUserAccessFormRawValue = FormValueOf<NewUserAccess>;

type UserAccessFormDefaults = Pick<NewUserAccess, 'id' | 'registrationAt'>;

type UserAccessFormGroupContent = {
  id: FormControl<UserAccessFormRawValue['id'] | NewUserAccess['id']>;
  username: FormControl<UserAccessFormRawValue['username']>;
  ipAddress: FormControl<UserAccessFormRawValue['ipAddress']>;
  registrationAt: FormControl<UserAccessFormRawValue['registrationAt']>;
};

export type UserAccessFormGroup = FormGroup<UserAccessFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserAccessFormService {
  createUserAccessFormGroup(userAccess: UserAccessFormGroupInput = { id: null }): UserAccessFormGroup {
    const userAccessRawValue = this.convertUserAccessToUserAccessRawValue({
      ...this.getFormDefaults(),
      ...userAccess,
    });
    return new FormGroup<UserAccessFormGroupContent>({
      id: new FormControl(
        { value: userAccessRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      username: new FormControl(userAccessRawValue.username, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      ipAddress: new FormControl(userAccessRawValue.ipAddress, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      registrationAt: new FormControl(userAccessRawValue.registrationAt, {
        validators: [Validators.required],
      }),
    });
  }

  getUserAccess(form: UserAccessFormGroup): IUserAccess | NewUserAccess {
    return this.convertUserAccessRawValueToUserAccess(form.getRawValue() as UserAccessFormRawValue | NewUserAccessFormRawValue);
  }

  resetForm(form: UserAccessFormGroup, userAccess: UserAccessFormGroupInput): void {
    const userAccessRawValue = this.convertUserAccessToUserAccessRawValue({ ...this.getFormDefaults(), ...userAccess });
    form.reset(
      {
        ...userAccessRawValue,
        id: { value: userAccessRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserAccessFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      registrationAt: currentTime,
    };
  }

  private convertUserAccessRawValueToUserAccess(
    rawUserAccess: UserAccessFormRawValue | NewUserAccessFormRawValue
  ): IUserAccess | NewUserAccess {
    return {
      ...rawUserAccess,
      registrationAt: dayjs(rawUserAccess.registrationAt, DATE_TIME_FORMAT),
    };
  }

  private convertUserAccessToUserAccessRawValue(
    userAccess: IUserAccess | (Partial<NewUserAccess> & UserAccessFormDefaults)
  ): UserAccessFormRawValue | PartialWithRequiredKeyOf<NewUserAccessFormRawValue> {
    return {
      ...userAccess,
      registrationAt: userAccess.registrationAt ? userAccess.registrationAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
