#!/usr/bin/env sh

semgrep scan \
    --verbose \
    --disable-version-check \
    --metrics off \
    --config "${SEMGREP_HOME}/rules" \
    --sarif \
    -o "${2}/semgrep-result.sarif.json" "${1}"
