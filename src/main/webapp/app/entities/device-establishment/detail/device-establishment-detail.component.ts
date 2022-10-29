import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceEstablishment } from '../device-establishment.model';

@Component({
  selector: 'jhi-device-establishment-detail',
  templateUrl: './device-establishment-detail.component.html',
})
export class DeviceEstablishmentDetailComponent implements OnInit {
  deviceEstablishment: IDeviceEstablishment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceEstablishment }) => {
      this.deviceEstablishment = deviceEstablishment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
