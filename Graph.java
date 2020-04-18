import java.util.*;

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
        ArrayList<Node> connectedNodes; 
        // Stores connected nodes with weights
        Map< Node,Integer> weightedEdges =  new HashMap< Node,Integer>(); 
        int id;
        int weight;
        int x;
        int y;
        // Includes the heuristic value when using A*
        int distance;


        // Specific constructor for gridgraphs
        Node(int newX, int newY, int newId)
        {
            connectedNodes = new ArrayList<Node>();
            id = newId;
            x = newX;
            y = newY;
            weight = 1;
        }

        // Specific constructor for unweighted graphs
        Node(int newId)
        {
            connectedNodes = new ArrayList<Node>();
            id = newId;
            weight = 0;
        }

        // Specific constructor for weighted graphs
        Node(int newId, int newWeight)
        {
            connectedNodes = new ArrayList<Node>();
            id = newId;
            weight = newWeight;
        }

        
    } 


    // Allows priority queue to compare nodes by their total distance travelled 
    static class NodeComparator implements Comparator<Node>{ 
          
        public int compare(Node node1, Node node2) { 
            if (node1.distance < node2.distance) 
                return -1; 
            else if (node1.distance > node2.distance) 
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
                return path;
            }

            boolean visited[] = new boolean[graph.numNodes];
            visited[start.id] = true;

            path.addAll(DFSRec_helper(graph, start, end, visited));  

            return path;
        }

        ArrayList<Node> DFSRec_helper(Graph graph, final Node start, final Node end, boolean visited[] )
        {
            // Start is adjusted each recursive call. If the node passed as start is equal to end then we've found a path.
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

                if ( path.get(path.size() - 1) == end )
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

                } 

            } 

            return BFT_Order;
    
        } 
    
    
    } 


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
            return;
        }
        else if (first.connectedNodes.contains(second))
            return;       
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
    }

   // Adds a directed edge from first that points to second
    void addDirectedEdge(final Node first, final Node second)
    {
        if ( first == second )
        {
            return;
        }
        else if (first.connectedNodes.contains(second))
            return;
        else
            first.connectedNodes.add(second);
    }

    // removes the directed edge from first that points to second
    void removeDirectedEdge(final Node first, final Node second)
    {
        if (first.connectedNodes.contains(second))
            first.connectedNodes.remove(second);

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

} 