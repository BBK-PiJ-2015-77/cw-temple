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
import java.util.Map;
import java.util.HashMap;

import game.EscapeState;
import game.ExplorationState;
import game.NodeStatus;
import game.Node;
import game.Edge;

public class Explorer {

    private Collection<NodeStatus> currentNeighbours;
    private List<NodeStatus> currentNeighboursList;
    private Set<Long> visitedNodes;
    private List<Long> route;

    private Collection<Node> escapeMap;
    private Collection<Node> escapeNeighbours;
    private List<Node> escapeNeighboursList;

    private Map<Node, Integer> distance;
    private Map<Node, Node> predecessors;
    private Set<Node> settledNodes;
    private Set<Node> unSettledNodes;

    private List<Node> tempEscapeNeighboursList;
    private Set<Node> visitedEscapeNodes;
    private List<Node> escapeRoute;
    private List<Node> goldRoute;

    //delete if necessary
    private Node saveNode;

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
            /**
            for (NodeStatus ns : currentNeighboursList) {
                System.out.print(ns.getId() + ", ");
            }
            System.out.println(" ");
             */

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

            System.out.println("Route so far:");
            for (Long l : route) {
                System.out.println(l);
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

        //Starting again


        //stage 2



        /**
        while (UnSettledNodes is not empty) {
            evaluationNode = getNodeWithLowestDistance(UnSettledNodes)
            remove evaluationNode from UnSettledNodes
            add evaluationNode to SettledNodes
            evaluatedNeighbors(evaluationNode)
        }

        getNodeWithLowestDistance(UnSettledNodes){
            find the node with the lowest distance in UnSettledNodes and return it
        }

        evaluatedNeighbors(evaluationNode){
            Foreach destinationNode which can be reached via an edge from evaluationNode AND which is not in SettledNodes {
                edgeDistance = getDistance(edge(evaluationNode, destinationNode))
                newDistance = distance[evaluationNode] + edgeDistance
                if (distance[destinationNode]  > newDistance) {
                    distance[destinationNode]  = newDistance
                    add destinationNode to UnSettledNodes
                }
            }
        }
         */

        ///////

        /**
        System.out.println("Escape Stage: ");

        //delete if necessarry


        escapeMap = state.getVertices();
        for (Node n : escapeMap) {
            System.out.println(n.getId());
        }
        tempEscapeNeighboursList = new ArrayList<>();
        escapeNeighboursList = new ArrayList<>();
        visitedEscapeNodes = new HashSet<>();
        escapeRoute = new ArrayList<>();

        visitedEscapeNodes.add(state.getCurrentNode());
        escapeRoute.add(state.getCurrentNode());

        Node exitNode = state.getExit();
        System.out.println("Escape nodes id: " + exitNode.getId());
        //exitNode.getEdge()

        //get to the exit
        while(!state.getCurrentNode().equals(exitNode)) {

            escapeNeighbours = state.getCurrentNode().getNeighbours();
            escapeNeighboursList.addAll(escapeNeighbours);

            System.out.println("Current Neighbours: ");
            for (Node n : escapeNeighboursList) {
                System.out.println(n.getId());
            }

            //sort the list of neighbours by whichever is nearest the orb
            Collections.sort(escapeNeighboursList, new Comparator<Node>() {
                public int compare (Node o1, Node o2) {
                    if (state.getCurrentNode().getEdge(o1).length() == state.getCurrentNode().getEdge(o2).length()) {
                        return 0;
                    } else {
                        return state.getCurrentNode().getEdge(o1).length() < state.getCurrentNode().getEdge(o2).length() ? -1 : 1;
                    }
                }
            });

            //delete if necessary
            if (state.getCurrentNode().getId() == 1541) {
                saveNode = state.getCurrentNode();
            }

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
                escapeNeighboursList.clear();
            }

            System.out.println("Exit route so far:");
            for (Node n : escapeRoute) {
                System.out.println(n.getId());
            }

        }

        //we've guaranteed we can get to the exit, now can grab as much gold as
        //possible by visiting as many nodes as possible
        int timeLeft = state.getTimeRemaining();
        while(state.getTimeRemaining()> (timeLeft/2)) {
            goldRoute = new ArrayList<>();
            goldRoute.add(state.getCurrentNode());
            escapeNeighbours = state.getCurrentNode().getNeighbours();
            escapeNeighboursList.addAll(escapeNeighbours);
            int pathLength = 0;

            boolean newNeighbours = false;
            for (Node n : escapeNeighboursList) {
                //move to a neighbouring node if it hasn't yet been visited
                //and only if there are enough moves left to make
                if (!visitedEscapeNodes.contains(n) && checkTime(state,n,pathLength)) {
                    newNeighbours = true;
                    pathLength = pathLength + state.getCurrentNode().getEdge(n).length();
                    state.moveTo(n);
                    escapeNeighboursList.clear();
                    visitedEscapeNodes.add(state.getCurrentNode());
                    goldRoute.add(state.getCurrentNode());
                    break;
                }
            }

            if(!newNeighbours) {
                pathLength = pathLength + state.getCurrentNode().getEdge(escapeNeighboursList.get(0)).length();
                state.moveTo(escapeNeighboursList.get(0));
                escapeNeighboursList.clear();
                visitedEscapeNodes.add(state.getCurrentNode());
                goldRoute.add(state.getCurrentNode());
            }


        }

        //return to exit
        while(!state.getCurrentNode().equals(exitNode)) {
            goldRoute.remove(goldRoute.size()-1);
            state.moveTo(goldRoute.get(goldRoute.size()-1));
        }
         */
        //

        /**
        escapeNeighbours = state.getCurrentNode().getNeighbours();
        escapeNeighboursList.addAll(escapeNeighbours);
        Node penultimate = escapeNeighboursList.get(0);
        System.out.println(state.getTimeRemaining());

        if (state.getTimeRemaining()%2 != 0) {
            state.moveTo(penultimate);
            escapeNeighboursList.clear();
            escapeNeighbours = state.getCurrentNode().getNeighbours();
            escapeNeighboursList.addAll(escapeNeighbours);
            Node penultimate2 = escapeNeighboursList.get(0);
            state.moveTo(penultimate2);

        } else {
            finishMap(state,penultimate,exitNode,state.getTimeRemaining());
        }


        Set<Node> l1 = state.getCurrentNode().getNeighbours();
        List<Node> secondLast = new ArrayList<>();
        secondLast.addAll(l1);
        Node check2 = secondLast.get(0);
        state.moveTo(check2);

        state.moveTo(saveNode);

        Edge ege = check2.getEdge(saveNode);
        System.out.println("Edge length is: " + ege.length());
        System.out.println("Edge source is: " + ege.getSource().getId());
        System.out.println("Edge destination is: " + ege.getDest().getId());

        Set<Edge> exits = saveNode.getExits();
        int testInt = 0;
        for (Edge e : exits) {
            testInt++;
            System.out.println(testInt);
            System.out.println("Edge length is: " + e.length());
            System.out.println("Edge source is: " + e.getSource().getId());
            System.out.println("Edge destination is: " + e.getDest().getId());
        }
         */


        /**
        while(state.getCurrentNode().equals(exitNode)) {
            state.moveTo(escapeRoute.get(escapeRoute.size()-1));
        }
        */

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

    private void finishMap(EscapeState state, Node pen, Node fin, int time) {
        while (time >= 0) {
            state.moveTo(pen);
            state.moveTo(fin);
        }
    }

    //checks whether there is enough time remaining to make a move to a node
    //and get back
    private boolean checkTime(EscapeState state, Node neighbour, int pathLength) {
        int checkLength = state.getCurrentNode().getEdge(neighbour).length()*2;
        int lengthConstraint = state.getTimeRemaining();
        if((checkLength + pathLength)<= lengthConstraint) {
            return true;
        } else {
            return false;
        }
    }

    private List<Node> escapePath(EscapeState state) {
        escapeNeighboursList = new ArrayList<>();
        visitedEscapeNodes = new HashSet<>();
        escapeRoute = new ArrayList<>();

        distance = new HashMap<>();
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        predecessors = new HashMap<>();

        escapeMap = state.getVertices();

        Node startNode = state.getCurrentNode();
        Node exitNode = state.getExit();

        distance.put(startNode,0);
        unSettledNodes.add(startNode);

        while(unSettledNodes.size() > 0) {
            //get node with lowest distance
            Node tempNode = getMin(unSettledNodes);

            if(tempNode.equals(exitNode)) {
                break;
            }
            //add to settledNodes
            settledNodes.add(tempNode);
            //remove from unSettledNodes
            unSettledNodes.remove(tempNode);
            //get neighbour with shortest node?????
            findMinDistance(tempNode, state);
        }
    }

    private void findMinDistance(Node node, EscapeState state) {

        escapeNeighboursList = new ArrayList<>();

        escapeNeighbours = state.getCurrentNode().getNeighbours();
        escapeNeighboursList.addAll(escapeNeighbours);
        Node currentNode = state.getCurrentNode();

        for (Node n : escapeNeighboursList) {
            if(getShortestDistance(n) > getShortestDistance(node) + currentNode.getEdge(n)) {
                distance.put(n, getShortestDistance(node) + currentNode.getEdge(n));
                predecessors.put(n, node);
                unSettledNodes.add(n);
            }
        }
    }

    //getShortestDistance

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

    private int getShortestDistance(Node destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /**
    evaluatedNeighbors(evaluationNode){
        Foreach destinationNode which can be reached via an edge from evaluationNode AND which is not in SettledNodes {
            edgeDistance = getDistance(edge(evaluationNode, destinationNode))
            newDistance = distance[evaluationNode] + edgeDistance
            if (distance[destinationNode]  > newDistance) {
                distance[destinationNode]  = newDistance
                add destinationNode to UnSettledNodes
            }
        }
    }
    */


}
