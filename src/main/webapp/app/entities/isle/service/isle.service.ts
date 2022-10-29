import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIsle, NewIsle } from '../isle.model';

export type PartialUpdateIsle = Partial<IIsle> & Pick<IIsle, 'id'>;

export type EntityResponseType = HttpResponse<IIsle>;
export type EntityArrayResponseType = HttpResponse<IIsle[]>;

@Injectable({ providedIn: 'root' })
export class IsleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/isles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(isle: NewIsle): Observable<EntityResponseType> {
    return this.http.post<IIsle>(this.resourceUrl, isle, { observe: 'response' });
  }

  update(isle: IIsle): Observable<EntityResponseType> {
    return this.http.put<IIsle>(`${this.resourceUrl}/${this.getIsleIdentifier(isle)}`, isle, { observe: 'response' });
  }

  partialUpdate(isle: PartialUpdateIsle): Observable<EntityResponseType> {
    return this.http.patch<IIsle>(`${this.resourceUrl}/${this.getIsleIdentifier(isle)}`, isle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIsle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIsle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIsleIdentifier(isle: Pick<IIsle, 'id'>): number {
    return isle.id;
  }

  compareIsle(o1: Pick<IIsle, 'id'> | null, o2: Pick<IIsle, 'id'> | null): boolean {
    return o1 && o2 ? this.getIsleIdentifier(o1) === this.getIsleIdentifier(o2) : o1 === o2;
  }

  addIsleToCollectionIfMissing<Type extends Pick<IIsle, 'id'>>(
    isleCollection: Type[],
    ...islesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const isles: Type[] = islesToCheck.filter(isPresent);
    if (isles.length > 0) {
      const isleCollectionIdentifiers = isleCollection.map(isleItem => this.getIsleIdentifier(isleItem)!);
      const islesToAdd = isles.filter(isleItem => {
        const isleIdentifier = this.getIsleIdentifier(isleItem);
        if (isleCollectionIdentifiers.includes(isleIdentifier)) {
          return false;
        }
        isleCollectionIdentifiers.push(isleIdentifier);
        return true;
      });
      return [...islesToAdd, ...isleCollection];
    }
    return isleCollection;
  }
}
