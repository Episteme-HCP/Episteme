#include <cstdint>
#include <algorithm>

extern "C" {
    /**
     * Optimized image processing loop for Project Panama.
     * 
     * @param pixels Pointer to the INT_ARGB pixel array (MemorySegment)
     * @param width Image width
     * @param height Image height
     * @param opCode Operation to perform (1: Grayscale, 2: Invert, 3: Sepia)
     */
    __declspec(dllexport) void process_image(int32_t* pixels, int32_t width, int32_t height, int32_t opCode) {
        int32_t length = width * height;
        
        // Use a simple loop that the compiler can auto-vectorize with AVX2/AVX-512
        for (int32_t i = 0; i < length; ++i) {
            uint32_t argb = (uint32_t)pixels[i];
            uint8_t a = (argb >> 24) & 0xFF;
            uint8_t r = (argb >> 16) & 0xFF;
            uint8_t g = (argb >> 8) & 0xFF;
            uint8_t b = argb & 0xFF;

            if (opCode == 1) { // Grayscale (Luminance)
                uint8_t gray = (uint8_t)(0.299f * r + 0.587f * g + 0.114f * b);
                pixels[i] = (int32_t)((a << 24) | (gray << 16) | (gray << 8) | gray);
            } else if (opCode == 2) { // Invert
                pixels[i] = (int32_t)((a << 24) | ((255 - r) << 16) | ((255 - g) << 8) | (255 - b));
            } else if (opCode == 3) { // Sepia (Approximate)
                uint8_t tr = (uint8_t)std::min(255.0f, (r * 0.393f) + (g * 0.769f) + (b * 0.189f));
                uint8_t tg = (uint8_t)std::min(255.0f, (r * 0.349f) + (g * 0.686f) + (b * 0.168f));
                uint8_t tb = (uint8_t)std::min(255.0f, (r * 0.272f) + (g * 0.534f) + (b * 0.131f));
                pixels[i] = (int32_t)((a << 24) | (tr << 16) | (tg << 8) | tb);
            }
        }
    }
}
