package Wallet;

import java.io.IOException;

public class AlipayWallet extends Wallet {

    AlipayWallet(double number) throws IOException {
        super(number, 6666);
    }

    public static void main(String[] args) throws IOException {
        AlipayWallet wallet = new AlipayWallet(1000);
        wallet.execute();
    }

}
