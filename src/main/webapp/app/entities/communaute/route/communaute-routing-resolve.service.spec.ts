jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommunaute, Communaute } from '../communaute.model';
import { CommunauteService } from '../service/communaute.service';

import { CommunauteRoutingResolveService } from './communaute-routing-resolve.service';

describe('Service Tests', () => {
  describe('Communaute routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommunauteRoutingResolveService;
    let service: CommunauteService;
    let resultCommunaute: ICommunaute | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommunauteRoutingResolveService);
      service = TestBed.inject(CommunauteService);
      resultCommunaute = undefined;
    });

    describe('resolve', () => {
      it('should return ICommunaute returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunaute = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunaute).toEqual({ id: 123 });
      });

      it('should return new ICommunaute if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunaute = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommunaute).toEqual(new Communaute());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Communaute })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunaute = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunaute).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
