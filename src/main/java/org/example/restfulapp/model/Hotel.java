package org.example.restfulapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String brand;
    private String description;
    @OneToOne
    @Cascade(CascadeType.ALL)
    private Contacts contacts;
    @OneToOne
    @Cascade(CascadeType.ALL)
    private Address address;
    @OneToOne
    @Cascade(CascadeType.ALL)
    private ArrivalTime arrivalTime;
    @ManyToMany
    @JoinTable(
            name = "HOTEL_AMENITIES",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @Cascade(CascadeType.ALL)
    private List<Amenity> amenities;
}
