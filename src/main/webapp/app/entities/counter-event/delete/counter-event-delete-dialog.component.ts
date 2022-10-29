import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICounterEvent } from '../counter-event.model';
import { CounterEventService } from '../service/counter-event.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './counter-event-delete-dialog.component.html',
})
export class CounterEventDeleteDialogComponent {
  counterEvent?: ICounterEvent;

  constructor(protected counterEventService: CounterEventService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.counterEventService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
