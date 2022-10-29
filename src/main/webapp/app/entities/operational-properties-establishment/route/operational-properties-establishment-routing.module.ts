import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OperationalPropertiesEstablishmentComponent } from '../list/operational-properties-establishment.component';
import { OperationalPropertiesEstablishmentDetailComponent } from '../detail/operational-properties-establishment-detail.component';
import { OperationalPropertiesEstablishmentUpdateComponent } from '../update/operational-properties-establishment-update.component';
import { OperationalPropertiesEstablishmentRoutingResolveService } from './operational-properties-establishment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const operationalPropertiesEstablishmentRoute: Routes = [
  {
    path: '',
    component: OperationalPropertiesEstablishmentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperationalPropertiesEstablishmentDetailComponent,
    resolve: {
      operationalPropertiesEstablishment: OperationalPropertiesEstablishmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperationalPropertiesEstablishmentUpdateComponent,
    resolve: {
      operationalPropertiesEstablishment: OperationalPropertiesEstablishmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperationalPropertiesEstablishmentUpdateComponent,
    resolve: {
      operationalPropertiesEstablishment: OperationalPropertiesEstablishmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(operationalPropertiesEstablishmentRoute)],
  exports: [RouterModule],
})
export class OperationalPropertiesEstablishmentRoutingModule {}
