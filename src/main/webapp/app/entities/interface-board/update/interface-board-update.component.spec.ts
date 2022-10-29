import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InterfaceBoardFormService } from './interface-board-form.service';
import { InterfaceBoardService } from '../service/interface-board.service';
import { IInterfaceBoard } from '../interface-board.model';

import { InterfaceBoardUpdateComponent } from './interface-board-update.component';

describe('InterfaceBoard Management Update Component', () => {
  let comp: InterfaceBoardUpdateComponent;
  let fixture: ComponentFixture<InterfaceBoardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interfaceBoardFormService: InterfaceBoardFormService;
  let interfaceBoardService: InterfaceBoardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InterfaceBoardUpdateComponent],
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
      .overrideTemplate(InterfaceBoardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterfaceBoardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interfaceBoardFormService = TestBed.inject(InterfaceBoardFormService);
    interfaceBoardService = TestBed.inject(InterfaceBoardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const interfaceBoard: IInterfaceBoard = { id: 456 };

      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      expect(comp.interfaceBoard).toEqual(interfaceBoard);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterfaceBoard>>();
      const interfaceBoard = { id: 123 };
      jest.spyOn(interfaceBoardFormService, 'getInterfaceBoard').mockReturnValue(interfaceBoard);
      jest.spyOn(interfaceBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interfaceBoard }));
      saveSubject.complete();

      // THEN
      expect(interfaceBoardFormService.getInterfaceBoard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(interfaceBoardService.update).toHaveBeenCalledWith(expect.objectContaining(interfaceBoard));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterfaceBoard>>();
      const interfaceBoard = { id: 123 };
      jest.spyOn(interfaceBoardFormService, 'getInterfaceBoard').mockReturnValue({ id: null });
      jest.spyOn(interfaceBoardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interfaceBoard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interfaceBoard }));
      saveSubject.complete();

      // THEN
      expect(interfaceBoardFormService.getInterfaceBoard).toHaveBeenCalled();
      expect(interfaceBoardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterfaceBoard>>();
      const interfaceBoard = { id: 123 };
      jest.spyOn(interfaceBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interfaceBoardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
