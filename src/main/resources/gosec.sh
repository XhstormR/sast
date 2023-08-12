#!/usr/bin/env sh

gosec -no-fail -fmt sarif -out "${2}/gosec-result.sarif.json" "${1}/..."
