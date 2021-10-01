jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GuardService } from '../service/guard.service';
import { IGuard, Guard } from '../guard.model';
import { IGem } from 'app/entities/gem/gem.model';
import { GemService } from 'app/entities/gem/service/gem.service';

import { GuardUpdateComponent } from './guard-update.component';

describe('Component Tests', () => {
  describe('Guard Management Update Component', () => {
    let comp: GuardUpdateComponent;
    let fixture: ComponentFixture<GuardUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let guardService: GuardService;
    let gemService: GemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GuardUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GuardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GuardUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      guardService = TestBed.inject(GuardService);
      gemService = TestBed.inject(GemService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Gem query and add missing value', () => {
        const guard: IGuard = { id: 456 };
        const guard: IGem = { id: 76285 };
        guard.guard = guard;

        const gemCollection: IGem[] = [{ id: 90270 }];
        jest.spyOn(gemService, 'query').mockReturnValue(of(new HttpResponse({ body: gemCollection })));
        const additionalGems = [guard];
        const expectedCollection: IGem[] = [...additionalGems, ...gemCollection];
        jest.spyOn(gemService, 'addGemToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ guard });
        comp.ngOnInit();

        expect(gemService.query).toHaveBeenCalled();
        expect(gemService.addGemToCollectionIfMissing).toHaveBeenCalledWith(gemCollection, ...additionalGems);
        expect(comp.gemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const guard: IGuard = { id: 456 };
        const guard: IGem = { id: 22336 };
        guard.guard = guard;

        activatedRoute.data = of({ guard });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(guard));
        expect(comp.gemsSharedCollection).toContain(guard);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Guard>>();
        const guard = { id: 123 };
        jest.spyOn(guardService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ guard });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: guard }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(guardService.update).toHaveBeenCalledWith(guard);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Guard>>();
        const guard = new Guard();
        jest.spyOn(guardService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ guard });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: guard }));
        saveSubject.complete();

        // THEN
        expect(guardService.create).toHaveBeenCalledWith(guard);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Guard>>();
        const guard = { id: 123 };
        jest.spyOn(guardService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ guard });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(guardService.update).toHaveBeenCalledWith(guard);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackGemById', () => {
        it('Should return tracked Gem primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGemById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
