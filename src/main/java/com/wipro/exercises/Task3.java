/*
Java: Vending Machine

        Design a Vending machine that serves 3 drinks: Coke, Fanta, and Sprite

        Create a VendingMachine class that should implement the following methods.

        Here's the text without the HTML tags:

        1. **void registerDrink(int buttonNumber, Drink drink)** to register a type of drink with a particular buttonNumber of the vending machine. Here, Drink is an interface provided in the locked stub.

        2. **ServedDrinkSummary(int buttonNumber, int money)** to return the Drink that is served when buttonNumber is pressed, and an amount of money is entered by a customer.

        - The class contains two attributes.
        - **change** - the units of currency to return to the consumer.
        - **drink** - the drink requested.

        - There are three potential error conditions.
        - If the machine has run out of the drink, an OutOfStockException should be thrown with the message, "{drinkName} out of stock".
        - If the money entered by the user is not enough to buy the drink, an InsufficientMoneyException should be thrown.
        - If the drink is out of stock and the user has not entered enough money, throw an OutOfStockException with the previously mentioned message.

        - OutOfStockException, InsufficientMoneyException, and ServedDrinkSummary classes are provided in the locked stub.

        The locked stub validates the VendingMachine class by processing some requests

        Constraints

- 1 ≤ totalNumberOfRequests ≤ 10^4
        - 0 ≤ buttonNumber ≤ 2
        - 0 ≤ money ≤ 100
        - 0 ≤ quantity available of each drink ≤ 10^4
        - 0 ≤ price per unit of each drink ≤ 10^4
        - No two drinks are assigned the same buttonNumber.


        Input Format For Custom Testing

        Input from stdin will be processed as follows and passed to the appropriate function.

        The first line contains three integers that represent the quantity of Coke, Fanta, and Sprite in the vending machine respectively.

        The second line contains three integers that represent the price per unit of Coke, Fanta, and Sprite respectively.

        The third line contains three integers that represent the buttonNumbers assigned to Coke, Fanta, and Sprite respectively.

        The fourth line contains an integer, totalNumberOfRequests.

        The next totalNumberOfRequests lines contain the buttonNumber pressed and the amount of money inserted into the vending machine separated by a space.


        Sample Case 0

        Sample Input For Custom Testing

        STDIN Function
        ----- --------
        2 1 0 quantity Coke = 2, Fanta = 1, Sprite = 0
        5 10 15 price Coke = 5, Fanta = 10, Sprite = 15
        0 1 2 buttonNumbers Coke = 0, Fanta = 1, Sprite = 2
        4 totalNumberOfRequests = 4
        0 20 buttonNumber = 0, money = 20 for the first request
        2 20 buttonNumber = 2, money = 20 for the second request
        2 5 ...
        1 5

        Sample Output

        Vending machine set up
        Coke 15
        Sprite out of stock
        Sprite out of stock
        Insufficient money

        Explanation

        1. 0 20: A request to process a can of Coke and 20 units of money is provided to the vending machine. Coke is in stock and costs 5 units of money, so 15 units in change is returned to the consumer. The locked stub prints "Coke 15".

        2. 2 20: A request to process a can of Sprite and 20 units of money is provided to the vending machine. Sprite is out of stock, so an OutOfStockException is thrown, and the stub prints "Sprite out of stock".

        3. 2 5: A request to process a can of Sprite and 5 units of money is provided to the vending machine. Sprite is out of stock, and the amount of money is insufficient, so an OutOfStockException is thrown, and the stub prints, "Sprite is out of stock".

        4. 1 5: A request to process a can of Fanta and 5 units of money is provided. Since the price of a unit of Fanta is 10 and it is in stock, an InsufficientMoneyException is thrown, and the stub prints "Insufficient money".
*/
package com.wipro.exercises;

import java.io.*;
import java.util.*;

interface Drink{
    int getPrice();
    String getName();
    int getQuantityLeft();
    void serveDrink();
}

class ServeDrinkSummary{
    Drink servedDrink;
    int change;
}

class OutOfStockException extends Exception{
    public OutOfStockException(String message){
        super(message);
    }
}

class InsufficientMoneyException extends Exception{
    public InsufficientMoneyException(){
        super();
    }
}

class VendingMachine {
    private Map<Integer, Drink> buttonToDrinkMapping = new HashMap<>();
    private Set<Drink> servedDrinks = new HashSet<>();

    void registerDrink(int buttonIdx, Drink drink) {
        buttonToDrinkMapping.put(buttonIdx, drink);
    }

