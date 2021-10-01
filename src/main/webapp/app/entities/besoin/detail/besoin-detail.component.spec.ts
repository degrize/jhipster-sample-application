import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BesoinDetailComponent } from './besoin-detail.component';

describe('Component Tests', () => {
  describe('Besoin Management Detail Component', () => {
    let comp: BesoinDetailComponent;
    let fixture: ComponentFixture<BesoinDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BesoinDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ besoin: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BesoinDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BesoinDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load besoin on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.besoin).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
