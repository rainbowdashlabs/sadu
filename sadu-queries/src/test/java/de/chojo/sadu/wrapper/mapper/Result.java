/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

class Result {
    int id;
    String result;

    public Result(int id, String result) {
        this.id = id;
        this.result = result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;

        Result result1 = (Result) o;

        if (id != result1.id) return false;
        return result.equals(result1.result);
    }

    @Override
    public int hashCode() {
        int result1 = id;
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

    @Override
    public String toString() {
        return "Result{id=%d, result='%s'}".formatted(id, result);
    }
}
