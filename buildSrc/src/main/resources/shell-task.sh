#!/usr/bin/env sh

input=$(realpath ${1})
output=$(realpath ${2})
echo -e "PID: $$\nInput : ${input}\nOutput: ${output}"

time=$(( ${RANDOM} % 5 + 1 ))
date "+%F %T"
echo sleep ${time} && sleep ${time}
date "+%F %T"
