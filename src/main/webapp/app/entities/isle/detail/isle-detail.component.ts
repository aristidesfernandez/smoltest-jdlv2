import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsle } from '../isle.model';

@Component({
  selector: 'jhi-isle-detail',
  templateUrl: './isle-detail.component.html',
})
export class IsleDetailComponent implements OnInit {
  isle: IIsle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isle }) => {
      this.isle = isle;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
