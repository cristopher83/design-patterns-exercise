package oop.inheritance.terminal.verifone;

import oop.inheritance.terminal.Display;

public class VerifoneAdapter implements Display {
    private Verifone240Display verifone240Display ;

    public VerifoneAdapter(){
        this.verifone240Display = new Verifone240Display();
    }
    public  void showMessage(int x, int y, String msg){
        verifone240Display.print(x,y,msg);
    }
}
