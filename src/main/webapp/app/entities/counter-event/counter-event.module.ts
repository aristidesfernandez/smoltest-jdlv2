import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CounterEventComponent } from './list/counter-event.component';
import { CounterEventDetailComponent } from './detail/counter-event-detail.component';
import { CounterEventUpdateComponent } from './update/counter-event-update.component';
import { CounterEventDeleteDialogComponent } from './delete/counter-event-delete-dialog.component';
import { CounterEventRoutingModule } from './route/counter-event-routing.module';

@NgModule({
  imports: [SharedModule, CounterEventRoutingModule],
  declarations: [CounterEventComponent, CounterEventDetailComponent, CounterEventUpdateComponent, CounterEventDeleteDialogComponent],
})
export class CounterEventModule {}
