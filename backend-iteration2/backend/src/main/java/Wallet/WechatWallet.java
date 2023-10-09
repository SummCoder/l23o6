package Wallet;

import java.io.IOException;

public class WechatWallet extends Wallet{

    WechatWallet(double number) throws IOException {
        super(number, 7777);
    }

    public static void main(String[] args) throws IOException {
        WechatWallet wallet = new WechatWallet(100000);
        wallet.execute();
    }
}