    ServeDrinkSummary dispatch(int buttonPressed, int money)
            throws OutOfStockException, InsufficientMoneyException {

        if (!buttonToDrinkMapping.containsKey(buttonPressed)) {
            throw new IllegalArgumentException("Invalid button pressed");
        }

        Drink drink = buttonToDrinkMapping.get(buttonPressed);

        if (drink.getQuantityLeft() == 0) {
            throw new OutOfStockException(drink.getName() + " out of stock");
        }

        if (money < drink.getPrice()) {
            throw new InsufficientMoneyException();
        }

        if (servedDrinks.contains(drink)) {
            throw new IllegalArgumentException("Drink already served.");
        }

        drink.serveDrink();
        servedDrinks.add(drink);

        ServeDrinkSummary summary = new ServeDrinkSummary();
        summary.servedDrink = drink;
        summary.change = money - drink.getPrice();

        // Resetting the set after serving the drink
        servedDrinks.clear();

        return summary;
    }
}


class Task3 {

    public static void main(String[] args) throws IOException{
        String arr[];
        int quantity[] = new int[3], pricePerUnit[] = new int[3], buttonAssigned[] = new int[3];

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        arr = br.readLine().split(" ");
        for(int i = 0; i < 3; i++){
            quantity[i] = Integer.parseInt(arr[i]);
        }

        arr = br.readLine().split(" ");
        for(int i = 0; i < 3; i++){
            pricePerUnit[i] = Integer.parseInt(arr[i]);
        }

        arr = br.readLine().split(" ");
        for(int i = 0; i < 3; i++){
            buttonAssigned[i] = Integer.parseInt(arr[i]);
        }

        VendingMachine vendingMachine = new VendingMachine();
        Drink coke = new Coke(pricePerUnit[0], "Coke", quantity[0]);
        Drink fanta = new Fanta(pricePerUnit[1], "Fanta", quantity[1]);
        Drink sprite = new Sprite(pricePerUnit[2], "Sprite", quantity[2]);
        vendingMachine.registerDrink(buttonAssigned[0], coke);
        vendingMachine.registerDrink(buttonAssigned[1], fanta);
        vendingMachine.registerDrink(buttonAssigned[2], sprite);
        out.println("Vending machine set up");

        int totalNumberOfRequests = Integer.parseInt(br.readLine().trim());
        while(totalNumberOfRequests-- > 0){
            arr = br.readLine().split(" ");
            int buttonPressed = Integer.parseInt(arr[0]),
                    money = Integer.parseInt(arr[1]);
            try{
                ServeDrinkSummary serveDrinkSummary = vendingMachine.dispatch(buttonPressed, money);
                serveDrinkSummary.servedDrink.serveDrink();
                out.println(serveDrinkSummary.servedDrink.getName() + " " + serveDrinkSummary.change);
            }catch(OutOfStockException e){
                out.println(e.getMessage());
            }catch(InsufficientMoneyException e){
                out.println("Insufficient money");
            }
        }

        out.flush();
        out.close();
    }
}

class Coke implements Drink{
    private int price;
    private String name;
    private int quantityLeft;

    public Coke(int price, String name, int quantityLeft){
        this.price = price;
        this.name = name;
        this.quantityLeft = quantityLeft;
    }

    public int getPrice(){
        return this.price;
    }

    public String getName(){
        return this.name;
    }

    public int getQuantityLeft(){
        return this.quantityLeft;
    }

    public void serveDrink(){
        if(this.quantityLeft > 0)
            this.quantityLeft -= 1;
    }
}

class Fanta implements Drink{
    private int price;
    private String name;
    private int quantityLeft;

    public Fanta(int price, String name, int quantityLeft){
        this.price = price;
        this.name = name;
        this.quantityLeft = quantityLeft;
    }

    public int getPrice(){
        return this.price;
    }

    public String getName(){
        return this.name;
    }

    public int getQuantityLeft(){
        return this.quantityLeft;
    }

    public void serveDrink(){
        if(this.quantityLeft > 0)
            this.quantityLeft -= 1;
    }
}

class Sprite implements Drink{
    private int price;
    private String name;
    private int quantityLeft;

    public Sprite(int price, String name, int quantityLeft){
        this.price = price;
        this.name = name;
        this.quantityLeft = quantityLeft;
    }

    public int getPrice(){
        return this.price;
    }

    public String getName(){
        return this.name;
    }

    public int getQuantityLeft(){
        return this.quantityLeft;
    }

    public void serveDrink(){
        if(this.quantityLeft > 0)
            this.quantityLeft -= 1;
    }
}