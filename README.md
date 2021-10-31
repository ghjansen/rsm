# RSM - Recursive State Machine
The Recursive State Machine (RSM) is an experimental Finite State Machine that allows each State to contain another Finite State Machine within itself. This approach allows for breaking down a single big State into multiple smaller States. Therefore, the Recursive State Machine can be used to design and execute complex automata with as many recursive layers as necessary.

## Rules
**1** - A State can contain only one Finite State Machine (recursively).

**2** - A Finite State Machine can contain multiple States.

**3** - When a State is not found during the execution of a "child" Finite State Machine, the child Finite State Machine will be interrupted and report the State to the "parent" Finite State Machine, and so on recursively until either the State is found to continue the execution, or the whole execution will be interrupted.

## Example

The following diagram shows and example of a regular Finite State Machine, which contains 4 States (s1, s2, s3 and s4), some transitions between these States, along with the initial and final States. 

<p align="center">
  <img src="src/resources/diagram1.png" />
</p>

Assuming that the logic behind the State s3 is too big, or reached enough complexity to justify being refactored into multiple parts, the State s3 can be redesigned to contain a Finite State Machine within itself, translating its logic into multiple States (s3-1, s3-2 and s3-2), as shown by the diagram bellow:

<p align="center">
  <img src="src/resources/diagram2.png" />
</p>

The State s3 continues to exist as before, changing only internally, which does not affect the Finite State Machine from which s3 is part of, and the overall result from the execution of both designs is the same.

The same way a State can be translated into a Finite State Machine to refine its design, a Finite State Machine can also be added to a new State in a level above. Considering the last diagram above, the Finite State Machine containing the States s1, s2, s3 and s4 could be aggregated into a new State called "r1", which is part of another Finite State Machine containing the states r1, r2, r3, r4, r5 and r6, controlling an even more abstract set of rules. Thus, this approach allows the automata design to grow in both abstraction and specialization.
