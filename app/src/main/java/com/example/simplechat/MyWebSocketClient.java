package com.example.simplechat;

import android.util.Log;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class MyWebSocketClient extends WebSocketClient {
    final static String TAG = "SimpleChat.MyWebSocketClient";
    TextView chatHistoryTextView = null;
    private boolean _isOpen = false;

    public MyWebSocketClient(URI serverUri, TextView view) {
        super(serverUri);
        this.chatHistoryTextView = view;
    }
    public boolean isOpen() {
        return this._isOpen;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d(TAG, "new connection opened");
        this._isOpen = true;
    }

    @Override
    public void onMessage(String message) {
        String history = this.chatHistoryTextView.getText().toString();
        String updatedHistory = new StringBuilder(message).append("\n")
                .append(history).toString();
        this.chatHistoryTextView.setText(updatedHistory);

        Log.i(TAG, "received message: " + message);
        Log.i(TAG, "received message: " + history);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i(TAG,"closed with exit code " + code + " additional info: " + reason);
        this._isOpen = false;

    }

    @Override
    public void onMessage(ByteBuffer message) {
        Log.i(TAG,"received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        //  Log.e(TAG,"an error occurred:" + ex);
        this._isOpen = false;
    }

}