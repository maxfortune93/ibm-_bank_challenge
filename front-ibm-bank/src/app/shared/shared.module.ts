import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { CustomMaterialPaginator } from './app-material/custom-material-paginator';


@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
  ],
  exports:[
  ],
  providers: [
    {
      provide: MatPaginatorIntl,
      useValue: CustomMaterialPaginator()
    }
  ],
})
export class SharedModule { }
