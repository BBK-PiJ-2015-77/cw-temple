package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

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
    private Set<Node> escapeNeighbours;
    private List<Node> escapeNeighboursList;

    private Map<Node, Double> distance;
    private Map<Node, Node> predecessors;
    private Set<Node> settledNodes;
    private Set<Node> unSettledNodes;

    private Set<Node> visitedEscapeNodes;
    private List<Node> escapeRoute;
    private List<Node> goldRoute;

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

        List<Node> path = escapePath(state);

        if (path != null) {
            for (Node n : path) {
                state.moveTo(n);
                pickUpGold(state);
            }
        }

        //we've guaranteed we can get to the exit, now can grab as much gold as
        //possible by visiting as many nodes as possible
        int timeLeft = state.getTimeRemaining();
        visitedEscapeNodes = new HashSet<>();
        visitedEscapeNodes.add(state.getCurrentNode());
        goldRoute = new ArrayList<>();
        goldRoute.add(state.getCurrentNode());
        int pathLength = 0;

        boolean newNeighbours;
        boolean validMove;

        while(state.getTimeRemaining() > (timeLeft/2)) {

            escapeNeighbours = state.getCurrentNode().getNeighbours();
            escapeNeighboursList.addAll(escapeNeighbours);

            validMove = false;
            for (Node n : escapeNeighboursList) {
                if(checkTime(state,n,pathLength)) {
                    validMove = true;
                }
            }

            if(!validMove) {
                break;
            }

            newNeighbours = false;
            for (Node n : escapeNeighboursList) {
                //move to a neighbouring node if it hasn't yet been visited
                //and only if there are enough moves left to make
                if (!visitedEscapeNodes.contains(n)) {
                    newNeighbours = true;
                    if(checkTime(state,n,pathLength)) {
                        pathLength = pathLength + state.getCurrentNode().getEdge(n).length();
                        state.moveTo(n);
                        pickUpGold(state);
                        escapeNeighboursList.clear();
                        visitedEscapeNodes.add(state.getCurrentNode());
                        goldRoute.add(state.getCurrentNode());
                        break;
                    } else {
                        returnToExit(state, goldRoute);
                    }
                }
            }

            if(!newNeighbours && checkTime(state,escapeNeighboursList.get(0),pathLength)) {
                goldRoute.remove(goldRoute.size()-1);
                pathLength = pathLength - state.getCurrentNode().getEdge(goldRoute.get(goldRoute.size()-1)).length();
                state.moveTo(goldRoute.get(goldRoute.size()-1));
                escapeNeighboursList.clear();
            }
        }

        //return to exit
        if (!state.getCurrentNode().equals(state.getExit())) {
            returnToExit(state, goldRoute);
        }

    }

    private void returnToExit(EscapeState state, List<Node> route) {
        while(!state.getCurrentNode().equals(state.getExit())) {
            route.remove(route.size()-1);
            //something is going wrong below. indexoutofbounds exception
            state.moveTo(route.get(route.size()-1));


        }
    }

    //checks whether there is enough time remaining to make a move to a node
    //and get back
    private boolean checkTime(EscapeState state, Node neighbour, int pathLength) {
        int checkLength = state.getCurrentNode().getEdge(neighbour).length()*2;
        int lengthConstraint = state.getTimeRemaining();
        return ((checkLength + pathLength)<= lengthConstraint);
    }

    private void pickUpGold(EscapeState state) {
        if(state.getCurrentNode().getTile().getGold() != 0) {
            state.pickUpGold();
        }
    }

    private List<Node> escapePath(EscapeState state) {
        escapeNeighboursList = new ArrayList<>();

        visitedEscapeNodes = new HashSet<>();
        escapeRoute = new ArrayList<>();
        List<Node> path = new ArrayList<>();

        distance = new HashMap<>();
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        predecessors = new HashMap<>();

        escapeMap = state.getVertices();

        Node startNode = state.getCurrentNode();
        Node exitNode = state.getExit();

        double d = 0.0;
        distance.put(startNode,d);
        unSettledNodes.add(startNode);

        while(unSettledNodes.size() > 0) {
            //get node with lowest distance
            Node tempNode = getMin(unSettledNodes);

            if(tempNode.equals(exitNode)) {
                break;
            }

            settledNodes.add(tempNode);
            unSettledNodes.remove(tempNode);

            escapeNeighbours = tempNode.getNeighbours();
            escapeNeighboursList.addAll(escapeNeighbours);

            for (Node n : escapeNeighboursList) {
                double neighbourDist = getShortestDistance(tempNode) + tempNode.getEdge(n).length();
                if(getShortestDistance(n) > neighbourDist) {
                    distance.put(n, neighbourDist);
                    predecessors.put(n, tempNode);
                    unSettledNodes.add(n);
                }
            }
            escapeNeighboursList.clear();
        }

        Node step = exitNode;
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        Collections.reverse(path);
        path.remove(0);
        return path;


    }

    private Node getMin(Set<Node> nodes) {
        Node min = null;
        for (Node n : nodes) {
            if (min == null) {
                min = n;
            } else {
                if (getShortestDistance(n) < getShortestDistance(min)) {
                    min = n;
                }
            }
        }
        return min;
    }

    private double getShortestDistance(Node n) {
        Double d = distance.get(n);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

}
