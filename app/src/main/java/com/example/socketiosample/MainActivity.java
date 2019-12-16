package com.example.socketiosample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    int i = 0;
    TextView send, rec;
    private Socket socket;

    {
        try {
            socket = IO.socket("http://e236eaed.ngrok.io");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectView();
        Log.d("socketio---->", "start");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d("socketio---->", "socket connected");
                socket.emit("chat message",
                        "even connect: message sent from android to socketio server");
                // socket.disconnect(); // why is there a disconnect here?
            }
        }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {

            @Override
            public void call(Object... arg0) {
                // TODO Auto-generated method stub
                rec.setText(arg0[0].toString());
                Log.d("socketio---->", "socket event message" + arg0);

            }
        });

        // 2nd segment test without connecting to 1 long method
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                // TODO Auto-generated method stub
                Log.d("socketio---->", "socket event connect error");

            }
        });

        socket.on("newsget", new Emitter.Listener() {

            @Override
            public void call(final Object... arg0) {
                // TODO Auto-generated method stub
                Log.d("socketio---->", "socket event message" + arg0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        rec.setText(arg0[0].toString());
                        // add the message to view

                    }
                });
            }
        });

        socket.on("news", new Emitter.Listener() {

            @Override
            public void call(final Object... arg0) {
                // TODO Auto-generated method stub
                Log.d("socketio---->", "socket event news" + arg0[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        rec.setText(arg0[0].toString());
                        // add the message to view

                    }
                });
                socket.emit("news",
                        arg0[0].toString());
            }
        });

        socket.connect();
    }


    private void connectView() {
        send = (TextView) findViewById(R.id.textView3);
        rec = (TextView) findViewById(R.id.textView4);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend("Socket Request :-" + i++);

            }
        });
    }

    private void attemptSend(String send) {
        String message = send;
        if (TextUtils.isEmpty(send)) {
            return;
        }
        this.send.setText(send);
        socket.emit("news", message,socket.id());
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }
                    rec.setText(message);
                    // add the message to view

                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        socket.disconnect();
        //socket.off("news", onNewMessage);
    }
}
