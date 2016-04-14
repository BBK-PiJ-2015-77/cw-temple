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
import game.Node;

public class Explorer {

    private Collection<NodeStatus> currentNeighbours;
    private List<NodeStatus> currentNeighboursList;
    private Set<Long> visitedNodes;
    private List<Long> route;

    private Collection<Node> escapeMap;
    private Collection<Node> escapeNeighbours;
    private List<Node> escapeNeighboursList;
    private Set<Node> visitedEscapeNodes;
    private List<Node> escapeRoute;

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

            //sort the list of neighbours by whichever is nearest the orb
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
            //sorted by id value, not by distance! don't think the
            //sorting is working correctly yet
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

        escapeMap = state.getVertices();
        escapeNeighboursList = new ArrayList<>();
        visitedEscapeNodes = new HashSet<>();
        escapeRoute = new ArrayList<>();

        visitedEscapeNodes.add(state.getCurrentNode());
        escapeRoute.add(state.getCurrentNode());

        Node exitNode = state.getExit();

        while(!state.getCurrentNode().equals(exitNode)) {

            escapeNeighbours = state.getCurrentNode().getNeighbours();

            escapeNeighboursList.addAll(escapeNeighbours);

            //need to pick up gold if present
            if(state.getCurrentNode().getTile().getGold() != 0) {
                state.pickUpGold();
            }

            boolean newNeighbours = false;
            for (Node n : escapeNeighboursList) {
                if (!visitedEscapeNodes.contains(n)) {
                    newNeighbours = true;
                    state.moveTo(n);
                    escapeNeighboursList.clear();
                    visitedEscapeNodes.add(state.getCurrentNode());
                    escapeRoute.add(state.getCurrentNode());
                    break;
                }
            }

            if (!newNeighbours) {
                escapeRoute.remove(escapeRoute.size()-1);
                System.out.println("Moving to: " + escapeRoute.get(escapeRoute.size()-1).getId());
                state.moveTo(escapeRoute.get(escapeRoute.size()-1));
            }

        }


        while(state.getCurrentNode().equals(exitNode)) {
            state.moveTo(escapeRoute.get(escapeRoute.size()-1));
        }

        /**
        System.out.println("Escape Map: ");
        for (Node n : escapeMap) {
            System.out.println(n.getId());
        }
         */

        /**
         * Return your current location in the graph.
         */
        //public Node getCurrentNode();

        /**
         * Return the exit from the cavern.
         * This is the node you need to move to in order to escape.
         */
        //public Node getExit();

        /**
         * Return all the nodes in the graph, in no particular order.
         */
        //public Collection<Node> getVertices();

        /**
         * Change your current location n.
         * Throw an IllegalArgumentException if n is not a neihgbor of your current location.
         */
        //public void moveTo(Node n);

        /**
         * Picks up any gold on the current tile.
         * You must first check that there is gold before picking it up.
         * <p>
         * Throw an IllegalStateException if there is no gold at the current location,
         * either because there never was any or because you picked it up already.
         */
        //public void pickUpGold();

        /**
         * Return the time remaining to escape from the cavern.
         * This value changes with every call to moveTo(Node),
         * and if it reaches 0 before you escape, you have failed to escape.
         */
        //public int getTimeRemaining();
    }


}
