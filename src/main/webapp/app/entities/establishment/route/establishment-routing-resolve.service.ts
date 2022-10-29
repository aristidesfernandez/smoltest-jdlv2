import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEstablishment } from '../establishment.model';
import { EstablishmentService } from '../service/establishment.service';

@Injectable({ providedIn: 'root' })
export class EstablishmentRoutingResolveService implements Resolve<IEstablishment | null> {
  constructor(protected service: EstablishmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEstablishment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((establishment: HttpResponse<IEstablishment>) => {
          if (establishment.body) {
            return of(establishment.body);
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
