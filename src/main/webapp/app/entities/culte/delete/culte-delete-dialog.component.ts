import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICulte } from '../culte.model';
import { CulteService } from '../service/culte.service';

@Component({
  templateUrl: './culte-delete-dialog.component.html',
})
export class CulteDeleteDialogComponent {
  culte?: ICulte;

  constructor(protected culteService: CulteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.culteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
