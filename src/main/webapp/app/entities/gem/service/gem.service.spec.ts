import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGem, Gem } from '../gem.model';

import { GemService } from './gem.service';

describe('Service Tests', () => {
  describe('Gem Service', () => {
    let service: GemService;
    let httpMock: HttpTestingController;
    let elemDefault: IGem;
    let expectedResult: IGem | IGem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GemService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        annee: 'AAAAAAA',
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

      it('should create a Gem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Gem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Gem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            annee: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Gem', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
            annee: 'BBBBBB',
          },
          new Gem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Gem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            annee: 'BBBBBB',
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

      it('should delete a Gem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGemToCollectionIfMissing', () => {
        it('should add a Gem to an empty array', () => {
          const gem: IGem = { id: 123 };
          expectedResult = service.addGemToCollectionIfMissing([], gem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(gem);
        });

        it('should not add a Gem to an array that contains it', () => {
          const gem: IGem = { id: 123 };
          const gemCollection: IGem[] = [
            {
              ...gem,
            },
            { id: 456 },
          ];
          expectedResult = service.addGemToCollectionIfMissing(gemCollection, gem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Gem to an array that doesn't contain it", () => {
          const gem: IGem = { id: 123 };
          const gemCollection: IGem[] = [{ id: 456 }];
          expectedResult = service.addGemToCollectionIfMissing(gemCollection, gem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(gem);
        });

        it('should add only unique Gem to an array', () => {
          const gemArray: IGem[] = [{ id: 123 }, { id: 456 }, { id: 86505 }];
          const gemCollection: IGem[] = [{ id: 123 }];
          expectedResult = service.addGemToCollectionIfMissing(gemCollection, ...gemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const gem: IGem = { id: 123 };
          const gem2: IGem = { id: 456 };
          expectedResult = service.addGemToCollectionIfMissing([], gem, gem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(gem);
          expect(expectedResult).toContain(gem2);
        });

        it('should accept null and undefined values', () => {
          const gem: IGem = { id: 123 };
          expectedResult = service.addGemToCollectionIfMissing([], null, gem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(gem);
        });

        it('should return initial array if no Gem is added', () => {
          const gemCollection: IGem[] = [{ id: 123 }];
          expectedResult = service.addGemToCollectionIfMissing(gemCollection, undefined, null);
          expect(expectedResult).toEqual(gemCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
