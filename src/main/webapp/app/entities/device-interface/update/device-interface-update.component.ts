import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DeviceInterfaceFormService, DeviceInterfaceFormGroup } from './device-interface-form.service';
import { IDeviceInterface } from '../device-interface.model';
import { DeviceInterfaceService } from '../service/device-interface.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { InterfaceBoardService } from 'app/entities/interface-board/service/interface-board.service';
import { DeviceInterfaceStatus } from 'app/entities/enumerations/device-interface-status.model';

@Component({
  selector: 'jhi-device-interface-update',
  templateUrl: './device-interface-update.component.html',
})
export class DeviceInterfaceUpdateComponent implements OnInit {
  isSaving = false;
  deviceInterface: IDeviceInterface | null = null;
  deviceInterfaceStatusValues = Object.keys(DeviceInterfaceStatus);

  devicesSharedCollection: IDevice[] = [];
  interfaceBoardsSharedCollection: IInterfaceBoard[] = [];

  editForm: DeviceInterfaceFormGroup = this.deviceInterfaceFormService.createDeviceInterfaceFormGroup();

  constructor(
    protected deviceInterfaceService: DeviceInterfaceService,
    protected deviceInterfaceFormService: DeviceInterfaceFormService,
    protected deviceService: DeviceService,
    protected interfaceBoardService: InterfaceBoardService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  compareInterfaceBoard = (o1: IInterfaceBoard | null, o2: IInterfaceBoard | null): boolean =>
    this.interfaceBoardService.compareInterfaceBoard(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceInterface }) => {
      this.deviceInterface = deviceInterface;
      if (deviceInterface) {
        this.updateForm(deviceInterface);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceInterface = this.deviceInterfaceFormService.getDeviceInterface(this.editForm);
    if (deviceInterface.id !== null) {
      this.subscribeToSaveResponse(this.deviceInterfaceService.update(deviceInterface));
    } else {
      this.subscribeToSaveResponse(this.deviceInterfaceService.create(deviceInterface));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceInterface>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(deviceInterface: IDeviceInterface): void {
    this.deviceInterface = deviceInterface;
    this.deviceInterfaceFormService.resetForm(this.editForm, deviceInterface);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
      this.devicesSharedCollection,
      deviceInterface.device
    );
    this.interfaceBoardsSharedCollection = this.interfaceBoardService.addInterfaceBoardToCollectionIfMissing<IInterfaceBoard>(
      this.interfaceBoardsSharedCollection,
      deviceInterface.interfaceBoard
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.deviceInterface?.device)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));

    this.interfaceBoardService
      .query()
      .pipe(map((res: HttpResponse<IInterfaceBoard[]>) => res.body ?? []))
      .pipe(
        map((interfaceBoards: IInterfaceBoard[]) =>
          this.interfaceBoardService.addInterfaceBoardToCollectionIfMissing<IInterfaceBoard>(
            interfaceBoards,
            this.deviceInterface?.interfaceBoard
          )
        )
      )
      .subscribe((interfaceBoards: IInterfaceBoard[]) => (this.interfaceBoardsSharedCollection = interfaceBoards));
  }
}
