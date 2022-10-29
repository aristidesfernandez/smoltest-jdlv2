import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMunicipality } from '../municipality.model';
import { MunicipalityService } from '../service/municipality.service';

@Injectable({ providedIn: 'root' })
export class MunicipalityRoutingResolveService implements Resolve<IMunicipality | null> {
  constructor(protected service: MunicipalityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMunicipality | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((municipality: HttpResponse<IMunicipality>) => {
          if (municipality.body) {
            return of(municipality.body);
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
