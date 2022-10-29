import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MunicipalityComponent } from './list/municipality.component';
import { MunicipalityDetailComponent } from './detail/municipality-detail.component';
import { MunicipalityUpdateComponent } from './update/municipality-update.component';
import { MunicipalityDeleteDialogComponent } from './delete/municipality-delete-dialog.component';
import { MunicipalityRoutingModule } from './route/municipality-routing.module';

@NgModule({
  imports: [SharedModule, MunicipalityRoutingModule],
  declarations: [MunicipalityComponent, MunicipalityDetailComponent, MunicipalityUpdateComponent, MunicipalityDeleteDialogComponent],
})
export class MunicipalityModule {}
