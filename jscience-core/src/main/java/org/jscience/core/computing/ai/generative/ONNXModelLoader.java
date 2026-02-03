package org.jscience.core.computing.ai.generative;

import ai.onnxruntime.*;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.numbers.real.Real;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility for loading and executing ONNX models.
 */
public class ONNXModelLoader implements AutoCloseable {
    private final OrtEnvironment env;
    private final OrtSession session;

    public ONNXModelLoader(String modelPath) throws OrtException {
        this.env = OrtEnvironment.getEnvironment();
        this.session = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    public Vector<Real> runInference(float[] inputData, String inputName) throws OrtException {
        long[] shape = {1, inputData.length};
        try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(inputData), shape)) {
            try (OrtSession.Result results = session.run(Collections.singletonMap(inputName, inputTensor))) {
                float[][] output = (float[][]) results.get(0).getValue();
                List<Real> resultList = new ArrayList<>();
                for (float f : output[0]) {
                    resultList.add(Real.of(f));
                }
                return new DenseVector<>(resultList, org.jscience.core.mathematics.sets.Reals.getInstance());
            }
        }
    }

    @Override
    public void close() throws OrtException {
        session.close();
        env.close();
    }
}
