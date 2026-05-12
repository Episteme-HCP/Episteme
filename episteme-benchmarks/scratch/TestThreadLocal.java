
import org.episteme.core.mathematics.context.MathContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadLocal {
    public static void main(String[] args) throws Exception {
        MathContext.setCurrent(MathContext.exact());
        System.out.println("Main thread: " + MathContext.getCurrent().getRealPrecision());
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            System.out.println("Executor thread: " + MathContext.getCurrent().getRealPrecision());
        }).get();
        executor.shutdown();
    }
}
