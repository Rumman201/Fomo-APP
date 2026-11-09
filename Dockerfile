# ==============================================================================
# Fomo Android - Reproducible Containerized CI Build Environment
# OpenJDK 17 + Android Command-line Tools + Android SDK 34 Platform & Build-Tools
# ==============================================================================

FROM eclipse-temurin:17-jdk-jammy

LABEL maintainer="Fomo Engineering Team"
LABEL description="Deterministic Android Build & CI Image for Fomo Mobile"

ENV ANDROID_SDK_ROOT="/opt/android-sdk"
ENV ANDROID_HOME="/opt/android-sdk"
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools"
ENV GRADLE_USER_HOME="/root/.gradle"

# Install fundamental system dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    curl \
    git \
    unzip \
    zip \
    ca-certificates \
    libncurses5 \
    libstdc++6 \
    && rm -rf /var/lib/apt/lists/*

# Install Android Command-Line Tools (version 11076708)
ARG CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    curl -sS -L -o /tmp/cmdline-tools.zip ${CMDLINE_TOOLS_URL} && \
    unzip -q /tmp/cmdline-tools.zip -d /tmp/cmdline-tools && \
    mv /tmp/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
    rm -rf /tmp/cmdline-tools.zip /tmp/cmdline-tools

# Accept Android SDK licenses and install platforms and build tools
RUN yes | sdkmanager --licenses > /dev/null && \
    sdkmanager \
      "platform-tools" \
      "platforms;android-34" \
      "build-tools;34.0.0"

WORKDIR /workspace

# Pre-warm build cache permissions
RUN mkdir -p /root/.gradle

CMD ["./gradlew", "testDebugUnitTest", "lintDebug", "assembleRelease"]

