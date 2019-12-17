package com.example.socketiosample;


import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

public class CustomObserable extends Observable<Object> {

    private Socket socket;

    {
        try {
            socket = IO.socket("http://73bfd75d.ngrok.io");
        } catch (URISyntaxException e) {
        }
    }


    @Override
    protected void subscribeActual(Observer<? super Object> observer) {
        Listener listener = new Listener(socket, observer);
        observer.onSubscribe(listener);

        doSocketIoThing(observer);
        observer.onComplete();
        doSocketIoThing(observer);

    }

    private void doSocketIoThing(final Observer<? super Object> observer) {

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                SocketModel socketModel = new SocketModel();
                socketModel.setType(1);
                socketModel.setMessage("Connected....");
                observer.onNext(socketModel);
            }
        }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {

            @Override
            public void call(Object... arg0) {
                // TODO Auto-generated method stub
                SocketModel socketModel = new SocketModel();
                socketModel.setType(2);
                socketModel.setMessage(arg0[0].toString());
                observer.onNext(socketModel);

            }
        });

        // 2nd segment test without connecting to 1 long method
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                // TODO Auto-generated method stub
                observer.onNext(null);

            }
        });

        socket.on("newsget", new Emitter.Listener() {

            @Override
            public void call(final Object... arg0) {
                // TODO Auto-generated method stub
                SocketModel socketModel = new SocketModel();
                socketModel.setType(3);
                socketModel.setMessage(arg0[0].toString());
                observer.onNext(socketModel);

            }
        });

        socket.on("news", new Emitter.Listener() {

            @Override
            public void call(final Object... arg0) {
                // TODO Auto-generated method stub
                SocketModel socketModel = new SocketModel();
                socketModel.setType(4);
                socketModel.setMessage(arg0[0].toString());
                observer.onNext(socketModel);


            }
        });

        socket.connect();
    }

    static final class Listener extends MainThreadDisposable {
        private final Socket socket;

        Listener(Socket socket, Observer<? super Socket> observer) {
            this.socket = socket;

        }

        @Override
        protected void onDispose() {
            socket.disconnect();
        }
    }
}
