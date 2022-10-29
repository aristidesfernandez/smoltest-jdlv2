import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CounterTypeComponent } from './list/counter-type.component';
import { CounterTypeDetailComponent } from './detail/counter-type-detail.component';
import { CounterTypeUpdateComponent } from './update/counter-type-update.component';
import { CounterTypeDeleteDialogComponent } from './delete/counter-type-delete-dialog.component';
import { CounterTypeRoutingModule } from './route/counter-type-routing.module';

@NgModule({
  imports: [SharedModule, CounterTypeRoutingModule],
  declarations: [CounterTypeComponent, CounterTypeDetailComponent, CounterTypeUpdateComponent, CounterTypeDeleteDialogComponent],
})
export class CounterTypeModule {}
