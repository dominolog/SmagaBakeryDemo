package pl.cubesoft.smagabakery;

import java.util.List;

import pl.cubesoft.smagabakery.model.GetContactsResult;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by CUBESOFT on 20.10.2017.
 */

public interface SmagaBakeryAPIService {

    @GET("zadanie.json")
    Observable<List<GetContactsResult>> getContacts();
}
