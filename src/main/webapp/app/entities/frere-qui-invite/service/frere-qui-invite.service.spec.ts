import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { IFrereQuiInvite, FrereQuiInvite } from '../frere-qui-invite.model';

import { FrereQuiInviteService } from './frere-qui-invite.service';

describe('Service Tests', () => {
  describe('FrereQuiInvite Service', () => {
    let service: FrereQuiInviteService;
    let httpMock: HttpTestingController;
    let elemDefault: IFrereQuiInvite;
    let expectedResult: IFrereQuiInvite | IFrereQuiInvite[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FrereQuiInviteService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomComplet: 'AAAAAAA',
        contact: 'AAAAAAA',
        sexe: Sexe.F,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FrereQuiInvite', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FrereQuiInvite()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FrereQuiInvite', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomComplet: 'BBBBBB',
            contact: 'BBBBBB',
            sexe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FrereQuiInvite', () => {
        const patchObject = Object.assign(
          {
            nomComplet: 'BBBBBB',
            sexe: 'BBBBBB',
          },
          new FrereQuiInvite()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FrereQuiInvite', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomComplet: 'BBBBBB',
            contact: 'BBBBBB',
            sexe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FrereQuiInvite', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFrereQuiInviteToCollectionIfMissing', () => {
        it('should add a FrereQuiInvite to an empty array', () => {
          const frereQuiInvite: IFrereQuiInvite = { id: 123 };
          expectedResult = service.addFrereQuiInviteToCollectionIfMissing([], frereQuiInvite);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(frereQuiInvite);
        });

        it('should not add a FrereQuiInvite to an array that contains it', () => {
          const frereQuiInvite: IFrereQuiInvite = { id: 123 };
          const frereQuiInviteCollection: IFrereQuiInvite[] = [
            {
              ...frereQuiInvite,
            },
            { id: 456 },
          ];
          expectedResult = service.addFrereQuiInviteToCollectionIfMissing(frereQuiInviteCollection, frereQuiInvite);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FrereQuiInvite to an array that doesn't contain it", () => {
          const frereQuiInvite: IFrereQuiInvite = { id: 123 };
          const frereQuiInviteCollection: IFrereQuiInvite[] = [{ id: 456 }];
          expectedResult = service.addFrereQuiInviteToCollectionIfMissing(frereQuiInviteCollection, frereQuiInvite);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(frereQuiInvite);
        });

        it('should add only unique FrereQuiInvite to an array', () => {
          const frereQuiInviteArray: IFrereQuiInvite[] = [{ id: 123 }, { id: 456 }, { id: 70238 }];
          const frereQuiInviteCollection: IFrereQuiInvite[] = [{ id: 123 }];
          expectedResult = service.addFrereQuiInviteToCollectionIfMissing(frereQuiInviteCollection, ...frereQuiInviteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const frereQuiInvite: IFrereQuiInvite = { id: 123 };
          const frereQuiInvite2: IFrereQuiInvite = { id: 456 };
          expectedResult = service.addFrereQuiInviteToCollectionIfMissing([], frereQuiInvite, frereQuiInvite2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(frereQuiInvite);
          expect(expectedResult).toContain(frereQuiInvite2);
        });

        it('should accept null and undefined values', () => {
          const frereQuiInvite: IFrereQuiInvite = { id: 123 };
          expectedResult = service.addFrereQuiInviteToCollectionIfMissing([], null, frereQuiInvite, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(frereQuiInvite);
        });

        it('should return initial array if no FrereQuiInvite is added', () => {
          const frereQuiInviteCollection: IFrereQuiInvite[] = [{ id: 123 }];
          expectedResult = service.addFrereQuiInviteToCollectionIfMissing(frereQuiInviteCollection, undefined, null);
          expect(expectedResult).toEqual(frereQuiInviteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
