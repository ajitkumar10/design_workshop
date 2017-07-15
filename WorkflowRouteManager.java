package org.ajit.workflow;

/**
This class determines the workflow routes from state A and state B
and shortest path by cost
where workflow is to process a request through requisite approvals 
and workflow states are different approval groups
and cost is the SLA for corresponding approval group to action a request once in their queue.

The algorihtm used is Dijkstra's algorithm (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
It picks the unvisited vertex with the lowest distance, calculates the distance through it to each unvisited neighbor, 
and updates the neighbor's distance if smaller. Mark visited (set to red) when done with neighbors.

*/

// TODO - add the implementation here
