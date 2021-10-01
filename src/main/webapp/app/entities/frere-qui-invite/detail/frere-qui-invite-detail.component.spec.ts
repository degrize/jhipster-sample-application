import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FrereQuiInviteDetailComponent } from './frere-qui-invite-detail.component';

describe('Component Tests', () => {
  describe('FrereQuiInvite Management Detail Component', () => {
    let comp: FrereQuiInviteDetailComponent;
    let fixture: ComponentFixture<FrereQuiInviteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FrereQuiInviteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ frereQuiInvite: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FrereQuiInviteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FrereQuiInviteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load frereQuiInvite on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.frereQuiInvite).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
