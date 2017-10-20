package pl.cubesoft.smagabakery;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import pl.cubesoft.smagabakery.activity.MainActivity;


/**
 * Created by CUBESOFT on 16.01.2017.
 */


@Singleton
@Component(modules={AppModule.class})
public interface AppComponent extends AndroidInjector<SmagaBakeryApplication> {


    void inject(MainActivity mainActivity);
}
