package org.example.restfulapp.repo;

import org.example.restfulapp.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface HotelRepo extends JpaRepository<Hotel, Integer> {
    @Query("SELECT DISTINCT h.brand, COUNT(h) FROM Hotel h GROUP BY h.brand ORDER BY h.brand ASC")
    List<Object[]> getHotelCountByBrand();

    @Query("SELECT DISTINCT a.city, COUNT(h) FROM Hotel h JOIN h.address a GROUP BY a.city")
    List<Object[]> getHotelCountByCity();

    @Query("SELECT DISTINCT a.county, COUNT(h) FROM Hotel h JOIN h.address a GROUP BY a.county")
    List<Object[]> getHotelCountByCounty();

}
