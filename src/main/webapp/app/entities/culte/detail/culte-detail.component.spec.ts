import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CulteDetailComponent } from './culte-detail.component';

describe('Component Tests', () => {
  describe('Culte Management Detail Component', () => {
    let comp: CulteDetailComponent;
    let fixture: ComponentFixture<CulteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CulteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ culte: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CulteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CulteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load culte on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.culte).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
