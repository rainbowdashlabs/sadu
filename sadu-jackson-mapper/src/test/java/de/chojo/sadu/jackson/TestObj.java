/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record TestObj(int first, long second, String third, Instant fourth, UUID fifth) {
    @JsonCreator
    public TestObj(@JsonProperty("first") int first,
                   @JsonProperty("second") long second,
                   @JsonProperty("third") String third,
                   @JsonProperty("fourth") Instant fourth,
                   @JsonProperty("fifth") UUID fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
    }
}
