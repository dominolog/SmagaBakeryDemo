package pl.cubesoft.smagabakery;

import android.app.Application;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class AppModule {

    Application application;



    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }




    @Provides
    @Singleton
    public SmagaBakeryAPIService provideSmagaBakeryAPIService(Retrofit retrofit) {
        return retrofit.create(SmagaBakeryAPIService.class);
    }

    @Provides
    @Singleton
    public PersonController providePersonController(SmagaBakeryAPIService apiService) {
        return new PersonController(apiService);
    }


    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient httpClient) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BuildConfig.WEB_SERVICE_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return retrofitBuilder.build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient() {


        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        return new OkHttpClient.Builder()
                .followRedirects(true)
                .addInterceptor(interceptor)

                .build();

    }

    @Provides
    @Singleton
    public ImageLoader provideImageLoader(Application context, OkHttpClient okHttpClient) {
        return new ImageLoaderPicasso(context, okHttpClient);
    }


    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }




}
