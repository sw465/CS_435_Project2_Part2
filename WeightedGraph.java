import java.util.*;

class WeightedGraph extends Graph
{

    void addWeightedEdge(final Node first, final Node second, final int edgeWeight)
    {
        if ( first == second )
        {
            return;
        }
        else if ( first.weightedEdges.containsKey(second) )
            return;
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