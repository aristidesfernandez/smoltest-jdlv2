import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICurrencyType } from '../currency-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../currency-type.test-samples';

import { CurrencyTypeService } from './currency-type.service';

const requireRestSample: ICurrencyType = {
  ...sampleWithRequiredData,
};

describe('CurrencyType Service', () => {
  let service: CurrencyTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICurrencyType | ICurrencyType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CurrencyTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CurrencyType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const currencyType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(currencyType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CurrencyType', () => {
      const currencyType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(currencyType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CurrencyType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CurrencyType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CurrencyType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCurrencyTypeToCollectionIfMissing', () => {
      it('should add a CurrencyType to an empty array', () => {
        const currencyType: ICurrencyType = sampleWithRequiredData;
        expectedResult = service.addCurrencyTypeToCollectionIfMissing([], currencyType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(currencyType);
      });

      it('should not add a CurrencyType to an array that contains it', () => {
        const currencyType: ICurrencyType = sampleWithRequiredData;
        const currencyTypeCollection: ICurrencyType[] = [
          {
            ...currencyType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCurrencyTypeToCollectionIfMissing(currencyTypeCollection, currencyType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CurrencyType to an array that doesn't contain it", () => {
        const currencyType: ICurrencyType = sampleWithRequiredData;
        const currencyTypeCollection: ICurrencyType[] = [sampleWithPartialData];
        expectedResult = service.addCurrencyTypeToCollectionIfMissing(currencyTypeCollection, currencyType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(currencyType);
      });

      it('should add only unique CurrencyType to an array', () => {
        const currencyTypeArray: ICurrencyType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const currencyTypeCollection: ICurrencyType[] = [sampleWithRequiredData];
        expectedResult = service.addCurrencyTypeToCollectionIfMissing(currencyTypeCollection, ...currencyTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const currencyType: ICurrencyType = sampleWithRequiredData;
        const currencyType2: ICurrencyType = sampleWithPartialData;
        expectedResult = service.addCurrencyTypeToCollectionIfMissing([], currencyType, currencyType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(currencyType);
        expect(expectedResult).toContain(currencyType2);
      });

      it('should accept null and undefined values', () => {
        const currencyType: ICurrencyType = sampleWithRequiredData;
        expectedResult = service.addCurrencyTypeToCollectionIfMissing([], null, currencyType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(currencyType);
      });

      it('should return initial array if no CurrencyType is added', () => {
        const currencyTypeCollection: ICurrencyType[] = [sampleWithRequiredData];
        expectedResult = service.addCurrencyTypeToCollectionIfMissing(currencyTypeCollection, undefined, null);
        expect(expectedResult).toEqual(currencyTypeCollection);
      });
    });

    describe('compareCurrencyType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCurrencyType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCurrencyType(entity1, entity2);
        const compareResult2 = service.compareCurrencyType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCurrencyType(entity1, entity2);
        const compareResult2 = service.compareCurrencyType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCurrencyType(entity1, entity2);
        const compareResult2 = service.compareCurrencyType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
