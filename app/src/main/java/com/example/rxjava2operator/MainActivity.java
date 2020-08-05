package com.example.rxjava2operator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rxjava2operator.net.Api;
import com.example.rxjava2operator.net.IPApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// https://juejin.im/entry/57f4aaceda2f60004f73f041
// https://www.jianshu.com/p/ed082d5ce0a4
// https://www.jianshu.com/p/fc2e551b907c
// https://blog.csdn.net/THEONE10211024/article/details/50435325
// ip: https://api.ipify.org/
// 大纲：https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Defer.html
// https://www.jianshu.com/p/f54f32b39b7c
// 项目中常用的：https://juejin.im/post/5c2d960af265da61285a3cfc
//RxJava的四个基本概念
//        　　Observable(被观察者)
//        　　Observer(观察者)
//        　　subscribe(订阅)
//            事件
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    TextView tvTips;
    TextView tvContent;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTips = findViewById(R.id.tvTips);
        tvContent = findViewById(R.id.tvContent);

        findViewById(R.id.btnClear).setOnClickListener(v -> tvContent.setText(""));

        // 简单的demo，展示api
        findViewById(R.id.btnDefault).setOnClickListener(v -> {
            tvContent.setText("");
            tvTips.setText("这是一个默认形式上的调用API接口");
            defaultValue().subscribe(getObserver());
        });

        // 创建操作符
        // Create,使用一个函数从头开始创建一个Observable
        findViewById(R.id.btnCreate).setOnClickListener(v -> {
            tvContent.setText("");
            tvTips.setText("https://www.jianshu.com/p/3b37d98d60ab");
            Observable<String> source = getSource("Create");
            // 这个修改值不起变化，从创建Observable起，就已经确定传了此值
            source.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });

        // Defer https://blog.csdn.net/weixin_39923324/article/details/88886313
        findViewById(R.id.btnDefer).setOnClickListener(v -> {
            tvContent.setText("");
            tvTips.setText("使用一个函数从头创建一个Observable,一个形式正确的有限Observable必须尝试调用观察者的onCompleted正好一次或者它的onError正好一次，而且此后不能再调用观察者的任何其它方法。在传递给create方法的函数中检查观察者的isUnsubscribed状态，以便在没有观察者的时候，让你的Observable停止发射数据或者做昂贵的运算。");

            Observable<String> source = Observable.defer(() -> getSource("Defer"));
            source
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });

        // 将一个或多个对象转换成发射这个或这些对象的一个Observable
        findViewById(R.id.btnJust).setOnClickListener(v -> {
            Observable<String> value = Observable.just("1", "2", "2", "3", "4", "5"); // 可以发现，返回的是一个Observable
            value.subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    tvTips.setText("");
                }

                @Override
                public void onNext(String s) {
                    // 返回一个按参数列表顺序发射这些数据的Observable
                    tvTips.setText(s);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
        });

        // 循环请求Api
        findViewById(R.id.btnInterval).setOnClickListener(v -> intervalGetApi());

        // single
        // https://dieyidezui.com/deep-into-rxjava2-scheduler/
        // https://www.jianshu.com/p/4f4ee35b8473
        // https://www.jianshu.com/p/b037dbae9d8f
