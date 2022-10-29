import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEstablishment, NewEstablishment } from '../establishment.model';

export type PartialUpdateEstablishment = Partial<IEstablishment> & Pick<IEstablishment, 'id'>;

type RestOf<T extends IEstablishment | NewEstablishment> = Omit<T, 'liquidationTime' | 'startTime' | 'closeTime'> & {
  liquidationTime?: string | null;
  startTime?: string | null;
  closeTime?: string | null;
};

export type RestEstablishment = RestOf<IEstablishment>;

export type NewRestEstablishment = RestOf<NewEstablishment>;

export type PartialUpdateRestEstablishment = RestOf<PartialUpdateEstablishment>;

export type EntityResponseType = HttpResponse<IEstablishment>;
export type EntityArrayResponseType = HttpResponse<IEstablishment[]>;

@Injectable({ providedIn: 'root' })
export class EstablishmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/establishments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(establishment: NewEstablishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(establishment);
    return this.http
      .post<RestEstablishment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(establishment: IEstablishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(establishment);
    return this.http
      .put<RestEstablishment>(`${this.resourceUrl}/${this.getEstablishmentIdentifier(establishment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(establishment: PartialUpdateEstablishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(establishment);
    return this.http
      .patch<RestEstablishment>(`${this.resourceUrl}/${this.getEstablishmentIdentifier(establishment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEstablishment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEstablishment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEstablishmentIdentifier(establishment: Pick<IEstablishment, 'id'>): number {
    return establishment.id;
  }

  compareEstablishment(o1: Pick<IEstablishment, 'id'> | null, o2: Pick<IEstablishment, 'id'> | null): boolean {
    return o1 && o2 ? this.getEstablishmentIdentifier(o1) === this.getEstablishmentIdentifier(o2) : o1 === o2;
  }

  addEstablishmentToCollectionIfMissing<Type extends Pick<IEstablishment, 'id'>>(
    establishmentCollection: Type[],
    ...establishmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const establishments: Type[] = establishmentsToCheck.filter(isPresent);
    if (establishments.length > 0) {
      const establishmentCollectionIdentifiers = establishmentCollection.map(
        establishmentItem => this.getEstablishmentIdentifier(establishmentItem)!
      );
      const establishmentsToAdd = establishments.filter(establishmentItem => {
        const establishmentIdentifier = this.getEstablishmentIdentifier(establishmentItem);
        if (establishmentCollectionIdentifiers.includes(establishmentIdentifier)) {
          return false;
        }
        establishmentCollectionIdentifiers.push(establishmentIdentifier);
        return true;
      });
      return [...establishmentsToAdd, ...establishmentCollection];
    }
    return establishmentCollection;
  }

  protected convertDateFromClient<T extends IEstablishment | NewEstablishment | PartialUpdateEstablishment>(establishment: T): RestOf<T> {
    return {
      ...establishment,
      liquidationTime: establishment.liquidationTime?.toJSON() ?? null,
      startTime: establishment.startTime?.toJSON() ?? null,
      closeTime: establishment.closeTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEstablishment: RestEstablishment): IEstablishment {
    return {
      ...restEstablishment,
      liquidationTime: restEstablishment.liquidationTime ? dayjs(restEstablishment.liquidationTime) : undefined,
      startTime: restEstablishment.startTime ? dayjs(restEstablishment.startTime) : undefined,
      closeTime: restEstablishment.closeTime ? dayjs(restEstablishment.closeTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEstablishment>): HttpResponse<IEstablishment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEstablishment[]>): HttpResponse<IEstablishment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
