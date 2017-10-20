package pl.cubesoft.smagabakery;

import com.annimon.stream.Optional;

import java.util.List;

import pl.cubesoft.smagabakery.model.Person;
import rx.Observable;

/**
 * Created by CUBESOFT on 20.10.2017.
 */

public class PersonController {

    private final SmagaBakeryAPIService apiService;
    private Optional<List<Person>> persons = Optional.empty();

    PersonController(SmagaBakeryAPIService apiService) {
        this.apiService = apiService;
    }

    public Observable<List<Person>> getContacts(boolean force) {
        Observable<List<Person>> memory = persons.isPresent() ? Observable.just(persons.get()) : Observable.empty();
        Observable<List<Person>> network = apiService.getContacts()
                .map(result -> { persons = Optional.of(result.get(0).getItems());
                return result.get(0).getItems();});
        Observable<List<Person>> source = force ? network : Observable
                .concat(memory, /*disk,*/ network)
                .first();
        return source;

    }

    public void removeContact(int position) {
        persons.ifPresent(persons1 -> persons1.remove(position));
    }
}
