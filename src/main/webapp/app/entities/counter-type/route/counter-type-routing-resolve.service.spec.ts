import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICounterType } from '../counter-type.model';
import { CounterTypeService } from '../service/counter-type.service';

import { CounterTypeRoutingResolveService } from './counter-type-routing-resolve.service';

describe('CounterType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CounterTypeRoutingResolveService;
  let service: CounterTypeService;
  let resultCounterType: ICounterType | null | undefined;

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
    routingResolveService = TestBed.inject(CounterTypeRoutingResolveService);
    service = TestBed.inject(CounterTypeService);
    resultCounterType = undefined;
  });

  describe('resolve', () => {
    it('should return ICounterType returned by find', () => {
      // GIVEN
      service.find = jest.fn(counterCode => of(new HttpResponse({ body: { counterCode } })));
      mockActivatedRouteSnapshot.params = { counterCode: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCounterType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultCounterType).toEqual({ counterCode: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCounterType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCounterType).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ICounterType>({ body: null })));
      mockActivatedRouteSnapshot.params = { counterCode: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCounterType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultCounterType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
