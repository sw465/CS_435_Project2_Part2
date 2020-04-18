import java.util.*;

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

            } 
            
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

                        int numEdges = currNode.connectedNodes.size();
                        for ( int j = 0; j < numEdges; j++ )
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

                        

                    } 
                }
            } 

            return topSort;

        } 


        
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

        } 


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

    } 
 
} 