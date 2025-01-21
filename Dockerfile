FROM sbtscala/scala-sbt:eclipse-temurin-23.0.1_11_1.10.7_3.6.2

RUN apt-get update && apt-get install -y --no-install-recommends \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1 \
    libgtk-3-0 \
    libgtk-3-dev \
    libgl1-mesa-dri \
    libgl1-mesa-dev \
    libxcb1 \
    libx11-xcb1 \
    libpulse0 \
    openjfx \
    libswt-gtk-4-java \
    gstreamer1.0-libav \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-ugly \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-pulseaudio \
    gstreamer1.0-alsa \
    xvfb \
    x11vnc \
    fonts-dejavu \
    default-jdk \
    libgtk-3-0 \
    && apt-get clean && rm -rf /var/lib/apt/lists/*


ENV DISPLAY=host.docker.internal:0
ENV XDG_RUNTIME_DIR=/tmp/runtime-root
ENV LIBGL_ALWAYS_INDIRECT=true

RUN mkdir -p /tmp/runtime-root && chmod 700 /tmp/runtime-root

WORKDIR /echorelics
ADD . /echorelics

RUN sbt assembly

CMD ["java", "-jar", "target/scala-3.5.1/EchoRelics.jar"]