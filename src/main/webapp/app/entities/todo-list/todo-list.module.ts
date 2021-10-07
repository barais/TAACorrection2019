import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TODOListComponent } from './list/todo-list.component';
import { TODOListDetailComponent } from './detail/todo-list-detail.component';
import { TODOListUpdateComponent } from './update/todo-list-update.component';
import { TODOListDeleteDialogComponent } from './delete/todo-list-delete-dialog.component';
import { TODOListRoutingModule } from './route/todo-list-routing.module';

@NgModule({
  imports: [SharedModule, TODOListRoutingModule],
  declarations: [TODOListComponent, TODOListDetailComponent, TODOListUpdateComponent, TODOListDeleteDialogComponent],
  entryComponents: [TODOListDeleteDialogComponent],
})
export class TODOListModule {}
