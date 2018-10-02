import javax.swing.*;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Vertex
{
    static int vertexCount;
    static String edgeName = "";
    static mxGraph graph;
    static Object defaultParent;
    Object parent;
    int number;
    int parentNumber;
    String name;
    int v;
    int v1;
    int v2;
    int v3;
    List<Vertex> child = new ArrayList<>();
    Vertex thisParent;
    private Object thisVertex;


    public Vertex(String name, int v, int v1, int v2, int v3)
    {
        this(defaultParent, name, v, v1, v2, v3);
    }
    public Vertex(Object parent, String name, int v, int v1, int v2, int v3)
    {
        vertexCount++;
        this.number = vertexCount;
        this.parent = parent;
        if(name != null) this.name = name;
        else
            this.name = number + "-" + parentNumber;
        this.v = v;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }


    public List<Vertex> getChild()
    {
        return child;
    }


    public void draw()
    {
        this.parentNumber = thisParent != null ? thisParent.number : 0;
        this.name = number + "-" + parentNumber;
        thisVertex = graph.insertVertex(parent, null, name, v, v1, v2, v3);
        Object childVertex;
        for(Vertex child : child)
        {
            child.thisParent = this;
            child.draw();
            graph.insertEdge(parent, null, edgeName, thisVertex, child.thisVertex);
        }
    }


    public void show()
    {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setUseBoundingBox(false);

        layout.execute(parent);
    }
}

public class Main extends JFrame
{
    private static final int MAX_CHILDREN = 5;

    private static int MAX_VERTEX = 20;

    public static void main(String[] args)
    {
        Main frame = new Main();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setVisible(true);
    }
    public Main()
    {
        super("Graph");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        Vertex.defaultParent = parent;
        Vertex.graph = graph;
        graph.getModel().beginUpdate();
        try
        {
            Vertex root = new Vertex(null, 0, 0, 40, 15);
            generateGraph(root);
            root.draw();
            root.show();
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }
    public void generateGraph(Vertex currentRoot)
    {
        if(Vertex.vertexCount >= MAX_VERTEX) return;
        Random rand = new Random();
        Vertex vert;
        for(int i = 0; i < rand.nextInt(MAX_CHILDREN); i++)
        {
            vert = new Vertex(null, 0, 0, 40, 15);
            currentRoot.child.add(vert);
            generateGraph(vert);
        }
    }

}
