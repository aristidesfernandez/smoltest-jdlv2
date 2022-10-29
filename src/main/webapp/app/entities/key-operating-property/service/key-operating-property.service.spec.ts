import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IKeyOperatingProperty } from '../key-operating-property.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../key-operating-property.test-samples';

import { KeyOperatingPropertyService } from './key-operating-property.service';

const requireRestSample: IKeyOperatingProperty = {
  ...sampleWithRequiredData,
};

describe('KeyOperatingProperty Service', () => {
  let service: KeyOperatingPropertyService;
  let httpMock: HttpTestingController;
  let expectedResult: IKeyOperatingProperty | IKeyOperatingProperty[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KeyOperatingPropertyService);
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

    it('should create a KeyOperatingProperty', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const keyOperatingProperty = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(keyOperatingProperty).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KeyOperatingProperty', () => {
      const keyOperatingProperty = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(keyOperatingProperty).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KeyOperatingProperty', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KeyOperatingProperty', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KeyOperatingProperty', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKeyOperatingPropertyToCollectionIfMissing', () => {
      it('should add a KeyOperatingProperty to an empty array', () => {
        const keyOperatingProperty: IKeyOperatingProperty = sampleWithRequiredData;
        expectedResult = service.addKeyOperatingPropertyToCollectionIfMissing([], keyOperatingProperty);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(keyOperatingProperty);
      });

      it('should not add a KeyOperatingProperty to an array that contains it', () => {
        const keyOperatingProperty: IKeyOperatingProperty = sampleWithRequiredData;
        const keyOperatingPropertyCollection: IKeyOperatingProperty[] = [
          {
            ...keyOperatingProperty,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKeyOperatingPropertyToCollectionIfMissing(keyOperatingPropertyCollection, keyOperatingProperty);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KeyOperatingProperty to an array that doesn't contain it", () => {
        const keyOperatingProperty: IKeyOperatingProperty = sampleWithRequiredData;
        const keyOperatingPropertyCollection: IKeyOperatingProperty[] = [sampleWithPartialData];
        expectedResult = service.addKeyOperatingPropertyToCollectionIfMissing(keyOperatingPropertyCollection, keyOperatingProperty);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(keyOperatingProperty);
      });

      it('should add only unique KeyOperatingProperty to an array', () => {
        const keyOperatingPropertyArray: IKeyOperatingProperty[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const keyOperatingPropertyCollection: IKeyOperatingProperty[] = [sampleWithRequiredData];
        expectedResult = service.addKeyOperatingPropertyToCollectionIfMissing(keyOperatingPropertyCollection, ...keyOperatingPropertyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const keyOperatingProperty: IKeyOperatingProperty = sampleWithRequiredData;
        const keyOperatingProperty2: IKeyOperatingProperty = sampleWithPartialData;
        expectedResult = service.addKeyOperatingPropertyToCollectionIfMissing([], keyOperatingProperty, keyOperatingProperty2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(keyOperatingProperty);
        expect(expectedResult).toContain(keyOperatingProperty2);
      });

      it('should accept null and undefined values', () => {
        const keyOperatingProperty: IKeyOperatingProperty = sampleWithRequiredData;
        expectedResult = service.addKeyOperatingPropertyToCollectionIfMissing([], null, keyOperatingProperty, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(keyOperatingProperty);
      });

      it('should return initial array if no KeyOperatingProperty is added', () => {
        const keyOperatingPropertyCollection: IKeyOperatingProperty[] = [sampleWithRequiredData];
        expectedResult = service.addKeyOperatingPropertyToCollectionIfMissing(keyOperatingPropertyCollection, undefined, null);
        expect(expectedResult).toEqual(keyOperatingPropertyCollection);
      });
    });

    describe('compareKeyOperatingProperty', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKeyOperatingProperty(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareKeyOperatingProperty(entity1, entity2);
        const compareResult2 = service.compareKeyOperatingProperty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareKeyOperatingProperty(entity1, entity2);
        const compareResult2 = service.compareKeyOperatingProperty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareKeyOperatingProperty(entity1, entity2);
        const compareResult2 = service.compareKeyOperatingProperty(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
