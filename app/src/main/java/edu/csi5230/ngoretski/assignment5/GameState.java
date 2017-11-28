package edu.csi5230.ngoretski.assignment5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 27/11/17.
 *
 * tracking state
 */

public class GameState {

    public static boolean running = false;

    private static List<Integer> listOfMoney = new ArrayList<Integer>();

    public static synchronized void setMoney (int time, int money) {
        while (listOfMoney.size() <= time) {
            listOfMoney.add(null);
        }
        listOfMoney.set(time, money);
    }

    public static synchronized List<Integer> getListOfMoney() {
        return listOfMoney;
    }
}
