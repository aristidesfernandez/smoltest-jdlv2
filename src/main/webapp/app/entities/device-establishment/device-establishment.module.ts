import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceEstablishmentComponent } from './list/device-establishment.component';
import { DeviceEstablishmentDetailComponent } from './detail/device-establishment-detail.component';
import { DeviceEstablishmentUpdateComponent } from './update/device-establishment-update.component';
import { DeviceEstablishmentDeleteDialogComponent } from './delete/device-establishment-delete-dialog.component';
import { DeviceEstablishmentRoutingModule } from './route/device-establishment-routing.module';

@NgModule({
  imports: [SharedModule, DeviceEstablishmentRoutingModule],
  declarations: [
    DeviceEstablishmentComponent,
    DeviceEstablishmentDetailComponent,
    DeviceEstablishmentUpdateComponent,
    DeviceEstablishmentDeleteDialogComponent,
  ],
})
export class DeviceEstablishmentModule {}
