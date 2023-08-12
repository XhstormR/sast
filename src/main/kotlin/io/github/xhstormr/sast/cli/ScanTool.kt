package io.github.xhstormr.sast.cli

enum class ScanTool(val script: String) {
    KICS("kics.sh"),
    Gosec("gosec.sh"),
    CodeQL("codeql.sh"),
    Semgrep("semgrep.sh"),
    Gitleaks("gitleaks.sh"),
}
