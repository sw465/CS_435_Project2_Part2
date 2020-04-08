import java.util.*;

//import org.graalvm.compiler.nodes.calc.AddNode;


class Main
{
    
    public static void main(String[] args) 
    {

        // Graph randomGraph = createRandomUnweightedGraphIter(6);

        //randomGraph.printNodesWithEdges();

        // ArrayList<Graph.Node> path =  randomGraph.GS.DFSRec(randomGraph, randomGraph.nodeList.get(1), randomGraph.nodeList.get(5));
        // ArrayList<Graph.Node> path01 =  randomGraph.GS.DFSIter(randomGraph, randomGraph.nodeList.get(1), randomGraph.nodeList.get(5));
        // ArrayList<Graph.Node> BFT_Rec_Path =  randomGraph.GS.BFTRec(randomGraph);
        // ArrayList<Graph.Node> BFT_Iter_Path =  randomGraph.GS.BFTIter(randomGraph);
        // ArrayList<Graph.Node> BFT_Rec_10000 =  BFTRecLinkedList();
        // ArrayList<Graph.Node> BFT_Iter_10000 =  BFTIterLinkedList();
        

        // printPath(path);
        // printPath(path01);
        // printPath(BFT_Rec_Path);
        // printPath(BFT_Iter_Path);
        // printPath(BFT_Rec_10000);
        // printPath(BFT_Iter_10000);

        // ************** Part 2 **************

        // *** Question 4 ***

        // DirectedGraph directedGraph = createRandomDAGIter(1000);

       // directedGraph.printNodesWithEdges();

        // ArrayList<Graph.Node> topSort_khansPath = directedGraph.TS.Kahns(directedGraph);
        // ArrayList<Graph.Node> topSort_mDFSPath = directedGraph.TS.mDFS(directedGraph);

        // printPath(topSort_khansPath);
        // printPath(topSort_mDFSPath);

        // *** Question 5 ***


        // WeightedGraph weightedGraph = createRandomCompleteWeightedGraph(100);
        // weightedGraph.printNodesWithEdges();

        // System.out.println(weightedGraph.nodeList.get(0).id);
        // HashMap<Graph.Node,Integer> result = dijkstras(weightedGraph.nodeList.get(0));
        // printDijkstraResults(result);

        // *** Question 6 ***

        // int n = 100;
        // GridGraph gridGraph = createRandomGridGraph(n);
        // gridGraph.printNodesWithEdges();

        // ArrayList<Graph.Node> path = astar(gridGraph.nodeList.get(0), gridGraph.nodeList.get(n*n-1));
        // printGridPath(path);



    } // End Main



    // Prints a path by printing the node id's in order
    static void printPath( ArrayList<Graph.Node> path ) 
    {
        System.out.println("Printing Path: ");
        for ( int i = 0; i < path.size(); i++ )
            System.out.print( path.get(i).id + ", " );
        
            System.out.println();
    }

    // Prints a path from a gridGraph by printing each node's x and y coordinates
    static void printGridPath( ArrayList<Graph.Node> path ) 
    {
        if ( path.isEmpty() )
            System.out.println("There was no path from source to destination");
        else
        {
            System.out.println("Printing Grid Path: ");
            for ( int i = 0; i < path.size(); i++ )
                System.out.print( "(" + path.get(i).x + "," + path.get(i).y + "), " );
            
                System.out.println();
        }
    }




    static Graph createRandomUnweightedGraphIter(int n)
    {
        Graph newGraph = new Graph();
        
        // Add n nodes
        for ( int i = 0; i < n; i++ )
            newGraph.addNode();

        // A list 0 to n-1 for connections that are possible
        ArrayList<Integer> possibleEdges = new ArrayList<Integer>();
        for ( int i = 0; i < n; i++ )
            possibleEdges.add(i);

        // Set edges for each node created
        // Each node can have up to n-1 edges if it cant have an edge to itself
        for ( int i = 0; i < n; i++ )
        {
            int numEdges = new Random().nextInt(n);
            
            // possibleEdges will shuffle for each new node we set edges for
            Collections.shuffle(possibleEdges);

            int edgeList[] =  new int[numEdges];

            // Get the first numEdges elements from possibleEdges
            // These will be the values for the edge connections made
            for ( int j = 0; j < numEdges; j++ )
                edgeList[j] = possibleEdges.get(j);

            for ( int j = 0; j < numEdges; j++ )
                // Left parameter stays the same. Right parameter gets random index from edgeList, subsequently 
                // getting a random node from nodeList to add edge with
                newGraph.addUndirectedEdge( newGraph.nodeList.get(i), newGraph.nodeList.get(edgeList[j]) );

        }

        return newGraph;
    }

