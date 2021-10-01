import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommunauteDetailComponent } from './communaute-detail.component';

describe('Component Tests', () => {
  describe('Communaute Management Detail Component', () => {
    let comp: CommunauteDetailComponent;
    let fixture: ComponentFixture<CommunauteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CommunauteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ communaute: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CommunauteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommunauteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load communaute on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.communaute).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
