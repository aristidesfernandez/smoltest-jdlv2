import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEventDevice } from '../event-device.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../event-device.test-samples';

import { EventDeviceService, RestEventDevice } from './event-device.service';

const requireRestSample: RestEventDevice = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
};

describe('EventDevice Service', () => {
  let service: EventDeviceService;
  let httpMock: HttpTestingController;
  let expectedResult: IEventDevice | IEventDevice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventDeviceService);
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

    it('should create a EventDevice', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const eventDevice = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eventDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EventDevice', () => {
      const eventDevice = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eventDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EventDevice', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EventDevice', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EventDevice', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEventDeviceToCollectionIfMissing', () => {
      it('should add a EventDevice to an empty array', () => {
        const eventDevice: IEventDevice = sampleWithRequiredData;
        expectedResult = service.addEventDeviceToCollectionIfMissing([], eventDevice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventDevice);
      });

      it('should not add a EventDevice to an array that contains it', () => {
        const eventDevice: IEventDevice = sampleWithRequiredData;
        const eventDeviceCollection: IEventDevice[] = [
          {
            ...eventDevice,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEventDeviceToCollectionIfMissing(eventDeviceCollection, eventDevice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EventDevice to an array that doesn't contain it", () => {
        const eventDevice: IEventDevice = sampleWithRequiredData;
        const eventDeviceCollection: IEventDevice[] = [sampleWithPartialData];
        expectedResult = service.addEventDeviceToCollectionIfMissing(eventDeviceCollection, eventDevice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventDevice);
      });

      it('should add only unique EventDevice to an array', () => {
        const eventDeviceArray: IEventDevice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eventDeviceCollection: IEventDevice[] = [sampleWithRequiredData];
        expectedResult = service.addEventDeviceToCollectionIfMissing(eventDeviceCollection, ...eventDeviceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eventDevice: IEventDevice = sampleWithRequiredData;
        const eventDevice2: IEventDevice = sampleWithPartialData;
        expectedResult = service.addEventDeviceToCollectionIfMissing([], eventDevice, eventDevice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventDevice);
        expect(expectedResult).toContain(eventDevice2);
      });

      it('should accept null and undefined values', () => {
        const eventDevice: IEventDevice = sampleWithRequiredData;
        expectedResult = service.addEventDeviceToCollectionIfMissing([], null, eventDevice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventDevice);
      });

      it('should return initial array if no EventDevice is added', () => {
        const eventDeviceCollection: IEventDevice[] = [sampleWithRequiredData];
        expectedResult = service.addEventDeviceToCollectionIfMissing(eventDeviceCollection, undefined, null);
        expect(expectedResult).toEqual(eventDeviceCollection);
      });
    });

    describe('compareEventDevice', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEventDevice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareEventDevice(entity1, entity2);
        const compareResult2 = service.compareEventDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareEventDevice(entity1, entity2);
        const compareResult2 = service.compareEventDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareEventDevice(entity1, entity2);
        const compareResult2 = service.compareEventDevice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
