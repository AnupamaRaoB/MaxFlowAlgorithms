
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

//import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;


class Graph
{
    int size;
    ArrayList<Edge> edges;
    Graph(int size,ArrayList<Edge> edges)
    {
        this.size=size;
        this.edges=edges;

    }
}

class Edge{
    int source_vertex;
    int destination_vertex;
    int flowcacpcity;

    // Constructor
    Edge(int source, int destination, int flowcacpcity){
        source_vertex = source;
        destination_vertex = destination;
        this.flowcacpcity = flowcacpcity;
    }
}


/* Used for reading data from input file and save them to Edge classes */
class Utils{
    // Get data from file
    public Graph getInputFile(String inputFile)
    {
        ArrayList<Edge> result = new ArrayList<Edge>();
        int count=0;
        File file = new File(inputFile);
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>();

        while (input.hasNextLine()) {
            list.add(input.nextLine());
        }
        //int array
        // make adjacency matrix
        for (String line:list)
        {

            String [] values = line.split(" ");

            int source = Integer.valueOf(values[0]);
            if(values.length==1) {
                int destination=5;
                int flowcacpcity=0;
                Edge edge = new Edge(source, destination, flowcacpcity);
                result.add(edge);
            }
            count++;
            for (int i=1; i<values.length; i++)
            {
                String[] data = values[i].split(":");
                int destination = Integer.valueOf(data[0]);
                int flowcacpcity = Integer.valueOf(data[1]);
                Edge edge = new Edge(source, destination, flowcacpcity);
                result.add(edge);
            }
        }
        Graph g =new Graph(count,result);

        return g;
    }

