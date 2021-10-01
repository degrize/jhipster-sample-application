import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { SituationMatrimoniale } from 'app/entities/enumerations/situation-matrimoniale.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { CanalInvitation } from 'app/entities/enumerations/canal-invitation.model';
import { INouveau, Nouveau } from '../nouveau.model';

import { NouveauService } from './nouveau.service';

describe('Service Tests', () => {
  describe('Nouveau Service', () => {
    let service: NouveauService;
    let httpMock: HttpTestingController;
    let elemDefault: INouveau;
    let expectedResult: INouveau | INouveau[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NouveauService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nomComplet: 'AAAAAAA',
        contact: 'AAAAAAA',
        trancheAge: 'AAAAAAA',
        situationMatrimoniale: SituationMatrimoniale.SEUL,
        date: currentDate,
        impressionsDuCulte: 'AAAAAAA',
        sexe: Sexe.F,
        invitePar: CanalInvitation.RADIO,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Nouveau', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Nouveau()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Nouveau', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomComplet: 'BBBBBB',
            contact: 'BBBBBB',
            trancheAge: 'BBBBBB',
            situationMatrimoniale: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            impressionsDuCulte: 'BBBBBB',
            sexe: 'BBBBBB',
            invitePar: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Nouveau', () => {
        const patchObject = Object.assign(
          {
            nomComplet: 'BBBBBB',
            trancheAge: 'BBBBBB',
            impressionsDuCulte: 'BBBBBB',
            sexe: 'BBBBBB',
          },
          new Nouveau()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Nouveau', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomComplet: 'BBBBBB',
            contact: 'BBBBBB',
            trancheAge: 'BBBBBB',
            situationMatrimoniale: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            impressionsDuCulte: 'BBBBBB',
            sexe: 'BBBBBB',
            invitePar: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Nouveau', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNouveauToCollectionIfMissing', () => {
        it('should add a Nouveau to an empty array', () => {
          const nouveau: INouveau = { id: 123 };
          expectedResult = service.addNouveauToCollectionIfMissing([], nouveau);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nouveau);
        });

        it('should not add a Nouveau to an array that contains it', () => {
          const nouveau: INouveau = { id: 123 };
          const nouveauCollection: INouveau[] = [
            {
              ...nouveau,
            },
            { id: 456 },
          ];
          expectedResult = service.addNouveauToCollectionIfMissing(nouveauCollection, nouveau);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Nouveau to an array that doesn't contain it", () => {
          const nouveau: INouveau = { id: 123 };
          const nouveauCollection: INouveau[] = [{ id: 456 }];
          expectedResult = service.addNouveauToCollectionIfMissing(nouveauCollection, nouveau);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nouveau);
        });

        it('should add only unique Nouveau to an array', () => {
          const nouveauArray: INouveau[] = [{ id: 123 }, { id: 456 }, { id: 98738 }];
          const nouveauCollection: INouveau[] = [{ id: 123 }];
          expectedResult = service.addNouveauToCollectionIfMissing(nouveauCollection, ...nouveauArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const nouveau: INouveau = { id: 123 };
          const nouveau2: INouveau = { id: 456 };
          expectedResult = service.addNouveauToCollectionIfMissing([], nouveau, nouveau2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nouveau);
          expect(expectedResult).toContain(nouveau2);
        });

        it('should accept null and undefined values', () => {
          const nouveau: INouveau = { id: 123 };
          expectedResult = service.addNouveauToCollectionIfMissing([], null, nouveau, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nouveau);
        });

        it('should return initial array if no Nouveau is added', () => {
          const nouveauCollection: INouveau[] = [{ id: 123 }];
          expectedResult = service.addNouveauToCollectionIfMissing(nouveauCollection, undefined, null);
          expect(expectedResult).toEqual(nouveauCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
