import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceInterface } from '../device-interface.model';
import { DeviceInterfaceService } from '../service/device-interface.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './device-interface-delete-dialog.component.html',
})
export class DeviceInterfaceDeleteDialogComponent {
  deviceInterface?: IDeviceInterface;

  constructor(protected deviceInterfaceService: DeviceInterfaceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceInterfaceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
