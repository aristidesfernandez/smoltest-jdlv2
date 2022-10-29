import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsleComponent } from './list/isle.component';
import { IsleDetailComponent } from './detail/isle-detail.component';
import { IsleUpdateComponent } from './update/isle-update.component';
import { IsleDeleteDialogComponent } from './delete/isle-delete-dialog.component';
import { IsleRoutingModule } from './route/isle-routing.module';

@NgModule({
  imports: [SharedModule, IsleRoutingModule],
  declarations: [IsleComponent, IsleDetailComponent, IsleUpdateComponent, IsleDeleteDialogComponent],
})
export class IsleModule {}
