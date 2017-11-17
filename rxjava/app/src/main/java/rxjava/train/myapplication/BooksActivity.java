package rxjava.train.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BooksActivity extends AppCompatActivity {

    Disposable disposable;
    RecyclerView bookRecyclerView;
    ProgressBar progressBar;
    SimpleStringAdapter simpleStringAdapter;
    RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configLayout();
        createObservable();
    }

    private void createObservable() {
        Observable<List<String>> observable = Observable.fromCallable(() -> restClient.getFavoriteBooks());
        disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        displayBooks(strings);
                    }
                });
    }

    private void displayBooks(List<String> books) {
        simpleStringAdapter.setStrings(books);
        progressBar.setVisibility(View.GONE);
        bookRecyclerView.setVisibility(View.VISIBLE);
    }

    private void configLayout() {
        setContentView(R.layout.activity_books);
        progressBar = findViewById(R.id.loader);
        bookRecyclerView = findViewById(R.id.books_list);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        simpleStringAdapter = new SimpleStringAdapter(this);
        bookRecyclerView.setAdapter(simpleStringAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
