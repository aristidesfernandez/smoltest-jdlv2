import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEstablishment } from '../establishment.model';
import { EstablishmentService } from '../service/establishment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './establishment-delete-dialog.component.html',
})
export class EstablishmentDeleteDialogComponent {
  establishment?: IEstablishment;

  constructor(protected establishmentService: EstablishmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.establishmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
