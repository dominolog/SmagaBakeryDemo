package pl.cubesoft.smagabakery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by CUBESOFT on 20.10.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetContactsResult {

    @JsonProperty("Category Name")
    String categoryName;

    @JsonProperty("items")
    List<Person> items;

    public String getCategoryName() {
        return categoryName;
    }

    public List<Person> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "GetContactsResult{" +
                "categoryName='" + categoryName + '\'' +
                ", items=" + items +
                '}';
    }
}
