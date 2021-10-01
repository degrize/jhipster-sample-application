jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CulteService } from '../service/culte.service';
import { ICulte, Culte } from '../culte.model';
import { IImageCulte } from 'app/entities/image-culte/image-culte.model';
import { ImageCulteService } from 'app/entities/image-culte/service/image-culte.service';

import { CulteUpdateComponent } from './culte-update.component';

describe('Component Tests', () => {
  describe('Culte Management Update Component', () => {
    let comp: CulteUpdateComponent;
    let fixture: ComponentFixture<CulteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let culteService: CulteService;
    let imageCulteService: ImageCulteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CulteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CulteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CulteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      culteService = TestBed.inject(CulteService);
      imageCulteService = TestBed.inject(ImageCulteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ImageCulte query and add missing value', () => {
        const culte: ICulte = { id: 456 };
        const imageCultes: IImageCulte[] = [{ id: 34761 }];
        culte.imageCultes = imageCultes;

        const imageCulteCollection: IImageCulte[] = [{ id: 72771 }];
        jest.spyOn(imageCulteService, 'query').mockReturnValue(of(new HttpResponse({ body: imageCulteCollection })));
        const additionalImageCultes = [...imageCultes];
        const expectedCollection: IImageCulte[] = [...additionalImageCultes, ...imageCulteCollection];
        jest.spyOn(imageCulteService, 'addImageCulteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ culte });
        comp.ngOnInit();

        expect(imageCulteService.query).toHaveBeenCalled();
        expect(imageCulteService.addImageCulteToCollectionIfMissing).toHaveBeenCalledWith(imageCulteCollection, ...additionalImageCultes);
        expect(comp.imageCultesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const culte: ICulte = { id: 456 };
        const imageCultes: IImageCulte = { id: 45480 };
        culte.imageCultes = [imageCultes];

        activatedRoute.data = of({ culte });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(culte));
        expect(comp.imageCultesSharedCollection).toContain(imageCultes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Culte>>();
        const culte = { id: 123 };
        jest.spyOn(culteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ culte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: culte }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(culteService.update).toHaveBeenCalledWith(culte);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Culte>>();
        const culte = new Culte();
        jest.spyOn(culteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ culte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: culte }));
        saveSubject.complete();

        // THEN
        expect(culteService.create).toHaveBeenCalledWith(culte);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Culte>>();
        const culte = { id: 123 };
        jest.spyOn(culteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ culte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(culteService.update).toHaveBeenCalledWith(culte);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackImageCulteById', () => {
        it('Should return tracked ImageCulte primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackImageCulteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedImageCulte', () => {
        it('Should return option if no ImageCulte is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedImageCulte(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected ImageCulte for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedImageCulte(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this ImageCulte is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedImageCulte(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
