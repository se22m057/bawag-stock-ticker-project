export interface StockData {
    name:string;
    symbol: string;
    price: number;
    stockTickAverage: {
      averageLast7: number;
      averageLast30: number;
    };
    stockChange?: 'increase' | 'decrease'; // Hinzugefügt für Animation
    change: number;
    showChangeFactor: boolean;
  }