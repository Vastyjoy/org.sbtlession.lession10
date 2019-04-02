import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PerformanceTest {
    public static void main(String[] args) throws IllegalAccessException {
        List<Integer> listInteger=new ArrayList<>();
        Random random=new Random();
        int size=1000000;
        for(int i=0; i<size;i++){
            listInteger.add(random.nextInt(1000));
        }

        int countExperiment=200;
        long runTimeOptimize=0;
        long runTimeNotOptimize=0;
        long runTimeStreamAPI=0;
        for(int i=0; i<countExperiment;i++){
            long curTimeBefore=System.nanoTime();
            List<Integer> stream=listInteger.stream().
                    filter(p->p>100)
                    .filter(p->p<900)
                    .filter(p->p>350)
                    .filter(p->p!=400)
                    .filter(p->p!=521)
                    .filter(p->p!=356)
                    .collect(Collectors.toList());
            long curTimeAfter=System.nanoTime();
            runTimeStreamAPI+=curTimeAfter-curTimeBefore;
        }
        runTimeStreamAPI=runTimeStreamAPI/countExperiment;
        for(int i=0; i<countExperiment; i++){
            long curTimeBefore=System.nanoTime();
            Streams<Integer> optimizeStream=Streams.of(listInteger);
            List<Integer>  map=optimizeStream.filter(p->p>100)
                    .filter(p->p<900)
                    .filter(p->p>350)
                    .filter(p->p!=400)
                    .filter(p->p!=521)
                    .filter(p->p!=356)
                    .toList();
            long curTimeAfter=System.nanoTime();
            runTimeOptimize+=curTimeAfter-curTimeBefore;
        }
        runTimeOptimize=runTimeOptimize/countExperiment;

        for(int i=0; i<countExperiment; i++){
            long curTimeBefore=System.nanoTime();
            Streams<Integer> notOptimizeStream=Streams.of(listInteger);
            notOptimizeStream.setOptimize(false);
            List<Integer>  map=notOptimizeStream.filter(p->p>100)
                    .filter(p->p<900)
                    .filter(p->p>350)
                    .filter(p->p!=400)
                    .filter(p->p!=521)
                    .filter(p->p!=356)
                    .toList();
            long curTimeAfter=System.nanoTime();
            runTimeNotOptimize+=curTimeAfter-curTimeBefore;
        }
        runTimeNotOptimize=runTimeNotOptimize/countExperiment;
        System.out.println("Количество экспериментов:"+countExperiment);
        System.out.println("Размер списка:"+size);
        System.out.println("Время выполнения StreamAPI:"+runTimeStreamAPI);
        System.out.println("Время выполнения оптимизированного: "+ runTimeOptimize);
        System.out.println("Время выполнения неоптимизированного: "+ runTimeNotOptimize);
        System.out.println("Разница timeNotOptimize/timeOptimize:"+((double)runTimeNotOptimize/(double)runTimeOptimize));
        System.out.println("Разница timeStreamAPI/timeOptimize:"+((double)runTimeStreamAPI/(double)runTimeOptimize));

    }
}
