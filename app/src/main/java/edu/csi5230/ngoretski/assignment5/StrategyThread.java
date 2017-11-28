package edu.csi5230.ngoretski.assignment5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Random;

/**
 * Created by nathan on 27/11/17.
 */

public class StrategyThread extends Thread {

    private Handler handler;
    int money = 100;
    int time = 0;
    Context context;

    public StrategyThread(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;

        // initial money
        GameState.getListOfMoney().clear();
        GameState.setMoney(0, 100);
        time = 1;
    }

    @Override
    public void run() {
        super.run();

        int goodStockChange = (int) (new Random().nextDouble() * 100);
        int badStockChange = (int) (new Random().nextDouble() * 100);

        money = money + goodStockChange - badStockChange;

        System.out.println("money:"+money+" good:"+goodStockChange+" bad:"+badStockChange);

        if (money < 0) {
            money = 0;
        }

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("money", money);
        msg.setData(bundle);

        Intent i = new Intent();
        i.setAction("edu.csi5230.ngoretski.assignment5");
        i.putExtra("money", money);
        i.putExtra("time", time);
        context.sendBroadcast(i);

        handler.sendMessage(msg);

        time++;

        // games over if you get 0 money
        if (money ==0 ) {
            GameState.running = false;
        }

        // 1 second delay until running again
        if (GameState.running) {
            handler.postDelayed(this, 1000);
        }
    }

}
