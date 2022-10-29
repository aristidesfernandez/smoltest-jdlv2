import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceInterfaceComponent } from './list/device-interface.component';
import { DeviceInterfaceDetailComponent } from './detail/device-interface-detail.component';
import { DeviceInterfaceUpdateComponent } from './update/device-interface-update.component';
import { DeviceInterfaceDeleteDialogComponent } from './delete/device-interface-delete-dialog.component';
import { DeviceInterfaceRoutingModule } from './route/device-interface-routing.module';

@NgModule({
  imports: [SharedModule, DeviceInterfaceRoutingModule],
  declarations: [
    DeviceInterfaceComponent,
    DeviceInterfaceDetailComponent,
    DeviceInterfaceUpdateComponent,
    DeviceInterfaceDeleteDialogComponent,
  ],
})
export class DeviceInterfaceModule {}
