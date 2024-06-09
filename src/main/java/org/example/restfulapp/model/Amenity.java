package org.example.restfulapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "amenities")
    private List<Hotel> hotels;

    @Override
    public String toString() {
        return name;
    }
}
