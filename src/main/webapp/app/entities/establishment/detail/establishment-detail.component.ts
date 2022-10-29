import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEstablishment } from '../establishment.model';

@Component({
  selector: 'jhi-establishment-detail',
  templateUrl: './establishment-detail.component.html',
})
export class EstablishmentDetailComponent implements OnInit {
  establishment: IEstablishment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ establishment }) => {
      this.establishment = establishment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
