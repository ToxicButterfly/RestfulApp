package org.example.restfulapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.restfulapp.dto.HotelDTO;
import org.example.restfulapp.dto.ShortHotelDTO;
import org.example.restfulapp.model.Hotel;
import org.example.restfulapp.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("property-view")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("hotels")
    public ResponseEntity<List<ShortHotelDTO>> getHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("hotels/{id}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable Integer id) {
        return ResponseEntity.ok(hotelService.getHotelInfo(id));
    }

    @GetMapping("search")
    public ResponseEntity<List<ShortHotelDTO>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String county,
            @RequestParam(required = false) List<String> amenities
    ) {
        Map<String, Object> map = new HashMap<>();
        if (name != null) {
            map.put("name", name);
        }
        if (brand != null) {
            map.put("brand", brand);
        }
        if (city != null) {
            map.put("city", city);
        }
        if (county != null) {
            map.put("county", county);
        }
        if (amenities != null && !amenities.isEmpty()) {
            map.put("amenities", amenities);
        }
        return ResponseEntity.ok(hotelService.searchByParams(map));
    }

    @PostMapping(value = "hotels")
    public ResponseEntity<ShortHotelDTO> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.createHotel(hotel));
    }

    @PostMapping("hotels/{id}/amenities")
    public ResponseEntity<List<String>> amenities(@PathVariable Integer id, @RequestBody List<String> amenities) {
        return ResponseEntity.ok(hotelService.updateAmenities(id, amenities));
    }

    @GetMapping("histogram/{param}")
    public ResponseEntity<Map<String, Long>> histogram(@PathVariable String param) {
        return ResponseEntity.ok(hotelService.makeHistogram(param));
    }

}
