package com.glady.backend.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@AllArgsConstructor
public class VoutcherDeposits {
    private List<VoutcherDeposit> voutcherDepositList;

    @SuppressWarnings("unused")
    public VoutcherDeposits() {
    }

    public VoutcherDeposits(Spliterator<VoutcherDeposit> spliterator) {
        voutcherDepositList = StreamSupport.stream(spliterator, false)
                .collect(Collectors.toList());
    }
}
