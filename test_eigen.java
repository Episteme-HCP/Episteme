import org.nd4j.linalg.eigen.Eigen;
import java.lang.reflect.Method;
public class TestEigen {
    public static void main(String[] args) {
        for(Method m : Eigen.class.getMethods()) {
            System.out.println(m.getName() + ' ' + java.util.Arrays.toString(m.getParameterTypes()));
        }
    }
}
