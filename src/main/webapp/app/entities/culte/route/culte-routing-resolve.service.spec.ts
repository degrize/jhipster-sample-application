jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICulte, Culte } from '../culte.model';
import { CulteService } from '../service/culte.service';

import { CulteRoutingResolveService } from './culte-routing-resolve.service';

describe('Service Tests', () => {
  describe('Culte routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CulteRoutingResolveService;
    let service: CulteService;
    let resultCulte: ICulte | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CulteRoutingResolveService);
      service = TestBed.inject(CulteService);
      resultCulte = undefined;
    });

    describe('resolve', () => {
      it('should return ICulte returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCulte = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCulte).toEqual({ id: 123 });
      });

      it('should return new ICulte if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCulte = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCulte).toEqual(new Culte());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Culte })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCulte = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCulte).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
