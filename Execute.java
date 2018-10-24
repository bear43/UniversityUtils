import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Execute
{
    private static double getAverage(List<Double> list)
    {
        double average = 0.0;
        for(double d : list)
            average += d;
        return average/(double)list.size();
    }

    private static double getDispersion(List<Double> list, double average)
    {
        double dispersion = 0.0;
        double current;
        for(double d : list)
        {
            current = d-average;
            dispersion += current*current;
        }
        return dispersion/(double)list.size();
    }

    public static void main(String[] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader("Common_table.txt"));
        String currentLine = br.readLine();
        String[] params;
        List<Double> alpha = new ArrayList<>();
        List<Double> singleVertex = new ArrayList<>();
        List<Double> level = new ArrayList<>();
        while(currentLine != null && !currentLine.isEmpty())
        {
            params = currentLine.split(" ");
            singleVertex.add((double)Integer.parseInt(params[3]));
            level.add((double)Integer.parseInt(params[4]));
            alpha.add(Double.parseDouble(params[1]));
            currentLine = br.readLine();
        }
        br.close();
        double avg = getAverage(alpha);
        System.out.println("Средняя альфа: " + avg);
        System.out.println("Дисперсия: " + getDispersion(alpha, avg));
        avg = (double)Math.round(getAverage(singleVertex));
        System.out.println("Среднее число висячих вершин: " + avg);
        System.out.println("Дисперсия: " + getDispersion(singleVertex, avg));
        avg = (double)Math.round(getAverage(level));
        System.out.println("Среднее число уровней: " + avg);
        System.out.println("Дисперсия: " + getDispersion(level, avg));
    }
}
