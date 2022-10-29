import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IKeyOperatingProperty } from '../key-operating-property.model';
import { KeyOperatingPropertyService } from '../service/key-operating-property.service';

import { KeyOperatingPropertyRoutingResolveService } from './key-operating-property-routing-resolve.service';

describe('KeyOperatingProperty routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: KeyOperatingPropertyRoutingResolveService;
  let service: KeyOperatingPropertyService;
  let resultKeyOperatingProperty: IKeyOperatingProperty | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(KeyOperatingPropertyRoutingResolveService);
    service = TestBed.inject(KeyOperatingPropertyService);
    resultKeyOperatingProperty = undefined;
  });

  describe('resolve', () => {
    it('should return IKeyOperatingProperty returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKeyOperatingProperty = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKeyOperatingProperty).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKeyOperatingProperty = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultKeyOperatingProperty).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IKeyOperatingProperty>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKeyOperatingProperty = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKeyOperatingProperty).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
