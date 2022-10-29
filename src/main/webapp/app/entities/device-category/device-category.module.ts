import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceCategoryComponent } from './list/device-category.component';
import { DeviceCategoryDetailComponent } from './detail/device-category-detail.component';
import { DeviceCategoryUpdateComponent } from './update/device-category-update.component';
import { DeviceCategoryDeleteDialogComponent } from './delete/device-category-delete-dialog.component';
import { DeviceCategoryRoutingModule } from './route/device-category-routing.module';

@NgModule({
  imports: [SharedModule, DeviceCategoryRoutingModule],
  declarations: [
    DeviceCategoryComponent,
    DeviceCategoryDetailComponent,
    DeviceCategoryUpdateComponent,
    DeviceCategoryDeleteDialogComponent,
  ],
})
export class DeviceCategoryModule {}
