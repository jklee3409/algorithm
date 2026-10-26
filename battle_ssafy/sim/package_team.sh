#!/usr/bin/env bash
# 최종 팀 코드 3개 생성: package_team.sh <ROLE1> <ROLE2> <ROLE3> [닉네임1] [닉네임2] [닉네임3]
#   → team/Player1/Main.java, team/Player2/Main.java, team/Player3/Main.java
#   (공통 엔진 Main.java 에서 ROLE/NICKNAME 만 바꾸고, 원본과 같은 CRLF 줄바꿈으로 저장)
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
ROLES=("$1" "$2" "$3")
NICKS=("${4:-대전3_이종규}" "${5:-대전3_팀원2}" "${6:-대전3_팀원3}")
for i in 0 1 2; do
  n=$((i + 1))
  dir="$ROOT/team/Player$n"
  mkdir -p "$dir"
  sed -e "s/static final String STRATEGY = \"[A-D]\";/static final String STRATEGY = \"C\";/" \
      -e "s/static final String ROLE = \"[A-Z0-9]*\";/static final String ROLE = \"${ROLES[$i]}\";/" \
      -e "s/String NICKNAME = \"[^\"]*\";/String NICKNAME = \"${NICKS[$i]}\";/" \
      "$ROOT/Main.java" | sed 's/$/\r/' > "$dir/Main.java"
  echo "Player$n → ROLE=${ROLES[$i]} NICKNAME=${NICKS[$i]} ($dir/Main.java)"
done
