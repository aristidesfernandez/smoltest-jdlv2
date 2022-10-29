import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventDevice } from '../event-device.model';

@Component({
  selector: 'jhi-event-device-detail',
  templateUrl: './event-device-detail.component.html',
})
export class EventDeviceDetailComponent implements OnInit {
  eventDevice: IEventDevice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventDevice }) => {
      this.eventDevice = eventDevice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
