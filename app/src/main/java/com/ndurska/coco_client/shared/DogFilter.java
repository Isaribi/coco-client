package com.ndurska.coco_client.shared;


import com.ndurska.coco_client.database.dto.DogDto;

import java.util.ArrayList;
import java.util.List;

public class DogFilter {
    private final List<DogDto> dogs;

    public DogFilter(List<DogDto> dogs) {
        this.dogs = dogs;
    }

    public List<DogDto> dogsContaining(String phrase) {
        List<DogDto> searchResults = new ArrayList<>();
        String[] keywords = phrase.split(" ");
        int matches;

        for (DogDto dog : dogs) {
            matches = 0;
            for (String keyword : keywords)
                if (dog.toStringShort().toLowerCase().contains(keyword.toLowerCase()))
                    matches++;
            if (matches == keywords.length)
                searchResults.add(dog);
        }
        return searchResults;

    }
}
