/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestObj testObj = (TestObj) o;
        return first == testObj.first && second == testObj.second && Objects.equals(fifth, testObj.fifth) && Objects.equals(third, testObj.third) && Objects.equals(fourth.getEpochSecond(), testObj.fourth.getEpochSecond());
    }

    @Override
    public int hashCode() {
        int result = first;
        result = 31 * result + Long.hashCode(second);
        result = 31 * result + Objects.hashCode(third);
        result = 31 * result + Objects.hashCode(fourth.getEpochSecond());
        result = 31 * result + Objects.hashCode(fifth);
        return result;
    }
}
