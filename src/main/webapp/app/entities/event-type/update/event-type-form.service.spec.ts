import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../event-type.test-samples';

import { EventTypeFormService } from './event-type-form.service';

describe('EventType Form Service', () => {
  let service: EventTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventTypeFormService);
  });

  describe('Service methods', () => {
    describe('createEventTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventCode: expect.any(Object),
            sasCode: expect.any(Object),
            description: expect.any(Object),
            isStorable: expect.any(Object),
            isPriority: expect.any(Object),
            procesador: expect.any(Object),
            isAlarm: expect.any(Object),
          })
        );
      });

      it('passing IEventType should create a new form with FormGroup', () => {
        const formGroup = service.createEventTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventCode: expect.any(Object),
            sasCode: expect.any(Object),
            description: expect.any(Object),
            isStorable: expect.any(Object),
            isPriority: expect.any(Object),
            procesador: expect.any(Object),
            isAlarm: expect.any(Object),
          })
        );
      });
    });

    describe('getEventType', () => {
      it('should return NewEventType for default EventType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEventTypeFormGroup(sampleWithNewData);

        const eventType = service.getEventType(formGroup) as any;

        expect(eventType).toMatchObject(sampleWithNewData);
      });

      it('should return NewEventType for empty EventType initial value', () => {
        const formGroup = service.createEventTypeFormGroup();

        const eventType = service.getEventType(formGroup) as any;

        expect(eventType).toMatchObject({});
      });

      it('should return IEventType', () => {
        const formGroup = service.createEventTypeFormGroup(sampleWithRequiredData);

        const eventType = service.getEventType(formGroup) as any;

        expect(eventType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEventType should not enable id FormControl', () => {
        const formGroup = service.createEventTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEventType should disable id FormControl', () => {
        const formGroup = service.createEventTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
