# NJU_Software_Testing

# 项目概述
本项目是一个用于对目标程序进行模糊测试的工具，旨在通过生成多样化的测试输入（种子）、执行目标程序、监控执行过程并评估覆盖率，来发现程序中的潜在漏洞和缺陷。它整合了多个关键组件，包括种子变异、调度排序、测试执行监控以及覆盖率评估与可视化等功能，以提供全面的模糊测试解决方案。
# 项目架构
```
src/main/java/ 
├── energy/
│   └── EnergyScheduler
├── evaluation/
│   ├── CoverageChart
│   ├── CoverageData
│   └── CoverageEvaluator
├── executor/
│   ├── ProcessRunner
│   └── TestExecutor
├── mutation/
│   ├── Mutator
│   ├── ArithMutator
│   ├── BitflipMutator
│   ├── HavocMutator
│   ├── InterestMutator
│   └── SpliceMutator
├── monitoring/
│   └── FuzzingMonitor
├── seedsort/
│   ├── Seed
│   └── SeedSorter
└── fuzzer
```
## 核心组件包
### seedsort包：
负责种子的排序操作，定义了 ___Seed___ 类来表示种子，包含种子的数据、覆盖率、执行时间、入队顺序以及能量值等属性。同时提供了多种排序策略的实现，如按入队顺序、覆盖率、执行时间和能量值排序，通过 ___SeedSorter___ 类统一对外提供排序服务，使得在不同场景下可以灵活选择合适的种子排序方式。
### mutation包：
包含了各种变异器实现，它们都实现了 ___Mutator___ 接口。变异器用于对种子进行变异操作，以生成新的测试输入，例如 ___ArithMutator___ 通过算术运算变异种子字节数据，___BitflipMutator___ 进行位翻转变异，___HavocMutator___ 制造混乱式变异，___InterestMutator___ 基于特定规则变异，___SpliceMutator___ 则从内部维护的种子池中选取数据拼接变异，为模糊测试提供多样化的输入来源。
### monitoring包：
主要包含 ___FuzzingMonitor___ 类，用于监控模糊测试的执行过程。它记录测试的开始时间、执行次数、总执行时间，能够记录每次执行的种子相关信息，如覆盖率、执行时间，并支持标记特殊种子，最后提供打印监控结果的方法，让用户直观了解测试的执行情况。
### execution包：
包含 ___ProcessRunner___ 类用于运行目标模糊测试程序，以及 ___TestExecutor___ 类协调种子与执行过程，将种子数据传递给目标程序并处理执行过程中的输入输出，确保测试目标能够顺利执行。
### energy包：
包含与种子能量调度相关的逻辑，通过特定算法为种子分配能量值，并根据能量对种子进行排序，使得能量高的种子在后续测试中有更高的优先级，优化测试资源分配。
### evaluation包：
负责评估测试的覆盖率，___CoverageEvaluator___ 类是核心，它使用 ___JaCoCo___ 等工具读取执行数据和类文件，分析覆盖率，内部的 ___CoverageDataCollector___ 类用于收集来自 CSV 文件的覆盖率数据及对应的名称，为可视化做准备；___CoverageChart___ 类利用 ___JFreeChart___ 库将收集到的覆盖率数据绘制成可视化图表，直观展示覆盖率随时间的变化趋势。
## 类层次关系
___Mutator___ 接口是变异器的顶层规范，各种具体的变异器实现类都实现该接口，实现多态性，方便在运行时根据策略选择不同的变异操作。
___Seed___ 类作为种子的基础表示，被多个组件使用，如变异器用它作为输入生成新种子，排序组件对其列表进行排序，监控组件记录其执行信息，体现了数据在不同功能模块间的流转。
___FuzzingMonitor___、___CoverageEvaluator___ 等类作为各自功能模块的核心控制类，协调内部组件完成监控、评估等复杂任务，它们与其他辅助类协作，构建起完整的功能体系。
# 流程概述
## 初始化阶段
用户通过命令行指定目标程序路径和初始种子路径，程序启动时解析这些参数，确保路径的有效性。
初始化各个核心组件，包括创建 ___FuzzingMonitor___ 实例开始监控，初始化种子队列（从初始种子路径读取初始种子数据并封装成 ___Seed___ 对象放入队列），准备好变异器、能量调度器等组件的初始状态。
## 模糊测试循环阶段
按照设定的调度策略（默认为按入队顺序，用户可指定能量、覆盖率、执行时间等策略）对种子队列进行排序，选择队列头部的种子作为当前执行种子。
使用 ___TestExecutor___ 结合 ___ProcessRunner___ 执行目标程序，将当前种子数据传递给目标程序，同时捕获执行过程中的信息，如执行时间、覆盖率。
___FuzzingMonitor___ 记录本次执行的详细信息，包括种子数据、覆盖率、执行时间、是否为特殊种子等，根据执行结果更新执行次数、总执行时间等统计信息。
依据选定的变异策略（默认为 ___ArithMutator___，用户可指定其他变异器）对当前种子进行变异，生成新的种子并添加到种子队列，同时可能涉及能量调度组件根据种子执行情况更新种子能量值并重新排序种子队列。
重复上述步骤，持续迭代执行，直到达到设定的迭代次数（当前示例为 100 次，可根据实际需求调整）或满足其他终止条件（如时间限制、发现特定漏洞等）。
## 结果评估与展示阶段
___FuzzingMonitor___ 打印出详细的监控结果，包括执行次数、总执行时间、覆盖率估算值、执行速度以及特殊种子信息，让用户对测试执行情况有全面了解。
___CoverageEvaluator___ 介入，通过 ___JaCoCo___ 等工具分析目标程序的实际覆盖率，收集来自 ___CSV___ 文件（或其他数据源）的覆盖率数据及名称，___CoverageChart___ 利用这些数据绘制可视化图表，以直观的方式展示代码覆盖率随时间的变化趋势，辅助用户评估测试的有效性和发现潜在的测试盲点。
# Docker
```dockerfile
# 基础镜像使用 Ubuntu 22.04
FROM ubuntu:22.04

# 安装必备工具和依赖
RUN apt-get update && apt-get install -y \
    libtool \
    build-essential \
    cmake \
    python3 \
    gcc \
    clang \
    llvm \
    file \
    binutils \
    git \
    curl \
    python3-pip \
    wget \
    unzip \
    bzip2 \
    git \
    vim \
    && apt-get clean

# 安装 AFL++（模糊测试工具）
RUN git clone --depth 1 https://github.com/AFLplusplus/AFLplusplus.git /AFLplusplus \
    && cd /AFLplusplus \
    && make && make install

# 下载并解压模糊目标
RUN mkdir /targets \
    && cd /targets \
    && git clone https://github.com/QRXqrx/NJU-AT-fuzz-targets.git

# 设置工作目录
WORKDIR /targets/NJU-AT-fuzz-targets

# 设置 af-cc 编译器
ENV CC=/AFLplusplus/afl-cc
ENV CXX=/AFLplusplus/afl-cc

# 为每个模糊目标编译并插装
RUN for target in $(ls); do \
    if [ -d "$target" ]; then \
        cd $target && \
        if [ -f "autogen.sh" ]; then \
            ./autogen.sh && \
            ./configure --disable-shared && \
            make; \
        elif [ -f "CMakeLists.txt" ]; then \
            mkdir build && \
            cd build && \
            cmake .. && \
            make; \
        else \
            make; \
        fi; \
        cd ..; \
    fi; \
done

# 安装数据分析所需的 Python 包
RUN pip3 install numpy matplotlib pandas
```
# 使用方法
## 进入工作目录
在命令行中进入包含 ___fuzzer___ 可执行文件和相关依赖的工作目录。
## 命令行参数
### 格式：```java fuzzer [-i <target_program_path>] [-o <initial_seed_path>] [-s <seed_sort_strategy>] [-m <mutation_strategy>]```
### -i：指定模糊目标的程序路径，必填项，指向需要进行模糊测试的可执行程序文件。
### -o：指定模糊目标的初始种子路径，必填项，路径应指向初始种子数据文件。
### -s：种子选择策略参数，可选，默认值为 ___enqueueOrder___，可选择的值有 ___energy___（根据能量调度种子排序）、___coverage___（按覆盖率排序种子）、___executionTime___（依执行时间排序种子），用于决定在每次迭代中如何选择种子进行测试。
### -m：变异策略参数，可选，默认值为 ___arith___，可选择的值对应 ___mutation___ 包中的各种变异器名称，如 ___bitflip___、___havoc___、___interest___、___splice___ 等，确定对种子采用何种变异方式生成新测试输入。
### 示例
假设目标程序路径为`/path/to/target/program`，初始种子路径为`/path/to/seed/file`，想要使用能量调度策略和位翻转变异策略，命令如下：
```java fuzzer -i /path/to/target/program -o /path/to/seed/file -s energy -m bitflip```
## 查看结果
### 监控信息
在工具运行过程中，会在终端输出相关的监控信息，包括当前正在测试的种子、测试的进度、已执行的迭代次数、种子的执行时间和覆盖率等数据。可以根据这些信息了解模糊测试的进行情况。
### 覆盖率评估
当模糊测试完成后，会进行覆盖率评估并生成覆盖率报告。通过CoverageChart类实现的可视化功能，会弹出图形界面展示覆盖率随时间的变化趋势。如果是在无图形界面的服务器环境下运行，会将覆盖率数据以文件形式（ ___CSV___ ）保存到指定目录下，可以通过相应的工具查看和分析这些数据。


# 演示视频链接
https://www.bilibili.com/video/BV1bcrxYYEtb/
