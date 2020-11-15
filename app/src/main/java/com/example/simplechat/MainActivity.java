package com.example.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.net.URISyntaxException;
import com.example.simplechat.MyWebSocketClient;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    MyWebSocketClient wsClient = null;
    final static String TAG = "SimpleChat";
    TextView chatHistoryTextView = null;
    Button sendButton = null;
    EditText usernameEditText = null;
    EditText chatMessageEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Find related components
         */
        chatHistoryTextView = (TextView) findViewById(R.id.chat_history);
        sendButton = (Button) findViewById(R.id.send);
        usernameEditText   = (EditText)findViewById(R.id.username);
        chatMessageEditText   = (EditText)findViewById(R.id.chat_message);

        connectToServer();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i(TAG, "Click send button");
//                Log.i(TAG, usernameEditText.getText().toString());
//                Log.i(TAG, chatMessageEditText.getText().toString());
                String message = new StringBuilder(usernameEditText.getText()
                        .toString()).append(":")
                        .append(chatMessageEditText.getText().toString()).toString();

                sendMessage(message);

                if (wsClient != null && wsClient.isOpen()) {
                    String history = new StringBuilder(message).append("\n")
                            .append(chatHistoryTextView.getText().toString()).toString();
                    chatHistoryTextView.setText(history);
                } else {
                    String history = new StringBuilder("Web socket server is down ...").append("\n")
                            .append(chatHistoryTextView.getText().toString()).toString();
                    chatHistoryTextView.setText(history);
                }
            }
        });
    }



    public void connectToServer() {
        try {
            wsClient = new MyWebSocketClient(new URI("ws://172.25.3.82:8080"), chatHistoryTextView);
            wsClient.connectBlocking();
            Log.i(TAG, "Websocket connected");
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        /**
         * Retry to connect to server
         */
        if (wsClient == null || ! wsClient.isOpen()) {
            connectToServer();
        }
        if (wsClient != null && wsClient.isOpen()) {
            wsClient.send(message);
        }
    }

}