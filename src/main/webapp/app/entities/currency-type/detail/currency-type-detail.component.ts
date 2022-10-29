import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICurrencyType } from '../currency-type.model';

@Component({
  selector: 'jhi-currency-type-detail',
  templateUrl: './currency-type-detail.component.html',
})
export class CurrencyTypeDetailComponent implements OnInit {
  currencyType: ICurrencyType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currencyType }) => {
      this.currencyType = currencyType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
