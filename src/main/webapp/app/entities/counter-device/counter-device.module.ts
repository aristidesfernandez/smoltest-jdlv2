import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CounterDeviceComponent } from './list/counter-device.component';
import { CounterDeviceDetailComponent } from './detail/counter-device-detail.component';
import { CounterDeviceUpdateComponent } from './update/counter-device-update.component';
import { CounterDeviceDeleteDialogComponent } from './delete/counter-device-delete-dialog.component';
import { CounterDeviceRoutingModule } from './route/counter-device-routing.module';

@NgModule({
  imports: [SharedModule, CounterDeviceRoutingModule],
  declarations: [CounterDeviceComponent, CounterDeviceDetailComponent, CounterDeviceUpdateComponent, CounterDeviceDeleteDialogComponent],
})
export class CounterDeviceModule {}
