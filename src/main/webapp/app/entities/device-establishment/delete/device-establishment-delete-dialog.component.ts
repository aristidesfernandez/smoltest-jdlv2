import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceEstablishment } from '../device-establishment.model';
import { DeviceEstablishmentService } from '../service/device-establishment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './device-establishment-delete-dialog.component.html',
})
export class DeviceEstablishmentDeleteDialogComponent {
  deviceEstablishment?: IDeviceEstablishment;

  constructor(protected deviceEstablishmentService: DeviceEstablishmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.deviceEstablishmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
