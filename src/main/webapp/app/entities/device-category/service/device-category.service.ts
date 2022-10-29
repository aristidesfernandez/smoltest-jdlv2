import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeviceCategory, NewDeviceCategory } from '../device-category.model';

export type PartialUpdateDeviceCategory = Partial<IDeviceCategory> & Pick<IDeviceCategory, 'id'>;

export type EntityResponseType = HttpResponse<IDeviceCategory>;
export type EntityArrayResponseType = HttpResponse<IDeviceCategory[]>;

@Injectable({ providedIn: 'root' })
export class DeviceCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/device-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deviceCategory: NewDeviceCategory): Observable<EntityResponseType> {
    return this.http.post<IDeviceCategory>(this.resourceUrl, deviceCategory, { observe: 'response' });
  }

  update(deviceCategory: IDeviceCategory): Observable<EntityResponseType> {
    return this.http.put<IDeviceCategory>(`${this.resourceUrl}/${this.getDeviceCategoryIdentifier(deviceCategory)}`, deviceCategory, {
      observe: 'response',
    });
  }

  partialUpdate(deviceCategory: PartialUpdateDeviceCategory): Observable<EntityResponseType> {
    return this.http.patch<IDeviceCategory>(`${this.resourceUrl}/${this.getDeviceCategoryIdentifier(deviceCategory)}`, deviceCategory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeviceCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeviceCategoryIdentifier(deviceCategory: Pick<IDeviceCategory, 'id'>): number {
    return deviceCategory.id;
  }

  compareDeviceCategory(o1: Pick<IDeviceCategory, 'id'> | null, o2: Pick<IDeviceCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeviceCategoryIdentifier(o1) === this.getDeviceCategoryIdentifier(o2) : o1 === o2;
  }

  addDeviceCategoryToCollectionIfMissing<Type extends Pick<IDeviceCategory, 'id'>>(
    deviceCategoryCollection: Type[],
    ...deviceCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deviceCategories: Type[] = deviceCategoriesToCheck.filter(isPresent);
    if (deviceCategories.length > 0) {
      const deviceCategoryCollectionIdentifiers = deviceCategoryCollection.map(
        deviceCategoryItem => this.getDeviceCategoryIdentifier(deviceCategoryItem)!
      );
      const deviceCategoriesToAdd = deviceCategories.filter(deviceCategoryItem => {
        const deviceCategoryIdentifier = this.getDeviceCategoryIdentifier(deviceCategoryItem);
        if (deviceCategoryCollectionIdentifiers.includes(deviceCategoryIdentifier)) {
          return false;
        }
        deviceCategoryCollectionIdentifiers.push(deviceCategoryIdentifier);
        return true;
      });
      return [...deviceCategoriesToAdd, ...deviceCategoryCollection];
    }
    return deviceCategoryCollection;
  }
}
