package org.example.restfulapp.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.restfulapp.convert.HotelDTOConverter;
import org.example.restfulapp.dto.HotelDTO;
import org.example.restfulapp.dto.ShortHotelDTO;
import org.example.restfulapp.exception.HotelNotFoundException;
import org.example.restfulapp.model.Address;
import org.example.restfulapp.model.Amenity;
import org.example.restfulapp.model.Hotel;
import org.example.restfulapp.repo.AmenityRepo;
import org.example.restfulapp.repo.HotelRepo;
import org.example.restfulapp.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo;
    private final AmenityRepo amenityRepo;
    private final HotelDTOConverter hotelDTOConverter;
    @Autowired
    private EntityManager entityManager;

    @SneakyThrows
    public List<ShortHotelDTO> getAllHotels() {
        List<Hotel> hotels = hotelRepo.findAll();
        if (hotels.isEmpty()) {
            throw new HotelNotFoundException();
        }
        return hotelRepo.findAll()
                .stream()
                .map(hotelDTOConverter::convertHotelToShortHotelDTO)
                .toList();
    }

    @SneakyThrows
    @Override
    public HotelDTO getHotelInfo(int id) {
        Hotel hotel = hotelRepo.findById(id).orElseThrow(HotelNotFoundException::new);
        return hotelDTOConverter.convertHotelToHotelDTO(hotel);
    }

    @SneakyThrows
    @Override
    public List<ShortHotelDTO> searchByParams(Map<String, Object> map) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> query = cb.createQuery(Hotel.class);
        Root<Hotel> hotel = query.from(Hotel.class);
        Join<Hotel, Address> addressJoin = hotel.join("address", JoinType.LEFT);
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Hotel> subHotel = subquery.from(Hotel.class);
        Join<Hotel, Amenity> subAmenityJoin = subHotel.join("amenities", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case "city":
                    predicates.add(cb.equal(addressJoin.get("city"), entry.getValue()));
                    break;
                case "amenities":
                    @SuppressWarnings("unchecked")
                    List<String> amenityNames = (List<String>) entry.getValue();
                    for(String amenity : amenityNames) {
                        subquery.select(subHotel.get("id"))
                                .where(cb.equal(subAmenityJoin.get("name"), amenity));
                        predicates.add(hotel.get("id").in(subquery));
                    }
                    break;
                default:
                    predicates.add(cb.equal(hotel.get(entry.getKey()), entry.getValue()));
                    break;
            }
        }
        query.select(hotel).where(cb.and(predicates.toArray(new Predicate[0])));

        List<Hotel> hotels = entityManager.createQuery(query).getResultList();
        if (hotels.isEmpty()) {
            throw new HotelNotFoundException();
        }
        return entityManager.createQuery(query)
                .getResultList()
                .stream()
                .map(hotelDTOConverter::convertHotelToShortHotelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShortHotelDTO createHotel(Hotel hotel) {
        hotelRepo.save(hotel);
        return hotelDTOConverter.convertHotelToShortHotelDTO(hotel);
    }

    @Override
    public List<String> updateAmenities(Integer id, List<String> amenities) {
        Hotel hotel = hotelRepo.findById(id).orElse(new Hotel());
        if (hotel.getId() == null) {
            return new ArrayList<>();
        }
        List<Amenity> amenitiesToAdd = amenities.stream()
                .map(name -> {
                    Optional<Amenity> existingAmenity = amenityRepo.findByName(name);
                    return existingAmenity.orElseGet(() -> {
                        Amenity newAmenity = new Amenity();
                        newAmenity.setName(name);
                        return amenityRepo.save(newAmenity);
                    });
                })
                .toList();
        hotel.getAmenities().addAll(amenitiesToAdd);
        hotelRepo.save(hotel);
        return amenities;
    }

    @Override
    public Map<String, Long> makeHistogram(String param) {
        HashMap<String, Long> histogram = new HashMap<>();
        switch (param) {
            case "brand":
                List<Object[]> countByBrand = hotelRepo.getHotelCountByBrand();
                for(Object[] o : countByBrand) {
                    histogram.put((String) o[0], (Long) o[1]);
                }
                break;
            case "city":
                List<Object[]> countByCity = hotelRepo.getHotelCountByCity();
                for(Object[] o : countByCity) {
                    histogram.put((String) o[0], (Long) o[1]);
                }
                break;
            case "county":
                List<Object[]> countByCounty = hotelRepo.getHotelCountByCounty();
                for(Object[] o : countByCounty) {
                    histogram.put((String) o[0], (Long) o[1]);
                }
                break;
            case "amenities":
                List<Amenity> amenities = amenityRepo.findAll();
                for(Amenity amenity : amenities) {
                    histogram.put(amenity.getName(), (long) amenity.getHotels().size());
                }
                break;
        }
        return histogram;
    }
}
