package oop.inheritance;

import java.security.Key;
import java.time.LocalDateTime;

import oop.inheritance.data.CommunicationType;
import oop.inheritance.data.SupportedTerminal;
import oop.inheritance.terminal.Display;
import oop.inheritance.terminal.KeyBoard;
import oop.inheritance.terminal.TerminalFactory;
import oop.library.ingenico.model.Card;
import oop.library.ingenico.model.Transaction;
import oop.library.ingenico.model.TransactionResponse;
import oop.library.ingenico.services.*;
import oop.library.v240m.VerifoneV240mDisplay;


public class Application {

    private CommunicationType communicationType = CommunicationType.ETHERNET;
    private SupportedTerminal supportedTerminal;
    private TerminalFactory terminalFactory;

    public Application(TerminalFactory terminalFactory) {
        this.terminalFactory = terminalFactory;
    }

    public void showMenu() {
        Display display = terminalFactory.getDisplay();

         display.showMessage(5, 5, "MENU");
         display.showMessage(5, 10, "1. VENTA");
         display.showMessage(5, 13, "2. DEVOLUCION");
         display.showMessage(5, 16, "3. REPORTE");
         display.showMessage(5, 23, "4. CONFIGURACION");
    }


    public String readKey() {
        KeyBoard keyBoard = terminalFactory.getKeyBoard();
        return keyBoard.getChar();
    }

    public void doSale() {
        IngenicoCardSwipper cardSwipper = new IngenicoCardSwipper();
        IngenicoChipReader chipReader = new IngenicoChipReader();
        IngenicoDisplay ingenicoDisplay = new IngenicoDisplay();
        IngenicoKeyboard ingenicoKeyboard = new IngenicoKeyboard();
        Card card;

        //REFACTOR
        card = readingCard(cardSwipper, chipReader);

        ingenicoDisplay.clear();
        ingenicoDisplay.showMessage(5, 20, "Capture monto:");

        String amount = ingenicoKeyboard.readLine(); //Amount with decimal point as string

        Transaction transaction = new Transaction();

        transaction.setLocalDateTime(LocalDateTime.now());
        transaction.setCard(card);
        transaction.setAmountInCents(Integer.parseInt(amount.replace(".", "")));

        //REFACTOR
        transactionResponse(ingenicoDisplay, transaction);
    }

    private void transactionResponse(IngenicoDisplay ingenicoDisplay, Transaction transaction) {
        TransactionResponse response = sendSale(transaction);

        if (response.isApproved()) {
            ingenicoDisplay.showMessage(5, 25, "APROBADA");
            printReceipt(transaction, response.getHostReference());
        } else {
            ingenicoDisplay.showMessage(5, 25, "DENEGADA");
        }
    }

    //Refactor
    private Card readingCard(IngenicoCardSwipper cardSwipper, IngenicoChipReader chipReader) {
        Card card;
        do {
            card = cardSwipper.readCard();
            if (card == null) {
                card = chipReader.readCard();
            }
        } while (card == null);
        return card;
    }

    private void printReceipt(Transaction transaction, String hostReference) {
        IngenicoPrinter ingenicoPrinter = new IngenicoPrinter();
        Card card = transaction.getCard();

        ingenicoPrinter.print(5, "APROBADA");
        ingenicoPrinter.lineFeed();
        ingenicoPrinter.print(5, card.getAccount());
        ingenicoPrinter.lineFeed();
        ingenicoPrinter.print(5, "" + transaction.getAmountInCents());
        ingenicoPrinter.lineFeed();
        ingenicoPrinter.print(5, "________________");

    }

    private TransactionResponse sendSale(Transaction transaction) {
        IngenicoEthernet ethernet = new IngenicoEthernet();
        IngenicoModem modem = new IngenicoModem();
        IngenicoGPS gps = new IngenicoGPS();
        TransactionResponse transactionResponse = null;

        switch (communicationType) {
            case ETHERNET:
                ethernet.open();
                ethernet.send(transaction);
                transactionResponse = ethernet.receive();
                ethernet.close();
                break;
            case GPS:
                gps.open();
                gps.send(transaction);
                transactionResponse = gps.receive();
                gps.close();
                break;
            case MODEM:
                modem.open();
                modem.send(transaction);
                transactionResponse = modem.receive();
                modem.close();
                break;
        }

        return transactionResponse;
    }

    public void doRefund() {
    }

    public void printReport() {
    }

    public void showConfiguration() {
    }

    public void clearScreen() { //REFACTOR
        if (supportedTerminal == SupportedTerminal.INGENICO) {
            clearScreenIngenico(new IngenicoDisplay());
        } else {
            clearScreenVerifone(new VerfoneV24mDisplay());
        }
    }
    public void clearScreenIngenico(IngenicoDisplay ingenicoDisplay){
        ingenicoDisplay.clear();
    }
    public void clearScreenVerifone(VerifoneV24mDisplay verifoneV24mDisplay){
        verifoneV24mDisplay.clear();
    }

}
