import java.util.*;

//import org.graalvm.compiler.graph.Node;


class Graph {

    // Stores all nodes in the graph
    ArrayList<Node> nodeList;
    int numNodes;
    GraphSearch GS = new GraphSearch();

    Graph() 
    { 
        nodeList = new ArrayList<Node>();
        numNodes = 0;
    } 

    class Node 
    {
        // Stores all nodes connected to this node
        ArrayList<Node> connectedNodes; 
        // Stores connected nodes with weights
        Map< Node,Integer> weightedEdges =  new HashMap< Node,Integer>(); 
        // unique identifier for this node
        int id;
        int weight;
        int x;
        int y;
        // Distance travelled so far
        int g;
        int f;


        Node(int newX, int newY, int newId)
        {
            connectedNodes = new ArrayList<Node>();
            id = newId;
            x = newX;
            y = newY;
            weight = 1;
        }

        Node(int newId)
        {
            connectedNodes = new ArrayList<Node>();
            id = newId;
            weight = 0;
        }

        Node(int newId, int newWeight)
        {
            connectedNodes = new ArrayList<Node>();
            id = newId;
            weight = newWeight;
        }

        
    } 

    // Allows priority queue to compare nodes by their total distance travelled, 'g'
    static class WeightedNodeComparator implements Comparator<Node>{ 
              
        public int compare(Node node1, Node node2) { 
            if (node1.g < node2.g) 
                return -1; 
            else if (node1.g > node2.g) 
                return 1; 
            else
                return 0; 
            } 
    } 

    // Allows priority queue to compare nodes by their total distance travelled + a heurisic, 'f'
    static class UnweightedNodeComparator implements Comparator<Node>{ 
          
        public int compare(Node node1, Node node2) { 
            if (node1.f < node2.f) 
                return -1; 
            else if (node1.f > node2.f) 
                return 1; 
            else
                return 0; 
            } 
    } 

    class GraphSearch 
    {

        // Recursively returns an ArrayList of the Nodes in the Graph in a valid Depth-First Search order. 
        // The first node in the array should be start and the last should be end. 
        // If no valid DFS path goes from start to end, return null.
        ArrayList<Node> DFSRec(Graph graph, final Node start, final Node end )
        {
            ArrayList<Node>  path = new ArrayList<Node>();

            if ( start == end )
            {
                System.out.println("Start and end are the same node");
                return path;
            }

            boolean visited[] = new boolean[graph.numNodes];
            visited[start.id] = true;

            path.addAll(DFSRec_helper(graph, start, end, visited));  

            return path;
        }

        ArrayList<Node> DFSRec_helper(Graph graph, final Node start, final Node end, boolean visited[] )
        {
            if ( start == end )
            {
                ArrayList<Node>  path = new ArrayList<Node>();
                path.add(end);
                return path;
            }

            int numEdges = start.connectedNodes.size();
            for ( int i = 0; i < numEdges; i++ )
            {
                // Create a new array each loop and add the start node
                ArrayList<Node>  path = new ArrayList<Node>();
                path.add(start);
                Node nodeToCheck = start.connectedNodes.get(i);

                if ( !visited[nodeToCheck.id] )
                {
                    visited[nodeToCheck.id] = true;
                    path.addAll(DFSRec_helper(graph, nodeToCheck, end, visited));
                }

                if ( path.contains(end) )
                    return path;
                
                // If the path doesnt contain 'end' then discard path, what was returned in recursive call
            }

            // return an empty array if path not found.
            ArrayList<Node>  path = new ArrayList<Node>();

            return path;

        }

    
        // Iteratively returns an ArrayList of the Nodes in the Graph in a valid Depth-First Search order. 
        // The first node in the array should be start and the last should be end. 
        // If no valid DFS path goes from start to end, return null.
        ArrayList<Node> DFSIter( Graph graph, final Node start, final Node end)
        {
            boolean visited[] = new boolean[graph.numNodes];
            Stack<Node> stack = new Stack<Node>();
            Map< Node,Node> parentPath =  new HashMap< Node,Node>(); 
            stack.push(start);

            while ( !stack.isEmpty() )
            {
                Node currNode = stack.pop();
                // end loop when goal node is found
                if ( currNode == end )
                    break;
                // If node has already been visited, skip it
                if ( visited[currNode.id] )
                    continue;
                else
                {
                    visited[currNode.id] = true;
                    int numEdges = currNode.connectedNodes.size();

                    for ( int i = 0; i < numEdges; i++ )
                    {
                        Node edgeNode = currNode.connectedNodes.get(i);
                        if ( !visited[edgeNode.id] )
                        {
                            stack.push( edgeNode );
                            parentPath.put( edgeNode, currNode);
                        }
                    }
                        
                }
            }

            ArrayList<Node>  path = new ArrayList<Node>();
            Node currNode = end;
            while ( currNode != null )
            {
                path.add(0, currNode);
                currNode = parentPath.get(currNode);
            }

            return path;
        }
    
