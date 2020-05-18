package com.example.rxjava2operator;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rxjava2operator.net.Api;
import com.example.rxjava2operator.net.IPApi;

// https://juejin.im/entry/57f4aaceda2f60004f73f041
// https://www.jianshu.com/p/ed082d5ce0a4
// https://www.jianshu.com/p/fc2e551b907c
// https://blog.csdn.net/THEONE10211024/article/details/50435325
// https://api.ipify.org/
// https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Defer.html
// https://www.jianshu.com/p/f54f32b39b7c
//RxJava的四个基本概念
//        　　Observable(被观察者)
//        　　Observer(观察者)
//        　　subscribe(订阅)
//            事件
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    TextView tvTips;
    TextView tvContent;
    Disposable disposable;
    String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTips = findViewById(R.id.tvTips);
        tvContent = findViewById(R.id.tvContent);

        // 简单的demo，展示api
        findViewById(R.id.btnDefault).setOnClickListener(v -> {
            tvContent.setText("");
            tvTips.setText("这是一个默认形式上的调用API接口");
            defaultValue().subscribe(getObserver());
        });

        // 创建操作符
        // Create,使用一个函数从头开始创建一个Observable
        findViewById(R.id.btnCreate).setOnClickListener(v -> {
            value = "Create";
            tvContent.setText("");
            tvTips.setText("使用一个函数从头创建一个Observable,一个形式正确的有限Observable必须尝试调用观察者的onCompleted正好一次或者它的onError正好一次，而且此后不能再调用观察者的任何其它方法。在传递给create方法的函数中检查观察者的isUnsubscribed状态，以便在没有观察者的时候，让你的Observable停止发射数据或者做昂贵的运算。");
            Observable<String> source = getSource(value);
            value = value + "修改值+1";
            source
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        });

        // Defer https://blog.csdn.net/weixin_39923324/article/details/88886313
        findViewById(R.id.btnDefer).setOnClickListener(v -> {
            value = "Defer";
            tvContent.setText("");
            tvTips.setText("使用一个函数从头创建一个Observable,一个形式正确的有限Observable必须尝试调用观察者的onCompleted正好一次或者它的onError正好一次，而且此后不能再调用观察者的任何其它方法。在传递给create方法的函数中检查观察者的isUnsubscribed状态，以便在没有观察者的时候，让你的Observable停止发射数据或者做昂贵的运算。");

            Observable<String> source = Observable.defer(() -> getSource(value));
            value = value + "修改值+1";
            source
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
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
                for (int i = 1; i < 3; i++) {
                    observableEmitter.onNext("线程名称:" + Thread.currentThread().getName() + "\n" + "onNext:" + i + "\n");
                }
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
