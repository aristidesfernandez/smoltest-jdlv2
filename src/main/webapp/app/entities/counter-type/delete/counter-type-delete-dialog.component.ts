import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICounterType } from '../counter-type.model';
import { CounterTypeService } from '../service/counter-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './counter-type-delete-dialog.component.html',
})
export class CounterTypeDeleteDialogComponent {
  counterType?: ICounterType;

  constructor(protected counterTypeService: CounterTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.counterTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
