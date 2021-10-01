jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuartierService } from '../service/quartier.service';
import { IQuartier, Quartier } from '../quartier.model';
import { IVille } from 'app/entities/ville/ville.model';
import { VilleService } from 'app/entities/ville/service/ville.service';

import { QuartierUpdateComponent } from './quartier-update.component';

describe('Component Tests', () => {
  describe('Quartier Management Update Component', () => {
    let comp: QuartierUpdateComponent;
    let fixture: ComponentFixture<QuartierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let quartierService: QuartierService;
    let villeService: VilleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuartierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuartierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuartierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      quartierService = TestBed.inject(QuartierService);
      villeService = TestBed.inject(VilleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Ville query and add missing value', () => {
        const quartier: IQuartier = { id: 456 };
        const villes: IVille[] = [{ id: 52697 }];
        quartier.villes = villes;

        const villeCollection: IVille[] = [{ id: 71410 }];
        jest.spyOn(villeService, 'query').mockReturnValue(of(new HttpResponse({ body: villeCollection })));
        const additionalVilles = [...villes];
        const expectedCollection: IVille[] = [...additionalVilles, ...villeCollection];
        jest.spyOn(villeService, 'addVilleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(villeService.query).toHaveBeenCalled();
        expect(villeService.addVilleToCollectionIfMissing).toHaveBeenCalledWith(villeCollection, ...additionalVilles);
        expect(comp.villesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const quartier: IQuartier = { id: 456 };
        const villes: IVille = { id: 87918 };
        quartier.villes = [villes];

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(quartier));
        expect(comp.villesSharedCollection).toContain(villes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Quartier>>();
        const quartier = { id: 123 };
        jest.spyOn(quartierService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quartier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(quartierService.update).toHaveBeenCalledWith(quartier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Quartier>>();
        const quartier = new Quartier();
        jest.spyOn(quartierService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quartier }));
        saveSubject.complete();

        // THEN
        expect(quartierService.create).toHaveBeenCalledWith(quartier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Quartier>>();
        const quartier = { id: 123 };
        jest.spyOn(quartierService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(quartierService.update).toHaveBeenCalledWith(quartier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVilleById', () => {
        it('Should return tracked Ville primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVilleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedVille', () => {
        it('Should return option if no Ville is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedVille(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Ville for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedVille(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Ville is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedVille(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
