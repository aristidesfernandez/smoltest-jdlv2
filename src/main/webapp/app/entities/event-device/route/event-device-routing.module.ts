import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventDeviceComponent } from '../list/event-device.component';
import { EventDeviceDetailComponent } from '../detail/event-device-detail.component';
import { EventDeviceUpdateComponent } from '../update/event-device-update.component';
import { EventDeviceRoutingResolveService } from './event-device-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const eventDeviceRoute: Routes = [
  {
    path: '',
    component: EventDeviceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventDeviceDetailComponent,
    resolve: {
      eventDevice: EventDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventDeviceUpdateComponent,
    resolve: {
      eventDevice: EventDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventDeviceUpdateComponent,
    resolve: {
      eventDevice: EventDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventDeviceRoute)],
  exports: [RouterModule],
})
export class EventDeviceRoutingModule {}
