import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperator, NewOperator } from '../operator.model';

export type PartialUpdateOperator = Partial<IOperator> & Pick<IOperator, 'id'>;

type RestOf<T extends IOperator | NewOperator> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestOperator = RestOf<IOperator>;

export type NewRestOperator = RestOf<NewOperator>;

export type PartialUpdateRestOperator = RestOf<PartialUpdateOperator>;

export type EntityResponseType = HttpResponse<IOperator>;
export type EntityArrayResponseType = HttpResponse<IOperator[]>;

@Injectable({ providedIn: 'root' })
export class OperatorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operators');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operator: NewOperator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operator);
    return this.http
      .post<RestOperator>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(operator: IOperator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operator);
    return this.http
      .put<RestOperator>(`${this.resourceUrl}/${this.getOperatorIdentifier(operator)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(operator: PartialUpdateOperator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operator);
    return this.http
      .patch<RestOperator>(`${this.resourceUrl}/${this.getOperatorIdentifier(operator)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOperator>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOperator[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperatorIdentifier(operator: Pick<IOperator, 'id'>): number {
    return operator.id;
  }

  compareOperator(o1: Pick<IOperator, 'id'> | null, o2: Pick<IOperator, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperatorIdentifier(o1) === this.getOperatorIdentifier(o2) : o1 === o2;
  }

  addOperatorToCollectionIfMissing<Type extends Pick<IOperator, 'id'>>(
    operatorCollection: Type[],
    ...operatorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operators: Type[] = operatorsToCheck.filter(isPresent);
    if (operators.length > 0) {
      const operatorCollectionIdentifiers = operatorCollection.map(operatorItem => this.getOperatorIdentifier(operatorItem)!);
      const operatorsToAdd = operators.filter(operatorItem => {
        const operatorIdentifier = this.getOperatorIdentifier(operatorItem);
        if (operatorCollectionIdentifiers.includes(operatorIdentifier)) {
          return false;
        }
        operatorCollectionIdentifiers.push(operatorIdentifier);
        return true;
      });
      return [...operatorsToAdd, ...operatorCollection];
    }
    return operatorCollection;
  }

  protected convertDateFromClient<T extends IOperator | NewOperator | PartialUpdateOperator>(operator: T): RestOf<T> {
    return {
      ...operator,
      startDate: operator.startDate?.toJSON() ?? null,
      endDate: operator.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOperator: RestOperator): IOperator {
    return {
      ...restOperator,
      startDate: restOperator.startDate ? dayjs(restOperator.startDate) : undefined,
      endDate: restOperator.endDate ? dayjs(restOperator.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOperator>): HttpResponse<IOperator> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOperator[]>): HttpResponse<IOperator[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
