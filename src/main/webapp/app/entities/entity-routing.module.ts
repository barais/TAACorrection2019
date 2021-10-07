import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'todo-list',
        data: { pageTitle: 'correctionTaaApp.tODOList.home.title' },
        loadChildren: () => import('./todo-list/todo-list.module').then(m => m.TODOListModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'correctionTaaApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'correctionTaaApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
