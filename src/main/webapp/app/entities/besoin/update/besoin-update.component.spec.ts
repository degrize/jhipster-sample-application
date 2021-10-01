jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BesoinService } from '../service/besoin.service';
import { IBesoin, Besoin } from '../besoin.model';

import { BesoinUpdateComponent } from './besoin-update.component';

describe('Component Tests', () => {
  describe('Besoin Management Update Component', () => {
    let comp: BesoinUpdateComponent;
    let fixture: ComponentFixture<BesoinUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let besoinService: BesoinService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BesoinUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BesoinUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BesoinUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      besoinService = TestBed.inject(BesoinService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const besoin: IBesoin = { id: 456 };

        activatedRoute.data = of({ besoin });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(besoin));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Besoin>>();
        const besoin = { id: 123 };
        jest.spyOn(besoinService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ besoin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: besoin }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(besoinService.update).toHaveBeenCalledWith(besoin);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Besoin>>();
        const besoin = new Besoin();
        jest.spyOn(besoinService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ besoin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: besoin }));
        saveSubject.complete();

        // THEN
        expect(besoinService.create).toHaveBeenCalledWith(besoin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Besoin>>();
        const besoin = { id: 123 };
        jest.spyOn(besoinService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ besoin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(besoinService.update).toHaveBeenCalledWith(besoin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
