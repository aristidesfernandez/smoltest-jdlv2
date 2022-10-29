import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeviceCategory } from '../device-category.model';
import { DeviceCategoryService } from '../service/device-category.service';

@Injectable({ providedIn: 'root' })
export class DeviceCategoryRoutingResolveService implements Resolve<IDeviceCategory | null> {
  constructor(protected service: DeviceCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceCategory | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deviceCategory: HttpResponse<IDeviceCategory>) => {
          if (deviceCategory.body) {
            return of(deviceCategory.body);
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
