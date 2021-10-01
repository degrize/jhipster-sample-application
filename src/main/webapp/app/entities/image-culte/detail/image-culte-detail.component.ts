import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImageCulte } from '../image-culte.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-image-culte-detail',
  templateUrl: './image-culte-detail.component.html',
})
export class ImageCulteDetailComponent implements OnInit {
  imageCulte: IImageCulte | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageCulte }) => {
      this.imageCulte = imageCulte;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
