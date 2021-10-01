jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ImageCulteService } from '../service/image-culte.service';
import { IImageCulte, ImageCulte } from '../image-culte.model';

import { ImageCulteUpdateComponent } from './image-culte-update.component';

describe('Component Tests', () => {
  describe('ImageCulte Management Update Component', () => {
    let comp: ImageCulteUpdateComponent;
    let fixture: ComponentFixture<ImageCulteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let imageCulteService: ImageCulteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ImageCulteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ImageCulteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImageCulteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      imageCulteService = TestBed.inject(ImageCulteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const imageCulte: IImageCulte = { id: 456 };

        activatedRoute.data = of({ imageCulte });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(imageCulte));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ImageCulte>>();
        const imageCulte = { id: 123 };
        jest.spyOn(imageCulteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageCulte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: imageCulte }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(imageCulteService.update).toHaveBeenCalledWith(imageCulte);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ImageCulte>>();
        const imageCulte = new ImageCulte();
        jest.spyOn(imageCulteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageCulte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: imageCulte }));
        saveSubject.complete();

        // THEN
        expect(imageCulteService.create).toHaveBeenCalledWith(imageCulte);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ImageCulte>>();
        const imageCulte = { id: 123 };
        jest.spyOn(imageCulteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageCulte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(imageCulteService.update).toHaveBeenCalledWith(imageCulte);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
