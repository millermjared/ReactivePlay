package com.cluttereddesk.reactiveplay;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Matt on 5/24/15.
 */
public class IntentFactory {

    private static IntentFactory mInstance;

    public Observable launchIntent(Context context, final Class target) {
        Intent i = new Intent(context, target);
        context.startActivity(i);
        return Observable.create(new Observable.OnSubscribe<MethodInvocation>() {

            @Override
            public void call(Subscriber<? super MethodInvocation> subscriber) {
                MethodInvocation mi = new MethodInvocation();
                Class[] args = {String.class};
                try {
                    Method m = target.getMethod("handle", args);
                    mi.target = m;
                } catch (NoSuchMethodException e) {
                    Log.e("IntentFactory", "no method found for handle", e);
                }
                String[] output = {"goodOutcome"};
                mi.inputs = output;

                subscriber.onNext(mi);
            }
        });
    }

    public static synchronized IntentFactory getInstance() {
        if (mInstance == null) {
            mInstance = new IntentFactory();
        }
        return mInstance;
    }
}
