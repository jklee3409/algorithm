#!/usr/bin/env bash
# 설계안별 Main.java 생성 + 컴파일 (시뮬레이터용)
#   designs/A_blitz/Main.java, designs/B_fortress/Main.java, designs/C_adaptive/Main.java 는
#   최상위 Main.java 에서 STRATEGY 상수만 바꿔 생성한다 (공통 엔진은 동일).
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUTDIR="${1:-$ROOT/sim/build}"
mkdir -p "$OUTDIR/bots" "$OUTDIR/sim"

gen() {  # gen <letter> <out_dir>
  mkdir -p "$2"
  sed "s/static final String STRATEGY = \"[A-D]\";/static final String STRATEGY = \"$1\";/" "$ROOT/Main.java" > "$2/Main.java"
}
gen A "$ROOT/designs/A_blitz"
gen B "$ROOT/designs/B_fortress"
gen C "$ROOT/designs/C_adaptive"
gen D "$ROOT/sim/opponents/D_hunter"   # 시뮬레이션 전용 상대(탱크 사냥형)

compile() {  # compile <src> <name>
  mkdir -p "$OUTDIR/bots/$2"
  javac --release 8 -Xlint:-options -encoding UTF-8 -d "$OUTDIR/bots/$2" "$1" 2>&1 | grep -v JAVA_TOOL_OPTIONS || true
}
compile "$ROOT/original/Main.java" base
compile "$ROOT/sim/opponents/base_fixed/Main.java" basefix
compile "$ROOT/designs/A_blitz/Main.java" A
compile "$ROOT/designs/B_fortress/Main.java" B
compile "$ROOT/designs/C_adaptive/Main.java" C
compile "$ROOT/sim/opponents/D_hunter/Main.java" D
compile "$ROOT/Main.java" final

javac -encoding UTF-8 -d "$OUTDIR/sim" "$ROOT/sim/BattleSim.java" 2>&1 | grep -v JAVA_TOOL_OPTIONS || true
echo "built into $OUTDIR"
