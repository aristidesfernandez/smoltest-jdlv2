import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeviceInterface } from '../device-interface.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../device-interface.test-samples';

import { DeviceInterfaceService, RestDeviceInterface } from './device-interface.service';

const requireRestSample: RestDeviceInterface = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('DeviceInterface Service', () => {
  let service: DeviceInterfaceService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeviceInterface | IDeviceInterface[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeviceInterfaceService);
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

    it('should create a DeviceInterface', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const deviceInterface = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deviceInterface).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeviceInterface', () => {
      const deviceInterface = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deviceInterface).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeviceInterface', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeviceInterface', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeviceInterface', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeviceInterfaceToCollectionIfMissing', () => {
      it('should add a DeviceInterface to an empty array', () => {
        const deviceInterface: IDeviceInterface = sampleWithRequiredData;
        expectedResult = service.addDeviceInterfaceToCollectionIfMissing([], deviceInterface);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceInterface);
      });

      it('should not add a DeviceInterface to an array that contains it', () => {
        const deviceInterface: IDeviceInterface = sampleWithRequiredData;
        const deviceInterfaceCollection: IDeviceInterface[] = [
          {
            ...deviceInterface,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeviceInterfaceToCollectionIfMissing(deviceInterfaceCollection, deviceInterface);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeviceInterface to an array that doesn't contain it", () => {
        const deviceInterface: IDeviceInterface = sampleWithRequiredData;
        const deviceInterfaceCollection: IDeviceInterface[] = [sampleWithPartialData];
        expectedResult = service.addDeviceInterfaceToCollectionIfMissing(deviceInterfaceCollection, deviceInterface);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceInterface);
      });

      it('should add only unique DeviceInterface to an array', () => {
        const deviceInterfaceArray: IDeviceInterface[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deviceInterfaceCollection: IDeviceInterface[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceInterfaceToCollectionIfMissing(deviceInterfaceCollection, ...deviceInterfaceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deviceInterface: IDeviceInterface = sampleWithRequiredData;
        const deviceInterface2: IDeviceInterface = sampleWithPartialData;
        expectedResult = service.addDeviceInterfaceToCollectionIfMissing([], deviceInterface, deviceInterface2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceInterface);
        expect(expectedResult).toContain(deviceInterface2);
      });

      it('should accept null and undefined values', () => {
        const deviceInterface: IDeviceInterface = sampleWithRequiredData;
        expectedResult = service.addDeviceInterfaceToCollectionIfMissing([], null, deviceInterface, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceInterface);
      });

      it('should return initial array if no DeviceInterface is added', () => {
        const deviceInterfaceCollection: IDeviceInterface[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceInterfaceToCollectionIfMissing(deviceInterfaceCollection, undefined, null);
        expect(expectedResult).toEqual(deviceInterfaceCollection);
      });
    });

    describe('compareDeviceInterface', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeviceInterface(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeviceInterface(entity1, entity2);
        const compareResult2 = service.compareDeviceInterface(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeviceInterface(entity1, entity2);
        const compareResult2 = service.compareDeviceInterface(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeviceInterface(entity1, entity2);
        const compareResult2 = service.compareDeviceInterface(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
