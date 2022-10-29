import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KeyOperatingPropertyComponent } from './list/key-operating-property.component';
import { KeyOperatingPropertyDetailComponent } from './detail/key-operating-property-detail.component';
import { KeyOperatingPropertyUpdateComponent } from './update/key-operating-property-update.component';
import { KeyOperatingPropertyDeleteDialogComponent } from './delete/key-operating-property-delete-dialog.component';
import { KeyOperatingPropertyRoutingModule } from './route/key-operating-property-routing.module';

@NgModule({
  imports: [SharedModule, KeyOperatingPropertyRoutingModule],
  declarations: [
    KeyOperatingPropertyComponent,
    KeyOperatingPropertyDetailComponent,
    KeyOperatingPropertyUpdateComponent,
    KeyOperatingPropertyDeleteDialogComponent,
  ],
})
export class KeyOperatingPropertyModule {}
