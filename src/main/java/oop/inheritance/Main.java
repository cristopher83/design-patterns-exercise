package oop.inheritance;

import oop.inheritance.data.SupportedTerminal;
import oop.inheritance.terminal.TerminalFactory;

public class Main {

    public static void main(String[] args) {
        Application application = new Application(TerminalFactory.getFactory(SupportedTerminal.INGENICO));

        while (true) {
            run(application);
        }
    }

    public static void run(Application application) {
        application.clearScreen();
        application.showMenu();

        String key = application.readKey();

        switch (key) {
            case "1":
                application.doSale();
                break;
            case "2":
                application.doRefund();
                break;
            case "3":
                application.printReport();
                break;
            case "4":
                application.showConfiguration();
                break;
        }
    }

}