    static DirectedGraph createRandomDAGIter(final int n)
    {
        DirectedGraph directedGraph = new DirectedGraph();

        // Add n nodes
        for ( int i = 0; i < n; i++ )
            directedGraph.addNode();

        // A list 0 to n-1 for connections that are possible
        ArrayList<Integer> possibleEdges = new ArrayList<Integer>();
        for ( int i = 0; i < n; i++ )
            possibleEdges.add(i);

        // Set edges for each node created
        // Nodes can only have edges to nodes ahead of them to prevent cycles
        // Each node can have up to n-i-1 edges if it cant have an edge to itself
        for ( int i = 0; i < n; i++ )
        {
            // If there are 5 nodes in graph, the 0th node can have directed edge to all nodes
            // ahead of it, 4 possible. The 1st can only connect to 3. 2nd to 2 etc.
            // The last node should have 0 edges, so n - i would be 5 - 4 = 1. 
            // Random().nextInt(1) will return ints from 0 to 1 not including 1, so 0.
            int numEdges = new Random().nextInt(n - i);


            // Make sub list that only includes node id's of nodes with larger id's than current node. 
            // Setting edges for node 2 with 5 total nodes will return the list [3,4]
            ArrayList<Integer> possibleEdgesFromCurrentNode = new ArrayList<Integer>( possibleEdges.subList(i + 1, n) );
            
            Collections.shuffle(possibleEdgesFromCurrentNode);

            int edgeList[] =  new int[numEdges];

            // Get the first numEdges elements from possibleEdgesFromCurrentNode
            // These will be the values for the edge connections made
            for ( int j = 0; j < numEdges; j++ )
                edgeList[j] = possibleEdgesFromCurrentNode.get(j);

            for ( int j = 0; j < numEdges; j++ )
                // Left parameter stays the same. Right parameter gets random index from edgeList, subsequently 
                // getting a random node from nodeList to add edge with
                directedGraph.addDirectedEdge( directedGraph.nodeList.get(i), directedGraph.nodeList.get(edgeList[j]) );

        } // End 'i' for loop

        return directedGraph;
        
    } // End createRandomDAGIter

    static Graph createLinkedList(int n)
    {
        Graph newGraph = new Graph();

        // Create initial node
        newGraph.addNode();
        // After each new node is created, make the prev point to the new node.
        for ( int i = 1; i < n; i++ )
        {
            newGraph.addNode();
            newGraph.addDirectedEdge(newGraph.nodeList.get(i-1), newGraph.nodeList.get(i));
        }

        return newGraph;
    }

    // This should run a BFT recursively on a LinkedList. Your LinkedList should have 10,000 nodes.
    static ArrayList<Graph.Node> BFTRecLinkedList()
    {
        Graph linkedList = createLinkedList(1000);
        return linkedList.GS.BFTRec(linkedList);
    }

    // This should run a BFT iteratively on a LinkedList. Your LinkedList should have 10,000 nodes.
    static ArrayList<Graph.Node> BFTIterLinkedList()
    {
        Graph linkedList = createLinkedList(10000);
        return linkedList.GS.BFTIter(linkedList);
    }

    static WeightedGraph createRandomCompleteWeightedGraph(final int n)
    {
        WeightedGraph weightedGraph = new WeightedGraph();

        for ( int i = 0; i < n; i++ )
            weightedGraph.addNode();
        
        for ( int i = 0; i < n; i++ )
        {
            Graph.Node currNode = weightedGraph.nodeList.get(i);

            for ( int j = 0; j < n; j++ )
            {
                if ( j == currNode.id )
                    continue;
                else
                {
                    int newWeight = new Random().nextInt(1000) + 1;
                    Graph.Node newEdge = weightedGraph.nodeList.get(j);
                    weightedGraph.addWeightedEdge(currNode, newEdge, newWeight);
                }
            }
        }

        return weightedGraph;
    }

