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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import kotlin.text.Charsets;
import moe.codeest.rxsocketclient.RxSocketClient;
import moe.codeest.rxsocketclient.SocketClient;
import moe.codeest.rxsocketclient.SocketSubscriber;
import moe.codeest.rxsocketclient.meta.SocketConfig;
import moe.codeest.rxsocketclient.meta.SocketOption;
import moe.codeest.rxsocketclient.meta.ThreadStrategy;

public class MainActivity extends AppCompatActivity {
    int i = 0;
    final String TAG = "SOCKET CONNECTION";
    TextView send, rec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectView();
        Log.d("socketio---->", "start");
        customSocket();
    }

    private void customSocket() {
        conenct().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                Log.d("Connection Type", ((SocketModel) o).getType() + "");
                Log.d("Connection Message ", ((SocketModel) o).getMessage());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Connection ", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
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
        // socket.emit("news", message, socket.id());
    }

    public static Observable<Object> conenct() {
        return new CustomObserable();
    }
}
