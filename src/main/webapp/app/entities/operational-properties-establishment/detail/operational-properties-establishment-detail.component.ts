import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperationalPropertiesEstablishment } from '../operational-properties-establishment.model';

@Component({
  selector: 'jhi-operational-properties-establishment-detail',
  templateUrl: './operational-properties-establishment-detail.component.html',
})
export class OperationalPropertiesEstablishmentDetailComponent implements OnInit {
  operationalPropertiesEstablishment: IOperationalPropertiesEstablishment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operationalPropertiesEstablishment }) => {
      this.operationalPropertiesEstablishment = operationalPropertiesEstablishment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
