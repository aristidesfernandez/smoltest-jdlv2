import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeviceType } from '../device-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../device-type.test-samples';

import { DeviceTypeService } from './device-type.service';

const requireRestSample: IDeviceType = {
  ...sampleWithRequiredData,
};

describe('DeviceType Service', () => {
  let service: DeviceTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeviceType | IDeviceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeviceTypeService);
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

    it('should create a DeviceType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const deviceType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deviceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeviceType', () => {
      const deviceType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deviceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeviceType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeviceType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeviceType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeviceTypeToCollectionIfMissing', () => {
      it('should add a DeviceType to an empty array', () => {
        const deviceType: IDeviceType = sampleWithRequiredData;
        expectedResult = service.addDeviceTypeToCollectionIfMissing([], deviceType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceType);
      });

      it('should not add a DeviceType to an array that contains it', () => {
        const deviceType: IDeviceType = sampleWithRequiredData;
        const deviceTypeCollection: IDeviceType[] = [
          {
            ...deviceType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, deviceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeviceType to an array that doesn't contain it", () => {
        const deviceType: IDeviceType = sampleWithRequiredData;
        const deviceTypeCollection: IDeviceType[] = [sampleWithPartialData];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, deviceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceType);
      });

      it('should add only unique DeviceType to an array', () => {
        const deviceTypeArray: IDeviceType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deviceTypeCollection: IDeviceType[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, ...deviceTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deviceType: IDeviceType = sampleWithRequiredData;
        const deviceType2: IDeviceType = sampleWithPartialData;
        expectedResult = service.addDeviceTypeToCollectionIfMissing([], deviceType, deviceType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceType);
        expect(expectedResult).toContain(deviceType2);
      });

      it('should accept null and undefined values', () => {
        const deviceType: IDeviceType = sampleWithRequiredData;
        expectedResult = service.addDeviceTypeToCollectionIfMissing([], null, deviceType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceType);
      });

      it('should return initial array if no DeviceType is added', () => {
        const deviceTypeCollection: IDeviceType[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(deviceTypeCollection);
      });
    });

    describe('compareDeviceType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeviceType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeviceType(entity1, entity2);
        const compareResult2 = service.compareDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeviceType(entity1, entity2);
        const compareResult2 = service.compareDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeviceType(entity1, entity2);
        const compareResult2 = service.compareDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
