import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StockDataService {
  private eventSource!: EventSource;

  constructor(private http: HttpClient) { }

  getStockData(symbol: string[]): Observable<any> {
    return new Observable(observer => {
      this.eventSource = new EventSource(`http://localhost:8080/ticker/stream/${symbol}`);
      this.eventSource.onmessage = event => {
        observer.next(JSON.parse(event.data));
      };
      this.eventSource.onerror = error => {
        observer.error(error);
      };

      return () => {
        this.eventSource.close();
      };
    });
  }

  getAllStocks(): Observable<string[]> {
    return this.http.get<any[]>('http://localhost:8080/stock/all').pipe(
      map(stocks => stocks.map(stock => stock.symbol))
    );
  }

  closeEventSource() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }
}