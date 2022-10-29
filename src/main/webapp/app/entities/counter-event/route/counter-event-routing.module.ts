import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CounterEventComponent } from '../list/counter-event.component';
import { CounterEventDetailComponent } from '../detail/counter-event-detail.component';
import { CounterEventUpdateComponent } from '../update/counter-event-update.component';
import { CounterEventRoutingResolveService } from './counter-event-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const counterEventRoute: Routes = [
  {
    path: '',
    component: CounterEventComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CounterEventDetailComponent,
    resolve: {
      counterEvent: CounterEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CounterEventUpdateComponent,
    resolve: {
      counterEvent: CounterEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CounterEventUpdateComponent,
    resolve: {
      counterEvent: CounterEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(counterEventRoute)],
  exports: [RouterModule],
})
export class CounterEventRoutingModule {}
