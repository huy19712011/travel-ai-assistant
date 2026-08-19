package org.example.travelaiassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItineraryResponse {

    private String destination;
    private List<ItineraryDay> itinerary;
    private String summary;
}
