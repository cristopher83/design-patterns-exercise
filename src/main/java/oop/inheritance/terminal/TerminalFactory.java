package oop.inheritance.terminal;

import oop.inheritance.data.SupportedTerminal;
import oop.inheritance.terminal.ingenico.IngenicoTerminalFactory;

public abstract class TerminalFactory {
    public static TerminalFactory getFactory(SupportedTerminal ingenico){
        switch (SupportedTerminal){
            case INGENICO:
                return new IngenicoTerminalFactory();
            case VERIFONE:
                return new Verifone240TerminalFactory();
        }
        return null;
    }

    public abstract Display getDisplay();
    public abstract KeyBoard getKeyBoard();

}
