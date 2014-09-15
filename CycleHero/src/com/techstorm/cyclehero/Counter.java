package com.techstorm.cyclehero;

public class Counter {

    private int count = 0;

    // add number to current count.
    public void increase(int number) {
        this.count += number;
    }

    // subtract number from current count.
    public void decrease(int number) {
        this.count -= number;
    }

    public void reset() {
    	this.count = 0;
    }
    
    // get current count.
    public int getValue() {
        return this.count;
    }
}