import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventDevice } from '../event-device.model';
import { EventDeviceService } from '../service/event-device.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './event-device-delete-dialog.component.html',
})
export class EventDeviceDeleteDialogComponent {
  eventDevice?: IEventDevice;

  constructor(protected eventDeviceService: EventDeviceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.eventDeviceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