//        Schedulers.computation()
//        计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在computation() 中，否则 I/O 操作的等待时间会浪费 CPU
//        由边界线程池支持，其大小高达可用处理器的数量。它用于计算或 CPU 密集型工作，如调整图像大小、处理大型数据集等。请注意：当您分配的计算线程数超过可用内核时，由于上下文切换和线程创建开销，性能将下降，因为线程会争夺处理器的时间。
//        Schedulers.io()	代表适用于io操作的调度器，增长或缩减来自适应的线程池，通常用于网络、读写文件等io密集型的操作。重点需要注意的是线程池是无限制的，大量的I/O调度操作将创建许多个线程并占用内存。
//        Schedulers.trampoline()	在某个调用 schedule 的线程执行
//        Schedulers.newThread()	每个 Worker 对应一个新线程
//        Schedulers.single()	所有 Worker 使用同一个线程执行任务
//        Schedulers.from(Executor)	使用 Executor 作为任务执行的线程
        // https://www.tutorialspoint.com/rxjava/rxjava_computation_scheduler.htm
        findViewById(R.id.btnComputation).setOnClickListener(v -> {
            tvContent.setText("");
            Observable<String> source = getSource("Schedulers.computation()");
            source.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });

        findViewById(R.id.btnIo).setOnClickListener(v -> {
            tvContent.setText("");
            Observable<String> source = getSource("Schedulers.io()");
            source.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });

        findViewById(R.id.btnTrampoline).setOnClickListener(v -> {
            tvContent.setText("");
            Observable<String> source = Observable.create(observableEmitter -> {
                if (!observableEmitter.isDisposed()) {
                    Thread.sleep(3000);
                    observableEmitter.onNext("Trampoline - 3秒后执行第一个\n");
                    observableEmitter.onComplete();
                }
            });
            source.subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
            Observable<String> source2 = Observable.create(observableEmitter -> {
                if (!observableEmitter.isDisposed()) {
                    observableEmitter.onNext("Trampoline - 立即执行第二个\n");
                    observableEmitter.onComplete();
                }
            });
            source2.subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });

        findViewById(R.id.btnNewThread).setOnClickListener(v -> {
            tvContent.setText("");
            Observable<String> source = getSource("Schedulers.newThread()");
            source.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });


        findViewById(R.id.btnSingle).setOnClickListener(v -> {
            tvContent.setText("");
            Observable<String> source = getSource("Schedulers.single()");
            source.subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });

        findViewById(R.id.btnFromExecutor).setOnClickListener(v -> {
            // 众多例子可以看这个 https://vimsky.com/examples/detail/java-method-io.reactivex.schedulers.Schedulers.from.html
            tvContent.setText("");
            // 创建固定大小的线程池
            ExecutorService executor = Executors.newFixedThreadPool(3);
            Scheduler pooledScheduler = Schedulers.from(executor);
            Observable<String> source = getSource("Schedulers.from(xxx)");
            source.subscribeOn(pooledScheduler)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });


        // 跳转别的窗体
        findViewById(R.id.btnTo).setOnClickListener(v -> MainActivity.this.startActivity(new Intent(MainActivity.this, ToActivity.class)));

//        /*
//         * 通过merge（）合并事件 & 同时发送事件
//         **/
//        Observable.merge(defaultValue(), defaultValue())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(String value) {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    // 接收合并事件后，统一展示
//                    @Override
//                    public void onComplete() {
//                    }
//                });

    }

    /**
     * 循环请求Api
     */
    private void intervalGetApi() {
        Observable.interval(0, 1, TimeUnit.SECONDS)// 0延迟，每隔一秒发送数据
                .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                .flatMap((Function<Long, ObservableSource<String>>) aLong -> {
                    // 上传数据
                    return Api.getInstance().retrofit.create(IPApi.class).GetIP();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("test", "test " + o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("test", "test " + e.getMessage());
                        intervalGetApi();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<String> defaultValue() {
        IPApi ipApi = Api.getInstance().retrofit.create(IPApi.class);
        return ipApi.GetIP().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建被观察者
     *
     * @return ObservableSource
     */
    private Observable<String> getSource(String value) {
        return Observable.create(observableEmitter -> {
            if (!observableEmitter.isDisposed()) {
                for (int i = 0; i < 2; i++) {
                    observableEmitter.onNext("线程名称:" + Thread.currentThread().getName() + "\n" + "onNext:" + i + "\n");
                }
                Thread.sleep(1000);
                observableEmitter.onNext("线程名称:" + Thread.currentThread().getName() + "\n" + "onNext:" + value + "\n");
                observableEmitter.onComplete();
            }
        });
    }

    /**
     * 创建观察者
     *
     * @return Observer
     */
    private Observer<? super String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
                tvContent.append("onSubscribe\n");
            }

            @Override
            public void onNext(String string) {
                System.out.println("接收----->" + string);
                tvContent.append(string);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }

        };
    }


}
