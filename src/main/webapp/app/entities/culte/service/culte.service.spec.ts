import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICulte, Culte } from '../culte.model';

import { CulteService } from './culte.service';

describe('Service Tests', () => {
  describe('Culte Service', () => {
    let service: CulteService;
    let httpMock: HttpTestingController;
    let elemDefault: ICulte;
    let expectedResult: ICulte | ICulte[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CulteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        theme: 'AAAAAAA',
        date: currentDate,
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

      it('should create a Culte', () => {
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

        service.create(new Culte()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Culte', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            theme: 'BBBBBB',
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Culte', () => {
        const patchObject = Object.assign(
          {
            theme: 'BBBBBB',
          },
          new Culte()
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

      it('should return a list of Culte', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            theme: 'BBBBBB',
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Culte', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCulteToCollectionIfMissing', () => {
        it('should add a Culte to an empty array', () => {
          const culte: ICulte = { id: 123 };
          expectedResult = service.addCulteToCollectionIfMissing([], culte);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(culte);
        });

        it('should not add a Culte to an array that contains it', () => {
          const culte: ICulte = { id: 123 };
          const culteCollection: ICulte[] = [
            {
              ...culte,
            },
            { id: 456 },
          ];
          expectedResult = service.addCulteToCollectionIfMissing(culteCollection, culte);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Culte to an array that doesn't contain it", () => {
          const culte: ICulte = { id: 123 };
          const culteCollection: ICulte[] = [{ id: 456 }];
          expectedResult = service.addCulteToCollectionIfMissing(culteCollection, culte);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(culte);
        });

        it('should add only unique Culte to an array', () => {
          const culteArray: ICulte[] = [{ id: 123 }, { id: 456 }, { id: 27178 }];
          const culteCollection: ICulte[] = [{ id: 123 }];
          expectedResult = service.addCulteToCollectionIfMissing(culteCollection, ...culteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const culte: ICulte = { id: 123 };
          const culte2: ICulte = { id: 456 };
          expectedResult = service.addCulteToCollectionIfMissing([], culte, culte2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(culte);
          expect(expectedResult).toContain(culte2);
        });

        it('should accept null and undefined values', () => {
          const culte: ICulte = { id: 123 };
          expectedResult = service.addCulteToCollectionIfMissing([], null, culte, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(culte);
        });

        it('should return initial array if no Culte is added', () => {
          const culteCollection: ICulte[] = [{ id: 123 }];
          expectedResult = service.addCulteToCollectionIfMissing(culteCollection, undefined, null);
          expect(expectedResult).toEqual(culteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
