import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CurrencyTypeComponent } from '../list/currency-type.component';
import { CurrencyTypeDetailComponent } from '../detail/currency-type-detail.component';
import { CurrencyTypeUpdateComponent } from '../update/currency-type-update.component';
import { CurrencyTypeRoutingResolveService } from './currency-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const currencyTypeRoute: Routes = [
  {
    path: '',
    component: CurrencyTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CurrencyTypeDetailComponent,
    resolve: {
      currencyType: CurrencyTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CurrencyTypeUpdateComponent,
    resolve: {
      currencyType: CurrencyTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CurrencyTypeUpdateComponent,
    resolve: {
      currencyType: CurrencyTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(currencyTypeRoute)],
  exports: [RouterModule],
})
export class CurrencyTypeRoutingModule {}
