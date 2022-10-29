import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKeyOperatingProperty } from '../key-operating-property.model';

@Component({
  selector: 'jhi-key-operating-property-detail',
  templateUrl: './key-operating-property-detail.component.html',
})
export class KeyOperatingPropertyDetailComponent implements OnInit {
  keyOperatingProperty: IKeyOperatingProperty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keyOperatingProperty }) => {
      this.keyOperatingProperty = keyOperatingProperty;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
