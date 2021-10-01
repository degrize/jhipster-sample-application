import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INouveau } from '../nouveau.model';
import { NouveauService } from '../service/nouveau.service';

@Component({
  templateUrl: './nouveau-delete-dialog.component.html',
})
export class NouveauDeleteDialogComponent {
  nouveau?: INouveau;

  constructor(protected nouveauService: NouveauService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nouveauService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
