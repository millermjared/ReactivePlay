package com.cluttereddesk.reactiveplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import rx.Observable;
import rx.functions.Action1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;


public class MainActivity extends Activity implements FlowControllable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startTheFlow(View theView) {
        Log.d(MainActivity.class.getSimpleName(), "started the flow");

        Observable<MethodInvocation> screenAObservable = IntentFactory.getInstance().launchIntent(this, ScreenAActivity.class);

        screenAObservable.subscribe(new Action1<MethodInvocation>() {
            @Override
            public void call(MethodInvocation invocation) {
                try {
                    invocation.target.invoke(MainActivity.this, invocation.inputs);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void handle(String outcome) {
        Log.d(MainActivity.class.getSimpleName(), outcome);
    }
}
