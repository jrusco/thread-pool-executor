# Use the official VS Code Java image as a base
FROM mcr.microsoft.com/devcontainers/java:17

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# (Optional) Install additional tools here

# Switch back to vscode user
USER vscode
