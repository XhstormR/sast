#!/usr/bin/env sh

mkdir db
codeql database create  ./db/code1 --language java --command './gradlew classes --no-daemon --rerun-tasks' --source-root "${1}"
codeql database analyze ./db/code1 --format sarif-latest --output "${2}/codeql-result.sarif.json" codeql/java/ql/src/Security/CWE/CWE-798/HardcodedCredentialsApiCall.ql
