import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGem } from '../gem.model';
import { GemService } from '../service/gem.service';

@Component({
  templateUrl: './gem-delete-dialog.component.html',
})
export class GemDeleteDialogComponent {
  gem?: IGem;

  constructor(protected gemService: GemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
