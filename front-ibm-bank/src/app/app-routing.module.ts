import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer/customer.component';
import { TransactionComponent } from './transaction/transaction.component';
import { CustomerDetailsComponent } from './customer-details/customer-details.component';

const routes: Routes = [
  {path: '', pathMatch:'full', redirectTo: 'customer'},
  {path: 'customer' , component: CustomerComponent},
  {path: 'transaction', component: TransactionComponent},
  { path: 'customers/:id', component: CustomerDetailsComponent },
  { path: '**', redirectTo: 'customer' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
