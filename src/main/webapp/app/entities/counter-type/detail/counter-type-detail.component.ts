import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICounterType } from '../counter-type.model';

@Component({
  selector: 'jhi-counter-type-detail',
  templateUrl: './counter-type-detail.component.html',
})
export class CounterTypeDetailComponent implements OnInit {
  counterType: ICounterType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterType }) => {
      this.counterType = counterType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
