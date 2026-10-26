#!/usr/bin/env bash
# 파라미터/역할 변형 봇 빌드: build_variant.sh <build_dir> <name> <strategy A-D> "<tune>" [ROLE]
#   예) build_variant.sh build C_def1 C "defenders3=1"
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$1"; NAME="$2"; LETTER="$3"; TUNE="${4:-}"; ROLE="${5:-AUTO}"
mkdir -p "$OUT/src/$NAME" "$OUT/bots/$NAME"
sed -e "s/static final String STRATEGY = \"[A-D]\";/static final String STRATEGY = \"$LETTER\";/" \
    -e "s/static final String TUNE = \"[^\"]*\";/static final String TUNE = \"$TUNE\";/" \
    -e "s/static final String ROLE = \"[A-Z0-9]*\";/static final String ROLE = \"$ROLE\";/" \
    "$ROOT/Main.java" > "$OUT/src/$NAME/Main.java"
javac --release 8 -Xlint:-options -encoding UTF-8 -d "$OUT/bots/$NAME" "$OUT/src/$NAME/Main.java" 2>&1 | grep -v JAVA_TOOL_OPTIONS || true
