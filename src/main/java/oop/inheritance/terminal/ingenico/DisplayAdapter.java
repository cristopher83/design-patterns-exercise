package oop.inheritance.terminal.ingenico;

import oop.inheritance.terminal.Display;

public class DisplayAdapter implements Display {
    private IngenicoDisplay ingenicoDisplay;

    public DisplayAdapter(){
        this.ingenicoDisplay = new IngenicoDisplay();
    }
    public  void showMessage(int x, int y, String msg){

    }
}
