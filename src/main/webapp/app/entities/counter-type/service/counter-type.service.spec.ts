import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICounterType } from '../counter-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../counter-type.test-samples';

import { CounterTypeService } from './counter-type.service';

const requireRestSample: ICounterType = {
  ...sampleWithRequiredData,
};

describe('CounterType Service', () => {
  let service: CounterTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICounterType | ICounterType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CounterTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CounterType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const counterType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(counterType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CounterType', () => {
      const counterType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(counterType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CounterType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CounterType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CounterType', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCounterTypeToCollectionIfMissing', () => {
      it('should add a CounterType to an empty array', () => {
        const counterType: ICounterType = sampleWithRequiredData;
        expectedResult = service.addCounterTypeToCollectionIfMissing([], counterType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterType);
      });

      it('should not add a CounterType to an array that contains it', () => {
        const counterType: ICounterType = sampleWithRequiredData;
        const counterTypeCollection: ICounterType[] = [
          {
            ...counterType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCounterTypeToCollectionIfMissing(counterTypeCollection, counterType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CounterType to an array that doesn't contain it", () => {
        const counterType: ICounterType = sampleWithRequiredData;
        const counterTypeCollection: ICounterType[] = [sampleWithPartialData];
        expectedResult = service.addCounterTypeToCollectionIfMissing(counterTypeCollection, counterType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterType);
      });

      it('should add only unique CounterType to an array', () => {
        const counterTypeArray: ICounterType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const counterTypeCollection: ICounterType[] = [sampleWithRequiredData];
        expectedResult = service.addCounterTypeToCollectionIfMissing(counterTypeCollection, ...counterTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const counterType: ICounterType = sampleWithRequiredData;
        const counterType2: ICounterType = sampleWithPartialData;
        expectedResult = service.addCounterTypeToCollectionIfMissing([], counterType, counterType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterType);
        expect(expectedResult).toContain(counterType2);
      });

      it('should accept null and undefined values', () => {
        const counterType: ICounterType = sampleWithRequiredData;
        expectedResult = service.addCounterTypeToCollectionIfMissing([], null, counterType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterType);
      });

      it('should return initial array if no CounterType is added', () => {
        const counterTypeCollection: ICounterType[] = [sampleWithRequiredData];
        expectedResult = service.addCounterTypeToCollectionIfMissing(counterTypeCollection, undefined, null);
        expect(expectedResult).toEqual(counterTypeCollection);
      });
    });

    describe('compareCounterType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCounterType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { counterCode: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCounterType(entity1, entity2);
        const compareResult2 = service.compareCounterType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { counterCode: 'ABC' };
        const entity2 = { counterCode: 'CBA' };

        const compareResult1 = service.compareCounterType(entity1, entity2);
        const compareResult2 = service.compareCounterType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { counterCode: 'ABC' };
        const entity2 = { counterCode: 'ABC' };

        const compareResult1 = service.compareCounterType(entity1, entity2);
        const compareResult2 = service.compareCounterType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
