import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeviceCategory } from '../device-category.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../device-category.test-samples';

import { DeviceCategoryService } from './device-category.service';

const requireRestSample: IDeviceCategory = {
  ...sampleWithRequiredData,
};

describe('DeviceCategory Service', () => {
  let service: DeviceCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeviceCategory | IDeviceCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeviceCategoryService);
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

    it('should create a DeviceCategory', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const deviceCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deviceCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeviceCategory', () => {
      const deviceCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deviceCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeviceCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeviceCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeviceCategory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeviceCategoryToCollectionIfMissing', () => {
      it('should add a DeviceCategory to an empty array', () => {
        const deviceCategory: IDeviceCategory = sampleWithRequiredData;
        expectedResult = service.addDeviceCategoryToCollectionIfMissing([], deviceCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceCategory);
      });

      it('should not add a DeviceCategory to an array that contains it', () => {
        const deviceCategory: IDeviceCategory = sampleWithRequiredData;
        const deviceCategoryCollection: IDeviceCategory[] = [
          {
            ...deviceCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeviceCategoryToCollectionIfMissing(deviceCategoryCollection, deviceCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeviceCategory to an array that doesn't contain it", () => {
        const deviceCategory: IDeviceCategory = sampleWithRequiredData;
        const deviceCategoryCollection: IDeviceCategory[] = [sampleWithPartialData];
        expectedResult = service.addDeviceCategoryToCollectionIfMissing(deviceCategoryCollection, deviceCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceCategory);
      });

      it('should add only unique DeviceCategory to an array', () => {
        const deviceCategoryArray: IDeviceCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deviceCategoryCollection: IDeviceCategory[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceCategoryToCollectionIfMissing(deviceCategoryCollection, ...deviceCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deviceCategory: IDeviceCategory = sampleWithRequiredData;
        const deviceCategory2: IDeviceCategory = sampleWithPartialData;
        expectedResult = service.addDeviceCategoryToCollectionIfMissing([], deviceCategory, deviceCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceCategory);
        expect(expectedResult).toContain(deviceCategory2);
      });

      it('should accept null and undefined values', () => {
        const deviceCategory: IDeviceCategory = sampleWithRequiredData;
        expectedResult = service.addDeviceCategoryToCollectionIfMissing([], null, deviceCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceCategory);
      });

      it('should return initial array if no DeviceCategory is added', () => {
        const deviceCategoryCollection: IDeviceCategory[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceCategoryToCollectionIfMissing(deviceCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(deviceCategoryCollection);
      });
    });

    describe('compareDeviceCategory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeviceCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeviceCategory(entity1, entity2);
        const compareResult2 = service.compareDeviceCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeviceCategory(entity1, entity2);
        const compareResult2 = service.compareDeviceCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeviceCategory(entity1, entity2);
        const compareResult2 = service.compareDeviceCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
