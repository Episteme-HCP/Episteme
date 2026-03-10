package org.episteme;
import org.nd4j.linalg.eigen.Eigen;
import java.lang.reflect.Method;
public class TestEigen {
    public static void main(String[] args) {
        System.out.println("EIGEN_METHODS_START");
        for(Method m : Eigen.class.getMethods()) {
            System.out.print(m.getName() + "(");
            for (Class<?> p : m.getParameterTypes()) {
                System.out.print(p.getSimpleName() + ",");
            }
            System.out.println(") -> " + m.getReturnType().getSimpleName());
        }
        System.out.println("EIGEN_METHODS_END");
    }
}
