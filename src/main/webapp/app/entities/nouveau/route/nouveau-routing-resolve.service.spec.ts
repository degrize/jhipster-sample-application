jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INouveau, Nouveau } from '../nouveau.model';
import { NouveauService } from '../service/nouveau.service';

import { NouveauRoutingResolveService } from './nouveau-routing-resolve.service';

describe('Service Tests', () => {
  describe('Nouveau routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NouveauRoutingResolveService;
    let service: NouveauService;
    let resultNouveau: INouveau | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NouveauRoutingResolveService);
      service = TestBed.inject(NouveauService);
      resultNouveau = undefined;
    });

    describe('resolve', () => {
      it('should return INouveau returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNouveau = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNouveau).toEqual({ id: 123 });
      });

      it('should return new INouveau if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNouveau = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNouveau).toEqual(new Nouveau());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Nouveau })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNouveau = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNouveau).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
