import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeviceEstablishment } from '../device-establishment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../device-establishment.test-samples';

import { DeviceEstablishmentService, RestDeviceEstablishment } from './device-establishment.service';

const requireRestSample: RestDeviceEstablishment = {
  ...sampleWithRequiredData,
  registrationAt: sampleWithRequiredData.registrationAt?.toJSON(),
  departureAt: sampleWithRequiredData.departureAt?.toJSON(),
};

describe('DeviceEstablishment Service', () => {
  let service: DeviceEstablishmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeviceEstablishment | IDeviceEstablishment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeviceEstablishmentService);
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

    it('should create a DeviceEstablishment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const deviceEstablishment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deviceEstablishment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeviceEstablishment', () => {
      const deviceEstablishment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deviceEstablishment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeviceEstablishment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeviceEstablishment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeviceEstablishment', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeviceEstablishmentToCollectionIfMissing', () => {
      it('should add a DeviceEstablishment to an empty array', () => {
        const deviceEstablishment: IDeviceEstablishment = sampleWithRequiredData;
        expectedResult = service.addDeviceEstablishmentToCollectionIfMissing([], deviceEstablishment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceEstablishment);
      });

      it('should not add a DeviceEstablishment to an array that contains it', () => {
        const deviceEstablishment: IDeviceEstablishment = sampleWithRequiredData;
        const deviceEstablishmentCollection: IDeviceEstablishment[] = [
          {
            ...deviceEstablishment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeviceEstablishmentToCollectionIfMissing(deviceEstablishmentCollection, deviceEstablishment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeviceEstablishment to an array that doesn't contain it", () => {
        const deviceEstablishment: IDeviceEstablishment = sampleWithRequiredData;
        const deviceEstablishmentCollection: IDeviceEstablishment[] = [sampleWithPartialData];
        expectedResult = service.addDeviceEstablishmentToCollectionIfMissing(deviceEstablishmentCollection, deviceEstablishment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceEstablishment);
      });

      it('should add only unique DeviceEstablishment to an array', () => {
        const deviceEstablishmentArray: IDeviceEstablishment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deviceEstablishmentCollection: IDeviceEstablishment[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceEstablishmentToCollectionIfMissing(deviceEstablishmentCollection, ...deviceEstablishmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deviceEstablishment: IDeviceEstablishment = sampleWithRequiredData;
        const deviceEstablishment2: IDeviceEstablishment = sampleWithPartialData;
        expectedResult = service.addDeviceEstablishmentToCollectionIfMissing([], deviceEstablishment, deviceEstablishment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceEstablishment);
        expect(expectedResult).toContain(deviceEstablishment2);
      });

      it('should accept null and undefined values', () => {
        const deviceEstablishment: IDeviceEstablishment = sampleWithRequiredData;
        expectedResult = service.addDeviceEstablishmentToCollectionIfMissing([], null, deviceEstablishment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceEstablishment);
      });

      it('should return initial array if no DeviceEstablishment is added', () => {
        const deviceEstablishmentCollection: IDeviceEstablishment[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceEstablishmentToCollectionIfMissing(deviceEstablishmentCollection, undefined, null);
        expect(expectedResult).toEqual(deviceEstablishmentCollection);
      });
    });

    describe('compareDeviceEstablishment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeviceEstablishment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareDeviceEstablishment(entity1, entity2);
        const compareResult2 = service.compareDeviceEstablishment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareDeviceEstablishment(entity1, entity2);
        const compareResult2 = service.compareDeviceEstablishment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareDeviceEstablishment(entity1, entity2);
        const compareResult2 = service.compareDeviceEstablishment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
