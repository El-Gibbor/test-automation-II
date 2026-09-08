# Runs the suite headless inside a container that already has Chrome installed
FROM maven:3.9.9-eclipse-temurin-17

# Google Chrome (matches what WebDriverManager will pair a driver to at runtime)
RUN apt-get update \
    && apt-get install -y --no-install-recommends wget gnupg \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace

# Cache dependencies in their own layer before copying source, so code-only changes skip re-download
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src

ENV headless=true

ENTRYPOINT ["mvn", "-B", "test"]
