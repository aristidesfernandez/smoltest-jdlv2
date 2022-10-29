import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMunicipality } from '../municipality.model';
import { MunicipalityService } from '../service/municipality.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './municipality-delete-dialog.component.html',
})
export class MunicipalityDeleteDialogComponent {
  municipality?: IMunicipality;

  constructor(protected municipalityService: MunicipalityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.municipalityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
