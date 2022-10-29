import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KeyOperatingPropertyComponent } from '../list/key-operating-property.component';
import { KeyOperatingPropertyDetailComponent } from '../detail/key-operating-property-detail.component';
import { KeyOperatingPropertyUpdateComponent } from '../update/key-operating-property-update.component';
import { KeyOperatingPropertyRoutingResolveService } from './key-operating-property-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const keyOperatingPropertyRoute: Routes = [
  {
    path: '',
    component: KeyOperatingPropertyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KeyOperatingPropertyDetailComponent,
    resolve: {
      keyOperatingProperty: KeyOperatingPropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KeyOperatingPropertyUpdateComponent,
    resolve: {
      keyOperatingProperty: KeyOperatingPropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KeyOperatingPropertyUpdateComponent,
    resolve: {
      keyOperatingProperty: KeyOperatingPropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(keyOperatingPropertyRoute)],
  exports: [RouterModule],
})
export class KeyOperatingPropertyRoutingModule {}