        // Recursively returns an ArrayList of the Nodes in the Graph in a valid Breadth-First Traversal order.
        // Establishes a visited array and a queue to pass to recursive helper function
        ArrayList<Node> BFTRec(final Graph graph)
        {
            ArrayList<Node>  BFT_Order = new ArrayList<Node>();
            int numNodes = graph.numNodes;
            boolean visited[] = new boolean[numNodes];

            // Start at every node to ensure that all nodes are visited for unconnected graphs
            for ( int i = 0; i < numNodes; i++ )
            {
                if ( !visited[i] )
                {
                    visited[i] = true;
                    Node currNode = graph.nodeList.get(i);
                    Queue<Node> queue = new LinkedList<Node>();
                    queue.add(currNode);
                    BFT_Order.addAll( BFTRec_Helper(queue, visited) );  

                }
            }
            return BFT_Order;
        }

        ArrayList<Node> BFTRec_Helper(Queue<Node> queue, boolean visited[] )
        {
            ArrayList<Node>  BFT_Order = new ArrayList<Node>();
            int queueSize = queue.size();

            // If queue is empty we've reached the bottom, return
            if ( queueSize == 0 )
                return BFT_Order;

            // Add new connected nodes only for the nodes that are initially in queue
            for ( int i = 0; i < queueSize; i++ )
            {
                Node currNode = queue.remove();
                BFT_Order.add(currNode);
                int numEdges = currNode.connectedNodes.size();
                // Add all edges of currNode into the queue
                for ( int j = 0; j < numEdges; j++ )
                {
                    Node edgeNode = currNode.connectedNodes.get(j);
                    if ( !visited[edgeNode.id] )
                    {
                        visited[edgeNode.id] = true;
                        queue.add(edgeNode);
                    }
                }
            }

            // Returned arrays will be appended to current array
            BFT_Order.addAll( BFTRec_Helper(queue, visited) );

            return BFT_Order;
        }


    
        // Iteratively returns an ArrayList of all of the Nodes in the Graph in a valid Breadth-First Traversal.
        ArrayList<Node> BFTIter(final Graph graph)
        {
            ArrayList<Node>  BFT_Order = new ArrayList<Node>();
            int numNodes = graph.numNodes;
            boolean visited[] = new boolean[numNodes];
            Queue<Node> queue = new LinkedList<Node>();

            // Loop through all nodes in graph to find unconnected nodes
            for ( int i = 0; i < numNodes; i++ )
            {
                Node currNode = graph.nodeList.get(i);

                if ( !visited[currNode.id] )
                {
                    visited[i] = true;
                    queue.add(currNode);
                }
                else
                    continue;

                while ( !queue.isEmpty() )
                {
                    currNode = queue.remove();
                    BFT_Order.add(currNode);

                    int numEdges = currNode.connectedNodes.size();

                    for ( int j = 0; j < numEdges; j++ )
                    {
                        Node edgeNode =  currNode.connectedNodes.get(j);

                        if ( !visited[edgeNode.id] )
                        {
                            visited[edgeNode.id] = true;
                            queue.add(edgeNode);
                        }
                    }

                } // End while loop

            } // End Initial for loop

            return BFT_Order;
    
        } // End BFTIter
    
    
    } // End GraphSearch class


    void addNode()
    {
        // Create node with an id equal to the number of nodes currently in graph
        Node newNode = new Node(this.numNodes);
        this.nodeList.add(newNode);
        this.numNodes = this.numNodes + 1;
    }

