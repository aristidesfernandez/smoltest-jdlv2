import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceCategory } from '../device-category.model';
import { DeviceCategoryService } from '../service/device-category.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './device-category-delete-dialog.component.html',
})
export class DeviceCategoryDeleteDialogComponent {
  deviceCategory?: IDeviceCategory;

  constructor(protected deviceCategoryService: DeviceCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
