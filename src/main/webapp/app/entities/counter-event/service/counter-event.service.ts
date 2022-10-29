import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICounterEvent, NewCounterEvent } from '../counter-event.model';

export type PartialUpdateCounterEvent = Partial<ICounterEvent> & Pick<ICounterEvent, 'id'>;

export type EntityResponseType = HttpResponse<ICounterEvent>;
export type EntityArrayResponseType = HttpResponse<ICounterEvent[]>;

@Injectable({ providedIn: 'root' })
export class CounterEventService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counter-events');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(counterEvent: NewCounterEvent): Observable<EntityResponseType> {
    return this.http.post<ICounterEvent>(this.resourceUrl, counterEvent, { observe: 'response' });
  }

  update(counterEvent: ICounterEvent): Observable<EntityResponseType> {
    return this.http.put<ICounterEvent>(`${this.resourceUrl}/${this.getCounterEventIdentifier(counterEvent)}`, counterEvent, {
      observe: 'response',
    });
  }

  partialUpdate(counterEvent: PartialUpdateCounterEvent): Observable<EntityResponseType> {
    return this.http.patch<ICounterEvent>(`${this.resourceUrl}/${this.getCounterEventIdentifier(counterEvent)}`, counterEvent, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICounterEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterEvent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCounterEventIdentifier(counterEvent: Pick<ICounterEvent, 'id'>): string {
    return counterEvent.id;
  }

  compareCounterEvent(o1: Pick<ICounterEvent, 'id'> | null, o2: Pick<ICounterEvent, 'id'> | null): boolean {
    return o1 && o2 ? this.getCounterEventIdentifier(o1) === this.getCounterEventIdentifier(o2) : o1 === o2;
  }

  addCounterEventToCollectionIfMissing<Type extends Pick<ICounterEvent, 'id'>>(
    counterEventCollection: Type[],
    ...counterEventsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const counterEvents: Type[] = counterEventsToCheck.filter(isPresent);
    if (counterEvents.length > 0) {
      const counterEventCollectionIdentifiers = counterEventCollection.map(
        counterEventItem => this.getCounterEventIdentifier(counterEventItem)!
      );
      const counterEventsToAdd = counterEvents.filter(counterEventItem => {
        const counterEventIdentifier = this.getCounterEventIdentifier(counterEventItem);
        if (counterEventCollectionIdentifiers.includes(counterEventIdentifier)) {
          return false;
        }
        counterEventCollectionIdentifiers.push(counterEventIdentifier);
        return true;
      });
      return [...counterEventsToAdd, ...counterEventCollection];
    }
    return counterEventCollection;
  }
}
