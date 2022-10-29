import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICounterEvent } from '../counter-event.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../counter-event.test-samples';

import { CounterEventService } from './counter-event.service';

const requireRestSample: ICounterEvent = {
  ...sampleWithRequiredData,
};

describe('CounterEvent Service', () => {
  let service: CounterEventService;
  let httpMock: HttpTestingController;
  let expectedResult: ICounterEvent | ICounterEvent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CounterEventService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CounterEvent', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const counterEvent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(counterEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CounterEvent', () => {
      const counterEvent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(counterEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CounterEvent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CounterEvent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CounterEvent', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCounterEventToCollectionIfMissing', () => {
      it('should add a CounterEvent to an empty array', () => {
        const counterEvent: ICounterEvent = sampleWithRequiredData;
        expectedResult = service.addCounterEventToCollectionIfMissing([], counterEvent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterEvent);
      });

      it('should not add a CounterEvent to an array that contains it', () => {
        const counterEvent: ICounterEvent = sampleWithRequiredData;
        const counterEventCollection: ICounterEvent[] = [
          {
            ...counterEvent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCounterEventToCollectionIfMissing(counterEventCollection, counterEvent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CounterEvent to an array that doesn't contain it", () => {
        const counterEvent: ICounterEvent = sampleWithRequiredData;
        const counterEventCollection: ICounterEvent[] = [sampleWithPartialData];
        expectedResult = service.addCounterEventToCollectionIfMissing(counterEventCollection, counterEvent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterEvent);
      });

      it('should add only unique CounterEvent to an array', () => {
        const counterEventArray: ICounterEvent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const counterEventCollection: ICounterEvent[] = [sampleWithRequiredData];
        expectedResult = service.addCounterEventToCollectionIfMissing(counterEventCollection, ...counterEventArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const counterEvent: ICounterEvent = sampleWithRequiredData;
        const counterEvent2: ICounterEvent = sampleWithPartialData;
        expectedResult = service.addCounterEventToCollectionIfMissing([], counterEvent, counterEvent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterEvent);
        expect(expectedResult).toContain(counterEvent2);
      });

      it('should accept null and undefined values', () => {
        const counterEvent: ICounterEvent = sampleWithRequiredData;
        expectedResult = service.addCounterEventToCollectionIfMissing([], null, counterEvent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterEvent);
      });

      it('should return initial array if no CounterEvent is added', () => {
        const counterEventCollection: ICounterEvent[] = [sampleWithRequiredData];
        expectedResult = service.addCounterEventToCollectionIfMissing(counterEventCollection, undefined, null);
        expect(expectedResult).toEqual(counterEventCollection);
      });
    });

    describe('compareCounterEvent', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCounterEvent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareCounterEvent(entity1, entity2);
        const compareResult2 = service.compareCounterEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareCounterEvent(entity1, entity2);
        const compareResult2 = service.compareCounterEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareCounterEvent(entity1, entity2);
        const compareResult2 = service.compareCounterEvent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
