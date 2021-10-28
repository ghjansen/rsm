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

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Optional;

public class Machine {

    private State currentState;
    private final State initialState;
    private final HashMap<String,State> states;
    private final HashMap<SimpleImmutableEntry<State, State>, State> transitions;

    public Machine(State initialState, HashMap<String, State> states, HashMap<SimpleImmutableEntry<State, State>, State> transitions) {
        this.initialState = initialState;
        this.states = states;
        this.transitions = transitions;
        this.transitions.put(new SimpleImmutableEntry<>(null, initialState), initialState);
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getInitialState() {
        return initialState;
    }

    public HashMap<String, State> getStates() {
        return states;
    }

    public HashMap<SimpleImmutableEntry<State, State>, State> getTransitions() {
        return transitions;
    }

    public void run(){
        transitionCycle();
    }

    private void transitionCycle(){
        String nextStateName = initialState.getName();
        while (nextStateName != null) {
            this.currentState = transition(nextStateName);
            nextStateName = this.currentState.getLogic().execute();
        }
    }

    private State transition(String nextStateName){
        Optional<State> nextState = Optional.ofNullable(states.get(nextStateName));
        SimpleImmutableEntry<State,State> candidate = new SimpleImmutableEntry<>(this.currentState, nextState.orElseThrow());
        if (!transitions.containsKey(candidate)){
            throw new RuntimeException("No transition found from " + this.currentState.getName() + " to " + nextState.get().getName());
        }
        return nextState.get();
    }

}
