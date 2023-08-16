FROM checkmarx/kics:latest as kics

FROM gradle:graal as gradle

USER gradle

WORKDIR /app

COPY --chown=gradle:gradle gradle ./gradle/
COPY --chown=gradle:gradle buildSrc ./buildSrc/
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts gradle.properties ./
RUN echo "Cache Dependencies" \
    && gradle --no-daemon dependencies

COPY --chown=gradle:gradle ./ ./
RUN echo "Actual Build" \
    && gradle --no-daemon nativeCompile

FROM ubuntu:latest

ENV CODEQL_VERSION=2.14.2
ENV CODEQL_HOME=/opt/codeql/
ARG CODEQL_BINARY_URL=https://github.com/github/codeql-action/releases/download/codeql-bundle-v${CODEQL_VERSION}/codeql-bundle-linux64.tar.gz

ENV GITLEAKS_VERSION=8.17.0
ENV GITLEAKS_HOME=/opt/gitleaks/
ARG GITLEAKS_BINARY_URL=https://github.com/gitleaks/gitleaks/releases/download/v${GITLEAKS_VERSION}/gitleaks_${GITLEAKS_VERSION}_linux_x64.tar.gz

ENV GOSEC_VERSION=2.16.0
ENV GOSEC_HOME=/opt/gosec/
ARG GOSEC_BINARY_URL=https://github.com/securego/gosec/releases/download/v${GOSEC_VERSION}/gosec_${GOSEC_VERSION}_linux_amd64.tar.gz

ENV SEMGREP_HOME=/opt/semgrep/
ARG SEMGREP_RULES_URL=https://github.com/returntocorp/semgrep-rules/archive/refs/heads/develop.tar.gz

ENV KICS_HOME=/opt/kics/

# install semgrep
RUN echo "Installing Semgrep" \
    && apt-get update \
    && apt-get install --yes --no-install-recommends wget python3-pip \
    && echo "Downloading Semgrep Rules" \
    && wget -O /tmp/semgrep-rules.tar.gz ${SEMGREP_RULES_URL} \
    && mkdir -p "${SEMGREP_HOME}/rules" \
    && tar --extract \
        --file /tmp/semgrep-rules.tar.gz \
        --directory "${SEMGREP_HOME}/rules" \
        --strip-components 1 \
        --no-same-owner \
    && rm -rf "${SEMGREP_HOME}/rules/stats/" /tmp/semgrep-rules.tar.gz /var/lib/apt/lists/* \
    && python3 -m pip install --upgrade --no-cache-dir semgrep \
    \
    && echo "Testing Semgrep" \
    && echo semgrep --version && semgrep --version

# install codeql
RUN echo "Downloading CodeQL" \
    && wget -O /tmp/codeql.tar.gz ${CODEQL_BINARY_URL} \
    \
    && echo "Installing CodeQL" \
    && mkdir -p "${CODEQL_HOME}" \
    && tar --extract \
        --file /tmp/codeql.tar.gz \
        --directory "${CODEQL_HOME}" \
        --strip-components 1 \
        --no-same-owner \
    && rm -f /tmp/codeql.tar.gz \
    && ln -s "${CODEQL_HOME}/codeql" /usr/bin/codeql \
    \
    && echo "Testing CodeQL" \
    && echo codeql version && codeql version

# install gitleaks
RUN echo "Downloading Gitleaks" \
    && wget -O /tmp/gitleaks.tar.gz ${GITLEAKS_BINARY_URL} \
    \
    && echo "Installing Gitleaks" \
    && mkdir -p "${GITLEAKS_HOME}" \
    && tar --extract \
        --file /tmp/gitleaks.tar.gz \
        --directory "${GITLEAKS_HOME}" \
        --no-same-owner \
    && rm -f /tmp/gitleaks.tar.gz \
    && ln -s "${GITLEAKS_HOME}/gitleaks" /usr/bin/gitleaks \
    \
    && echo "Testing Gitleaks" \
    && echo gitleaks version && gitleaks version

# install gosec
RUN echo "Downloading Gosec" \
    && wget -O /tmp/gosec.tar.gz ${GOSEC_BINARY_URL} \
    \
    && echo "Installing Gosec" \
    && mkdir -p "${GOSEC_HOME}" \
    && tar --extract \
        --file /tmp/gosec.tar.gz \
        --directory "${GOSEC_HOME}" \
        --no-same-owner \
    && rm -f /tmp/gosec.tar.gz \
    && ln -s "${GOSEC_HOME}/gosec" /usr/bin/gosec \
    \
    && echo "Testing Gosec" \
    && echo gosec version && gosec version

# install kics
# https://github.com/Checkmarx/kics/blob/master/Dockerfile
COPY --from=kics /app/bin/ ${KICS_HOME}
RUN ln -s "${KICS_HOME}/kics" /usr/bin/kics \
    \
    && echo "Testing KICS" \
    && echo kics version && kics version

RUN echo "Adding app user and group" \
    && groupadd --gid 1000 app \
    && useradd --gid 1000 --uid 1000 --create-home --shell /bin/sh app

USER app

WORKDIR /app

COPY --from=gradle /app/build/native/nativeCompile/sast /app/

ENTRYPOINT ["/app/sast"]
CMD ["--help"]

# docker build -t sast .
# docker history sast
# docker run sast
