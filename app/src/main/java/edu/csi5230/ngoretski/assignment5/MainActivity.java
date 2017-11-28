package edu.csi5230.ngoretski.assignment5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button newGameButton;
    Button stopGameButton;
    Button viewGainButton;

    TextView stateView;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGameButton = (Button) findViewById(R.id.buttonStartGame);
        stopGameButton = (Button) findViewById(R.id.buttonStopGame);
        viewGainButton = (Button) findViewById(R.id.buttonShowGrowth);

        // handler is just used to set a timer on the thread
        final Handler handler = new Handler() {
        };

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GameState.running) {
                    return;
                }

                GameState.running = true;
                setGameStateText();

                // thread for the game
                new StrategyThread(handler, MainActivity.this.getApplicationContext()).start();
            }
        });

        stopGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GameState.running) {
                    return;
                }

                GameState.running = false;
                setGameStateText();
            }
        });

        viewGainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GameState.running) {
                    return;
                }

                Intent i = new Intent(MainActivity.this.getApplicationContext(), GrowthChart.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        stateView = (TextView) findViewById(R.id.textViewStatus);

        setGameStateText();
    }

    private void setGameStateText() {
        if (GameState.running) {
            stateView.setText("Game Running");
        }
        else {
            stateView.setText("Game not Running");
        }
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
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int money =  intent.getExtras().getInt("money");
                int time = intent.getExtras().getInt("time");

                GameState.setMoney(time, money);

                setGameStateText();
            }
        };
        registerReceiver(receiver, filter);

        setGameStateText();
    }

}
