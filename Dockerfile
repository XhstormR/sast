FROM gradle:latest

USER gradle

WORKDIR /app

# cache dependencies
COPY --chown=gradle:gradle gradle ./gradle/
COPY --chown=gradle:gradle buildSrc ./buildSrc/
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts gradle.properties ./
RUN gradle --no-daemon dependencies

# actual build
COPY --chown=gradle:gradle ./ ./
RUN gradle --no-daemon build

ENTRYPOINT ["gradle", "--no-daemon"]
CMD ["greet"]

# docker build -t sast .
# docker history sast
# docker run sast
