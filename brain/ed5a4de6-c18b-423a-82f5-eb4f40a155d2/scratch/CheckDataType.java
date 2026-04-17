package org.episteme.scratch;
import org.nd4j.linalg.api.buffer.DataType;
public class CheckDataType {
    public static void main(String[] args) {
        System.out.println("FLOAT: " + DataType.FLOAT);
        System.out.println("DOUBLE: " + DataType.DOUBLE);
        try {
            System.out.println("COMPLEXFLOAT: " + DataType.valueOf("COMPLEXFLOAT"));
            System.out.println("COMPLEXDOUBLE: " + DataType.valueOf("COMPLEXDOUBLE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
