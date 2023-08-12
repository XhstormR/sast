#!/usr/bin/env sh

input=$(realpath ${1})
output=$(realpath ${2})
echo -e "PID: $$\nInput : ${input}\nOutput: ${output}"

if git -C ${input} rev-parse > /dev/null 2>&1
then
    gitleaks detect -v -f sarif -r "${output}/gitleaks-result.sarif.json" -s "${input}"
else
    gitleaks detect -v -f sarif -r "${output}/gitleaks-result.sarif.json" -s "${input}" --no-git
fi
