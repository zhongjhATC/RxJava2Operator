package com.example.rxjava2operator.net.optional;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class RxTransformer {

    public static <T> ObservableTransformer<T, Optional<T>> handle_result() {
        return upstream -> upstream
                .flatMap(new Function<T, ObservableSource<Optional<T>>>() {
                             @Override
                             public ObservableSource<Optional<T>> apply(@NonNull T result) throws Exception {
                                 return createHttpData(new Optional(result));
                             }
                         }
                );
    }

    public static <T> Observable<Optional<T>> createHttpData(Optional<T> t) {

        return Observable.create(e -> {
            try {
                e.onNext(t);
                e.onComplete();
            } catch (Exception exc) {
                e.onError(exc);
            }
        });
    }

}
