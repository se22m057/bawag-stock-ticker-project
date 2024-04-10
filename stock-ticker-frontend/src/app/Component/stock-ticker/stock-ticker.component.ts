import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { StockDataService } from '../../Service/StockData/stock-data.service';
import { Subscription } from 'rxjs';
import { StockData } from '../../Model/StockData';

import { trigger, state, style, transition, animate } from '@angular/animations';

@Component({
  selector: 'app-stock-ticker',
  templateUrl: './stock-ticker.component.html',
  styleUrl: './stock-ticker.component.css',
  animations: [
    trigger('priceChange', [
      state('increase', style({
        backgroundColor: 'green',
        color: 'white'
      })),
      state('decrease', style({
        backgroundColor: 'red',
        color: 'white'
      })),
      transition('increase <=> decrease', animate('300ms ease-out')),
      transition('void => increase', animate('300ms ease-out')),
      transition('void => decrease', animate('300ms ease-out')),
    ])
  ]
})
export class StockTickerComponent implements OnInit, OnDestroy {
  symbols: string[] = [];
  stockChange: any;
  stockData: StockData[] = [];
  stockDataAverage: any;
  private subscription: Subscription = new Subscription;

  constructor(private stockDataService: StockDataService, private ngZone: NgZone) { }


  ngOnInit() {
    this.loadSymbolsAndSubscribeToStockData();
  }

  

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.stockDataService.closeEventSource();
  }

  loadSymbolsAndSubscribeToStockData() {//Abrufen aller Aktien vom Server
    this.stockDataService.getAllStocks().subscribe({
      next: (symbols) => {
        this.symbols = symbols;
        console.log(this.symbols);
  
        
        this.subscribeToStockData(this.symbols);
      },
      error: (error) => console.error('Es gab einen Fehler beim Laden der Aktien!', error)
    });
  }

  subscribeToStockData(symbols: string[]) {
    if (!symbols || symbols.length === 0) {
      console.error('Keine Symbole zum Abonnieren der StockDaten vorhanden');
      return;
    }
  
    this.subscription = this.stockDataService.getStockData(this.symbols).subscribe({
      next: (data) => {
        this.ngZone.run(() => { 
                // Überprüfen, ob das Object bereits existiert
          const existingStock = this.stockData.find(stock => stock.symbol === data.symbol);

          if (existingStock) {
            // Wenn das Symbol gefunden wurde,  nur notwendige Attribute anpassen
            existingStock.showChangeFactor = true;
            existingStock.change = existingStock.price ? (data.price - existingStock.price) : 0;
            existingStock.stockChange = data.price > existingStock.price ? 'increase' : 'decrease';
            existingStock.price = data.price;
            existingStock.stockTickAverage.averageLast7 = data.stockTickAverage.averageLast7;
            existingStock.stockTickAverage.averageLast30 = data.stockTickAverage.averageLast30;
            setTimeout(() => {//Animation nach 2,5 sec stoppen
              existingStock.stockChange = undefined;
              existingStock.showChangeFactor = false;
            }, 2500);
            

          } else {
            // Wenn das Symbol nicht gefunden wurde, neues Objekt der Liste hinzufügen
            this.stockData.push(data);
          }
  
        });
      },
      error: (err) => console.error(err),
      complete: () => console.log('Stream completed')
    });
  }

 
}