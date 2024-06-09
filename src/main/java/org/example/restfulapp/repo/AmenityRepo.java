package org.example.restfulapp.repo;

import org.example.restfulapp.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AmenityRepo extends JpaRepository<Amenity, Integer> {
    Optional<Amenity> findByName(String name);
    List<Amenity> findAll();

}
