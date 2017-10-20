package pl.cubesoft.smagabakery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 20.10.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @JsonProperty("firstNamE")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("avataR")
    String avatar;

    @JsonProperty("bDay")
    String dateBDay;

    @JsonProperty("description")
    String description;


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDateBDay() {
        return dateBDay;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", dateBDay='" + dateBDay + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
