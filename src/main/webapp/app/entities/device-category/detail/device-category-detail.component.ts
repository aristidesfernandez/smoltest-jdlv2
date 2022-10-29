import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceCategory } from '../device-category.model';

@Component({
  selector: 'jhi-device-category-detail',
  templateUrl: './device-category-detail.component.html',
})
export class DeviceCategoryDetailComponent implements OnInit {
  deviceCategory: IDeviceCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceCategory }) => {
      this.deviceCategory = deviceCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
