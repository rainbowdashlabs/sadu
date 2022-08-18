/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.types;

public interface SqlType {
    static SqlType ofName(String name){
        return () -> name;
    }

    String name();
}
