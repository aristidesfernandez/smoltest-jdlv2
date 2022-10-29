import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeviceType, NewDeviceType } from '../device-type.model';

export type PartialUpdateDeviceType = Partial<IDeviceType> & Pick<IDeviceType, 'id'>;

export type EntityResponseType = HttpResponse<IDeviceType>;
export type EntityArrayResponseType = HttpResponse<IDeviceType[]>;

@Injectable({ providedIn: 'root' })
export class DeviceTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/device-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deviceType: NewDeviceType): Observable<EntityResponseType> {
    return this.http.post<IDeviceType>(this.resourceUrl, deviceType, { observe: 'response' });
  }

  update(deviceType: IDeviceType): Observable<EntityResponseType> {
    return this.http.put<IDeviceType>(`${this.resourceUrl}/${this.getDeviceTypeIdentifier(deviceType)}`, deviceType, {
      observe: 'response',
    });
  }

  partialUpdate(deviceType: PartialUpdateDeviceType): Observable<EntityResponseType> {
    return this.http.patch<IDeviceType>(`${this.resourceUrl}/${this.getDeviceTypeIdentifier(deviceType)}`, deviceType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeviceType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeviceTypeIdentifier(deviceType: Pick<IDeviceType, 'id'>): number {
    return deviceType.id;
  }

  compareDeviceType(o1: Pick<IDeviceType, 'id'> | null, o2: Pick<IDeviceType, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeviceTypeIdentifier(o1) === this.getDeviceTypeIdentifier(o2) : o1 === o2;
  }

  addDeviceTypeToCollectionIfMissing<Type extends Pick<IDeviceType, 'id'>>(
    deviceTypeCollection: Type[],
    ...deviceTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deviceTypes: Type[] = deviceTypesToCheck.filter(isPresent);
    if (deviceTypes.length > 0) {
      const deviceTypeCollectionIdentifiers = deviceTypeCollection.map(deviceTypeItem => this.getDeviceTypeIdentifier(deviceTypeItem)!);
      const deviceTypesToAdd = deviceTypes.filter(deviceTypeItem => {
        const deviceTypeIdentifier = this.getDeviceTypeIdentifier(deviceTypeItem);
        if (deviceTypeCollectionIdentifiers.includes(deviceTypeIdentifier)) {
          return false;
        }
        deviceTypeCollectionIdentifiers.push(deviceTypeIdentifier);
        return true;
      });
      return [...deviceTypesToAdd, ...deviceTypeCollection];
    }
    return deviceTypeCollection;
  }
}
