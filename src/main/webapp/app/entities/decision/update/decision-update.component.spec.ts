jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DecisionService } from '../service/decision.service';
import { IDecision, Decision } from '../decision.model';

import { DecisionUpdateComponent } from './decision-update.component';

describe('Component Tests', () => {
  describe('Decision Management Update Component', () => {
    let comp: DecisionUpdateComponent;
    let fixture: ComponentFixture<DecisionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let decisionService: DecisionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DecisionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DecisionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DecisionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      decisionService = TestBed.inject(DecisionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const decision: IDecision = { id: 456 };

        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(decision));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Decision>>();
        const decision = { id: 123 };
        jest.spyOn(decisionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: decision }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(decisionService.update).toHaveBeenCalledWith(decision);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Decision>>();
        const decision = new Decision();
        jest.spyOn(decisionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: decision }));
        saveSubject.complete();

        // THEN
        expect(decisionService.create).toHaveBeenCalledWith(decision);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Decision>>();
        const decision = { id: 123 };
        jest.spyOn(decisionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(decisionService.update).toHaveBeenCalledWith(decision);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
