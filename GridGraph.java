import java.util.*;

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

        // Nodes next to each other will have a total coordinate difference of 1
        int xDifference = first.x - second.x;
        int yDifference = first.y - second.y;
        int totalDifference = Math.abs(xDifference) + Math.abs(yDifference);

        if ( first == second )
        {
            return;
        }
        else if (first.connectedNodes.contains(second))
        {
            return;
        }
        else if ( totalDifference > 1 )
        {
            return;
        }
        else
        {
            first.connectedNodes.add(second);
            second.connectedNodes.add(first);
        }
    }

}