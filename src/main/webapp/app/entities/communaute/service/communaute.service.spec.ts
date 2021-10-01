import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommunaute, Communaute } from '../communaute.model';

import { CommunauteService } from './communaute.service';

describe('Service Tests', () => {
  describe('Communaute Service', () => {
    let service: CommunauteService;
    let httpMock: HttpTestingController;
    let elemDefault: ICommunaute;
    let expectedResult: ICommunaute | ICommunaute[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CommunauteService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
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

      it('should create a Communaute', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Communaute()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Communaute', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Communaute', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
          },
          new Communaute()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Communaute', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
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

      it('should delete a Communaute', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCommunauteToCollectionIfMissing', () => {
        it('should add a Communaute to an empty array', () => {
          const communaute: ICommunaute = { id: 123 };
          expectedResult = service.addCommunauteToCollectionIfMissing([], communaute);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communaute);
        });

        it('should not add a Communaute to an array that contains it', () => {
          const communaute: ICommunaute = { id: 123 };
          const communauteCollection: ICommunaute[] = [
            {
              ...communaute,
            },
            { id: 456 },
          ];
          expectedResult = service.addCommunauteToCollectionIfMissing(communauteCollection, communaute);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Communaute to an array that doesn't contain it", () => {
          const communaute: ICommunaute = { id: 123 };
          const communauteCollection: ICommunaute[] = [{ id: 456 }];
          expectedResult = service.addCommunauteToCollectionIfMissing(communauteCollection, communaute);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communaute);
        });

        it('should add only unique Communaute to an array', () => {
          const communauteArray: ICommunaute[] = [{ id: 123 }, { id: 456 }, { id: 39232 }];
          const communauteCollection: ICommunaute[] = [{ id: 123 }];
          expectedResult = service.addCommunauteToCollectionIfMissing(communauteCollection, ...communauteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const communaute: ICommunaute = { id: 123 };
          const communaute2: ICommunaute = { id: 456 };
          expectedResult = service.addCommunauteToCollectionIfMissing([], communaute, communaute2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communaute);
          expect(expectedResult).toContain(communaute2);
        });

        it('should accept null and undefined values', () => {
          const communaute: ICommunaute = { id: 123 };
          expectedResult = service.addCommunauteToCollectionIfMissing([], null, communaute, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communaute);
        });

        it('should return initial array if no Communaute is added', () => {
          const communauteCollection: ICommunaute[] = [{ id: 123 }];
          expectedResult = service.addCommunauteToCollectionIfMissing(communauteCollection, undefined, null);
          expect(expectedResult).toEqual(communauteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
