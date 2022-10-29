import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICounterType, NewCounterType } from '../counter-type.model';

export type PartialUpdateCounterType = Partial<ICounterType> & Pick<ICounterType, 'counterCode'>;

export type EntityResponseType = HttpResponse<ICounterType>;
export type EntityArrayResponseType = HttpResponse<ICounterType[]>;

@Injectable({ providedIn: 'root' })
export class CounterTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counter-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(counterType: NewCounterType): Observable<EntityResponseType> {
    return this.http.post<ICounterType>(this.resourceUrl, counterType, { observe: 'response' });
  }

  update(counterType: ICounterType): Observable<EntityResponseType> {
    return this.http.put<ICounterType>(`${this.resourceUrl}/${this.getCounterTypeIdentifier(counterType)}`, counterType, {
      observe: 'response',
    });
  }

  partialUpdate(counterType: PartialUpdateCounterType): Observable<EntityResponseType> {
    return this.http.patch<ICounterType>(`${this.resourceUrl}/${this.getCounterTypeIdentifier(counterType)}`, counterType, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICounterType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCounterTypeIdentifier(counterType: Pick<ICounterType, 'counterCode'>): string {
    return counterType.counterCode;
  }

  compareCounterType(o1: Pick<ICounterType, 'counterCode'> | null, o2: Pick<ICounterType, 'counterCode'> | null): boolean {
    return o1 && o2 ? this.getCounterTypeIdentifier(o1) === this.getCounterTypeIdentifier(o2) : o1 === o2;
  }

  addCounterTypeToCollectionIfMissing<Type extends Pick<ICounterType, 'counterCode'>>(
    counterTypeCollection: Type[],
    ...counterTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const counterTypes: Type[] = counterTypesToCheck.filter(isPresent);
    if (counterTypes.length > 0) {
      const counterTypeCollectionIdentifiers = counterTypeCollection.map(
        counterTypeItem => this.getCounterTypeIdentifier(counterTypeItem)!
      );
      const counterTypesToAdd = counterTypes.filter(counterTypeItem => {
        const counterTypeIdentifier = this.getCounterTypeIdentifier(counterTypeItem);
        if (counterTypeCollectionIdentifiers.includes(counterTypeIdentifier)) {
          return false;
        }
        counterTypeCollectionIdentifiers.push(counterTypeIdentifier);
        return true;
      });
      return [...counterTypesToAdd, ...counterTypeCollection];
    }
    return counterTypeCollection;
  }
}
