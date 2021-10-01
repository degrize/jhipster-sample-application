import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImageCulte, ImageCulte } from '../image-culte.model';

import { ImageCulteService } from './image-culte.service';

describe('Service Tests', () => {
  describe('ImageCulte Service', () => {
    let service: ImageCulteService;
    let httpMock: HttpTestingController;
    let elemDefault: IImageCulte;
    let expectedResult: IImageCulte | IImageCulte[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ImageCulteService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        titre: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
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

      it('should create a ImageCulte', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ImageCulte()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ImageCulte', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            titre: 'BBBBBB',
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ImageCulte', () => {
        const patchObject = Object.assign(
          {
            titre: 'BBBBBB',
          },
          new ImageCulte()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ImageCulte', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            titre: 'BBBBBB',
            image: 'BBBBBB',
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

      it('should delete a ImageCulte', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addImageCulteToCollectionIfMissing', () => {
        it('should add a ImageCulte to an empty array', () => {
          const imageCulte: IImageCulte = { id: 123 };
          expectedResult = service.addImageCulteToCollectionIfMissing([], imageCulte);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(imageCulte);
        });

        it('should not add a ImageCulte to an array that contains it', () => {
          const imageCulte: IImageCulte = { id: 123 };
          const imageCulteCollection: IImageCulte[] = [
            {
              ...imageCulte,
            },
            { id: 456 },
          ];
          expectedResult = service.addImageCulteToCollectionIfMissing(imageCulteCollection, imageCulte);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ImageCulte to an array that doesn't contain it", () => {
          const imageCulte: IImageCulte = { id: 123 };
          const imageCulteCollection: IImageCulte[] = [{ id: 456 }];
          expectedResult = service.addImageCulteToCollectionIfMissing(imageCulteCollection, imageCulte);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(imageCulte);
        });

        it('should add only unique ImageCulte to an array', () => {
          const imageCulteArray: IImageCulte[] = [{ id: 123 }, { id: 456 }, { id: 52574 }];
          const imageCulteCollection: IImageCulte[] = [{ id: 123 }];
          expectedResult = service.addImageCulteToCollectionIfMissing(imageCulteCollection, ...imageCulteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const imageCulte: IImageCulte = { id: 123 };
          const imageCulte2: IImageCulte = { id: 456 };
          expectedResult = service.addImageCulteToCollectionIfMissing([], imageCulte, imageCulte2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(imageCulte);
          expect(expectedResult).toContain(imageCulte2);
        });

        it('should accept null and undefined values', () => {
          const imageCulte: IImageCulte = { id: 123 };
          expectedResult = service.addImageCulteToCollectionIfMissing([], null, imageCulte, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(imageCulte);
        });

        it('should return initial array if no ImageCulte is added', () => {
          const imageCulteCollection: IImageCulte[] = [{ id: 123 }];
          expectedResult = service.addImageCulteToCollectionIfMissing(imageCulteCollection, undefined, null);
          expect(expectedResult).toEqual(imageCulteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
