import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsleComponent } from '../list/isle.component';
import { IsleDetailComponent } from '../detail/isle-detail.component';
import { IsleUpdateComponent } from '../update/isle-update.component';
import { IsleRoutingResolveService } from './isle-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const isleRoute: Routes = [
  {
    path: '',
    component: IsleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsleDetailComponent,
    resolve: {
      isle: IsleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsleUpdateComponent,
    resolve: {
      isle: IsleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsleUpdateComponent,
    resolve: {
      isle: IsleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isleRoute)],
  exports: [RouterModule],
})
export class IsleRoutingModule {}
