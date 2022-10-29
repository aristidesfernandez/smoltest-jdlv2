import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAccess, NewUserAccess } from '../user-access.model';

export type PartialUpdateUserAccess = Partial<IUserAccess> & Pick<IUserAccess, 'id'>;

type RestOf<T extends IUserAccess | NewUserAccess> = Omit<T, 'registrationAt'> & {
  registrationAt?: string | null;
};

export type RestUserAccess = RestOf<IUserAccess>;

export type NewRestUserAccess = RestOf<NewUserAccess>;

export type PartialUpdateRestUserAccess = RestOf<PartialUpdateUserAccess>;

export type EntityResponseType = HttpResponse<IUserAccess>;
export type EntityArrayResponseType = HttpResponse<IUserAccess[]>;

@Injectable({ providedIn: 'root' })
export class UserAccessService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-accesses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userAccess: NewUserAccess): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAccess);
    return this.http
      .post<RestUserAccess>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userAccess: IUserAccess): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAccess);
    return this.http
      .put<RestUserAccess>(`${this.resourceUrl}/${this.getUserAccessIdentifier(userAccess)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userAccess: PartialUpdateUserAccess): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAccess);
    return this.http
      .patch<RestUserAccess>(`${this.resourceUrl}/${this.getUserAccessIdentifier(userAccess)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserAccess>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserAccess[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserAccessIdentifier(userAccess: Pick<IUserAccess, 'id'>): number {
    return userAccess.id;
  }

  compareUserAccess(o1: Pick<IUserAccess, 'id'> | null, o2: Pick<IUserAccess, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserAccessIdentifier(o1) === this.getUserAccessIdentifier(o2) : o1 === o2;
  }

  addUserAccessToCollectionIfMissing<Type extends Pick<IUserAccess, 'id'>>(
    userAccessCollection: Type[],
    ...userAccessesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userAccesses: Type[] = userAccessesToCheck.filter(isPresent);
    if (userAccesses.length > 0) {
      const userAccessCollectionIdentifiers = userAccessCollection.map(userAccessItem => this.getUserAccessIdentifier(userAccessItem)!);
      const userAccessesToAdd = userAccesses.filter(userAccessItem => {
        const userAccessIdentifier = this.getUserAccessIdentifier(userAccessItem);
        if (userAccessCollectionIdentifiers.includes(userAccessIdentifier)) {
          return false;
        }
        userAccessCollectionIdentifiers.push(userAccessIdentifier);
        return true;
      });
      return [...userAccessesToAdd, ...userAccessCollection];
    }
    return userAccessCollection;
  }

  protected convertDateFromClient<T extends IUserAccess | NewUserAccess | PartialUpdateUserAccess>(userAccess: T): RestOf<T> {
    return {
      ...userAccess,
      registrationAt: userAccess.registrationAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserAccess: RestUserAccess): IUserAccess {
    return {
      ...restUserAccess,
      registrationAt: restUserAccess.registrationAt ? dayjs(restUserAccess.registrationAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserAccess>): HttpResponse<IUserAccess> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserAccess[]>): HttpResponse<IUserAccess[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
