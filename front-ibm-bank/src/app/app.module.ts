import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { AppMaterialModule } from './shared/app-material/app-material.module';
import { NavTemplateComponent } from './nav-template/nav-template.component';
import { CustomerComponent } from './customer/customer.component';
import { TransactionComponent } from './transaction/transaction.component';
import { CustomerRegisterComponent } from './customer-register/customer-register.component';
import { SharedModule } from './shared/shared.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CustomerDetailsComponent } from './customer-details/customer-details.component';
import { CoreModule } from './core/core.module';
import { HttpClientModule } from '@angular/common/http';
import { CustomTransactionItemComponent } from './shared/custom-transaction-item/custom-transaction-item.component';
import { SearchFilterComponent } from './shared/search-filter/search-filter.component';
import { NgxSkeletonLoaderModule } from 'ngx-skeleton-loader';
import { LoadingSkeletonComponent } from './shared/loading-skeleton/loading-skeleton.component';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';


@NgModule({
  declarations: [
    AppComponent,
    NavTemplateComponent,
    CustomerComponent,
    TransactionComponent,
    CustomerRegisterComponent,
    CustomerDetailsComponent,
    CustomTransactionItemComponent,
    SearchFilterComponent,
    LoadingSkeletonComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AppMaterialModule,
    SharedModule,
    ReactiveFormsModule,
    FormsModule,
    CoreModule,
    HttpClientModule,
    NgxSkeletonLoaderModule.forRoot({ animation: 'pulse', loadingText: 'This item is actually loading...' }),
    NgxMaskDirective,
    NgxMaskPipe
  ],
  providers: [
    provideAnimationsAsync(),
    provideNgxMask()
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }
