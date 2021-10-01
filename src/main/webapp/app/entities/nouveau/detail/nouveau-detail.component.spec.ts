import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NouveauDetailComponent } from './nouveau-detail.component';

describe('Component Tests', () => {
  describe('Nouveau Management Detail Component', () => {
    let comp: NouveauDetailComponent;
    let fixture: ComponentFixture<NouveauDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NouveauDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ nouveau: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NouveauDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NouveauDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load nouveau on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nouveau).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
