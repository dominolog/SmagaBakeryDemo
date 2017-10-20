package pl.cubesoft.smagabakery;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by CUBESOFT on 07.04.2017.
 */

public class SmagaBakeryApplication extends Application {

    private AppComponent appComponent;



    @Override
    public void onCreate () {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