    void addNode(int weight) 
    {
        // Create node with an id equal to the number of nodes currently in graph
        Node newNode = new Node(this.numNodes, weight);
        this.nodeList.add(newNode);
        this.numNodes = this.numNodes + 1;
        
    }

    void addUndirectedEdge(final Node first, final Node second)
    {
        if ( first == second )
        {
            System.out.println("Nodes will not have edges to themselves");
            return;
        }
        
        if (first.connectedNodes.contains(second))
            System.out.println("Edge already exists");
        else
        {
            first.connectedNodes.add(second);
            second.connectedNodes.add(first);
        }
    }

    void removeUndirectedEdge(final Node first, final Node second)
    {
        if (first.connectedNodes.contains(second))
        {
            first.connectedNodes.remove(second);
            second.connectedNodes.remove(first);
        }
        else
            System.out.println("There is no edge between these nodes to remove");
    }

   // Adds a directed edge from first that points to second
    void addDirectedEdge(final Node first, final Node second)
    {
        if ( first == second )
        {
            System.out.println("Nodes will not have edges to themselves");
            return;
        }
        
        if (first.connectedNodes.contains(second))
            System.out.println("Edge already exists");
        else
            first.connectedNodes.add(second);
    }

    // removes the directed edge from first that points to second
    void removeDirectedEdge(final Node first, final Node second)
    {
        if (first.connectedNodes.contains(second))
            first.connectedNodes.remove(second);
        else
            System.out.println("There is no directed edge from first -> second to remove");
    }

    

    HashSet<Node> getAllNodes()
    {
        HashSet<Node> nodeSet = new HashSet<Node>();
        for ( int i = 0; i < this.numNodes; i++ )
            nodeSet.add(this.nodeList.get(i));
        
        return nodeSet;
    }

    

    // Prints each node id along with the nodes it's connected to
    void printNodesWithEdges()
    {
        for ( int i = 0; i < this.numNodes; i++ )
        {
            Node currNode = this.nodeList.get(i);
            System.out.println("Node id: " + currNode.id );
            
            int numEdges = currNode.connectedNodes.size();
            for ( int j = 0; j < numEdges; j++ )
            {
                Node currEdge = currNode.connectedNodes.get(j);
                System.out.print(currEdge.id + ",");
            }

            System.out.println();
        } 

       
    }

} // End Graph class





class DirectedGraph extends Graph 
{

    TopSort TS = new TopSort();

    public void addUndirectedEdge(final Node first, final Node second) {
        throw new UnsupportedOperationException();
    }

    public void removeUndirectedEdge(final Node first, final Node second) {
        throw new UnsupportedOperationException();
    }

    class TopSort 
    {
        ArrayList<Node> Kahns(final DirectedGraph graph)
        {
            int numNodes = graph.numNodes;
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();

            // initialize all nodes to 0 incoming edges
            for ( int i = 0; i < numNodes; i++ )
                map.put(i,0);

            // Go through each node to add a mapping of incoming edges
            for ( int i = 0; i < numNodes; i++ )
            {
                Node currNode = graph.nodeList.get(i);
                int numEdgesInCurrNode = currNode.connectedNodes.size();

                // Each connected node in currNode represents an incoming incoming edge for the connected node
                for ( int j = 0; j < numEdgesInCurrNode; j++ )
                {
                    Node currEdgeNode = currNode.connectedNodes.get(j);

                    // get value of the specified key
                    Integer count = map.get(currEdgeNode.id);
                    // increment the key's value by 1
                    map.put(currEdgeNode.id, count + 1);

                }

            } // End 'i' for loop
            
            Queue<Node> queue = new LinkedList<Node>();
            ArrayList<Node>  topSort = new ArrayList<Node>();

            // Check all nodes for 0 incoming edges
            // This ensures we go through all nodes even if the graph is disconnected 
            for ( int i = 0; i < numNodes; i++ )
            {
                if ( map.get(i) == 0 )
                {
                    Node nodeWithZeroIncoming = graph.nodeList.get(i);
                    queue.add( nodeWithZeroIncoming );

                    while ( !queue.isEmpty() )
                    {
                        Node currNode = queue.remove();
                        topSort.add( currNode );

                        int numedges = currNode.connectedNodes.size();
                        for ( int j = 0; j < numedges; j++ )
                        {
                            Node currEdgeNode = currNode.connectedNodes.get(j);
                            // Get the current amount of incoming edges for connected node
                            Integer currCount = map.get(currEdgeNode.id);

                            // Add node if incomning edges is 0 after visiting
                            // Reduce currCount twice so nodes with 0 incoming edges will be marked as -1 to signify is has been visited
                            if ( currCount - 1 == 0 )
                            {
                                queue.add( currEdgeNode );
                                currCount = currCount - 1;
                            }
                            
                            map.put(currEdgeNode.id, currCount - 1);
                        }

                        

                    } // End while loop
                }
            } // End 'i' loop

            return topSort;

        } // End Khans


        
        ArrayList<Node> mDFS(final DirectedGraph graph)
        {
            Stack<Node> stack = new Stack<Node>();

            int numNodes = graph.numNodes;
            boolean visited[] = new boolean[numNodes];

            for ( int i = 0; i < numNodes; i++ )
            {
                Node currNode = graph.nodeList.get(i);

                if ( !visited[currNode.id] )
                {
                    visited[currNode.id] = true;
                    mDFS_helper(currNode, visited, stack);
                }
            }

            ArrayList<Node>  path = new ArrayList<Node>();
            while ( !stack.isEmpty() )
                path.add( stack.pop() );

            return path;

        } // End mDFS


