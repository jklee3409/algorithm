#!/usr/bin/env bash
# 리그를 CPU 수만큼 샤드로 나눠 병렬 실행한다.
# 각 샤드는 독립 네트워크 네임스페이스(unshare -rn)에서 돌기 때문에 모두 127.0.0.1:8747 을 쓸 수 있다.
#   사용: run_league.sh <build_dir> <out_prefix> [BattleSim league 옵션...]
set -euo pipefail
SIM_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD="$1"; OUT="$2"; shift 2
SHARDS="${SHARDS:-$(nproc)}"
pids=()
for ((s=0; s<SHARDS; s++)); do
  unshare -rn sh -c "python3 -I '$SIM_DIR/tools/lo_up.py' && cd '$SIM_DIR' && java -Xss8m -cp '$BUILD/sim' BattleSim league --shard $s --shards $SHARDS --out '${OUT}_s$s.csv' $*" \
    > "${OUT}_s$s.txt" 2>&1 &
  pids+=($!)
done
for p in "${pids[@]}"; do wait "$p" || echo "shard pid $p failed"; done
head -1 "${OUT}_s0.csv" > "${OUT}.csv"
for ((s=0; s<SHARDS; s++)); do tail -n +2 "${OUT}_s$s.csv" >> "${OUT}.csv"; done
rm -f "${OUT}"_s*.csv
cat "${OUT}"_s*.txt | grep -v JAVA_TOOL > "${OUT}.txt" || true
rm -f "${OUT}"_s*.txt
echo "merged → ${OUT}.csv ($(($(wc -l < "${OUT}.csv") - 1)) games)"
