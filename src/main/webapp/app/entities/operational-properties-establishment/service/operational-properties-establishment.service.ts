import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperationalPropertiesEstablishment, NewOperationalPropertiesEstablishment } from '../operational-properties-establishment.model';

export type PartialUpdateOperationalPropertiesEstablishment = Partial<IOperationalPropertiesEstablishment> &
  Pick<IOperationalPropertiesEstablishment, 'id'>;

export type EntityResponseType = HttpResponse<IOperationalPropertiesEstablishment>;
export type EntityArrayResponseType = HttpResponse<IOperationalPropertiesEstablishment[]>;

@Injectable({ providedIn: 'root' })
export class OperationalPropertiesEstablishmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operational-properties-establishments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operationalPropertiesEstablishment: NewOperationalPropertiesEstablishment): Observable<EntityResponseType> {
    return this.http.post<IOperationalPropertiesEstablishment>(this.resourceUrl, operationalPropertiesEstablishment, {
      observe: 'response',
    });
  }

  update(operationalPropertiesEstablishment: IOperationalPropertiesEstablishment): Observable<EntityResponseType> {
    return this.http.put<IOperationalPropertiesEstablishment>(
      `${this.resourceUrl}/${this.getOperationalPropertiesEstablishmentIdentifier(operationalPropertiesEstablishment)}`,
      operationalPropertiesEstablishment,
      { observe: 'response' }
    );
  }

  partialUpdate(operationalPropertiesEstablishment: PartialUpdateOperationalPropertiesEstablishment): Observable<EntityResponseType> {
    return this.http.patch<IOperationalPropertiesEstablishment>(
      `${this.resourceUrl}/${this.getOperationalPropertiesEstablishmentIdentifier(operationalPropertiesEstablishment)}`,
      operationalPropertiesEstablishment,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperationalPropertiesEstablishment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperationalPropertiesEstablishment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperationalPropertiesEstablishmentIdentifier(
    operationalPropertiesEstablishment: Pick<IOperationalPropertiesEstablishment, 'id'>
  ): number {
    return operationalPropertiesEstablishment.id;
  }

  compareOperationalPropertiesEstablishment(
    o1: Pick<IOperationalPropertiesEstablishment, 'id'> | null,
    o2: Pick<IOperationalPropertiesEstablishment, 'id'> | null
  ): boolean {
    return o1 && o2
      ? this.getOperationalPropertiesEstablishmentIdentifier(o1) === this.getOperationalPropertiesEstablishmentIdentifier(o2)
      : o1 === o2;
  }

  addOperationalPropertiesEstablishmentToCollectionIfMissing<Type extends Pick<IOperationalPropertiesEstablishment, 'id'>>(
    operationalPropertiesEstablishmentCollection: Type[],
    ...operationalPropertiesEstablishmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operationalPropertiesEstablishments: Type[] = operationalPropertiesEstablishmentsToCheck.filter(isPresent);
    if (operationalPropertiesEstablishments.length > 0) {
      const operationalPropertiesEstablishmentCollectionIdentifiers = operationalPropertiesEstablishmentCollection.map(
        operationalPropertiesEstablishmentItem =>
          this.getOperationalPropertiesEstablishmentIdentifier(operationalPropertiesEstablishmentItem)!
      );
      const operationalPropertiesEstablishmentsToAdd = operationalPropertiesEstablishments.filter(
        operationalPropertiesEstablishmentItem => {
          const operationalPropertiesEstablishmentIdentifier = this.getOperationalPropertiesEstablishmentIdentifier(
            operationalPropertiesEstablishmentItem
          );
          if (operationalPropertiesEstablishmentCollectionIdentifiers.includes(operationalPropertiesEstablishmentIdentifier)) {
            return false;
          }
          operationalPropertiesEstablishmentCollectionIdentifiers.push(operationalPropertiesEstablishmentIdentifier);
          return true;
        }
      );
      return [...operationalPropertiesEstablishmentsToAdd, ...operationalPropertiesEstablishmentCollection];
    }
    return operationalPropertiesEstablishmentCollection;
  }
}