        void mDFS_helper(Node currNode, boolean visited[], Stack<Node> stack)
        {
            int numEdges = currNode.connectedNodes.size();

            if ( numEdges == 0 )
            {
                stack.push(currNode);
                return;
            }

            for ( int i = 0; i < numEdges; i++ )
            {
                Node currEdge = currNode.connectedNodes.get(i);
                if ( !visited[currEdge.id] )
                {
                    visited[currEdge.id] = true;
                    mDFS_helper(currEdge, visited, stack);
                }
                
            }
            stack.push(currNode);
        }

    } // End TopSort
 
} // End DirectdGraph


class WeightedGraph extends Graph
{

    void addWeightedEdge(final Node first, final Node second, final int edgeWeight)
    {
        if ( first == second )
        {
            System.out.println("Nodes will not have edges to themselves");
            return;
        }
        if ( first.weightedEdges.containsKey(second) )
            System.out.println("Edge already exists");
        else
        {
            first.weightedEdges.put(second, edgeWeight);
        }
    }

    void removeWeightedEdge(final Node first, final Node second)
    {
        if (first.weightedEdges.containsKey(second))
        {
            first.weightedEdges.remove(second);
        }
        else
            System.out.println("There is no directed edge from first -> second to remove");
    }

    

    @Override
    void printNodesWithEdges()
    {
        for ( int i = 0; i < this.numNodes; i++ )
        {
            Node currNode = this.nodeList.get(i);
            System.out.println("Node id: " + currNode.id );
            currNode.weightedEdges.forEach((node, weight) -> {
                System.out.print(node.id + " Weight: " + weight + ", ");
            });
            System.out.println();
        } 
    }
}

class GridGraph extends Graph
{

    void addGridNode(final int x, final int y)
    {
        // Create node with an id equal to the number of nodes currently in graph
        Node newNode = new Node(x, y, this.numNodes);
        this.nodeList.add(newNode);
        this.numNodes = this.numNodes + 1;
    }

    // Override just adds another check to make sure nodes are next to each other
    @Override
    void addUndirectedEdge(final Node first, final Node second)
    {
        if ( first == second )
        {
            System.out.println("Nodes will not have edges to themselves");
            return;
        }
        
        if (first.connectedNodes.contains(second))
        {
            System.out.println("Edge already exists");
            return;
        }

        // Nodes next to each other will have a total coordinate difference of 1
        int xDifference = first.x - second.x;
        int yDifference = first.y - second.y;
        int totalDifference = Math.abs(xDifference) + Math.abs(yDifference);

        if ( totalDifference > 1 )
        {
            System.out.println("Edge's are not directly next to each other.");
            return;
        }
        else
        {
            first.connectedNodes.add(second);
            second.connectedNodes.add(first);
        }
    }

}

