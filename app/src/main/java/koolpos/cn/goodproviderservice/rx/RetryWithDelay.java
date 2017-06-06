package koolpos.cn.goodproviderservice.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by caroline on 2017/6/6.
 */
public class RetryWithDelay implements Function<Observable<Throwable>, ObservableSource<?>> {

    private final int maxRetries;
    private int retryDelayMillis;
    private int retryCount;

    public static RetryWithDelay crate(){
        return new RetryWithDelay(5,1);
    }

    private RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }


    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> attempts) throws Exception {
        return attempts
                .flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                        if (++retryCount <= maxRetries) {
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            retryDelayMillis=retryDelayMillis*retryCount;
                            Loger.e("get error, it will try after " + retryDelayMillis
                                    + " millisecond, retry count " + retryCount);
                            return Observable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }
                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }

                });
    }
}