package pl.cubesoft.smagabakery.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import pl.cubesoft.smagabakery.SmagaBakeryApplication;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by CUBESOFT on 20.10.2017.
 */

public class BaseActivity extends AppCompatActivity {

    final CompositeSubscription compositeSubscription = new CompositeSubscription();

    protected SmagaBakeryApplication getMyApplication() {
        return (SmagaBakeryApplication) getApplication();
    }




    protected void subscribe (Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected void unsubscribe (Subscription subscription) {
        compositeSubscription.remove(subscription);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        compositeSubscription.clear();
    }



    public void showSnackBar(int viewId, CharSequence text) {
        Snackbar.make(findViewById(viewId), text, LENGTH_SHORT).show();
    }
}
