package org.example.restfulapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortHotelDTO {
    private Integer id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
