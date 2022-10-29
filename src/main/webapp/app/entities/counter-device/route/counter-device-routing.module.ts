import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CounterDeviceComponent } from '../list/counter-device.component';
import { CounterDeviceDetailComponent } from '../detail/counter-device-detail.component';
import { CounterDeviceUpdateComponent } from '../update/counter-device-update.component';
import { CounterDeviceRoutingResolveService } from './counter-device-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const counterDeviceRoute: Routes = [
  {
    path: '',
    component: CounterDeviceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CounterDeviceDetailComponent,
    resolve: {
      counterDevice: CounterDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CounterDeviceUpdateComponent,
    resolve: {
      counterDevice: CounterDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CounterDeviceUpdateComponent,
    resolve: {
      counterDevice: CounterDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(counterDeviceRoute)],
  exports: [RouterModule],
})
export class CounterDeviceRoutingModule {}
