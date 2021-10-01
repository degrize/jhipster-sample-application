jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FrereQuiInviteService } from '../service/frere-qui-invite.service';
import { IFrereQuiInvite, FrereQuiInvite } from '../frere-qui-invite.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';

import { FrereQuiInviteUpdateComponent } from './frere-qui-invite-update.component';

describe('Component Tests', () => {
  describe('FrereQuiInvite Management Update Component', () => {
    let comp: FrereQuiInviteUpdateComponent;
    let fixture: ComponentFixture<FrereQuiInviteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let frereQuiInviteService: FrereQuiInviteService;
    let quartierService: QuartierService;
    let departementService: DepartementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FrereQuiInviteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FrereQuiInviteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FrereQuiInviteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      frereQuiInviteService = TestBed.inject(FrereQuiInviteService);
      quartierService = TestBed.inject(QuartierService);
      departementService = TestBed.inject(DepartementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Quartier query and add missing value', () => {
        const frereQuiInvite: IFrereQuiInvite = { id: 456 };
        const quartier: IQuartier = { id: 81625 };
        frereQuiInvite.quartier = quartier;

        const quartierCollection: IQuartier[] = [{ id: 34447 }];
        jest.spyOn(quartierService, 'query').mockReturnValue(of(new HttpResponse({ body: quartierCollection })));
        const additionalQuartiers = [quartier];
        const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
        jest.spyOn(quartierService, 'addQuartierToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ frereQuiInvite });
        comp.ngOnInit();

        expect(quartierService.query).toHaveBeenCalled();
        expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
        expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Departement query and add missing value', () => {
        const frereQuiInvite: IFrereQuiInvite = { id: 456 };
        const departements: IDepartement[] = [{ id: 67769 }];
        frereQuiInvite.departements = departements;

        const departementCollection: IDepartement[] = [{ id: 52074 }];
        jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
        const additionalDepartements = [...departements];
        const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
        jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ frereQuiInvite });
        comp.ngOnInit();

        expect(departementService.query).toHaveBeenCalled();
        expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
          departementCollection,
          ...additionalDepartements
        );
        expect(comp.departementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const frereQuiInvite: IFrereQuiInvite = { id: 456 };
        const quartier: IQuartier = { id: 89434 };
        frereQuiInvite.quartier = quartier;
        const departements: IDepartement = { id: 7 };
        frereQuiInvite.departements = [departements];

        activatedRoute.data = of({ frereQuiInvite });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(frereQuiInvite));
        expect(comp.quartiersSharedCollection).toContain(quartier);
        expect(comp.departementsSharedCollection).toContain(departements);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FrereQuiInvite>>();
        const frereQuiInvite = { id: 123 };
        jest.spyOn(frereQuiInviteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ frereQuiInvite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: frereQuiInvite }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(frereQuiInviteService.update).toHaveBeenCalledWith(frereQuiInvite);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FrereQuiInvite>>();
        const frereQuiInvite = new FrereQuiInvite();
        jest.spyOn(frereQuiInviteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ frereQuiInvite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: frereQuiInvite }));
        saveSubject.complete();

        // THEN
        expect(frereQuiInviteService.create).toHaveBeenCalledWith(frereQuiInvite);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FrereQuiInvite>>();
        const frereQuiInvite = { id: 123 };
        jest.spyOn(frereQuiInviteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ frereQuiInvite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(frereQuiInviteService.update).toHaveBeenCalledWith(frereQuiInvite);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackQuartierById', () => {
        it('Should return tracked Quartier primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuartierById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDepartementById', () => {
        it('Should return tracked Departement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDepartementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedDepartement', () => {
        it('Should return option if no Departement is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedDepartement(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Departement for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedDepartement(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Departement is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedDepartement(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
