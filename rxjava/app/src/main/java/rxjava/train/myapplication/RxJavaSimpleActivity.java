package rxjava.train.myapplication;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaSimpleActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int value = 0;

    final Observable<Integer> serverDownloadObservable = Observable.create(e -> {
        SystemClock.sleep(10000);
        e.onNext(5);
        e.onComplete();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_simple);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            button.setEnabled(false);
            Disposable disposable = serverDownloadObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(integer -> {
                        updateTheUserInterface(integer);
                        button.setEnabled(true);
                    });
            compositeDisposable.add(disposable);
        });
    }

    private void updateTheUserInterface(Integer integer) {
        TextView textView = findViewById(R.id.resultView);
        textView.setText(String.valueOf(integer));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    public void onClick(View view) {
        Toast.makeText(this, "Still active " + value++, Toast.LENGTH_SHORT).show();
    }
}
