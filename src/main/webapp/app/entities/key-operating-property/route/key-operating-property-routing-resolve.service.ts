import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKeyOperatingProperty } from '../key-operating-property.model';
import { KeyOperatingPropertyService } from '../service/key-operating-property.service';

@Injectable({ providedIn: 'root' })
export class KeyOperatingPropertyRoutingResolveService implements Resolve<IKeyOperatingProperty | null> {
  constructor(protected service: KeyOperatingPropertyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKeyOperatingProperty | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((keyOperatingProperty: HttpResponse<IKeyOperatingProperty>) => {
          if (keyOperatingProperty.body) {
            return of(keyOperatingProperty.body);
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
