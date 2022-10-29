import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICounterDevice } from '../counter-device.model';

@Component({
  selector: 'jhi-counter-device-detail',
  templateUrl: './counter-device-detail.component.html',
})
export class CounterDeviceDetailComponent implements OnInit {
  counterDevice: ICounterDevice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterDevice }) => {
      this.counterDevice = counterDevice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
