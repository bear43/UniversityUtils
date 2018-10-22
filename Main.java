import javax.swing.*;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Vertex
{
    static int vertexCount;
    static mxGraph graph;
    static Object defaultParent;
    private Object parent;
    private int number;
    private int parentNumber;
    private String name;
    private int v;
    private int v1;
    private int v2;
    private int v3;
    List<Vertex> child = new ArrayList<>();
    private Vertex thisParent;
    private Object thisVertex;


    Vertex(String name, int v, int v1, int v2, int v3)
    {
        this(defaultParent, name, v, v1, v2, v3);
    }
    private Vertex(Object parent, String name, int v, int v1, int v2, int v3)
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


    void draw()
    {
        this.parentNumber = thisParent != null ? thisParent.number : 0;
        this.name = number + "-" + parentNumber;
        thisVertex = graph.insertVertex(parent, null, name, v, v1, v2, v3);
        graph.updateCellSize(thisVertex);
        for (Vertex child : child)
        {
            child.thisParent = this;
            child.draw();
            graph.insertEdge(parent, null, "", thisVertex, child.thisVertex);
        }
    }


    void show()
    {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setUseBoundingBox(false);

        layout.execute(parent);
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    private static void getVertices(List<Vertex> out, List<Vertex> currentLevelVertices)
    {
        if(currentLevelVertices.size() == 0) return;
        out.addAll(currentLevelVertices);
        ArrayList<Vertex> nextLevelVertices = new ArrayList<>();
        for(Vertex vertex : currentLevelVertices)
            nextLevelVertices.addAll(vertex.child);
        getVertices(out, nextLevelVertices);
    }

    List<Vertex> getAllVertices()
    {
        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> ret = new ArrayList<>();
        vertices.add(this);
        getVertices(ret, vertices);
        return ret;
    }
}

public class Main extends JFrame
{
    private static int MAX_CHILDREN ;

    private static int MAX_VERTEX ;

    private static int MIN_CHILDREN;

    private static boolean MIN_GENERATION;

    private static int FONT_SIZE = 11;

    private static Random rand = new Random();

    private static int width = 30;
    private static int height = 12;

    private static Vertex root;

    public static void main(String[] args) throws Exception
    {
        Scanner scn = new Scanner(System.in);
        System.out.print("Кол-во вершин(узлов): ");
        MAX_VERTEX = scn.nextInt();
        System.out.print("Максимальное кол-во потомков от одной вершины: ");
        MAX_CHILDREN = scn.nextInt()+1;
        System.out.print("Минимальное кол-во потомков от одной вершины: ");
        MIN_CHILDREN = scn.nextInt();
        if (MIN_CHILDREN == 0)
        {
            String answer;
            System.out.print("Принудительно генерировать все дерево вершин(Хотя бы одна вершина на уровне дает потомка)(Y/N): ");
            answer = scn.next();
            MIN_GENERATION = answer.toLowerCase().equals("y");
        }
        System.out.print("Размер шрифта: ");
        FONT_SIZE = scn.nextInt();
        Main frame = new Main();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
    private Main() throws Exception
    {
        super("Graph");

        mxGraph graph = new mxGraph();
        graph.setAutoSizeCells(true);
        Vertex.defaultParent = graph.getDefaultParent();
        Vertex.graph = graph;
        graph.getModel().beginUpdate();
        try
        {
            mxStylesheet style = new mxStylesheet();
            style.getDefaultVertexStyle().put(mxConstants.STYLE_FONTSIZE, FONT_SIZE);
            graph.setStylesheet(style);
            root = new Vertex(null, 0, 0, width, height);
            generateGraph(new Vertex[] {root});
            root.draw();
            root.show();
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        FileWriter vertexTableFile = new FileWriter("Vertex_table.txt");
        FileWriter singleVertexTableFile = new FileWriter("Single_Vertex_Table.txt");
        printVertices(root.getAllVertices(), vertexTableFile, singleVertexTableFile);
        vertexTableFile.close();
        singleVertexTableFile.close();
    }

    private void printVertices(List<Vertex> vertices, FileWriter vertexTableFile, FileWriter singleVertexTableFile) throws Exception
    {
        for(Vertex vertex : vertices)
        {
            if(vertex.child.size() == 0)
                singleVertexTableFile.append(vertex.toString()).append(",");
            vertexTableFile.append(vertex.toString()).append(",");
        }
    }

    private void generateGraph(Vertex[] thisLevelRoots)
    {
        if(thisLevelRoots.length == 0) return;
        Vertex vert;
        List<Vertex> children = new ArrayList<>();
        int count;
        boolean ok = false;
        for(int a = 0; a < thisLevelRoots.length; a++)
        {
            count = rand.nextInt(MAX_CHILDREN);
            if(count != 0) ok = true;
            while(count < MIN_CHILDREN || (MIN_GENERATION && !ok && count == 0 && thisLevelRoots.length-1-a == 0))
                count = rand.nextInt(MAX_CHILDREN);
            for (int i = 0; i < count; i++)
            {
                if (Vertex.vertexCount >= MAX_VERTEX) return;
                vert = new Vertex(null, 0, 0, width, height);
                children.add(vert);
                thisLevelRoots[a].child.add(vert);
            }
        }
        generateGraph(children.toArray(new Vertex[0]));
    }
}
