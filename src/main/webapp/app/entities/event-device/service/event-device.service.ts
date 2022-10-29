import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventDevice, NewEventDevice } from '../event-device.model';

export type PartialUpdateEventDevice = Partial<IEventDevice> & Pick<IEventDevice, 'id'>;

type RestOf<T extends IEventDevice | NewEventDevice> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestEventDevice = RestOf<IEventDevice>;

export type NewRestEventDevice = RestOf<NewEventDevice>;

export type PartialUpdateRestEventDevice = RestOf<PartialUpdateEventDevice>;

export type EntityResponseType = HttpResponse<IEventDevice>;
export type EntityArrayResponseType = HttpResponse<IEventDevice[]>;

@Injectable({ providedIn: 'root' })
export class EventDeviceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-devices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eventDevice: NewEventDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventDevice);
    return this.http
      .post<RestEventDevice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(eventDevice: IEventDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventDevice);
    return this.http
      .put<RestEventDevice>(`${this.resourceUrl}/${this.getEventDeviceIdentifier(eventDevice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(eventDevice: PartialUpdateEventDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventDevice);
    return this.http
      .patch<RestEventDevice>(`${this.resourceUrl}/${this.getEventDeviceIdentifier(eventDevice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestEventDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEventDevice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEventDeviceIdentifier(eventDevice: Pick<IEventDevice, 'id'>): string {
    return eventDevice.id;
  }

  compareEventDevice(o1: Pick<IEventDevice, 'id'> | null, o2: Pick<IEventDevice, 'id'> | null): boolean {
    return o1 && o2 ? this.getEventDeviceIdentifier(o1) === this.getEventDeviceIdentifier(o2) : o1 === o2;
  }

  addEventDeviceToCollectionIfMissing<Type extends Pick<IEventDevice, 'id'>>(
    eventDeviceCollection: Type[],
    ...eventDevicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eventDevices: Type[] = eventDevicesToCheck.filter(isPresent);
    if (eventDevices.length > 0) {
      const eventDeviceCollectionIdentifiers = eventDeviceCollection.map(
        eventDeviceItem => this.getEventDeviceIdentifier(eventDeviceItem)!
      );
      const eventDevicesToAdd = eventDevices.filter(eventDeviceItem => {
        const eventDeviceIdentifier = this.getEventDeviceIdentifier(eventDeviceItem);
        if (eventDeviceCollectionIdentifiers.includes(eventDeviceIdentifier)) {
          return false;
        }
        eventDeviceCollectionIdentifiers.push(eventDeviceIdentifier);
        return true;
      });
      return [...eventDevicesToAdd, ...eventDeviceCollection];
    }
    return eventDeviceCollection;
  }

  protected convertDateFromClient<T extends IEventDevice | NewEventDevice | PartialUpdateEventDevice>(eventDevice: T): RestOf<T> {
    return {
      ...eventDevice,
      createdAt: eventDevice.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEventDevice: RestEventDevice): IEventDevice {
    return {
      ...restEventDevice,
      createdAt: restEventDevice.createdAt ? dayjs(restEventDevice.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEventDevice>): HttpResponse<IEventDevice> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEventDevice[]>): HttpResponse<IEventDevice[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
