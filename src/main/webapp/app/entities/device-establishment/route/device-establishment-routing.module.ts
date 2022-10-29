import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeviceEstablishmentComponent } from '../list/device-establishment.component';
import { DeviceEstablishmentDetailComponent } from '../detail/device-establishment-detail.component';
import { DeviceEstablishmentUpdateComponent } from '../update/device-establishment-update.component';
import { DeviceEstablishmentRoutingResolveService } from './device-establishment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const deviceEstablishmentRoute: Routes = [
  {
    path: '',
    component: DeviceEstablishmentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceEstablishmentDetailComponent,
    resolve: {
      deviceEstablishment: DeviceEstablishmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceEstablishmentUpdateComponent,
    resolve: {
      deviceEstablishment: DeviceEstablishmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceEstablishmentUpdateComponent,
    resolve: {
      deviceEstablishment: DeviceEstablishmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deviceEstablishmentRoute)],
  exports: [RouterModule],
})
export class DeviceEstablishmentRoutingModule {}
