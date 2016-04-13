package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Comparator;

import game.EscapeState;
import game.ExplorationState;
import game.NodeStatus;

public class Explorer {

    private Collection<NodeStatus> currentNeighbours;
    private List<NodeStatus> currentNeighboursList;

    private NodeStatus nearestNode;
    private Set<Long> visitedNodes;
    private List<Long> route;
    private int dtt;

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {

        Deque<Long> stack = new ArrayDeque<>();
        stack.push(state.getCurrentLocation());
        Long previousNode = null;
        currentNeighboursList = new ArrayList<>();
        visitedNodes = new HashSet<>();
        route = new ArrayList<>();

        visitedNodes.add(state.getCurrentLocation());
        route.add(state.getCurrentLocation());
        System.out.println("The start node is: " + state.getCurrentLocation());

        while(state.getDistanceToTarget() != 0) {

            currentNeighbours = state.getNeighbours();

            for (NodeStatus tempNode : currentNeighbours) {
                currentNeighboursList.add(tempNode);
            }

            Collections.sort(currentNeighboursList, new Comparator<NodeStatus>() {
                public int compare (NodeStatus o1, NodeStatus o2) {
                    if (o1.getDistanceToTarget() == o2.getDistanceToTarget()) {
                        return 0;
                    } else {
                        return o1.getDistanceToTarget() < o2.getDistanceToTarget() ? -1 : 1;
                    }
                }
            });

            //just checking that they are actually sorted
            //sorted by id value, not by distance!
            for (NodeStatus ns : currentNeighboursList) {
                System.out.print(ns.getId() + ", ");
            }
            System.out.println(" ");

            boolean newNeighbours = false;
            for (NodeStatus ns : currentNeighboursList) {
                if (!visitedNodes.contains(ns.getId())) {
                    newNeighbours = true;
                    state.moveTo(ns.getId());
                    currentNeighboursList.clear();
                    visitedNodes.add(state.getCurrentLocation());
                    route.add(state.getCurrentLocation());
                    break;
                }
            }


            if (!newNeighbours) {
                route.remove(route.size()-1);
                System.out.println("Moving to: " + route.get(route.size()-1));
                state.moveTo(route.get(route.size()-1));
            }

            /**
            if (visitedNodes.contains(stack.getFirst())) {
                stack.pop();
                state.moveTo(previousNode);
            } else {
                previousNode = state.getCurrentLocation();
                state.moveTo(stack.getFirst());
                currentNeighboursList.clear();
                visitedNodes.add(state.getCurrentLocation());
            }


            System.out.println("Stack contents:");
            for(Long id : stack) {
                System.out.println(id);
            }





            System.out.println("Visited Nodes: ");
            for (long id : visitedNodes) {
                System.out.println(id);
            }
            */


        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {
        //TODO: Escape from the cavern before time runs out
    }


    //This method doesn't work because sometimes you have to move into spaces that aren't nearer
    private void returnNearest(List<NodeStatus> lns, NodeStatus ns) {
        Collections.sort(lns);
        for (NodeStatus tempNode : lns) {
            System.out.println(tempNode.getDistanceToTarget());
        }
        System.out.println("...");
        boolean match = false;

        for (NodeStatus tempNode : lns) {
            if (visitedNodes.contains(tempNode.getId())) {
                lns.remove(tempNode);
            } else  if (ns.getDistanceToTarget() > tempNode.getDistanceToTarget()) {
                nearestNode = tempNode;
                match = true;
            }
        }

        if(!match) {
            nearestNode = lns.get(0);
        }

    }


}
