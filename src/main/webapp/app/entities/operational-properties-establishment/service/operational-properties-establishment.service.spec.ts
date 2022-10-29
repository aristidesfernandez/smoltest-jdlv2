import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOperationalPropertiesEstablishment } from '../operational-properties-establishment.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../operational-properties-establishment.test-samples';

import { OperationalPropertiesEstablishmentService } from './operational-properties-establishment.service';

const requireRestSample: IOperationalPropertiesEstablishment = {
  ...sampleWithRequiredData,
};

describe('OperationalPropertiesEstablishment Service', () => {
  let service: OperationalPropertiesEstablishmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IOperationalPropertiesEstablishment | IOperationalPropertiesEstablishment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperationalPropertiesEstablishmentService);
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

    it('should create a OperationalPropertiesEstablishment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const operationalPropertiesEstablishment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(operationalPropertiesEstablishment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OperationalPropertiesEstablishment', () => {
      const operationalPropertiesEstablishment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(operationalPropertiesEstablishment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OperationalPropertiesEstablishment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OperationalPropertiesEstablishment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OperationalPropertiesEstablishment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOperationalPropertiesEstablishmentToCollectionIfMissing', () => {
      it('should add a OperationalPropertiesEstablishment to an empty array', () => {
        const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = sampleWithRequiredData;
        expectedResult = service.addOperationalPropertiesEstablishmentToCollectionIfMissing([], operationalPropertiesEstablishment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operationalPropertiesEstablishment);
      });

      it('should not add a OperationalPropertiesEstablishment to an array that contains it', () => {
        const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = sampleWithRequiredData;
        const operationalPropertiesEstablishmentCollection: IOperationalPropertiesEstablishment[] = [
          {
            ...operationalPropertiesEstablishment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOperationalPropertiesEstablishmentToCollectionIfMissing(
          operationalPropertiesEstablishmentCollection,
          operationalPropertiesEstablishment
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OperationalPropertiesEstablishment to an array that doesn't contain it", () => {
        const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = sampleWithRequiredData;
        const operationalPropertiesEstablishmentCollection: IOperationalPropertiesEstablishment[] = [sampleWithPartialData];
        expectedResult = service.addOperationalPropertiesEstablishmentToCollectionIfMissing(
          operationalPropertiesEstablishmentCollection,
          operationalPropertiesEstablishment
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operationalPropertiesEstablishment);
      });

      it('should add only unique OperationalPropertiesEstablishment to an array', () => {
        const operationalPropertiesEstablishmentArray: IOperationalPropertiesEstablishment[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const operationalPropertiesEstablishmentCollection: IOperationalPropertiesEstablishment[] = [sampleWithRequiredData];
        expectedResult = service.addOperationalPropertiesEstablishmentToCollectionIfMissing(
          operationalPropertiesEstablishmentCollection,
          ...operationalPropertiesEstablishmentArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = sampleWithRequiredData;
        const operationalPropertiesEstablishment2: IOperationalPropertiesEstablishment = sampleWithPartialData;
        expectedResult = service.addOperationalPropertiesEstablishmentToCollectionIfMissing(
          [],
          operationalPropertiesEstablishment,
          operationalPropertiesEstablishment2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operationalPropertiesEstablishment);
        expect(expectedResult).toContain(operationalPropertiesEstablishment2);
      });

      it('should accept null and undefined values', () => {
        const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = sampleWithRequiredData;
        expectedResult = service.addOperationalPropertiesEstablishmentToCollectionIfMissing(
          [],
          null,
          operationalPropertiesEstablishment,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operationalPropertiesEstablishment);
      });

      it('should return initial array if no OperationalPropertiesEstablishment is added', () => {
        const operationalPropertiesEstablishmentCollection: IOperationalPropertiesEstablishment[] = [sampleWithRequiredData];
        expectedResult = service.addOperationalPropertiesEstablishmentToCollectionIfMissing(
          operationalPropertiesEstablishmentCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(operationalPropertiesEstablishmentCollection);
      });
    });

    describe('compareOperationalPropertiesEstablishment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOperationalPropertiesEstablishment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOperationalPropertiesEstablishment(entity1, entity2);
        const compareResult2 = service.compareOperationalPropertiesEstablishment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOperationalPropertiesEstablishment(entity1, entity2);
        const compareResult2 = service.compareOperationalPropertiesEstablishment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOperationalPropertiesEstablishment(entity1, entity2);
        const compareResult2 = service.compareOperationalPropertiesEstablishment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
