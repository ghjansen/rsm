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

import org.apache.log4j.Logger;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Optional;

public class Machine implements Logic {

    private final Logger logger = Logger.getLogger(Machine.class);
    private static final String DEBUG_MSG_STATE_NOT_FOUND = "State %s not found in the recursive machine %s. Backtracking execution.";
    private static final String DEBUG_MSG_INITIAL_STATE = "Started recursive state machine %s.";
    private static final String DEBUG_MSG_FINAL_STATE = "Finished recursive state machine %s.";
    private static final String ERROR_MSG_TRANSITION_NOT_FOUND = "Transition %s to %s not found in the recursive state machine %s.";

    private static final String INITIAL_STATE_NAME = "initialState";
    public static final String FINAL_STATE_NAME = "finalState";

    private final String name;
    private final State initialState;
    private final State entranceState;
    private State currentState;
    private final State finalState;
    private final HashMap<String,State> states;
    private final HashMap<SimpleImmutableEntry<State, State>, State> transitions;

    public Machine(String name, State entranceState, HashMap<String, State> states, HashMap<SimpleImmutableEntry<State, State>, State> transitions) {
        this.name = name;
        this.initialState = new State(INITIAL_STATE_NAME, () -> {logger.debug(DEBUG_MSG_INITIAL_STATE.formatted(this.name));return null;});
        this.entranceState = entranceState;
        this.currentState = this.initialState;
        this.finalState = new State(FINAL_STATE_NAME, () -> {logger.debug(DEBUG_MSG_FINAL_STATE.formatted(this.name));return null;});
        this.states = states;
        this.transitions = transitions;
        this.transitions.put(new SimpleImmutableEntry<>(this.initialState, this.entranceState), this.initialState);
    }

    public String getName() {
        return name;
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

    @Override
    public String execute() {
        return transitionCycle();
    }

    private String transitionCycle(){
        initialState.getLogic().execute();
        Optional<String> nextStateName = Optional.ofNullable(entranceState.getName());
        while (nextStateName.isPresent()) {
            Optional<State> nextState = Optional.ofNullable(states.get(nextStateName.get()));
            if(nextState.isPresent()){
                validateTransition(nextState.get());
                this.currentState = nextState.get();
                nextStateName = Optional.ofNullable(this.currentState.getLogic().execute());
            } else {
                logger.debug(DEBUG_MSG_STATE_NOT_FOUND.formatted(nextStateName, this.name));
                break;
            }
        }
        return nextStateName.get();
    }

    private void validateTransition(State nextState){
        SimpleImmutableEntry<State,State> transition = new SimpleImmutableEntry<>(this.currentState, nextState);
        if (!transitions.containsKey(transition)) {
            final String errorMsg = ERROR_MSG_TRANSITION_NOT_FOUND.formatted(this.currentState.getName(), nextState.getName(), this.name);
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

}
