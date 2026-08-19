package org.example.travelaiassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItineraryDay {

    private int day; // 1st day, 2nd day, ...
    private List<String> attractions;
    private String food;
}
