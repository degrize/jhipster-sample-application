jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommunauteService } from '../service/communaute.service';
import { ICommunaute, Communaute } from '../communaute.model';

import { CommunauteUpdateComponent } from './communaute-update.component';

describe('Component Tests', () => {
  describe('Communaute Management Update Component', () => {
    let comp: CommunauteUpdateComponent;
    let fixture: ComponentFixture<CommunauteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let communauteService: CommunauteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommunauteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommunauteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommunauteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      communauteService = TestBed.inject(CommunauteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const communaute: ICommunaute = { id: 456 };

        activatedRoute.data = of({ communaute });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(communaute));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Communaute>>();
        const communaute = { id: 123 };
        jest.spyOn(communauteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ communaute });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communaute }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(communauteService.update).toHaveBeenCalledWith(communaute);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Communaute>>();
        const communaute = new Communaute();
        jest.spyOn(communauteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ communaute });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communaute }));
        saveSubject.complete();

        // THEN
        expect(communauteService.create).toHaveBeenCalledWith(communaute);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Communaute>>();
        const communaute = { id: 123 };
        jest.spyOn(communauteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ communaute });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(communauteService.update).toHaveBeenCalledWith(communaute);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
