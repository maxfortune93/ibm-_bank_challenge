import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { CustomMaterialPaginator } from './app-material/custom-material-paginator';
import { NgxSkeletonLoaderModule } from 'ngx-skeleton-loader';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    NgxSkeletonLoaderModule
  ],
  exports:[],
  providers: [
    {
      provide: MatPaginatorIntl,
      useFactory: CustomMaterialPaginator
    }
  ],
})
export class SharedModule { }
