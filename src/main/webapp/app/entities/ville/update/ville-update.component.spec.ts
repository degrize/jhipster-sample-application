jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VilleService } from '../service/ville.service';
import { IVille, Ville } from '../ville.model';

import { VilleUpdateComponent } from './ville-update.component';

describe('Component Tests', () => {
  describe('Ville Management Update Component', () => {
    let comp: VilleUpdateComponent;
    let fixture: ComponentFixture<VilleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let villeService: VilleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VilleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VilleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VilleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      villeService = TestBed.inject(VilleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ville: IVille = { id: 456 };

        activatedRoute.data = of({ ville });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ville));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ville>>();
        const ville = { id: 123 };
        jest.spyOn(villeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ville });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ville }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(villeService.update).toHaveBeenCalledWith(ville);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ville>>();
        const ville = new Ville();
        jest.spyOn(villeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ville });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ville }));
        saveSubject.complete();

        // THEN
        expect(villeService.create).toHaveBeenCalledWith(ville);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ville>>();
        const ville = { id: 123 };
        jest.spyOn(villeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ville });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(villeService.update).toHaveBeenCalledWith(ville);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
