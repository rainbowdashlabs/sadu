/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public record TestObj2(int first, long second, String third, LocalDateTime fourth, UUID fifth) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestObj2 testObj = (TestObj2) o;
        return first == testObj.first && second == testObj.second && Objects.equals(fifth, testObj.fifth) && Objects.equals(third, testObj.third) && Objects.equals(fourth.truncatedTo(ChronoUnit.SECONDS), testObj.fourth.truncatedTo(ChronoUnit.SECONDS));
    }

    @Override
    public int hashCode() {
        int result = first;
        result = 31 * result + Long.hashCode(second);
        result = 31 * result + Objects.hashCode(third);
        result = 31 * result + Objects.hashCode(fourth.truncatedTo(ChronoUnit.SECONDS));
        result = 31 * result + Objects.hashCode(fifth);
        return result;
    }
}
