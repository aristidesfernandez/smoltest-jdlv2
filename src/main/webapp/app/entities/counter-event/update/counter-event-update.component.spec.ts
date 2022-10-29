import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CounterEventFormService } from './counter-event-form.service';
import { CounterEventService } from '../service/counter-event.service';
import { ICounterEvent } from '../counter-event.model';
import { IEventDevice } from 'app/entities/event-device/event-device.model';
import { EventDeviceService } from 'app/entities/event-device/service/event-device.service';

import { CounterEventUpdateComponent } from './counter-event-update.component';

describe('CounterEvent Management Update Component', () => {
  let comp: CounterEventUpdateComponent;
  let fixture: ComponentFixture<CounterEventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let counterEventFormService: CounterEventFormService;
  let counterEventService: CounterEventService;
  let eventDeviceService: EventDeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CounterEventUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CounterEventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CounterEventUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    counterEventFormService = TestBed.inject(CounterEventFormService);
    counterEventService = TestBed.inject(CounterEventService);
    eventDeviceService = TestBed.inject(EventDeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call EventDevice query and add missing value', () => {
      const counterEvent: ICounterEvent = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const eventDevice: IEventDevice = { id: 'b63e03bf-3576-4022-8bad-75db93751509' };
      counterEvent.eventDevice = eventDevice;

      const eventDeviceCollection: IEventDevice[] = [{ id: 'ea310d5b-74e5-4429-a5ce-4ab8462f71eb' }];
      jest.spyOn(eventDeviceService, 'query').mockReturnValue(of(new HttpResponse({ body: eventDeviceCollection })));
      const additionalEventDevices = [eventDevice];
      const expectedCollection: IEventDevice[] = [...additionalEventDevices, ...eventDeviceCollection];
      jest.spyOn(eventDeviceService, 'addEventDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ counterEvent });
      comp.ngOnInit();

      expect(eventDeviceService.query).toHaveBeenCalled();
      expect(eventDeviceService.addEventDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        eventDeviceCollection,
        ...additionalEventDevices.map(expect.objectContaining)
      );
      expect(comp.eventDevicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const counterEvent: ICounterEvent = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const eventDevice: IEventDevice = { id: '3b962c5b-3633-4292-b6a3-8b4c68246c4e' };
      counterEvent.eventDevice = eventDevice;

      activatedRoute.data = of({ counterEvent });
      comp.ngOnInit();

      expect(comp.eventDevicesSharedCollection).toContain(eventDevice);
      expect(comp.counterEvent).toEqual(counterEvent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounterEvent>>();
      const counterEvent = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(counterEventFormService, 'getCounterEvent').mockReturnValue(counterEvent);
      jest.spyOn(counterEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterEvent }));
      saveSubject.complete();

      // THEN
      expect(counterEventFormService.getCounterEvent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(counterEventService.update).toHaveBeenCalledWith(expect.objectContaining(counterEvent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounterEvent>>();
      const counterEvent = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(counterEventFormService, 'getCounterEvent').mockReturnValue({ id: null });
      jest.spyOn(counterEventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterEvent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterEvent }));
      saveSubject.complete();

      // THEN
      expect(counterEventFormService.getCounterEvent).toHaveBeenCalled();
      expect(counterEventService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounterEvent>>();
      const counterEvent = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(counterEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(counterEventService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEventDevice', () => {
      it('Should forward to eventDeviceService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(eventDeviceService, 'compareEventDevice');
        comp.compareEventDevice(entity, entity2);
        expect(eventDeviceService.compareEventDevice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
