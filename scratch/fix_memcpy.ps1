$path = 'c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\main\java\org\episteme\nativ\mathematics\linearalgebra\backends\NativeCUDADenseLinearAlgebraBackend.java'
$content = [System.IO.File]::ReadAllText($path)
$content = $content -replace 'CUDA_MEMCPY\.invokeExact\(([^,]+), ([^,]+), ([^,]+), CUDA_MEMCPY_HOST_TO_DEVICE\)', 'CUDA_MEMCPY_H_TO_D.invokeExact($1.address(), $2, $3)'
$content = $content -replace 'CUDA_MEMCPY\.invokeExact\(([^,]+), ([^,]+), ([^,]+), CUDA_MEMCPY_DEVICE_TO_HOST\)', 'CUDA_MEMCPY_D_TO_H.invokeExact($1, $2.address(), $3)'
[System.IO.File]::WriteAllText($path, $content)
