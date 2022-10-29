import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperationalPropertiesEstablishment } from '../operational-properties-establishment.model';
import { OperationalPropertiesEstablishmentService } from '../service/operational-properties-establishment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './operational-properties-establishment-delete-dialog.component.html',
})
export class OperationalPropertiesEstablishmentDeleteDialogComponent {
  operationalPropertiesEstablishment?: IOperationalPropertiesEstablishment;

  constructor(
    protected operationalPropertiesEstablishmentService: OperationalPropertiesEstablishmentService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operationalPropertiesEstablishmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
