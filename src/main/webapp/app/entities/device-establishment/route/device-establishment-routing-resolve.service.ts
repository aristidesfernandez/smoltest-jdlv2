import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeviceEstablishment } from '../device-establishment.model';
import { DeviceEstablishmentService } from '../service/device-establishment.service';

@Injectable({ providedIn: 'root' })
export class DeviceEstablishmentRoutingResolveService implements Resolve<IDeviceEstablishment | null> {
  constructor(protected service: DeviceEstablishmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceEstablishment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deviceEstablishment: HttpResponse<IDeviceEstablishment>) => {
          if (deviceEstablishment.body) {
            return of(deviceEstablishment.body);
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
