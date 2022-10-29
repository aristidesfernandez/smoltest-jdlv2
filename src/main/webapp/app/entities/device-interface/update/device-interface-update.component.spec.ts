import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeviceInterfaceFormService } from './device-interface-form.service';
import { DeviceInterfaceService } from '../service/device-interface.service';
import { IDeviceInterface } from '../device-interface.model';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { InterfaceBoardService } from 'app/entities/interface-board/service/interface-board.service';

import { DeviceInterfaceUpdateComponent } from './device-interface-update.component';

describe('DeviceInterface Management Update Component', () => {
  let comp: DeviceInterfaceUpdateComponent;
  let fixture: ComponentFixture<DeviceInterfaceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceInterfaceFormService: DeviceInterfaceFormService;
  let deviceInterfaceService: DeviceInterfaceService;
  let deviceService: DeviceService;
  let interfaceBoardService: InterfaceBoardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeviceInterfaceUpdateComponent],
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
      .overrideTemplate(DeviceInterfaceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceInterfaceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceInterfaceFormService = TestBed.inject(DeviceInterfaceFormService);
    deviceInterfaceService = TestBed.inject(DeviceInterfaceService);
    deviceService = TestBed.inject(DeviceService);
    interfaceBoardService = TestBed.inject(InterfaceBoardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Device query and add missing value', () => {
      const deviceInterface: IDeviceInterface = { id: 456 };
      const device: IDevice = { id: '8e27f826-90d8-4bb5-b9bf-24e1bb3af0b4' };
      deviceInterface.device = device;

      const deviceCollection: IDevice[] = [{ id: 'a6a09925-02a6-4f36-a661-996c6970dbec' }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [device];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deviceInterface });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining)
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InterfaceBoard query and add missing value', () => {
      const deviceInterface: IDeviceInterface = { id: 456 };
      const interfaceBoard: IInterfaceBoard = { id: 69739 };
      deviceInterface.interfaceBoard = interfaceBoard;

      const interfaceBoardCollection: IInterfaceBoard[] = [{ id: 33706 }];
      jest.spyOn(interfaceBoardService, 'query').mockReturnValue(of(new HttpResponse({ body: interfaceBoardCollection })));
      const additionalInterfaceBoards = [interfaceBoard];
      const expectedCollection: IInterfaceBoard[] = [...additionalInterfaceBoards, ...interfaceBoardCollection];
      jest.spyOn(interfaceBoardService, 'addInterfaceBoardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deviceInterface });
      comp.ngOnInit();

      expect(interfaceBoardService.query).toHaveBeenCalled();
      expect(interfaceBoardService.addInterfaceBoardToCollectionIfMissing).toHaveBeenCalledWith(
        interfaceBoardCollection,
        ...additionalInterfaceBoards.map(expect.objectContaining)
      );
      expect(comp.interfaceBoardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deviceInterface: IDeviceInterface = { id: 456 };
      const device: IDevice = { id: 'd3688df8-1547-44fe-9502-6f7469c7ea5a' };
      deviceInterface.device = device;
      const interfaceBoard: IInterfaceBoard = { id: 40190 };
      deviceInterface.interfaceBoard = interfaceBoard;

      activatedRoute.data = of({ deviceInterface });
      comp.ngOnInit();

      expect(comp.devicesSharedCollection).toContain(device);
      expect(comp.interfaceBoardsSharedCollection).toContain(interfaceBoard);
      expect(comp.deviceInterface).toEqual(deviceInterface);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceInterface>>();
      const deviceInterface = { id: 123 };
      jest.spyOn(deviceInterfaceFormService, 'getDeviceInterface').mockReturnValue(deviceInterface);
      jest.spyOn(deviceInterfaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceInterface });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceInterface }));
      saveSubject.complete();

      // THEN
      expect(deviceInterfaceFormService.getDeviceInterface).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceInterfaceService.update).toHaveBeenCalledWith(expect.objectContaining(deviceInterface));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceInterface>>();
      const deviceInterface = { id: 123 };
      jest.spyOn(deviceInterfaceFormService, 'getDeviceInterface').mockReturnValue({ id: null });
      jest.spyOn(deviceInterfaceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceInterface: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceInterface }));
      saveSubject.complete();

      // THEN
      expect(deviceInterfaceFormService.getDeviceInterface).toHaveBeenCalled();
      expect(deviceInterfaceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceInterface>>();
      const deviceInterface = { id: 123 };
      jest.spyOn(deviceInterfaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceInterface });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceInterfaceService.update).toHaveBeenCalled();
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

    describe('compareInterfaceBoard', () => {
      it('Should forward to interfaceBoardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(interfaceBoardService, 'compareInterfaceBoard');
        comp.compareInterfaceBoard(entity, entity2);
        expect(interfaceBoardService.compareInterfaceBoard).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
