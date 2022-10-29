import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperationalPropertiesEstablishment } from '../operational-properties-establishment.model';
import { OperationalPropertiesEstablishmentService } from '../service/operational-properties-establishment.service';

@Injectable({ providedIn: 'root' })
export class OperationalPropertiesEstablishmentRoutingResolveService implements Resolve<IOperationalPropertiesEstablishment | null> {
  constructor(protected service: OperationalPropertiesEstablishmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperationalPropertiesEstablishment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operationalPropertiesEstablishment: HttpResponse<IOperationalPropertiesEstablishment>) => {
          if (operationalPropertiesEstablishment.body) {
            return of(operationalPropertiesEstablishment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
