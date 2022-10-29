import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CounterTypeComponent } from '../list/counter-type.component';
import { CounterTypeDetailComponent } from '../detail/counter-type-detail.component';
import { CounterTypeUpdateComponent } from '../update/counter-type-update.component';
import { CounterTypeRoutingResolveService } from './counter-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const counterTypeRoute: Routes = [
  {
    path: '',
    component: CounterTypeComponent,
    data: {
      defaultSort: 'counterCode,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':counterCode/view',
    component: CounterTypeDetailComponent,
    resolve: {
      counterType: CounterTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CounterTypeUpdateComponent,
    resolve: {
      counterType: CounterTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':counterCode/edit',
    component: CounterTypeUpdateComponent,
    resolve: {
      counterType: CounterTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(counterTypeRoute)],
  exports: [RouterModule],
})
export class CounterTypeRoutingModule {}
