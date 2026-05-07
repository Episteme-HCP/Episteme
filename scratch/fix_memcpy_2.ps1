$path = 'c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\main\java\org\episteme\nativ\mathematics\linearalgebra\backends\NativeCUDADenseLinearAlgebraBackend.java'
$content = [System.IO.File]::ReadAllText($path)

# Greedy match for the first part to handle commas in arguments
# For HOST_TO_DEVICE: (dst, src, len, flag) -> H_TO_D(dst.address(), src, len)
# Pattern matches: (everything_greedy), CUDA_MEMCPY_HOST_TO_DEVICE)
# We need to split the greedy part into 3 args: dst, src, len
# But dst is always the first arg before the first comma.

$content = $content -replace 'CUDA_MEMCPY\.invokeExact\(([^,]+),\s*(.+),\s*([^,]+),\s*CUDA_MEMCPY_HOST_TO_DEVICE\)', 'CUDA_MEMCPY_H_TO_D.invokeExact($1.address(), $2, $3)'
$content = $content -replace 'CUDA_MEMCPY\.invokeExact\(([^,]+),\s*(.+),\s*([^,]+),\s*CUDA_MEMCPY_DEVICE_TO_HOST\)', 'CUDA_MEMCPY_D_TO_H.invokeExact($1, $2.address(), $3)'

[System.IO.File]::WriteAllText($path, $content)
