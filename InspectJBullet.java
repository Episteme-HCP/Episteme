import com.bulletphysics.linearmath.Transform;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class InspectJBullet {
    public static void main(String[] args) {
        System.out.println("Transform methods:");
        for (Method m : Transform.class.getMethods()) {
            if (m.getName().toLowerCase().contains("rotation")) {
                System.out.println("  " + m);
            }
        }
        System.out.println("Transform fields:");
        for (Field f : Transform.class.getFields()) {
            System.out.println("  " + f.getType().getName() + " " + f.getName());
        }
        
        try {
            Class<?> qClass = Class.forName("com.bulletphysics.linearmath.Quaternion");
            System.out.println("Found com.bulletphysics.linearmath.Quaternion");
        } catch (ClassNotFoundException e) {
            System.out.println("com.bulletphysics.linearmath.Quaternion NOT found");
            try {
                Class<?> qClass = Class.forName("javax.vecmath.Quat4f");
                System.out.println("Found javax.vecmath.Quat4f");
            } catch (ClassNotFoundException e2) {
                System.out.println("javax.vecmath.Quat4f NOT found");
            }
        }
    }
}
