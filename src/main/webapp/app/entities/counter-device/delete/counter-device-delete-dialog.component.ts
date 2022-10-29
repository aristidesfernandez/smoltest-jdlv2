import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICounterDevice } from '../counter-device.model';
import { CounterDeviceService } from '../service/counter-device.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './counter-device-delete-dialog.component.html',
})
export class CounterDeviceDeleteDialogComponent {
  counterDevice?: ICounterDevice;

  constructor(protected counterDeviceService: CounterDeviceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.counterDeviceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
