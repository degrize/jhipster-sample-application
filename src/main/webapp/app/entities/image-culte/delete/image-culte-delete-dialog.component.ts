import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageCulte } from '../image-culte.model';
import { ImageCulteService } from '../service/image-culte.service';

@Component({
  templateUrl: './image-culte-delete-dialog.component.html',
})
export class ImageCulteDeleteDialogComponent {
  imageCulte?: IImageCulte;

  constructor(protected imageCulteService: ImageCulteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageCulteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
