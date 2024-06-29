import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class StockMarket {
    private HashMap<String, Stock> stocks;

    public StockMarket() {
        stocks = new HashMap<>();
        stocks.put("AAPL", new Stock("AAPL", 150.0));
        stocks.put("GOOGL", new Stock("GOOGL", 2800.0));
        stocks.put("AMZN", new Stock("AMZN", 3400.0));
        stocks.put("MSFT", new Stock("MSFT", 290.0));
    }

    public void updatePrices() {
        Random random = new Random();
        for (Stock stock : stocks.values()) {
            double change = random.nextDouble() * 10 - 5;
            stock.setPrice(stock.getPrice() + change);
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void addStock(String symbol, double price) {
        stocks.put(symbol, new Stock(symbol, price));
        System.out.println("Added new stock: " + symbol + " at $" + price);
    }

    public void displayMarket() {
        System.out.println("Current Market Data:");
        for (Stock stock : stocks.values()) {
            System.out.printf("%s: $%.2f%n", stock.getSymbol(), stock.getPrice());
        }
    }
}

class Portfolio {
    private HashMap<String, Integer> holdings;
    private double cash;

    public Portfolio(double initialCash) {
        holdings = new HashMap<>();
        cash = initialCash;
    }

    public void buyStock(String symbol, int quantity, double price) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (cash < price * quantity) {
            System.out.println("Not enough cash to buy " + quantity + " shares of " + symbol);
            return;
        }

        holdings.put(symbol, holdings.containsKey(symbol) ? holdings.get(symbol) + quantity : quantity);
        cash -= price * quantity;
        System.out.println("Bought " + quantity + " shares of " + symbol);
    }

    public void sellStock(String symbol, int quantity, double price) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (holdings.getOrDefault(symbol, 0) < quantity) {
            System.out.println("Not enough shares to sell " + quantity + " shares of " + symbol);
            return;
        }

        holdings.put(symbol, holdings.get(symbol) - quantity);
        cash += price * quantity;
        System.out.println("Sold " + quantity + " shares of " + symbol);
    }

    public void displayPortfolio(StockMarket market) {
        System.out.println("Current Portfolio:");
        double totalValue = cash;
        for (String symbol : holdings.keySet()) {
            int quantity = holdings.get(symbol);
            double price = market.getStock(symbol).getPrice();
            double value = quantity * price;
            totalValue += value;
            System.out.printf("%s: %d shares @ $%.2f each = $%.2f%n", symbol, quantity, price, value);
        }
        System.out.printf("Cash: $%.2f%n", cash);
        System.out.printf("Total Portfolio Value: $%.2f%n", totalValue);
    }
}

public class TradingPlatform {
    public static void main(String[] args) {
        StockMarket market = new StockMarket();
        Portfolio portfolio = new Portfolio(10000.0);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Display Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. Display Portfolio");
            System.out.println("5. Update Market Prices");
            System.out.println("6. Add New Stock");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            
            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        market.displayMarket();
                        break;
                    case 2:
                        System.out.print("Enter stock symbol to buy: ");
                        String buySymbol = scanner.next().toUpperCase();
                        System.out.print("Enter quantity to buy: ");
                        int buyQuantity = scanner.nextInt();
                        Stock buyStock = market.getStock(buySymbol);
                        if (buyStock != null) {
                            portfolio.buyStock(buySymbol, buyQuantity, buyStock.getPrice());
                        } else {
                            System.out.println("Stock symbol not found.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter stock symbol to sell: ");
                        String sellSymbol = scanner.next().toUpperCase();
                        System.out.print("Enter quantity to sell: ");
                        int sellQuantity = scanner.nextInt();
                        Stock sellStock = market.getStock(sellSymbol);
                        if (sellStock != null) {
                            portfolio.sellStock(sellSymbol, sellQuantity, sellStock.getPrice());
                        } else {
                            System.out.println("Stock symbol not found.");
                        }
                        break;
                    case 4:
                        portfolio.displayPortfolio(market);
                        break;
                    case 5:
                        market.updatePrices();
                        System.out.println("Market prices updated.");
                        break;
                    case 6:
                        System.out.print("Enter new stock symbol: ");
                        String newSymbol = scanner.next().toUpperCase();
                        System.out.print("Enter initial price: ");
                        double newPrice = scanner.nextDouble();
                        market.addStock(newSymbol, newPrice);
                        break;
                    case 7:
                        scanner.close();
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }
    }
}