    static WeightedGraph createLinkedList_Weighted(final int n)
    {
        WeightedGraph weightedGraph = new WeightedGraph();

        // Create initial node
        weightedGraph.addNode();
        // After each new node is created, make the prev point to the new node.
        for ( int i = 1; i < n; i++ )
        {
            weightedGraph.addNode();
            weightedGraph.addWeightedEdge(weightedGraph.nodeList.get(i-1), weightedGraph.nodeList.get(i), 1);
        }

        return weightedGraph;
    }

    static HashMap<Graph.Node,Integer> dijkstras(final Graph.Node start)
    {
        HashMap< Graph.Node,Integer> minimumDistanceMap =  new HashMap< Graph.Node,Integer>();
        HashMap< Graph.Node,Boolean> visited =  new HashMap< Graph.Node,Boolean>();
        HashMap< Graph.Node,Graph.Node> parentNode =  new HashMap< Graph.Node,Graph.Node>();
        PriorityQueue<Graph.Node> queue = new PriorityQueue<Graph.Node>(new Graph.WeightedNodeComparator());

        minimumDistanceMap.put(start, 0);
        queue.add(start);

        while ( !queue.isEmpty() )
        {
            Graph.Node currNode = queue.poll();
            visited.put(currNode, true);

            currNode.weightedEdges.forEach((edgeNode, weight) -> {
                if ( !visited.containsKey(edgeNode))
                {
                    int newDistance = minimumDistanceMap.get(currNode) + weight;
                    double oldDistance = Double.POSITIVE_INFINITY;
                    // if edgeNode has a distance set in minimumDistanceMap it will override being infinity
                    if ( minimumDistanceMap.containsKey(edgeNode) )
                        oldDistance = minimumDistanceMap.get(edgeNode);

                    // Replace distance in map if newDistance is shorter
                    if ( newDistance < oldDistance )
                    {
                        minimumDistanceMap.put(edgeNode, newDistance);
                        parentNode.put(edgeNode, currNode);
                    }
                    
                    edgeNode.g = newDistance;
                    queue.add(edgeNode);
                }
            });
        }

        return minimumDistanceMap;
    }

    static void printDijkstraResults(HashMap<Graph.Node,Integer> result)
    {
        result.forEach((node, weight) -> {
            System.out.println(node.id + " Weight: " + weight + ", ");
        });
    }

