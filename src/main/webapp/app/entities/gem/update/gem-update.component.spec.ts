jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GemService } from '../service/gem.service';
import { IGem, Gem } from '../gem.model';
import { IGuard } from 'app/entities/guard/guard.model';
import { GuardService } from 'app/entities/guard/service/guard.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';
import { FrereQuiInviteService } from 'app/entities/frere-qui-invite/service/frere-qui-invite.service';

import { GemUpdateComponent } from './gem-update.component';

describe('Component Tests', () => {
  describe('Gem Management Update Component', () => {
    let comp: GemUpdateComponent;
    let fixture: ComponentFixture<GemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let gemService: GemService;
    let guardService: GuardService;
    let departementService: DepartementService;
    let frereQuiInviteService: FrereQuiInviteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      gemService = TestBed.inject(GemService);
      guardService = TestBed.inject(GuardService);
      departementService = TestBed.inject(DepartementService);
      frereQuiInviteService = TestBed.inject(FrereQuiInviteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Guard query and add missing value', () => {
        const gem: IGem = { id: 456 };
        const guard: IGuard = { id: 29511 };
        gem.guard = guard;

        const guardCollection: IGuard[] = [{ id: 61829 }];
        jest.spyOn(guardService, 'query').mockReturnValue(of(new HttpResponse({ body: guardCollection })));
        const additionalGuards = [guard];
        const expectedCollection: IGuard[] = [...additionalGuards, ...guardCollection];
        jest.spyOn(guardService, 'addGuardToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ gem });
        comp.ngOnInit();

        expect(guardService.query).toHaveBeenCalled();
        expect(guardService.addGuardToCollectionIfMissing).toHaveBeenCalledWith(guardCollection, ...additionalGuards);
        expect(comp.guardsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Departement query and add missing value', () => {
        const gem: IGem = { id: 456 };
        const departement: IDepartement = { id: 9452 };
        gem.departement = departement;

        const departementCollection: IDepartement[] = [{ id: 66301 }];
        jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
        const additionalDepartements = [departement];
        const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
        jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ gem });
        comp.ngOnInit();

        expect(departementService.query).toHaveBeenCalled();
        expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
          departementCollection,
          ...additionalDepartements
        );
        expect(comp.departementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call FrereQuiInvite query and add missing value', () => {
        const gem: IGem = { id: 456 };
        const frereQuiInvites: IFrereQuiInvite[] = [{ id: 13504 }];
        gem.frereQuiInvites = frereQuiInvites;

        const frereQuiInviteCollection: IFrereQuiInvite[] = [{ id: 78990 }];
        jest.spyOn(frereQuiInviteService, 'query').mockReturnValue(of(new HttpResponse({ body: frereQuiInviteCollection })));
        const additionalFrereQuiInvites = [...frereQuiInvites];
        const expectedCollection: IFrereQuiInvite[] = [...additionalFrereQuiInvites, ...frereQuiInviteCollection];
        jest.spyOn(frereQuiInviteService, 'addFrereQuiInviteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ gem });
        comp.ngOnInit();

        expect(frereQuiInviteService.query).toHaveBeenCalled();
        expect(frereQuiInviteService.addFrereQuiInviteToCollectionIfMissing).toHaveBeenCalledWith(
          frereQuiInviteCollection,
          ...additionalFrereQuiInvites
        );
        expect(comp.frereQuiInvitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const gem: IGem = { id: 456 };
        const guard: IGuard = { id: 789 };
        gem.guard = guard;
        const departement: IDepartement = { id: 97894 };
        gem.departement = departement;
        const frereQuiInvites: IFrereQuiInvite = { id: 41841 };
        gem.frereQuiInvites = [frereQuiInvites];

        activatedRoute.data = of({ gem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(gem));
        expect(comp.guardsSharedCollection).toContain(guard);
        expect(comp.departementsSharedCollection).toContain(departement);
        expect(comp.frereQuiInvitesSharedCollection).toContain(frereQuiInvites);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Gem>>();
        const gem = { id: 123 };
        jest.spyOn(gemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ gem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: gem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(gemService.update).toHaveBeenCalledWith(gem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Gem>>();
        const gem = new Gem();
        jest.spyOn(gemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ gem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: gem }));
        saveSubject.complete();

        // THEN
        expect(gemService.create).toHaveBeenCalledWith(gem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Gem>>();
        const gem = { id: 123 };
        jest.spyOn(gemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ gem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(gemService.update).toHaveBeenCalledWith(gem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackGuardById', () => {
        it('Should return tracked Guard primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGuardById(0, entity);
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

      describe('trackFrereQuiInviteById', () => {
        it('Should return tracked FrereQuiInvite primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFrereQuiInviteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedFrereQuiInvite', () => {
        it('Should return option if no FrereQuiInvite is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedFrereQuiInvite(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected FrereQuiInvite for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedFrereQuiInvite(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this FrereQuiInvite is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedFrereQuiInvite(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
