package edu.csi5230.ngoretski.assignment5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class GrowthChart extends AppCompatActivity {

    WebView webView;

    Button returnButton;

    TextView moneyTextView;

    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_chart);

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/chart.html");

        returnButton = (Button) findViewById(R.id.buttonReturn);

        moneyTextView = (TextView) findViewById(R.id.textViewCurrent);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        //doing this in both activities
        IntentFilter filter = new IntentFilter("edu.csi5230.ngoretski.assignment5");

        // if the game was already running for a time we need to draw the current state after letting the
        // webview load
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                System.out.println("MONEY:"+GameState.getListOfMoney());

                webView.loadUrl("javascript:clearData()");

                for (int i = 0; i < GameState.getListOfMoney().size(); i ++) {
                    if (i % 10 == 0) {
                        System.out.println ("writing on resume:"+i+","+GameState.getListOfMoney().get(i));
                        webView.loadUrl("javascript:addValue('"  + i + "'," + GameState.getListOfMoney().get(i) + ")");
                    }
                }
            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int money =  intent.getExtras().getInt("money");
                int time = intent.getExtras().getInt("time");

                GameState.setMoney(time, money);

                if (time %10 == 0 && webView != null) {
                    webView.loadUrl("javascript:addValue('" + time + "'," + money + ")");
                }

                if (moneyTextView != null) {
                    moneyTextView.setText(String.valueOf(money));
                }


                if (!GameState.running) {
                    webView.loadUrl("javascript:addValue('" + (GameState.getListOfMoney().size() - 1) + "'," + GameState.getListOfMoney().get(GameState.getListOfMoney().size()-1) + ")");
                }
            }
        };
        registerReceiver(receiver, filter);
    }
}
