import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICounterDevice, NewCounterDevice } from '../counter-device.model';

export type PartialUpdateCounterDevice = Partial<ICounterDevice> & Pick<ICounterDevice, 'id'>;

export type EntityResponseType = HttpResponse<ICounterDevice>;
export type EntityArrayResponseType = HttpResponse<ICounterDevice[]>;

@Injectable({ providedIn: 'root' })
export class CounterDeviceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counter-devices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(counterDevice: NewCounterDevice): Observable<EntityResponseType> {
    return this.http.post<ICounterDevice>(this.resourceUrl, counterDevice, { observe: 'response' });
  }

  update(counterDevice: ICounterDevice): Observable<EntityResponseType> {
    return this.http.put<ICounterDevice>(`${this.resourceUrl}/${this.getCounterDeviceIdentifier(counterDevice)}`, counterDevice, {
      observe: 'response',
    });
  }

  partialUpdate(counterDevice: PartialUpdateCounterDevice): Observable<EntityResponseType> {
    return this.http.patch<ICounterDevice>(`${this.resourceUrl}/${this.getCounterDeviceIdentifier(counterDevice)}`, counterDevice, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICounterDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterDevice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCounterDeviceIdentifier(counterDevice: Pick<ICounterDevice, 'id'>): string {
    return counterDevice.id;
  }

  compareCounterDevice(o1: Pick<ICounterDevice, 'id'> | null, o2: Pick<ICounterDevice, 'id'> | null): boolean {
    return o1 && o2 ? this.getCounterDeviceIdentifier(o1) === this.getCounterDeviceIdentifier(o2) : o1 === o2;
  }

  addCounterDeviceToCollectionIfMissing<Type extends Pick<ICounterDevice, 'id'>>(
    counterDeviceCollection: Type[],
    ...counterDevicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const counterDevices: Type[] = counterDevicesToCheck.filter(isPresent);
    if (counterDevices.length > 0) {
      const counterDeviceCollectionIdentifiers = counterDeviceCollection.map(
        counterDeviceItem => this.getCounterDeviceIdentifier(counterDeviceItem)!
      );
      const counterDevicesToAdd = counterDevices.filter(counterDeviceItem => {
        const counterDeviceIdentifier = this.getCounterDeviceIdentifier(counterDeviceItem);
        if (counterDeviceCollectionIdentifiers.includes(counterDeviceIdentifier)) {
          return false;
        }
        counterDeviceCollectionIdentifiers.push(counterDeviceIdentifier);
        return true;
      });
      return [...counterDevicesToAdd, ...counterDeviceCollection];
    }
    return counterDeviceCollection;
  }
}
