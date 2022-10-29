import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKeyOperatingProperty } from '../key-operating-property.model';
import { KeyOperatingPropertyService } from '../service/key-operating-property.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './key-operating-property-delete-dialog.component.html',
})
export class KeyOperatingPropertyDeleteDialogComponent {
  keyOperatingProperty?: IKeyOperatingProperty;

  constructor(protected keyOperatingPropertyService: KeyOperatingPropertyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.keyOperatingPropertyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
