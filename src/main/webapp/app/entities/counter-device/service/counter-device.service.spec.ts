import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICounterDevice } from '../counter-device.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../counter-device.test-samples';

import { CounterDeviceService } from './counter-device.service';

const requireRestSample: ICounterDevice = {
  ...sampleWithRequiredData,
};

describe('CounterDevice Service', () => {
  let service: CounterDeviceService;
  let httpMock: HttpTestingController;
  let expectedResult: ICounterDevice | ICounterDevice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CounterDeviceService);
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

    it('should create a CounterDevice', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const counterDevice = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(counterDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CounterDevice', () => {
      const counterDevice = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(counterDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CounterDevice', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CounterDevice', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CounterDevice', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCounterDeviceToCollectionIfMissing', () => {
      it('should add a CounterDevice to an empty array', () => {
        const counterDevice: ICounterDevice = sampleWithRequiredData;
        expectedResult = service.addCounterDeviceToCollectionIfMissing([], counterDevice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterDevice);
      });

      it('should not add a CounterDevice to an array that contains it', () => {
        const counterDevice: ICounterDevice = sampleWithRequiredData;
        const counterDeviceCollection: ICounterDevice[] = [
          {
            ...counterDevice,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCounterDeviceToCollectionIfMissing(counterDeviceCollection, counterDevice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CounterDevice to an array that doesn't contain it", () => {
        const counterDevice: ICounterDevice = sampleWithRequiredData;
        const counterDeviceCollection: ICounterDevice[] = [sampleWithPartialData];
        expectedResult = service.addCounterDeviceToCollectionIfMissing(counterDeviceCollection, counterDevice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterDevice);
      });

      it('should add only unique CounterDevice to an array', () => {
        const counterDeviceArray: ICounterDevice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const counterDeviceCollection: ICounterDevice[] = [sampleWithRequiredData];
        expectedResult = service.addCounterDeviceToCollectionIfMissing(counterDeviceCollection, ...counterDeviceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const counterDevice: ICounterDevice = sampleWithRequiredData;
        const counterDevice2: ICounterDevice = sampleWithPartialData;
        expectedResult = service.addCounterDeviceToCollectionIfMissing([], counterDevice, counterDevice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterDevice);
        expect(expectedResult).toContain(counterDevice2);
      });

      it('should accept null and undefined values', () => {
        const counterDevice: ICounterDevice = sampleWithRequiredData;
        expectedResult = service.addCounterDeviceToCollectionIfMissing([], null, counterDevice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterDevice);
      });

      it('should return initial array if no CounterDevice is added', () => {
        const counterDeviceCollection: ICounterDevice[] = [sampleWithRequiredData];
        expectedResult = service.addCounterDeviceToCollectionIfMissing(counterDeviceCollection, undefined, null);
        expect(expectedResult).toEqual(counterDeviceCollection);
      });
    });

    describe('compareCounterDevice', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCounterDevice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareCounterDevice(entity1, entity2);
        const compareResult2 = service.compareCounterDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareCounterDevice(entity1, entity2);
        const compareResult2 = service.compareCounterDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareCounterDevice(entity1, entity2);
        const compareResult2 = service.compareCounterDevice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
