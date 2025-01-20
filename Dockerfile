# Use sbtscala base image with Java 23 and sbt installed
FROM sbtscala/scala-sbt:eclipse-temurin-23.0.1_11_1.10.7_3.6.2

# Install only necessary libraries
RUN apt-get update && apt-get install -y --no-install-recommends \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1 \
    libgtk-3-0 \
    libgl1-mesa-dri \
    libgl1-mesa-dev \
    gstreamer1.0-libav \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-ugly \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-pulseaudio \
    gstreamer1.0-alsa \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Set environment variables for JavaFX and OpenGL
ENV DISPLAY=:99
ENV LIBGL_ALWAYS_INDIRECT=true

# Set the working directory
WORKDIR /app

# Copy project files
COPY ./target/scala-3.5.1/EchoRelics.jar /app/EchoRelics.jar

# Run the JAR file
CMD ["java", "-jar", "EchoRelics.jar"]
