import org.episteme.nativ.technical.backend.gpu.cuda.CUDAManager;

public class CheckCUDA {
    public static void main(String[] args) {
        System.out.println("CUDA Available: " + CUDAManager.isAvailable());
        if (!CUDAManager.isAvailable()) {
            System.out.println("Check why it's not available...");
        }
    }
}
