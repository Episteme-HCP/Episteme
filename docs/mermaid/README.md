# JScience Architecture Documentation

This folder contains the Mermaid diagrams describing the JScience project architecture.

## Ecosystem Overview
`01_ecosystem_overview.mmd`

```mermaid
graph TD
    subgraph JScience_Ecosystem
        Parent[jscience-parent]
        Core[jscience-core]
        Native[jscience-native]
        Natural[jscience-natural]
        Social[jscience-social]
        Apps[jscience-featured-apps]
        Bench[jscience-benchmarks]
        DB[jscience-database]
        JNI[jscience-jni]
    end
    
    subgraph Distributed_Stack
        Server[jscience-server]
        Client[jscience-client]
        Worker[jscience-worker]
    end

    Parent --> Core
    Parent --> Native
    
    Native -.->|SPI| Core
    Natural --> Core
    Social --> Core
    Apps --> Core
    Bench --> Core
    
    Server --> Core
    Client --> Core
    Worker --> Core
    
    JNI --> Native
    DB --> Core
```

## Core Architecture Layers
`02_core_architecture.mmd`

```mermaid
graph TD
    User([User Application])
    
    subgraph API_Layer
        CC[ComputeContext]
        Tensor[Tensor API]
        Matrix[Matrix API]
        Quant[Quantity/Unit API]
    end
    
    subgraph Engine_Layer
        Orchestrator[Work Orchestrator]
        Planner[Execution Planner]
        Memory[Memory Manager]
    end
    
    subgraph Backend_Layer
        SPI[Backend SPI]
        OpenCL[OpenCL Backend]
        CUDA[CUDA Backend]
        Native[Native FFM Backend]
        Java[Java/Multicore Backend]
    end
    
    subgraph Hardware
        CPU[CPU]
        GPU[GPU]
        Cluster[Cluster/Grid]
    end
    
    User --> CC
    User --> Tensor
    
    CC --> Orchestrator
    Orchestrator --> Planner
    Planner --> SPI
    
    SPI --> OpenCL
    SPI --> CUDA
    SPI --> Native
    SPI --> Java
    
    OpenCL --> GPU
    CUDA --> GPU
    Native --> CPU
    Java --> CPU
    
    Orchestrator -.-> Cluster
```

## Backend Class Hierarchy
`03_backend_class_hierarchy.mmd`

```mermaid
classDiagram
    namespace API {
        class ComputeContext {
            +selectBackend()
            +compute()
        }
        class AlgorithmProvider {
            <<interface>>
            +getPriority()
            +isAvailable()
        }
    }
    
    namespace SPI {
        class Backend {
            <<interface>>
            +getName()
            +isAvailable()
        }
        class ComputeBackend {
            <<interface>>
        }
        class AudioBackend {
            <<interface>>
        }
    }
    
    namespace Implementations {
        class OpenCLBackend
        class CUDABackend
        class NativeSIMDBackend
        class NativeAudioBackend
        class OpenCLNBodyProvider
        class CUDANBodyProvider
    }
    
    ComputeContext --> Backend : Manages
    Backend <|-- ComputeBackend
    Backend <|-- AudioBackend
    
    ComputeBackend <|.. OpenCLBackend
    ComputeBackend <|.. CUDABackend
    ComputeBackend <|.. NativeSIMDBackend
    
    AudioBackend <|.. NativeAudioBackend
    
    AlgorithmProvider <|.. OpenCLNBodyProvider
    AlgorithmProvider <|.. CUDANBodyProvider
    AlgorithmProvider <|.. NativeSIMDBackend : (LinearAlgebra)
    
    OpenCLBackend ..> OpenCLNBodyProvider : Supports
    CUDABackend ..> CUDANBodyProvider : Supports
```
