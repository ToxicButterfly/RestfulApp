package org.example.restfulapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.restfulapp.model.Address;
import org.example.restfulapp.model.ArrivalTime;
import org.example.restfulapp.model.Contacts;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private Integer id;
    private String name;
    private String brand;
    private Address address;
    private Contacts contacts;
    private ArrivalTime arrivalTime;
    private List<String> amenities;
}
