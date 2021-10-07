import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITODOList } from '../todo-list.model';

@Component({
  selector: 'jhi-todo-list-detail',
  templateUrl: './todo-list-detail.component.html',
})
export class TODOListDetailComponent implements OnInit {
  tODOList: ITODOList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tODOList }) => {
      this.tODOList = tODOList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
