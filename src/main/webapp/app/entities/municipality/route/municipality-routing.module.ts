import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MunicipalityComponent } from '../list/municipality.component';
import { MunicipalityDetailComponent } from '../detail/municipality-detail.component';
import { MunicipalityUpdateComponent } from '../update/municipality-update.component';
import { MunicipalityRoutingResolveService } from './municipality-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const municipalityRoute: Routes = [
  {
    path: '',
    component: MunicipalityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MunicipalityDetailComponent,
    resolve: {
      municipality: MunicipalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MunicipalityUpdateComponent,
    resolve: {
      municipality: MunicipalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MunicipalityUpdateComponent,
    resolve: {
      municipality: MunicipalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(municipalityRoute)],
  exports: [RouterModule],
})
export class MunicipalityRoutingModule {}
