import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIsle } from '../isle.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../isle.test-samples';

import { IsleService } from './isle.service';

const requireRestSample: IIsle = {
  ...sampleWithRequiredData,
};

describe('Isle Service', () => {
  let service: IsleService;
  let httpMock: HttpTestingController;
  let expectedResult: IIsle | IIsle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IsleService);
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

    it('should create a Isle', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const isle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(isle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Isle', () => {
      const isle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(isle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Isle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Isle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Isle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIsleToCollectionIfMissing', () => {
      it('should add a Isle to an empty array', () => {
        const isle: IIsle = sampleWithRequiredData;
        expectedResult = service.addIsleToCollectionIfMissing([], isle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isle);
      });

      it('should not add a Isle to an array that contains it', () => {
        const isle: IIsle = sampleWithRequiredData;
        const isleCollection: IIsle[] = [
          {
            ...isle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIsleToCollectionIfMissing(isleCollection, isle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Isle to an array that doesn't contain it", () => {
        const isle: IIsle = sampleWithRequiredData;
        const isleCollection: IIsle[] = [sampleWithPartialData];
        expectedResult = service.addIsleToCollectionIfMissing(isleCollection, isle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isle);
      });

      it('should add only unique Isle to an array', () => {
        const isleArray: IIsle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const isleCollection: IIsle[] = [sampleWithRequiredData];
        expectedResult = service.addIsleToCollectionIfMissing(isleCollection, ...isleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const isle: IIsle = sampleWithRequiredData;
        const isle2: IIsle = sampleWithPartialData;
        expectedResult = service.addIsleToCollectionIfMissing([], isle, isle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isle);
        expect(expectedResult).toContain(isle2);
      });

      it('should accept null and undefined values', () => {
        const isle: IIsle = sampleWithRequiredData;
        expectedResult = service.addIsleToCollectionIfMissing([], null, isle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isle);
      });

      it('should return initial array if no Isle is added', () => {
        const isleCollection: IIsle[] = [sampleWithRequiredData];
        expectedResult = service.addIsleToCollectionIfMissing(isleCollection, undefined, null);
        expect(expectedResult).toEqual(isleCollection);
      });
    });

    describe('compareIsle', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIsle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIsle(entity1, entity2);
        const compareResult2 = service.compareIsle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIsle(entity1, entity2);
        const compareResult2 = service.compareIsle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIsle(entity1, entity2);
        const compareResult2 = service.compareIsle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
