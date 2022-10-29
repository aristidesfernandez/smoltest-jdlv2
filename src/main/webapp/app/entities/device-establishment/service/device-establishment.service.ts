import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeviceEstablishment, NewDeviceEstablishment } from '../device-establishment.model';

export type PartialUpdateDeviceEstablishment = Partial<IDeviceEstablishment> & Pick<IDeviceEstablishment, 'id'>;

type RestOf<T extends IDeviceEstablishment | NewDeviceEstablishment> = Omit<T, 'registrationAt' | 'departureAt'> & {
  registrationAt?: string | null;
  departureAt?: string | null;
};

export type RestDeviceEstablishment = RestOf<IDeviceEstablishment>;

export type NewRestDeviceEstablishment = RestOf<NewDeviceEstablishment>;

export type PartialUpdateRestDeviceEstablishment = RestOf<PartialUpdateDeviceEstablishment>;

export type EntityResponseType = HttpResponse<IDeviceEstablishment>;
export type EntityArrayResponseType = HttpResponse<IDeviceEstablishment[]>;

@Injectable({ providedIn: 'root' })
export class DeviceEstablishmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/device-establishments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deviceEstablishment: NewDeviceEstablishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceEstablishment);
    return this.http
      .post<RestDeviceEstablishment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(deviceEstablishment: IDeviceEstablishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceEstablishment);
    return this.http
      .put<RestDeviceEstablishment>(`${this.resourceUrl}/${this.getDeviceEstablishmentIdentifier(deviceEstablishment)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(deviceEstablishment: PartialUpdateDeviceEstablishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceEstablishment);
    return this.http
      .patch<RestDeviceEstablishment>(`${this.resourceUrl}/${this.getDeviceEstablishmentIdentifier(deviceEstablishment)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestDeviceEstablishment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDeviceEstablishment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeviceEstablishmentIdentifier(deviceEstablishment: Pick<IDeviceEstablishment, 'id'>): string {
    return deviceEstablishment.id;
  }

  compareDeviceEstablishment(o1: Pick<IDeviceEstablishment, 'id'> | null, o2: Pick<IDeviceEstablishment, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeviceEstablishmentIdentifier(o1) === this.getDeviceEstablishmentIdentifier(o2) : o1 === o2;
  }

  addDeviceEstablishmentToCollectionIfMissing<Type extends Pick<IDeviceEstablishment, 'id'>>(
    deviceEstablishmentCollection: Type[],
    ...deviceEstablishmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deviceEstablishments: Type[] = deviceEstablishmentsToCheck.filter(isPresent);
    if (deviceEstablishments.length > 0) {
      const deviceEstablishmentCollectionIdentifiers = deviceEstablishmentCollection.map(
        deviceEstablishmentItem => this.getDeviceEstablishmentIdentifier(deviceEstablishmentItem)!
      );
      const deviceEstablishmentsToAdd = deviceEstablishments.filter(deviceEstablishmentItem => {
        const deviceEstablishmentIdentifier = this.getDeviceEstablishmentIdentifier(deviceEstablishmentItem);
        if (deviceEstablishmentCollectionIdentifiers.includes(deviceEstablishmentIdentifier)) {
          return false;
        }
        deviceEstablishmentCollectionIdentifiers.push(deviceEstablishmentIdentifier);
        return true;
      });
      return [...deviceEstablishmentsToAdd, ...deviceEstablishmentCollection];
    }
    return deviceEstablishmentCollection;
  }

  protected convertDateFromClient<T extends IDeviceEstablishment | NewDeviceEstablishment | PartialUpdateDeviceEstablishment>(
    deviceEstablishment: T
  ): RestOf<T> {
    return {
      ...deviceEstablishment,
      registrationAt: deviceEstablishment.registrationAt?.toJSON() ?? null,
      departureAt: deviceEstablishment.departureAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDeviceEstablishment: RestDeviceEstablishment): IDeviceEstablishment {
    return {
      ...restDeviceEstablishment,
      registrationAt: restDeviceEstablishment.registrationAt ? dayjs(restDeviceEstablishment.registrationAt) : undefined,
      departureAt: restDeviceEstablishment.departureAt ? dayjs(restDeviceEstablishment.departureAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDeviceEstablishment>): HttpResponse<IDeviceEstablishment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDeviceEstablishment[]>): HttpResponse<IDeviceEstablishment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
