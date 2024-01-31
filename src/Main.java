//Galiev Ilyas

import com.sun.source.tree.BreakTree;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Reading the expression
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        //Separating the line of expression and counting functions
        String[] symbols = line.split(" ");
        int stack_size = 0;
        for (String symbol : symbols) {
            if (new Operators(symbol).priority >= 0 || symbol.equals("min") || symbol.equals("max")){
                stack_size += 1;
            }
        }
        //Shuting yard algorithm for our expression
        StackOfOperations<String> operations = new StackOfOperations<>(stack_size);
        StackOfOperations<String> values = new StackOfOperations<>(symbols.length);
        for ( String symbol : symbols){
            if (isNumber(symbol)){
                values.push(symbol);
            } else if (symbol.equals("(")){
                operations.push(symbol);
            } else if (symbol.equals(")")){
                if (!operations.isEmpty()){
                    while (!operations.peek().equals("(")){
                        int x = calculus( values.pop(), values.pop(), operations.pop());
                        values.push(x+"");
                        if (operations.isEmpty()){
                            break;
                        }
                    }
                    operations.pop();
                    if (!operations.isEmpty()){
                        if (operations.peek().equals("max") || operations.peek().equals("min")){
                            int x = calculus( values.pop(), values.pop(), operations.pop());
                            values.push(x+"");
                        }
                    }
                }
            } else if (symbol.equals("max") || symbol.equals("min")){
                operations.push(symbol);
            } else if (new Operators(symbol).priority > 0){
                Operators current = new Operators(symbol);
                if (operations.isEmpty()){
                    operations.push(symbol);
                } else {
                    while (getPriority(operations.peek()) >= current.priority){
                        int x = calculus( values.pop(), values.pop(), operations.pop());
                        values.push(x+"");
                        if (operations.isEmpty()){
                            break;
                        }
                    }
                    operations.push(symbol);
                }
            } else if (symbol.equals(",")){
                if (!operations.isEmpty()){
                    while (!operations.peek().equals("(")){
                        int x = calculus( values.pop(), values.pop(), operations.pop());
                        values.push(x+"");
                        if (operations.isEmpty()){
                            break;
                        }
                    }
                }
            }
        }
        //Adding to output last operators from operation stack
        while (!operations.isEmpty()){
            int x = calculus( values.pop(), values.pop(), operations.pop());
            values.push(x+"");
        }
        System.out.println(values.pop());
    }
    /*
    Function that compares so string to numbers. No more than 0 - 9.
     */
    static boolean isNumber(String value){
        return switch (value) {
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> true;
            default -> false;
        };
    }
    /*
    Function gets value of priority of the symbol.
     */
    static int getPriority(String item){
        return new Operators(item).priority;
    }
    /*
    Calculation of the value of operator
     */
    static int calculus(String arg_1, String arg_2, String operator){
        int argument1 = Integer.parseInt(arg_1);
        int argument2 = Integer.parseInt(arg_2);
        switch (operator){
            case "+": {
                return argument1 + argument2;
            }
            case "-": {
                return argument2 - argument1;
            }
            case "*": {
                return argument1 * argument2;
            }
            case "/": {
                return argument2 / argument1;
            }
            case "min":
                if (argument1 > argument2){
                    return argument2;
                } else {
                    return argument1;
                }
            case "max":
                if (argument1 > argument2){
                    return argument1;
                } else {
                    return argument2;
                }
            default:
                return 0;
        }
    }
}
/*
Interface of stacks with it common behavior.
 */
interface Stack<T>{
    void push(T item);
    T pop();
    T peek();
    boolean isEmpty();
}
/*
Class for operators that describes their properties.
 */
class Operators{
    public String function;
    public int priority;

    public Operators(String function) {
        this.function = function;
        switch (function) {
            case "+", "-" -> this.priority = 1;
            case "*", "/" -> this.priority = 2;
            case "(", ")" -> this.priority = 0;
            default -> this.priority = -1;
        }
    }

    public String getFunction() {
        return function;
    }

    public int getPriority() {
        return priority;
    }
}
/*
Class for array with stack behavior. Needed only for operations array.
 */
class StackOfOperations<T> implements Stack{

    private T[] data;
    private int top;
    public int size;

    public StackOfOperations(int size) {
        data = (T[]) new Object[size];
        this.size = size;
        this.top = -1;
    }

    @Override
    public void push(Object item) {
        top += 1;
        this.data[top] = (T) item;
    }

    @Override
    public T pop() {
        T x = data[top];
        top -= 1;
        return x;
    }

    @Override
    public T peek() {
        return data[top];
    }

    @Override
    public boolean isEmpty() {
        return top < 0;
    }
}
