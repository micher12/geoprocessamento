package components;

public class Tempo {
    
    private long start;
    private long end;


    public Tempo(){
        start = System.nanoTime();
    }


    public long getTime(){
        end = System.nanoTime();

        return (end - start) / 1_000_000;
    }

}
