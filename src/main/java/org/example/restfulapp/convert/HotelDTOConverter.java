package org.example.restfulapp.convert;

import org.example.restfulapp.dto.HotelDTO;
import org.example.restfulapp.dto.ShortHotelDTO;
import org.example.restfulapp.model.Hotel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HotelDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public ShortHotelDTO convertHotelToShortHotelDTO(Hotel hotel) {
        ShortHotelDTO shortHotelDTO = modelMapper.map(hotel, ShortHotelDTO.class);
        shortHotelDTO.setPhone(hotel.getContacts().getPhone());
        return shortHotelDTO;
    }
    public HotelDTO convertHotelToHotelDTO(Hotel hotel) {
        HotelDTO hotelDTO = modelMapper.map(hotel, HotelDTO.class);
        return hotelDTO;
    }
}
