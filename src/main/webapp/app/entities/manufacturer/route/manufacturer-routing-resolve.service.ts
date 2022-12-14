import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManufacturer } from '../manufacturer.model';
import { ManufacturerService } from '../service/manufacturer.service';

@Injectable({ providedIn: 'root' })
export class ManufacturerRoutingResolveService implements Resolve<IManufacturer | null> {
  constructor(protected service: ManufacturerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManufacturer | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((manufacturer: HttpResponse<IManufacturer>) => {
          if (manufacturer.body) {
            return of(manufacturer.body);
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
