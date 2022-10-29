import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CurrencyTypeComponent } from './list/currency-type.component';
import { CurrencyTypeDetailComponent } from './detail/currency-type-detail.component';
import { CurrencyTypeUpdateComponent } from './update/currency-type-update.component';
import { CurrencyTypeDeleteDialogComponent } from './delete/currency-type-delete-dialog.component';
import { CurrencyTypeRoutingModule } from './route/currency-type-routing.module';

@NgModule({
  imports: [SharedModule, CurrencyTypeRoutingModule],
  declarations: [CurrencyTypeComponent, CurrencyTypeDetailComponent, CurrencyTypeUpdateComponent, CurrencyTypeDeleteDialogComponent],
})
export class CurrencyTypeModule {}
