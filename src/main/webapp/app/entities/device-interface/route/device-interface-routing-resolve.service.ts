import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeviceInterface } from '../device-interface.model';
import { DeviceInterfaceService } from '../service/device-interface.service';

@Injectable({ providedIn: 'root' })
export class DeviceInterfaceRoutingResolveService implements Resolve<IDeviceInterface | null> {
  constructor(protected service: DeviceInterfaceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceInterface | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deviceInterface: HttpResponse<IDeviceInterface>) => {
          if (deviceInterface.body) {
            return of(deviceInterface.body);
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
