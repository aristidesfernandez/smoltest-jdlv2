import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeviceInterface, NewDeviceInterface } from '../device-interface.model';

export type PartialUpdateDeviceInterface = Partial<IDeviceInterface> & Pick<IDeviceInterface, 'id'>;

type RestOf<T extends IDeviceInterface | NewDeviceInterface> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestDeviceInterface = RestOf<IDeviceInterface>;

export type NewRestDeviceInterface = RestOf<NewDeviceInterface>;

export type PartialUpdateRestDeviceInterface = RestOf<PartialUpdateDeviceInterface>;

export type EntityResponseType = HttpResponse<IDeviceInterface>;
export type EntityArrayResponseType = HttpResponse<IDeviceInterface[]>;

@Injectable({ providedIn: 'root' })
export class DeviceInterfaceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/device-interfaces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deviceInterface: NewDeviceInterface): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceInterface);
    return this.http
      .post<RestDeviceInterface>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(deviceInterface: IDeviceInterface): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceInterface);
    return this.http
      .put<RestDeviceInterface>(`${this.resourceUrl}/${this.getDeviceInterfaceIdentifier(deviceInterface)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(deviceInterface: PartialUpdateDeviceInterface): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deviceInterface);
    return this.http
      .patch<RestDeviceInterface>(`${this.resourceUrl}/${this.getDeviceInterfaceIdentifier(deviceInterface)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDeviceInterface>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDeviceInterface[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeviceInterfaceIdentifier(deviceInterface: Pick<IDeviceInterface, 'id'>): number {
    return deviceInterface.id;
  }

  compareDeviceInterface(o1: Pick<IDeviceInterface, 'id'> | null, o2: Pick<IDeviceInterface, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeviceInterfaceIdentifier(o1) === this.getDeviceInterfaceIdentifier(o2) : o1 === o2;
  }

  addDeviceInterfaceToCollectionIfMissing<Type extends Pick<IDeviceInterface, 'id'>>(
    deviceInterfaceCollection: Type[],
    ...deviceInterfacesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deviceInterfaces: Type[] = deviceInterfacesToCheck.filter(isPresent);
    if (deviceInterfaces.length > 0) {
      const deviceInterfaceCollectionIdentifiers = deviceInterfaceCollection.map(
        deviceInterfaceItem => this.getDeviceInterfaceIdentifier(deviceInterfaceItem)!
      );
      const deviceInterfacesToAdd = deviceInterfaces.filter(deviceInterfaceItem => {
        const deviceInterfaceIdentifier = this.getDeviceInterfaceIdentifier(deviceInterfaceItem);
        if (deviceInterfaceCollectionIdentifiers.includes(deviceInterfaceIdentifier)) {
          return false;
        }
        deviceInterfaceCollectionIdentifiers.push(deviceInterfaceIdentifier);
        return true;
      });
      return [...deviceInterfacesToAdd, ...deviceInterfaceCollection];
    }
    return deviceInterfaceCollection;
  }

  protected convertDateFromClient<T extends IDeviceInterface | NewDeviceInterface | PartialUpdateDeviceInterface>(
    deviceInterface: T
  ): RestOf<T> {
    return {
      ...deviceInterface,
      startDate: deviceInterface.startDate?.toJSON() ?? null,
      endDate: deviceInterface.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDeviceInterface: RestDeviceInterface): IDeviceInterface {
    return {
      ...restDeviceInterface,
      startDate: restDeviceInterface.startDate ? dayjs(restDeviceInterface.startDate) : undefined,
      endDate: restDeviceInterface.endDate ? dayjs(restDeviceInterface.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDeviceInterface>): HttpResponse<IDeviceInterface> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDeviceInterface[]>): HttpResponse<IDeviceInterface[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
