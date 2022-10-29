import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeviceInterfaceComponent } from '../list/device-interface.component';
import { DeviceInterfaceDetailComponent } from '../detail/device-interface-detail.component';
import { DeviceInterfaceUpdateComponent } from '../update/device-interface-update.component';
import { DeviceInterfaceRoutingResolveService } from './device-interface-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const deviceInterfaceRoute: Routes = [
  {
    path: '',
    component: DeviceInterfaceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceInterfaceDetailComponent,
    resolve: {
      deviceInterface: DeviceInterfaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceInterfaceUpdateComponent,
    resolve: {
      deviceInterface: DeviceInterfaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceInterfaceUpdateComponent,
    resolve: {
      deviceInterface: DeviceInterfaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deviceInterfaceRoute)],
  exports: [RouterModule],
})
export class DeviceInterfaceRoutingModule {}
