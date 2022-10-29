import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OperationalPropertiesEstablishmentComponent } from './list/operational-properties-establishment.component';
import { OperationalPropertiesEstablishmentDetailComponent } from './detail/operational-properties-establishment-detail.component';
import { OperationalPropertiesEstablishmentUpdateComponent } from './update/operational-properties-establishment-update.component';
import { OperationalPropertiesEstablishmentDeleteDialogComponent } from './delete/operational-properties-establishment-delete-dialog.component';
import { OperationalPropertiesEstablishmentRoutingModule } from './route/operational-properties-establishment-routing.module';

@NgModule({
  imports: [SharedModule, OperationalPropertiesEstablishmentRoutingModule],
  declarations: [
    OperationalPropertiesEstablishmentComponent,
    OperationalPropertiesEstablishmentDetailComponent,
    OperationalPropertiesEstablishmentUpdateComponent,
    OperationalPropertiesEstablishmentDeleteDialogComponent,
  ],
})
export class OperationalPropertiesEstablishmentModule {}
