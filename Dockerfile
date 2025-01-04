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

