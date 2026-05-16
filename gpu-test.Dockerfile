# Episteme GPU Isolated Testing Environment
# Based on NVIDIA CUDA image for Ubuntu
FROM nvidia/cuda:12.3.1-devel-ubuntu22.04

# Install OpenJDK 21 (or 25 if available) and other dependencies
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    maven \
    libmpfr-dev \
    libgmp-dev \
    clinfo \
    ocl-icd-opencl-dev \
    && rm -rf /var/lib/apt/lists/*

# Set Environment Variables
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Create workspace
WORKDIR /app

# Copy the project (or use a volume mount if preferred)
COPY . .

# Build the project
RUN mvn clean install -DskipTests

# Default command: Run the isolated benchmarks
CMD ["mvn", "test", "-pl", "episteme-benchmarks", "--add-modules", "jdk.incubator.vector"]
