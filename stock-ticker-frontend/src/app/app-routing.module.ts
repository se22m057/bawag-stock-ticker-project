import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StockTickerComponent } from './Component/stock-ticker/stock-ticker.component';

const routes: Routes = [
  { path: 'stock-ticker', component: StockTickerComponent },
  // andere Routen können hier hinzugefügt werden
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
