import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GemDetailComponent } from './gem-detail.component';

describe('Component Tests', () => {
  describe('Gem Management Detail Component', () => {
    let comp: GemDetailComponent;
    let fixture: ComponentFixture<GemDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GemDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ gem: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load gem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gem).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
