import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InterfaceBoardFormService, InterfaceBoardFormGroup } from './interface-board-form.service';
import { IInterfaceBoard } from '../interface-board.model';
import { InterfaceBoardService } from '../service/interface-board.service';

@Component({
  selector: 'jhi-interface-board-update',
  templateUrl: './interface-board-update.component.html',
})
export class InterfaceBoardUpdateComponent implements OnInit {
  isSaving = false;
  interfaceBoard: IInterfaceBoard | null = null;

  editForm: InterfaceBoardFormGroup = this.interfaceBoardFormService.createInterfaceBoardFormGroup();

  constructor(
    protected interfaceBoardService: InterfaceBoardService,
    protected interfaceBoardFormService: InterfaceBoardFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interfaceBoard }) => {
      this.interfaceBoard = interfaceBoard;
      if (interfaceBoard) {
        this.updateForm(interfaceBoard);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interfaceBoard = this.interfaceBoardFormService.getInterfaceBoard(this.editForm);
    if (interfaceBoard.id !== null) {
      this.subscribeToSaveResponse(this.interfaceBoardService.update(interfaceBoard));
    } else {
      this.subscribeToSaveResponse(this.interfaceBoardService.create(interfaceBoard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterfaceBoard>>): void {
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

  protected updateForm(interfaceBoard: IInterfaceBoard): void {
    this.interfaceBoard = interfaceBoard;
    this.interfaceBoardFormService.resetForm(this.editForm, interfaceBoard);
  }
}
