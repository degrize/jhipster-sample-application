import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGuard, Guard } from '../guard.model';

import { GuardService } from './guard.service';

describe('Service Tests', () => {
  describe('Guard Service', () => {
    let service: GuardService;
    let httpMock: HttpTestingController;
    let elemDefault: IGuard;
    let expectedResult: IGuard | IGuard[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GuardService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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

      it('should create a Guard', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Guard()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Guard', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Guard', () => {
        const patchObject = Object.assign({}, new Guard());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Guard', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      it('should delete a Guard', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGuardToCollectionIfMissing', () => {
        it('should add a Guard to an empty array', () => {
          const guard: IGuard = { id: 123 };
          expectedResult = service.addGuardToCollectionIfMissing([], guard);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(guard);
        });

        it('should not add a Guard to an array that contains it', () => {
          const guard: IGuard = { id: 123 };
          const guardCollection: IGuard[] = [
            {
              ...guard,
            },
            { id: 456 },
          ];
          expectedResult = service.addGuardToCollectionIfMissing(guardCollection, guard);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Guard to an array that doesn't contain it", () => {
          const guard: IGuard = { id: 123 };
          const guardCollection: IGuard[] = [{ id: 456 }];
          expectedResult = service.addGuardToCollectionIfMissing(guardCollection, guard);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(guard);
        });

        it('should add only unique Guard to an array', () => {
          const guardArray: IGuard[] = [{ id: 123 }, { id: 456 }, { id: 34264 }];
          const guardCollection: IGuard[] = [{ id: 123 }];
          expectedResult = service.addGuardToCollectionIfMissing(guardCollection, ...guardArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const guard: IGuard = { id: 123 };
          const guard2: IGuard = { id: 456 };
          expectedResult = service.addGuardToCollectionIfMissing([], guard, guard2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(guard);
          expect(expectedResult).toContain(guard2);
        });

        it('should accept null and undefined values', () => {
          const guard: IGuard = { id: 123 };
          expectedResult = service.addGuardToCollectionIfMissing([], null, guard, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(guard);
        });

        it('should return initial array if no Guard is added', () => {
          const guardCollection: IGuard[] = [{ id: 123 }];
          expectedResult = service.addGuardToCollectionIfMissing(guardCollection, undefined, null);
          expect(expectedResult).toEqual(guardCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
