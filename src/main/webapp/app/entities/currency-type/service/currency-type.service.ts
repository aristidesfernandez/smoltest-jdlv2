import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICurrencyType, NewCurrencyType } from '../currency-type.model';

export type PartialUpdateCurrencyType = Partial<ICurrencyType> & Pick<ICurrencyType, 'id'>;

export type EntityResponseType = HttpResponse<ICurrencyType>;
export type EntityArrayResponseType = HttpResponse<ICurrencyType[]>;

@Injectable({ providedIn: 'root' })
export class CurrencyTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/currency-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(currencyType: NewCurrencyType): Observable<EntityResponseType> {
    return this.http.post<ICurrencyType>(this.resourceUrl, currencyType, { observe: 'response' });
  }

  update(currencyType: ICurrencyType): Observable<EntityResponseType> {
    return this.http.put<ICurrencyType>(`${this.resourceUrl}/${this.getCurrencyTypeIdentifier(currencyType)}`, currencyType, {
      observe: 'response',
    });
  }

  partialUpdate(currencyType: PartialUpdateCurrencyType): Observable<EntityResponseType> {
    return this.http.patch<ICurrencyType>(`${this.resourceUrl}/${this.getCurrencyTypeIdentifier(currencyType)}`, currencyType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICurrencyType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurrencyType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCurrencyTypeIdentifier(currencyType: Pick<ICurrencyType, 'id'>): number {
    return currencyType.id;
  }

  compareCurrencyType(o1: Pick<ICurrencyType, 'id'> | null, o2: Pick<ICurrencyType, 'id'> | null): boolean {
    return o1 && o2 ? this.getCurrencyTypeIdentifier(o1) === this.getCurrencyTypeIdentifier(o2) : o1 === o2;
  }

  addCurrencyTypeToCollectionIfMissing<Type extends Pick<ICurrencyType, 'id'>>(
    currencyTypeCollection: Type[],
    ...currencyTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const currencyTypes: Type[] = currencyTypesToCheck.filter(isPresent);
    if (currencyTypes.length > 0) {
      const currencyTypeCollectionIdentifiers = currencyTypeCollection.map(
        currencyTypeItem => this.getCurrencyTypeIdentifier(currencyTypeItem)!
      );
      const currencyTypesToAdd = currencyTypes.filter(currencyTypeItem => {
        const currencyTypeIdentifier = this.getCurrencyTypeIdentifier(currencyTypeItem);
        if (currencyTypeCollectionIdentifiers.includes(currencyTypeIdentifier)) {
          return false;
        }
        currencyTypeCollectionIdentifiers.push(currencyTypeIdentifier);
        return true;
      });
      return [...currencyTypesToAdd, ...currencyTypeCollection];
    }
    return currencyTypeCollection;
  }
}