    public void saveOutputFile(String outputFile, ArrayList<String> data)
    {
        try {
            // write first line
            Files.write(Paths.get(outputFile), data.get(0).getBytes());
            // append other lines
            for (int i = 1; i < data.size(); i++)
            {
                Files.write(Paths.get(outputFile), data.get(i).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




class  FindFlow
{
    int[][] capacitygraph;
    int source;
    int sink;
    int[][] residualgraph;
    int[] visited ;
    int size;
    int maxflow;//=0;
    HashMap<Integer,Integer> adjacenyList;
    int [] parent;
    int []  level;// = new int[size];

    FindFlow(int [] [] capacitygraph,int source,int sink)
    {
        this.capacitygraph= capacitygraph;
        this.source=source;
        this.sink=sink;
        size=capacitygraph.length;




        maxflow=0;



    }





    int  dfs(int source , int flow,int sink)
    {


        //Result result=null;

        int start =source;

        visited[start]=1;

        for (int i = 0; i < residualgraph.length; i++)
        {

            if (start==sink)
            {


                return flow;

            }

            else
            {
                if(residualgraph[start][i]>0 && visited[i]==0)
                {


                    int result=dfs(i,flow> residualgraph[start][i] ?residualgraph[start][i]:flow,sink);
                    if(result!=0)
                    {

                        residualgraph[start][i]-=result;
                        residualgraph[i][start]+=result;
                        return result;

                    }

                }


            }
        }
        //visited[start]=0;
        return 0;
    }



    int findMaxFulkesronFlow()
    {

        int flow =0;

        residualgraph = new int[size][size];
        visited=new int[size];

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
                residualgraph[i][j] = capacitygraph[i][j];
        }




        while (true)
        {


            Arrays.fill(visited, 0);
            maxflow=Integer.MAX_VALUE;
            int pathflow;
            if(!((pathflow = dfs(source,maxflow,sink))==0)) //.min==Integer.MAX_VALUE))
            {



                flow+=pathflow;//.min;

            }

            else break;




        }
        if(flow<=0)
        {
            System.out.println("there are no paths from source to destination ");
        }


        return flow;


    }


    int bfs(int source , int sink, int  min )
    {


        int size = residualgraph.length;
        int visit[]=  new int[size];
        Queue<Integer> queue = new LinkedList<>();
        visit[source]=1;


        parent[source]=Integer.MAX_VALUE;


        queue.add(source);

        //= source ;
        for (int i=0;i<size;i++)
        {
            if(!queue.isEmpty())
            {
                int start = queue.poll();
                if(start!= sink)
                {
                    for(int j=0;j<size;j++)
                    {

                        if(residualgraph[start][j]>0 && visit[j]==0)
                        {
                            queue.add(j);
                            visit[j]=1;

                            //adjacenyList.put(j,start );
                            parent[j]=start;
                            if (j==sink) break;
                        }



                    }
                }


                else break;
            }
            else break;

        }

        if(parent[sink]!=-1)
            for (int i=sink;parent[i]!=Integer.MAX_VALUE;)
            {

                min=residualgraph[parent[i]][i]<min?residualgraph[parent[i]][i]:min;
                i=parent[i];
                //parent[i]=0;

            }




        return min;


    }



    int findMaxEdomonKarpFlow()
    {
        int flow =0;

        residualgraph=new int[size][size];

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
                residualgraph[i][j] = capacitygraph[i][j];
        }
        parent=new int[size];

        while (true)
        {


            maxflow=Integer.MAX_VALUE;
            int pathflow=-1;
            Arrays.fill(parent, -1);

            if(!((pathflow = bfs (source,sink,maxflow))==Integer.MAX_VALUE)) //
            {


                for (int i=sink;parent[i]!=Integer.MAX_VALUE;i=parent[i])
                {

                    residualgraph[parent[i]][i]-=pathflow;

                    residualgraph[i][parent[i]] +=pathflow;

                }


                flow+=pathflow;

            }

            else break;



        }

        if(flow<=0)
        {
            System.out.println("there are no paths from source to destination ");
        }


        return flow;

    }



    int[] bfslevelgraph(int source,int sink)
    {
        int size = residualgraph.length;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        level[source]=0;
        while(!queue.isEmpty())
        {

            int start = queue.poll();

            if(start!= sink)
            {
                for(int j=0;j<size;j++)
                {

                    if(residualgraph[start][j]>0 &&   level[j]==9999) //visited[j]==0
                    {
                        queue.add(j);
                        level[j]=level[start]+1;
                        if (j==sink )break;

                    }

                }
            }


        }



        return level;


    }




    public int pushRelabelFlow() {




        int[] height = new int[size];
        height[source] = size - 1;
        int[] maxHeight = new int[size];
        int[][] flow = new int[size][size];

        int[] excess = new int[size];

        for (int i = 0; i < size; ++i) {
            flow[source][i] = capacitygraph[source][i];
            flow[i][source] = -flow[source][i];
            excess[i] = capacitygraph[source][i];
        }
        // infinite loop
        for (int count = 0;;) {
            if (count == 0) {
                for (int i = 0; i < size; ++i) {
                    if (i != source && i != sink && excess[i] > 0) {
                        if (count != 0 && height[i] > height[maxHeight[0]]) {
                            count = 0;
                        }
                        maxHeight[count++] = i;
                    }
                }
            }
            if (count == 0) {
                break;
            }
            // push part
            while (count != 0) {
                int i = maxHeight[count - 1];
                boolean pushed = false;
                for (int j = 0; j < size && excess[i] != 0; ++j) {
                    if (height[i] == height[j] + 1 && capacitygraph[i][j] - flow[i][j] > 0) {
                        int df = Math.min(capacitygraph[i][j] - flow[i][j], excess[i]);
                        flow[i][j] += df;
                        flow[j][i] -= df;
                        excess[i] -= df;
                        excess[j] += df;
                        if (excess[i] == 0) {
                            --count;
                        }
                        pushed = true;
                    }
                }
                // relabel part
                if (!pushed) {
                    height[i] = Integer.MAX_VALUE;
                    for (int j = 0; j < size; ++j) {
                        if (height[i] > height[j] + 1 && capacitygraph[i][j] - flow[i][j] > 0) {
                            height[i] = height[j] + 1;
                        }
                    }
                    if (height[i] > height[maxHeight[0]]) {
                        count = 0;
                        break;
                    }
                }
            }
        }

        // find max flow
        int maxFlow = 0;
        for (int i = 0; i < size; i++)
            maxFlow += flow[source][i];

        return maxFlow;
    }









    int dinicsDfs(int source , int flow,int sink)
    {
        int start =source;

        //visited[start]=1;


        for (int i = 0; i < residualgraph.length; i++)
        {

            if (start==sink)
            {


                return flow;

            }

            else
            {
                if(residualgraph[start][i]>0 && level[i]==level[start]+1)//level[i]==level[start]+1
                {


                    int result=dinicsDfs(i,flow> residualgraph[start][i] ?residualgraph[start][i]:flow,sink);
                    if(result!=0)
                    {

                        residualgraph[start][i]-=result;
                        residualgraph[i][start]+=result;
                        return result;

                    }

                }


            }
        }
        //visited[start]=0;
        return 0;
    }



    int findDinicsFlow()
    {

        int flow =0;
        int size =capacitygraph.length;
        level=new int[size];
        residualgraph=new int[size][size];


        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                residualgraph[i][j] = capacitygraph[i][j];
            }



        }
        while(true)
        {
            for (int i = 0; i < size; i++)
            {

                level[i]=9999;


            }

            level=bfslevelgraph(source,sink);
            if(level[sink]!=9999)
            {
                //int parent= Integer.MAX_VALUE;

                while (true)
                {


                    //HashMap<Integer,Integer> path= new HashMap<Integer,Integer>();
                    maxflow=Integer.MAX_VALUE;
                    int pathflow;
                    if(!((pathflow = dinicsDfs(source,maxflow,sink))==0))
                    {



                        flow+=pathflow;

                    }

                    else break;

                }

            }
            else break;

        }

        if(flow<=0)
        {
            System.out.println("there are no paths from source to destination ");
        }


        return flow;

    }


}




public  class MaxFlowAlgorithms
{



