package com.glady.backend.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@AllArgsConstructor
public class MealDeposits {
    private List<MealDeposit> mealDepositList;

    @SuppressWarnings("unused")
    public MealDeposits() {
    }

    public MealDeposits(Spliterator<MealDeposit> spliterator) {
        mealDepositList = StreamSupport.stream(spliterator, false)
                .collect(Collectors.toList());
    }
}