    static GridGraph createRandomGridGraph(int n)
    {
        GridGraph gridGraph = new GridGraph();
        // Determines the chance of creating an edge. Lower value increases chance.
        int connectionThreshold = 50;

        // Add nodes with coordinates, row by row
        for ( int i = 0; i < n; i++ )
            for ( int j = 0; j < n; j++ )
                gridGraph.addGridNode(i,j);


        // Nodes are stored in a list from node id 0 to node id n-1
        // Node 0's right node will be Node 1
        // Node 0's bottom node depends on 'n'. When n = 4, Node 0's bottom node will be 4, or i + n
        // Only checking right and bottom from each node will ensure all directions (up, down, left, right) will be checked once
        // Excluding nodes on the bottom row (no nodes below them) and nodes in last column ( no nodes to the right )
        for ( int i = 0; i < n*n; i++ )
        {
            Graph.Node currNode = gridGraph.nodeList.get(i);

            // // Left most nodes in grid dont have a node to the left
            // if ( i % n != 0 )
            // {
            //     int chanceOfConnection =  new Random().nextInt(101);
            //     if ( chanceOfConnection > connectionThreshold )
            //     {
            //         int leftNodeIndex = i - 1;
            //         Graph.Node nodeLeftOfCurrentNode = gridGraph.nodeList.get(leftNodeIndex);
            //         gridGraph.addUndirectedEdge(currNode, nodeLeftOfCurrentNode);
            //     }
            // }

            // Right most nodes in grid dont have a node to the right
            // Prevents making right connections if nodes are in the last column
            // If n = 4 and i = 7, 7 % 4 = 3. 4 - 1 = 3. 3 == 3, so node is in the last column, skip
            if ( i % n != n-1 )
            {
                int chanceOfConnection =  new Random().nextInt(101);
                if ( chanceOfConnection > connectionThreshold )
                {
                    int rightNodeIndex = i + 1;
                    Graph.Node nodeRightOfCurrentNode = gridGraph.nodeList.get(rightNodeIndex);
                    gridGraph.addUndirectedEdge(currNode, nodeRightOfCurrentNode);
                }
            }

            // // All nodes that are NOT in the first row will have a node above it
            // if ( i  >= n )
            // {
            //     int chanceOfConnection =  new Random().nextInt(101);
            //     if ( chanceOfConnection > connectionThreshold )
            //     {
            //         int topNodeIndex = i - n;
            //         Graph.Node nodeAboveCurrentNode = gridGraph.nodeList.get(topNodeIndex);
            //         gridGraph.addUndirectedEdge(currNode, nodeAboveCurrentNode);
            //     }
            //}

            // All nodes that are NOT in the last row will have a node below it
            // Prevents making bottom connections if nodes are in the last row
            // If n = 4, 16 - 1 - 4 = 11. All node id's <= 11 have bottom nodes
            if ( i <= n*n - 1 - n )
            {
                int chanceOfConnection =  new Random().nextInt(101);
                if ( chanceOfConnection > connectionThreshold )
                {
                    int bottomNodeIndex = i + n;
                    Graph.Node nodeBelowCurrentNode = gridGraph.nodeList.get(bottomNodeIndex);
                    gridGraph.addUndirectedEdge(currNode, nodeBelowCurrentNode);
                }
            }

        } // End 'i' for loop

        return gridGraph;

    } // End createRandomGridGraph

    
    static ArrayList<Graph.Node> astar(final Graph.Node sourceNode, final Graph.Node destNode)
    {
        HashMap< Graph.Node,Integer> minimumDistanceMap =  new HashMap< Graph.Node,Integer>();
        HashMap< Graph.Node,Boolean> visited =  new HashMap< Graph.Node,Boolean>();
        HashMap< Graph.Node,Graph.Node> parentNode =  new HashMap< Graph.Node,Graph.Node>();
        //Queue<Graph.Node> queue = new LinkedList<Graph.Node>();
        PriorityQueue<Graph.Node> queue = new PriorityQueue<Graph.Node>(new Graph.UnweightedNodeComparator());

        
        minimumDistanceMap.put(sourceNode, 0);
        queue.add(sourceNode);

        while ( !queue.isEmpty() )
        {
            // Get node with smallest f()   
            Graph.Node currNode = queue.poll();
            visited.put(currNode, true);

            int numEdges = currNode.connectedNodes.size();
            for ( int i = 0; i < numEdges; i++ )
            {
                Graph.Node edgeNode = currNode.connectedNodes.get(i);
                int weight = edgeNode.weight;

                if ( !visited.containsKey(edgeNode))
                {  
                    int newDistance = minimumDistanceMap.get(currNode) + weight;
                    //System.out.println("newDistance is: " + newDistance);

                    double oldDistance = Double.POSITIVE_INFINITY;
                    if ( minimumDistanceMap.containsKey(edgeNode) )
                        oldDistance = minimumDistanceMap.get(edgeNode);

                    if ( newDistance < oldDistance )
                    {
                        minimumDistanceMap.put(edgeNode, newDistance);
                        parentNode.put(edgeNode, currNode);
                    }

                    edgeNode.f = newDistance + calculateHeuristic(edgeNode, destNode);
                    queue.add(edgeNode);
                }
            }

            if ( currNode == destNode )
                break;
        }


        ArrayList<Graph.Node> path = new ArrayList<Graph.Node>();
        Graph.Node currNode = destNode;

        // If the destination node has no parent then no path was found and just return an empty list
        // Otherwise follow the chain of parents to create the path from source to destination
        if ( parentNode.containsKey(destNode) )
        {
            while ( currNode != null )
            {
                path.add(0, currNode);
                currNode = parentNode.get(currNode);
            }
        }

        return path;
    }

    static int calculateHeuristic ( Graph.Node currNode, Graph.Node destNode)
    {
        int xDifference = currNode.x - destNode.x;
        int yDifference = currNode.y - destNode.y;
        int totalDifference = Math.abs(xDifference) + Math.abs(yDifference);

        return totalDifference;
    }


} // end Main class


// https://www.techiedelight.com/increment-keys-value-map-java/
// https://mkyong.com/java/java-how-to-get-keys-and-values-from-map/
// https://www.geeksforgeeks.org/hashmap-containskey-method-in-java/
// https://stackoverflow.com/questions/12952024/how-to-implement-infinity-in-java
// https://www.callicoder.com/java-priority-queue/
// https://www.geeksforgeeks.org/implement-priorityqueue-comparator-java/
// https://www.w3schools.com/java/java_inheritance.asp
// https://www.youtube.com/watch?v=pVfj6mxhdMw
