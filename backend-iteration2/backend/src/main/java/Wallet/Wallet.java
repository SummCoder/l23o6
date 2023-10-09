package Wallet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.SneakyThrows;

public abstract class Wallet {
    private double balance;
    private final ServerSocket server;

    Wallet(double number, int port) throws IOException {
        balance = number;
        server = new ServerSocket(port);
    }

    public double getBalance(){
        return balance;
    }

    public void income(double in){
        balance += in;
    }

    public boolean outcome(double out){
        if(balance < out){
            return false;
        }
        balance -= out;
        return true;
    }

    public void execute() throws IOException {
        while (true){
            Socket socket = server.accept();
            Payment payment = new Payment(socket);
            payment.run();
        }

    }

    private class Payment implements Runnable {
        Socket socket;


        Payment(Socket socket){
            this.socket = socket;
        }

        @SneakyThrows
        @Override
        public void run() {
            System.out.println("Connected with " + socket.getRemoteSocketAddress() + '.');
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            String message = bufferedReader.readLine();
            double number = Double.parseDouble(message);
            System.out.println("    Payment accepted: " + number);

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            if(number >= 0){
                if(!outcome(number)){
                    writer.println("Failed");
                } else writer.println("Succeeded");
            } else {
                income(-number);
                writer.println("Succeeded");
            }
            writer.flush();
            System.out.println("    Now you have "+getBalance()+" yuan in your wallet.");
        }
    }

}
