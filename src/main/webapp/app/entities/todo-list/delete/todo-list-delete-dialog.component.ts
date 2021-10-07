import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITODOList } from '../todo-list.model';
import { TODOListService } from '../service/todo-list.service';

@Component({
  templateUrl: './todo-list-delete-dialog.component.html',
})
export class TODOListDeleteDialogComponent {
  tODOList?: ITODOList;

  constructor(protected tODOListService: TODOListService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tODOListService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
