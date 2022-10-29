import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICounterEvent } from '../counter-event.model';

@Component({
  selector: 'jhi-counter-event-detail',
  templateUrl: './counter-event-detail.component.html',
})
export class CounterEventDetailComponent implements OnInit {
  counterEvent: ICounterEvent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterEvent }) => {
      this.counterEvent = counterEvent;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
