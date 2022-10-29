import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CounterTypeService } from '../service/counter-type.service';

import { CounterTypeComponent } from './counter-type.component';
import SpyInstance = jest.SpyInstance;

describe('CounterType Management Component', () => {
  let comp: CounterTypeComponent;
  let fixture: ComponentFixture<CounterTypeComponent>;
  let service: CounterTypeService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'counter-type', component: CounterTypeComponent }]), HttpClientTestingModule],
      declarations: [CounterTypeComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'counterCode,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'counterCode,desc',
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CounterTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CounterTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CounterTypeService);
    routerNavigateSpy = jest.spyOn(comp.router, 'navigate');

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ counterCode: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.counterTypes?.[0]).toEqual(expect.objectContaining({ counterCode: 'ABC' }));
  });

  describe('trackCounterCode', () => {
    it('Should forward to counterTypeService', () => {
      const entity = { counterCode: 'ABC' };
      jest.spyOn(service, 'getCounterTypeIdentifier');
      const counterCode = comp.trackCounterCode(0, entity);
      expect(service.getCounterTypeIdentifier).toHaveBeenCalledWith(entity);
      expect(counterCode).toBe(entity.counterCode);
    });
  });

  it('should load a page', () => {
    // WHEN
    comp.navigateToPage(1);

    // THEN
    expect(routerNavigateSpy).toHaveBeenCalled();
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['counterCode,desc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.navigateToWithComponentValues();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['name,asc'],
        }),
      })
    );
  });

  it('should calculate the filter attribute', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ 'someId.in': ['dc4279ea-cfb9-11ec-9d64-0242ac120002'] }));
  });
});
