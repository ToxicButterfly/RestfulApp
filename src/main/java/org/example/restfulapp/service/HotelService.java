package org.example.restfulapp.service;

import org.example.restfulapp.dto.HotelDTO;
import org.example.restfulapp.dto.ShortHotelDTO;
import org.example.restfulapp.model.Hotel;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<ShortHotelDTO> getAllHotels();

    HotelDTO getHotelInfo(int id);

    List<ShortHotelDTO> searchByParams(Map<String, Object> map);

    ShortHotelDTO createHotel(Hotel hotel);

    List<String> updateAmenities(Integer id, List<String> amenities);

    Map<String, Long> makeHistogram(String param);
}
