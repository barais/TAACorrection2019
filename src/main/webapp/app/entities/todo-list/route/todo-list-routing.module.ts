import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TODOListComponent } from '../list/todo-list.component';
import { TODOListDetailComponent } from '../detail/todo-list-detail.component';
import { TODOListUpdateComponent } from '../update/todo-list-update.component';
import { TODOListRoutingResolveService } from './todo-list-routing-resolve.service';

const tODOListRoute: Routes = [
  {
    path: '',
    component: TODOListComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TODOListDetailComponent,
    resolve: {
      tODOList: TODOListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TODOListUpdateComponent,
    resolve: {
      tODOList: TODOListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TODOListUpdateComponent,
    resolve: {
      tODOList: TODOListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tODOListRoute)],
  exports: [RouterModule],
})
export class TODOListRoutingModule {}
