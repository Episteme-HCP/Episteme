/*
 * OpenCL Kernels for Basic Vision Operations
 */

// Simple grayscale conversion: (R+G+B)/3
// Input: int array (ARGB)
// Output: int array (ARGB)
__kernel void grayscale_kernel(__global const int* input, __global int* output, int width, int height) {
    int gid = get_global_id(0);
    int total = width * height;
    
    if (gid < total) {
        int pixel = input[gid];
        int alpha = (pixel >> 24) & 0xFF;
        int red   = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8)  & 0xFF;
        int blue  =  pixel        & 0xFF;
        
        int gray = (red + green + blue) / 3;
        
        output[gid] = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
    }
}

// Invert colors
__kernel void invert_kernel(__global const int* input, __global int* output, int width, int height) {
    int gid = get_global_id(0);
    int total = width * height;
    
    if (gid < total) {
        int pixel = input[gid];
        int alpha = (pixel >> 24) & 0xFF;
        int red   = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8)  & 0xFF;
        int blue  =  pixel        & 0xFF;
        
        output[gid] = (alpha << 24) | ((255 - red) << 16) | ((255 - green) << 8) | (255 - blue);
    }
}

// Simple threshold
__kernel void threshold_kernel(__global const int* input, __global int* output, int width, int height, int threshold) {
    int gid = get_global_id(0);
    int total = width * height;
    
    if (gid < total) {
        int pixel = input[gid];
        int alpha = (pixel >> 24) & 0xFF;
        int red   = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8)  & 0xFF;
        int blue  =  pixel        & 0xFF;
        
        int intensity = (red + green + blue) / 3;
        int val = (intensity > threshold) ? 255 : 0;
        
        output[gid] = (alpha << 24) | (val << 16) | (val << 8) | val;
    }
}

// Box Blur (3x3)
__kernel void blur_kernel(__global const int* input, __global int* output, int width, int height) {
    int x = get_global_id(0);
    int y = get_global_id(1);
    
    if (x < width && y < height) {
        int rSum = 0, gSum = 0, bSum = 0, count = 0;
        
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = x + dx;
                int ny = y + dy;
                
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    int pixel = input[ny * width + nx];
                    rSum += (pixel >> 16) & 0xFF;
                    gSum += (pixel >> 8)  & 0xFF;
                    bSum +=  pixel        & 0xFF;
                    count++;
                }
            }
        }
        
        int r = rSum / count;
        int g = gSum / count;
        int b = bSum / count;
        int alpha = (input[y * width + x] >> 24) & 0xFF;
        
        output[y * width + x] = (alpha << 24) | (r << 16) | (g << 8) | b;
    }
}
