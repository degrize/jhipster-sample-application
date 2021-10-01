jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FrereQuiInviteService } from '../service/frere-qui-invite.service';

import { FrereQuiInviteDeleteDialogComponent } from './frere-qui-invite-delete-dialog.component';

describe('Component Tests', () => {
  describe('FrereQuiInvite Management Delete Component', () => {
    let comp: FrereQuiInviteDeleteDialogComponent;
    let fixture: ComponentFixture<FrereQuiInviteDeleteDialogComponent>;
    let service: FrereQuiInviteService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FrereQuiInviteDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(FrereQuiInviteDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FrereQuiInviteDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FrereQuiInviteService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
