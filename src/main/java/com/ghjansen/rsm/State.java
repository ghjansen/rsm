/*
 * RSM - Recursive State Machine
 * Copyright (C) 2021  Guilherme Humberto Jansen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ghjansen.rsm;

public class State {

    private final String name;
    private final Logic logic;

    public State(String name, Logic logic) {
        this.name = name;
        this.logic = logic;
    }

    public String getName() {
        return name;
    }

    public Logic getLogic() {
        return logic;
    }
}
