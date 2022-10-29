import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICurrencyType } from '../currency-type.model';
import { CurrencyTypeService } from '../service/currency-type.service';

@Injectable({ providedIn: 'root' })
export class CurrencyTypeRoutingResolveService implements Resolve<ICurrencyType | null> {
  constructor(protected service: CurrencyTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICurrencyType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((currencyType: HttpResponse<ICurrencyType>) => {
          if (currencyType.body) {
            return of(currencyType.body);
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
