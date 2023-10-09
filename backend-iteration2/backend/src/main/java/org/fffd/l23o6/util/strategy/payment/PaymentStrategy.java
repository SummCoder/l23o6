package org.fffd.l23o6.util.strategy.payment;

import lombok.Cleanup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class PaymentStrategy {

    public abstract String getName();
    public abstract boolean pay(double number);

    public boolean pay(double number, int port){
        try {
            @Cleanup Socket socket = new Socket("127.0.0.1", port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(number);
            writer.flush();
            socket.shutdownOutput();

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String message = reader.readLine();
            return !message.equals("Failed");
        } catch (IOException e) {
            return false;
        }
    }

}
