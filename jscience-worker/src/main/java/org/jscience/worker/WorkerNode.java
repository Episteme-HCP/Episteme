/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.worker;

import org.jscience.physics.fluid.LatticeBoltzmann;
import org.jscience.physics.classical.mechanics.VelocityVerlet;
import org.jscience.physics.classical.mechanics.Particle;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jscience.economics.DistributedEconomyTask;
import org.jscience.politics.GeopoliticalEngineTask;
import org.jscience.server.proto.*;
import org.jscience.earth.climate.ClimateModelTask;
import org.jscience.biology.structure.DnaFoldingTask;
import org.jscience.biology.genome.CrisprTask;
import org.jscience.biology.structure.ProteinFoldingTask;
import org.jscience.physics.wave.WaveModelTask;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * JScience Worker Node.
 * Supports a wide range of scientific computation tasks.
 * 
 * <p>
 * Standardized execution for both legacy legacy (UTF-header) and modern
 * (Object-serialized) tasks.
 * </p>
 */
public class WorkerNode {

    private final ManagedChannel channel;
    private final ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private String workerId;

    public WorkerNode(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
    }

    public void start() {
        register();
        while (true) {
            try {
                pollAndExecute();
                Thread.sleep(100);
            } catch (Exception e) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
    }

    private void register() {
        WorkerRegistration reg = WorkerRegistration.newBuilder()
                .setHostname("worker-" + System.currentTimeMillis())
                .setCores(Runtime.getRuntime().availableProcessors()).build();
        this.workerId = blockingStub.registerWorker(reg).getWorkerId();
    }

    private void pollAndExecute() {
        TaskRequest task = blockingStub.requestTask(WorkerIdentifier.newBuilder().setWorkerId(workerId).build());
        if (task.getTaskId().isEmpty())
            return;

        try {
            byte[] resultBytes = executeTask(task);
            blockingStub.submitResult(TaskResult.newBuilder()
                    .setTaskId(task.getTaskId()).setStatus(Status.COMPLETED)
                    .setSerializedData(ByteString.copyFrom(resultBytes)).build());
        } catch (Exception e) {
            blockingStub.submitResult(TaskResult.newBuilder()
                    .setTaskId(task.getTaskId()).setStatus(Status.FAILED).setErrorMessage(e.toString()).build());
        }
    }

    private byte[] executeTask(TaskRequest task) throws Exception {
        byte[] taskData = task.getSerializedTask().toByteArray();
        String legacyType = "";

        // Attempt to detect legacy UTF header
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(taskData))) {
            legacyType = dis.readUTF();
        } catch (Exception e) {
            // Not a legacy task
        }

