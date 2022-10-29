import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventDeviceComponent } from './list/event-device.component';
import { EventDeviceDetailComponent } from './detail/event-device-detail.component';
import { EventDeviceUpdateComponent } from './update/event-device-update.component';
import { EventDeviceDeleteDialogComponent } from './delete/event-device-delete-dialog.component';
import { EventDeviceRoutingModule } from './route/event-device-routing.module';

@NgModule({
  imports: [SharedModule, EventDeviceRoutingModule],
  declarations: [EventDeviceComponent, EventDeviceDetailComponent, EventDeviceUpdateComponent, EventDeviceDeleteDialogComponent],
})
export class EventDeviceModule {}
