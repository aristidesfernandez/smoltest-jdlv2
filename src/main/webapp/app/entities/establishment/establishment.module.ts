import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EstablishmentComponent } from './list/establishment.component';
import { EstablishmentDetailComponent } from './detail/establishment-detail.component';
import { EstablishmentUpdateComponent } from './update/establishment-update.component';
import { EstablishmentDeleteDialogComponent } from './delete/establishment-delete-dialog.component';
import { EstablishmentRoutingModule } from './route/establishment-routing.module';

@NgModule({
  imports: [SharedModule, EstablishmentRoutingModule],
  declarations: [EstablishmentComponent, EstablishmentDetailComponent, EstablishmentUpdateComponent, EstablishmentDeleteDialogComponent],
})
export class EstablishmentModule {}
