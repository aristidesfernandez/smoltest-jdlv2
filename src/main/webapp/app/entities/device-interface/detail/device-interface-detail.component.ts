import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceInterface } from '../device-interface.model';

@Component({
  selector: 'jhi-device-interface-detail',
  templateUrl: './device-interface-detail.component.html',
})
export class DeviceInterfaceDetailComponent implements OnInit {
  deviceInterface: IDeviceInterface | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceInterface }) => {
      this.deviceInterface = deviceInterface;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
