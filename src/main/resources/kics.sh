#!/usr/bin/env sh

kics scan \
    --no-progress \
    --report-formats 'sarif,html,pdf' \
    --output-name kics-result \
    -o "${2}" -p "${1}"