        return switch (legacyType) {
            case "CLIMATE" -> executeClimateLegacy(taskData);
            case "DNA_FOLDING" -> executeDnaLegacy(taskData);
            case "FLUID_LBM" -> executeFluidLegacy(taskData);
            case "MOLECULAR_DYNAMICS" -> executeMolecularLegacy(taskData);
            case "WAVE_2D" -> executeWaveLegacy(taskData);
            case "MANDELBROT" -> executeMandelbrotLegacy(taskData);
            case "MONTECARLO_PI" -> executeMonteCarloLegacy(taskData);
            case "NBODY" -> executeNBodyLegacy(taskData);
            default -> {
                Object obj = deserialize(taskData);
                if (obj instanceof ProteinFoldingTask)
                    yield executeProteinFolding((ProteinFoldingTask) obj);
                if (obj instanceof CrisprTask)
                    yield executeCrisprScan((CrisprTask) obj);
                if (obj instanceof GeopoliticalEngineTask)
                    yield executePolitics((GeopoliticalEngineTask) obj);
                if (obj instanceof DistributedEconomyTask)
                    yield executeEconomy((DistributedEconomyTask) obj);
                throw new IllegalArgumentException("Unsupported task format or missing legacy header: " + legacyType);
            }
        };
    }

    private byte[] executeEconomy(DistributedEconomyTask task) throws Exception {
        task.run();
        return serialize(task);
    }

    private byte[] executePolitics(GeopoliticalEngineTask task) throws Exception {
        task.run();
        return serialize(task);
    }

    private byte[] executeProteinFolding(ProteinFoldingTask task) throws Exception {
        task.run();
        return serialize(task.getResult());
    }

    private byte[] executeCrisprScan(CrisprTask task) throws Exception {
        return serialize(task.scan());
    }

    private byte[] executeClimateLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF(); // Skip header
        int rows = dis.readInt();
        int cols = dis.readInt();
        double dt = dis.readDouble();
        ClimateModelTask model = new ClimateModelTask(rows, cols);
        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                temp[i][j] = dis.readDouble();
        model.updateState(temp);
        model.runStep(dt);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(rows);
        dos.writeInt(cols);
        double[][] result = model.getTemperatureMap();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                dos.writeDouble(result[i][j]);
        return bos.toByteArray();
    }

    private byte[] executeDnaLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF();
        String seq = dis.readUTF();
        double temp = dis.readDouble();
        int count = dis.readInt();
        List<DnaFoldingTask.Point3D> points = new ArrayList<>();
        for (int i = 0; i < count; i++)
            points.add(new DnaFoldingTask.Point3D(dis.readDouble(), dis.readDouble(), dis.readDouble()));
        DnaFoldingTask task = new DnaFoldingTask(seq, 200, temp);
        task.updateState(points, 0);
        task.run();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeDouble(task.getFinalEnergy());
        dos.writeInt(task.getFoldedStructure().size());
        for (DnaFoldingTask.Point3D p : task.getFoldedStructure()) {
            dos.writeDouble(p.x());
            dos.writeDouble(p.y());
            dos.writeDouble(p.z());
        }
        return bos.toByteArray();
    }

    private byte[] executeMolecularLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF();
        int n = dis.readInt();
        double dt = dis.readDouble();
        dis.readInt(); // Consume steps count
        double size = dis.readDouble();
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Particle p = new Particle(dis.readDouble(), dis.readDouble(), dis.readDouble(), dis.readDouble());
            p.setVelocity(dis.readDouble(), dis.readDouble(), dis.readDouble());
            particles.add(p);
        }
        VelocityVerlet solver = new VelocityVerlet(particles, size, 2.5);
        solver.step(dt);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeDouble(0.0); // Placeholder for energy
        dos.writeInt(n);
        for (Particle a : particles) {
            dos.writeDouble(a.getX());
            dos.writeDouble(a.getY());
            dos.writeDouble(a.getZ());
            dos.writeDouble(a.getVelocity().get(0).doubleValue());
            dos.writeDouble(a.getVelocity().get(1).doubleValue());
            dos.writeDouble(a.getVelocity().get(2).doubleValue());
        }
        return bos.toByteArray();
    }

    private byte[] executeWaveLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF();
        int w = dis.readInt();
        int h = dis.readInt();
        double c = dis.readDouble();
        double damp = dis.readDouble();
        WaveModelTask task = new WaveModelTask(w, h);
        task.setC(c);
        task.setDamping(damp);
        double[][] u = new double[w][h];
        double[][] up = new double[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                u[x][y] = dis.readDouble();
                up[x][y] = dis.readDouble();
            }
        task.updateState(u, up);
        task.step();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        double[][] ru = task.getU();
        double[][] rup = task.getUPrev();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                dos.writeDouble(ru[x][y]);
                dos.writeDouble(rup[x][y]);
            }
        return bos.toByteArray();
    }

    private byte[] executeFluidLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF();
        int w = dis.readInt();
        int h = dis.readInt();
        double visc = dis.readDouble();
        dis.readDouble(); // Consume inlet velocity
        boolean[][] obs = new boolean[w][h];
        double[][][] f = new double[w][h][9];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                obs[x][y] = dis.readBoolean();
                for (int i = 0; i < 9; i++)
                    f[x][y][i] = dis.readDouble();
            }
        }
        LatticeBoltzmann lbm = new LatticeBoltzmann(w, h, visc);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                lbm.setObstacle(x, y, obs[x][y]);
            }
        }
        // Copy distributions to lbm.f (simplified access)
        double[][][] dist = lbm.getDistributions();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                System.arraycopy(f[x][y], 0, dist[x][y], 0, 9);

        lbm.step();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        double[][] rho = lbm.getDensity();
        double[][][] resultF = lbm.getDistributions();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                dos.writeDouble(rho[x][y]);
                dos.writeDouble(0.0); // ux
                dos.writeDouble(0.0); // uy
                for (int i = 0; i < 9; i++)
                    dos.writeDouble(resultF[x][y][i]);
            }
        return bos.toByteArray();
    }

    private byte[] executeMandelbrotLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF();
        int startY = dis.readInt();
        int endY = dis.readInt();
        double minRe = dis.readDouble();
        double maxRe = dis.readDouble();
        double minIm = dis.readDouble();
        double maxIm = dis.readDouble();
        int maxIter = dis.readInt();
        int width = dis.readInt();
        int height = dis.readInt();
        int[] escape = new int[(endY - startY) * width];
        for (int y = startY; y < endY; y++) {
            double cIm = maxIm - y * (maxIm - minIm) / (height - 1);
            for (int x = 0; x < width; x++) {
                double cRe = minRe + x * (maxRe - minRe) / (width - 1);
                double zRe = 0, zIm = 0;
                int n = 0;
                while (zRe * zRe + zIm * zIm <= 4 && n < maxIter) {
                    double tr = zRe * zRe - zIm * zIm + cRe;
                    zIm = 2 * zRe * zIm + cIm;
                    zRe = tr;
                    n++;
                }
                escape[(y - startY) * width + x] = n;
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(escape.length);
        for (int v : escape)
            dos.writeInt(v);
        return bos.toByteArray();
    }

    private byte[] executeMonteCarloLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF();
        long n = dis.readLong();
        long inside = 0;
        Random r = new Random();
        for (long i = 0; i < n; i++) {
            double x = r.nextDouble() * 2 - 1;
            double y = r.nextDouble() * 2 - 1;
            if (x * x + y * y <= 1)
                inside++;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeLong(inside);
        dos.writeLong(n);
        return bos.toByteArray();
    }

    private byte[] executeNBodyLegacy(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        dis.readUTF();
        int count = dis.readInt();
        double dt = dis.readDouble();
        double G = dis.readDouble();
        double[] x = new double[count], y = new double[count], z = new double[count];
        double[] vx = new double[count], vy = new double[count], vz = new double[count], mass = new double[count];
        for (int i = 0; i < count; i++) {
            x[i] = dis.readDouble();
            y[i] = dis.readDouble();
            z[i] = dis.readDouble();
            vx[i] = dis.readDouble();
            vy[i] = dis.readDouble();
            vz[i] = dis.readDouble();
            mass[i] = dis.readDouble();
        }
        // Direct O(n2) for legacy compatibility
        for (int i = 0; i < count; i++) {
            double ax = 0, ay = 0, az = 0;
            for (int j = 0; j < count; j++) {
                if (i == j)
                    continue;
                double dx = x[j] - x[i], dy = y[j] - y[i], dz = z[j] - z[i];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz + 100);
                double f = G * mass[j] / (dist * dist * dist);
                ax += f * dx;
                ay += f * dy;
                az += f * dz;
            }
            vx[i] += ax * dt;
            vy[i] += ay * dt;
            vz[i] += az * dt;
            x[i] += vx[i] * dt;
            y[i] += vy[i] * dt;
            z[i] += vz[i] * dt;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(count);
        for (int i = 0; i < count; i++) {
            dos.writeDouble(x[i]);
            dos.writeDouble(y[i]);
            dos.writeDouble(z[i]);
            dos.writeDouble(vx[i]);
            dos.writeDouble(vy[i]);
            dos.writeDouble(vz[i]);
        }
        return bos.toByteArray();
    }

    private byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        return bos.toByteArray();
    }

    private Object deserialize(byte[] data) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    public static void main(String[] args) {
        new WorkerNode(args.length > 0 ? args[0] : "localhost", args.length > 1 ? Integer.parseInt(args[1]) : 50051)
                .start();
    }
}