    public static void main(String[] args)
    {

        if(args.length < 1)
        {
            System.out.println("Error. Provide input a file path as arguments");
            return;
        }

        Scanner sc = new Scanner(System.in);



        Utils utils = new Utils();
        Graph g = utils.getInputFile(args[0]);
        //int size =g.edges.size();
        ArrayList<Edge> edgeList = g.edges;
        int size = g.size;
        int edgesize=edgeList.size();

        int source =edgeList.get(0).source_vertex;
        int destination= edgeList.get(edgesize-1).source_vertex;

        int [][]capacitygraph = new int [size][size];

        for (int i=0;i<edgesize;i++)
        {
            Edge temp = edgeList.get(i);
            capacitygraph[temp.source_vertex][temp.destination_vertex]=temp.flowcacpcity;

        }
        edgeList.clear();




        FindFlow flow= new FindFlow(capacitygraph,source,destination);


/////////////////////////FORD FULKERSON/////////////////

        long startTime = System.currentTimeMillis();
        int maxflow2 = flow.findMaxFulkesronFlow();
        long end = System.currentTimeMillis();
        float elapseFulkerson= (end - startTime) * 0.001f;



////////////////////EDMOND KARP/////////////////
        startTime = System.currentTimeMillis();
        int maxflow1 = flow.findMaxEdomonKarpFlow();
        end = System.currentTimeMillis();
        float elapseEdmondKarp= (end - startTime) * 0.001f ;// LOOP_TIMES;
        // System.out.println("the edmond karp flow is "+maxflow1);


//////////////////DINICS ALGORITHMMMMMMM///////////////////
        startTime = System.currentTimeMillis();
        int maxflow3=flow.findDinicsFlow();
        end = System.currentTimeMillis();
        float elapseDinics= (end - startTime) * 0.001f;
        //System.out.println("the  dinics flow is "+maxflow3);


////////////pushflow/////////////////////////////

        startTime = System.currentTimeMillis();
        int maxflow4=flow.findDinicsFlow();
        end = System.currentTimeMillis();
        float elapsePushFlow= (end - startTime) * 0.001f;
        // System.out.println("the  dinics flow is "+maxflow4);




        System.out.println("the edmond karp flow is "+maxflow1+"\n"+"fukerson flow is "+maxflow2+"\n"+"dinic flow is "+maxflow3+"\n"+"push flow is "+maxflow4);

        String fileName = args[0]+".out";
        try {
            File file = new File(fileName);
            FileWriter fileWritter = new FileWriter(file);
            String content = "Ford-Fulkerson: " + String.valueOf(elapseFulkerson ) +  " sec\n"+ "Edmon karp: " + String.valueOf(elapseEdmondKarp) +  " sec\n"
                    + "Dinics Algorithm : " + String.valueOf(elapseDinics) +" sec\n"+ "Push Relable : " + String.valueOf(elapsePushFlow) + " sec\n\n\n\n" + "Max Flow:   "+maxflow1 ;

            fileWritter.write(content);

            fileWritter.close();
        } catch(IOException e) {
            e.printStackTrace();
        }


        sc.close();

    }

}
