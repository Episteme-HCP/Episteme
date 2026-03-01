#include <cstdint>

#if defined(_MSC_VER)
    #define EXPORT __declspec(dllexport)
#else
    #define EXPORT __attribute__((visibility("default")))
#endif

extern "C" {

/**
 * Basic image processing function called from Java's Foreign Function & Memory API (Panama)
 * 
 * @param pixels Raw ARGB int mapped memory segment
 * @param width Image width in pixels
 * @param height Image height in pixels
 * @param opCode Processing operation definition (e.g. 1 = grayscale)
 */
EXPORT void process_image(int32_t* pixels, int32_t width, int32_t height, int32_t opCode) {
    int32_t totalPixels = width * height;

    if (opCode == 1) { // Grayscale Example
        // Note: The C++ compiler will likely vectorize this loop using AVX/AVX2 automatically
        for (int32_t i = 0; i < totalPixels; ++i) {
            int32_t argb = pixels[i];
            
            // Extract ARGB channels
            int32_t a = (argb >> 24) & 0xFF;
            int32_t r = (argb >> 16) & 0xFF;
            int32_t g = (argb >> 8) & 0xFF;
            int32_t b = argb & 0xFF;
            
            // Compute luma grayscale using standard BT.601 weights
            int32_t gray = (int32_t)(0.299 * r + 0.587 * g + 0.114 * b);
            
            // Repack
            pixels[i] = (a << 24) | (gray << 16) | (gray << 8) | gray;
        }
    }
}

} // extern "C"
