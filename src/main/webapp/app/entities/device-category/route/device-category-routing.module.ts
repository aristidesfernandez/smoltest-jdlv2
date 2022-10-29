import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeviceCategoryComponent } from '../list/device-category.component';
import { DeviceCategoryDetailComponent } from '../detail/device-category-detail.component';
import { DeviceCategoryUpdateComponent } from '../update/device-category-update.component';
import { DeviceCategoryRoutingResolveService } from './device-category-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const deviceCategoryRoute: Routes = [
  {
    path: '',
    component: DeviceCategoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceCategoryDetailComponent,
    resolve: {
      deviceCategory: DeviceCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceCategoryUpdateComponent,
    resolve: {
      deviceCategory: DeviceCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceCategoryUpdateComponent,
    resolve: {
      deviceCategory: DeviceCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deviceCategoryRoute)],
  exports: [RouterModule],
})
export class DeviceCategoryRoutingModule {}
