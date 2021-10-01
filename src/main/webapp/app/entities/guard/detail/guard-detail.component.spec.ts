import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GuardDetailComponent } from './guard-detail.component';

describe('Component Tests', () => {
  describe('Guard Management Detail Component', () => {
    let comp: GuardDetailComponent;
    let fixture: ComponentFixture<GuardDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GuardDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ guard: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GuardDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GuardDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load guard on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.guard).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
