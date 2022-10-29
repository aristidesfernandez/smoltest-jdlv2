import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MunicipalityFormService, MunicipalityFormGroup } from './municipality-form.service';
import { IMunicipality } from '../municipality.model';
import { MunicipalityService } from '../service/municipality.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  selector: 'jhi-municipality-update',
  templateUrl: './municipality-update.component.html',
})
export class MunicipalityUpdateComponent implements OnInit {
  isSaving = false;
  municipality: IMunicipality | null = null;

  provincesSharedCollection: IProvince[] = [];

  editForm: MunicipalityFormGroup = this.municipalityFormService.createMunicipalityFormGroup();

  constructor(
    protected municipalityService: MunicipalityService,
    protected municipalityFormService: MunicipalityFormService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProvince = (o1: IProvince | null, o2: IProvince | null): boolean => this.provinceService.compareProvince(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ municipality }) => {
      this.municipality = municipality;
      if (municipality) {
        this.updateForm(municipality);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const municipality = this.municipalityFormService.getMunicipality(this.editForm);
    if (municipality.id !== null) {
      this.subscribeToSaveResponse(this.municipalityService.update(municipality));
    } else {
      this.subscribeToSaveResponse(this.municipalityService.create(municipality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMunicipality>>): void {
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

  protected updateForm(municipality: IMunicipality): void {
    this.municipality = municipality;
    this.municipalityFormService.resetForm(this.editForm, municipality);

    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing<IProvince>(
      this.provincesSharedCollection,
      municipality.province
    );
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing<IProvince>(provinces, this.municipality?.province)
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }
}
