import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKeyOperatingProperty, NewKeyOperatingProperty } from '../key-operating-property.model';

export type PartialUpdateKeyOperatingProperty = Partial<IKeyOperatingProperty> & Pick<IKeyOperatingProperty, 'id'>;

export type EntityResponseType = HttpResponse<IKeyOperatingProperty>;
export type EntityArrayResponseType = HttpResponse<IKeyOperatingProperty[]>;

@Injectable({ providedIn: 'root' })
export class KeyOperatingPropertyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/key-operating-properties');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(keyOperatingProperty: NewKeyOperatingProperty): Observable<EntityResponseType> {
    return this.http.post<IKeyOperatingProperty>(this.resourceUrl, keyOperatingProperty, { observe: 'response' });
  }

  update(keyOperatingProperty: IKeyOperatingProperty): Observable<EntityResponseType> {
    return this.http.put<IKeyOperatingProperty>(
      `${this.resourceUrl}/${this.getKeyOperatingPropertyIdentifier(keyOperatingProperty)}`,
      keyOperatingProperty,
      { observe: 'response' }
    );
  }

  partialUpdate(keyOperatingProperty: PartialUpdateKeyOperatingProperty): Observable<EntityResponseType> {
    return this.http.patch<IKeyOperatingProperty>(
      `${this.resourceUrl}/${this.getKeyOperatingPropertyIdentifier(keyOperatingProperty)}`,
      keyOperatingProperty,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKeyOperatingProperty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKeyOperatingProperty[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getKeyOperatingPropertyIdentifier(keyOperatingProperty: Pick<IKeyOperatingProperty, 'id'>): number {
    return keyOperatingProperty.id;
  }

  compareKeyOperatingProperty(o1: Pick<IKeyOperatingProperty, 'id'> | null, o2: Pick<IKeyOperatingProperty, 'id'> | null): boolean {
    return o1 && o2 ? this.getKeyOperatingPropertyIdentifier(o1) === this.getKeyOperatingPropertyIdentifier(o2) : o1 === o2;
  }

  addKeyOperatingPropertyToCollectionIfMissing<Type extends Pick<IKeyOperatingProperty, 'id'>>(
    keyOperatingPropertyCollection: Type[],
    ...keyOperatingPropertiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const keyOperatingProperties: Type[] = keyOperatingPropertiesToCheck.filter(isPresent);
    if (keyOperatingProperties.length > 0) {
      const keyOperatingPropertyCollectionIdentifiers = keyOperatingPropertyCollection.map(
        keyOperatingPropertyItem => this.getKeyOperatingPropertyIdentifier(keyOperatingPropertyItem)!
      );
      const keyOperatingPropertiesToAdd = keyOperatingProperties.filter(keyOperatingPropertyItem => {
        const keyOperatingPropertyIdentifier = this.getKeyOperatingPropertyIdentifier(keyOperatingPropertyItem);
        if (keyOperatingPropertyCollectionIdentifiers.includes(keyOperatingPropertyIdentifier)) {
          return false;
        }
        keyOperatingPropertyCollectionIdentifiers.push(keyOperatingPropertyIdentifier);
        return true;
      });
      return [...keyOperatingPropertiesToAdd, ...keyOperatingPropertyCollection];
    }
    return keyOperatingPropertyCollection;
  }
}
