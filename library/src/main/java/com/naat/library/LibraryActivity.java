package com.naat.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryActivity extends AppCompatActivity {

    private static final String TAG = LibraryActivity.class.getSimpleName();
    private Disposable disposable;
    private Button startTimer, stopTimer;
    private TextView counterTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        startTimer = findViewById(R.id.buttonStart);
        stopTimer = findViewById(R.id.buttonStop);
        counterTv = findViewById(R.id.counterTv);

        counterTv.setText("0");
        startTimer.setOnClickListener(view -> longDurationTask());
        stopTimer.setOnClickListener(view -> {
            disposable.dispose();
            counterTv.setText("0");
            disposable = null;
        });
    }

    private synchronized void longDurationTask(){
        if (disposable != null) return;

        this.disposable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        Log.d(TAG, aLong + "");
                        counterTv.setText(aLong + "");
                    });
    }
}