import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DeviceFormService, DeviceFormGroup } from './device-form.service';
import { IDevice } from '../device.model';
import { DeviceService } from '../service/device.service';
import { IModel } from 'app/entities/model/model.model';
import { ModelService } from 'app/entities/model/service/model.service';
import { IDeviceCategory } from 'app/entities/device-category/device-category.model';
import { DeviceCategoryService } from 'app/entities/device-category/service/device-category.service';
import { IDeviceType } from 'app/entities/device-type/device-type.model';
import { DeviceTypeService } from 'app/entities/device-type/service/device-type.service';
import { IFormula } from 'app/entities/formula/formula.model';
import { FormulaService } from 'app/entities/formula/service/formula.service';

@Component({
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;
  device: IDevice | null = null;

  modelsSharedCollection: IModel[] = [];
  deviceCategoriesSharedCollection: IDeviceCategory[] = [];
  deviceTypesSharedCollection: IDeviceType[] = [];
  formulasSharedCollection: IFormula[] = [];

  editForm: DeviceFormGroup = this.deviceFormService.createDeviceFormGroup();

  constructor(
    protected deviceService: DeviceService,
    protected deviceFormService: DeviceFormService,
    protected modelService: ModelService,
    protected deviceCategoryService: DeviceCategoryService,
    protected deviceTypeService: DeviceTypeService,
    protected formulaService: FormulaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareModel = (o1: IModel | null, o2: IModel | null): boolean => this.modelService.compareModel(o1, o2);

  compareDeviceCategory = (o1: IDeviceCategory | null, o2: IDeviceCategory | null): boolean =>
    this.deviceCategoryService.compareDeviceCategory(o1, o2);

  compareDeviceType = (o1: IDeviceType | null, o2: IDeviceType | null): boolean => this.deviceTypeService.compareDeviceType(o1, o2);

  compareFormula = (o1: IFormula | null, o2: IFormula | null): boolean => this.formulaService.compareFormula(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.device = device;
      if (device) {
        this.updateForm(device);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const device = this.deviceFormService.getDevice(this.editForm);
    if (device.id !== null) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
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

  protected updateForm(device: IDevice): void {
    this.device = device;
    this.deviceFormService.resetForm(this.editForm, device);

    this.modelsSharedCollection = this.modelService.addModelToCollectionIfMissing<IModel>(this.modelsSharedCollection, device.model);
    this.deviceCategoriesSharedCollection = this.deviceCategoryService.addDeviceCategoryToCollectionIfMissing<IDeviceCategory>(
      this.deviceCategoriesSharedCollection,
      device.deviceCategory
    );
    this.deviceTypesSharedCollection = this.deviceTypeService.addDeviceTypeToCollectionIfMissing<IDeviceType>(
      this.deviceTypesSharedCollection,
      device.deviceType
    );
    this.formulasSharedCollection = this.formulaService.addFormulaToCollectionIfMissing<IFormula>(
      this.formulasSharedCollection,
      device.formulaHandpay,
      device.formulaJackpot
    );
  }

  protected loadRelationshipsOptions(): void {
    this.modelService
      .query()
      .pipe(map((res: HttpResponse<IModel[]>) => res.body ?? []))
      .pipe(map((models: IModel[]) => this.modelService.addModelToCollectionIfMissing<IModel>(models, this.device?.model)))
      .subscribe((models: IModel[]) => (this.modelsSharedCollection = models));

    this.deviceCategoryService
      .query()
      .pipe(map((res: HttpResponse<IDeviceCategory[]>) => res.body ?? []))
      .pipe(
        map((deviceCategories: IDeviceCategory[]) =>
          this.deviceCategoryService.addDeviceCategoryToCollectionIfMissing<IDeviceCategory>(deviceCategories, this.device?.deviceCategory)
        )
      )
      .subscribe((deviceCategories: IDeviceCategory[]) => (this.deviceCategoriesSharedCollection = deviceCategories));

    this.deviceTypeService
      .query()
      .pipe(map((res: HttpResponse<IDeviceType[]>) => res.body ?? []))
      .pipe(
        map((deviceTypes: IDeviceType[]) =>
          this.deviceTypeService.addDeviceTypeToCollectionIfMissing<IDeviceType>(deviceTypes, this.device?.deviceType)
        )
      )
      .subscribe((deviceTypes: IDeviceType[]) => (this.deviceTypesSharedCollection = deviceTypes));

    this.formulaService
      .query()
      .pipe(map((res: HttpResponse<IFormula[]>) => res.body ?? []))
      .pipe(
        map((formulas: IFormula[]) =>
          this.formulaService.addFormulaToCollectionIfMissing<IFormula>(formulas, this.device?.formulaHandpay, this.device?.formulaJackpot)
        )
      )
      .subscribe((formulas: IFormula[]) => (this.formulasSharedCollection = formulas));
  }
}
