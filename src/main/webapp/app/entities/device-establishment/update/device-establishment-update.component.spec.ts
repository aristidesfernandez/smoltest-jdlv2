import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeviceEstablishmentFormService } from './device-establishment-form.service';
import { DeviceEstablishmentService } from '../service/device-establishment.service';
import { IDeviceEstablishment } from '../device-establishment.model';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

import { DeviceEstablishmentUpdateComponent } from './device-establishment-update.component';

describe('DeviceEstablishment Management Update Component', () => {
  let comp: DeviceEstablishmentUpdateComponent;
  let fixture: ComponentFixture<DeviceEstablishmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceEstablishmentFormService: DeviceEstablishmentFormService;
  let deviceEstablishmentService: DeviceEstablishmentService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeviceEstablishmentUpdateComponent],
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
      .overrideTemplate(DeviceEstablishmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceEstablishmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceEstablishmentFormService = TestBed.inject(DeviceEstablishmentFormService);
    deviceEstablishmentService = TestBed.inject(DeviceEstablishmentService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Device query and add missing value', () => {
      const deviceEstablishment: IDeviceEstablishment = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const device: IDevice = { id: 'ce7d7639-4b25-4295-8300-7c8283c21952' };
      deviceEstablishment.device = device;

      const deviceCollection: IDevice[] = [{ id: '422784ea-dd60-4bb4-9be0-c8f19b69bbab' }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [device];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deviceEstablishment });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining)
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deviceEstablishment: IDeviceEstablishment = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const device: IDevice = { id: '5bf61268-032f-40a4-810f-f495361e62d0' };
      deviceEstablishment.device = device;

      activatedRoute.data = of({ deviceEstablishment });
      comp.ngOnInit();

      expect(comp.devicesSharedCollection).toContain(device);
      expect(comp.deviceEstablishment).toEqual(deviceEstablishment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceEstablishment>>();
      const deviceEstablishment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(deviceEstablishmentFormService, 'getDeviceEstablishment').mockReturnValue(deviceEstablishment);
      jest.spyOn(deviceEstablishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceEstablishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceEstablishment }));
      saveSubject.complete();

      // THEN
      expect(deviceEstablishmentFormService.getDeviceEstablishment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceEstablishmentService.update).toHaveBeenCalledWith(expect.objectContaining(deviceEstablishment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceEstablishment>>();
      const deviceEstablishment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(deviceEstablishmentFormService, 'getDeviceEstablishment').mockReturnValue({ id: null });
      jest.spyOn(deviceEstablishmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceEstablishment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceEstablishment }));
      saveSubject.complete();

      // THEN
      expect(deviceEstablishmentFormService.getDeviceEstablishment).toHaveBeenCalled();
      expect(deviceEstablishmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceEstablishment>>();
      const deviceEstablishment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(deviceEstablishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceEstablishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceEstablishmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDevice', () => {
      it('Should forward to deviceService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(deviceService, 'compareDevice');
        comp.compareDevice(entity, entity2);
        expect(deviceService.compareDevice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
