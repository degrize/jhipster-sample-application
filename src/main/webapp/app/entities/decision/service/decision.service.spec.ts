import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DecisionType } from 'app/entities/enumerations/decision-type.model';
import { IDecision, Decision } from '../decision.model';

import { DecisionService } from './decision.service';

describe('Service Tests', () => {
  describe('Decision Service', () => {
    let service: DecisionService;
    let httpMock: HttpTestingController;
    let elemDefault: IDecision;
    let expectedResult: IDecision | IDecision[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DecisionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        decision: DecisionType.JE_DECIDE_D_ETRE_MEMBRE,
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

      it('should create a Decision', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Decision()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Decision', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            decision: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Decision', () => {
        const patchObject = Object.assign({}, new Decision());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Decision', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            decision: 'BBBBBB',
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

      it('should delete a Decision', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDecisionToCollectionIfMissing', () => {
        it('should add a Decision to an empty array', () => {
          const decision: IDecision = { id: 123 };
          expectedResult = service.addDecisionToCollectionIfMissing([], decision);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(decision);
        });

        it('should not add a Decision to an array that contains it', () => {
          const decision: IDecision = { id: 123 };
          const decisionCollection: IDecision[] = [
            {
              ...decision,
            },
            { id: 456 },
          ];
          expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, decision);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Decision to an array that doesn't contain it", () => {
          const decision: IDecision = { id: 123 };
          const decisionCollection: IDecision[] = [{ id: 456 }];
          expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, decision);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(decision);
        });

        it('should add only unique Decision to an array', () => {
          const decisionArray: IDecision[] = [{ id: 123 }, { id: 456 }, { id: 10023 }];
          const decisionCollection: IDecision[] = [{ id: 123 }];
          expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, ...decisionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const decision: IDecision = { id: 123 };
          const decision2: IDecision = { id: 456 };
          expectedResult = service.addDecisionToCollectionIfMissing([], decision, decision2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(decision);
          expect(expectedResult).toContain(decision2);
        });

        it('should accept null and undefined values', () => {
          const decision: IDecision = { id: 123 };
          expectedResult = service.addDecisionToCollectionIfMissing([], null, decision, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(decision);
        });

        it('should return initial array if no Decision is added', () => {
          const decisionCollection: IDecision[] = [{ id: 123 }];
          expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, undefined, null);
          expect(expectedResult).toEqual(decisionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
