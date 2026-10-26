#!/usr/bin/env python3
"""리그 결과 CSV 분석: 봇별 승률, 상대별 승률, 시나리오별 승률, 승리 사유, 안정성 지표."""
import csv
import sys
from collections import defaultdict


def load(paths):
    rows = []
    for p in paths:
        with open(p, encoding="utf-8") as f:
            rows.extend(csv.DictReader(f))
    return rows


def pct(w, n):
    return f"{100.0 * w / n:5.1f}%" if n else "  -  "


def reason_kind(r):
    r = r.strip('"')
    for k in ("turret destroyed", "tanks destroyed", "turn limit: damage", "turn limit: distance", "full tie", "timeout", "disconnect", "sim error"):
        if k in r:
            return k
    return r[:20]


def main(paths, focus=None):
    rows = load(paths)
    bots = sorted({r["botA"] for r in rows} | {r["botB"] for r in rows})
    games = defaultdict(int)
    wins = defaultdict(int)
    vs = defaultdict(lambda: [0, 0])          # (bot, opp) -> [wins, games]
    side = defaultdict(lambda: [0, 0])        # (bot, side)
    dim = defaultdict(lambda: [0, 0])         # (bot, dimkey) -> [wins, games]
    reasons = defaultdict(lambda: defaultdict(int))
    lossreasons = defaultdict(lambda: defaultdict(int))
    inval = defaultdict(int)
    maxms = defaultdict(int)
    dmg = defaultdict(int)
    yellow = defaultdict(int)
    for r in rows:
        tag = r["tag"].split("/")
        mapn = tag[0].split("-")[1] if tag[0].startswith("gen-") else tag[0]
        for s, me, op in (("A", r["botA"], r["botB"]), ("B", r["botB"], r["botA"])):
            won = r["winnerBot"] == me
            games[me] += 1
            wins[me] += won
            vs[(me, op)][0] += won
            vs[(me, op)][1] += 1
            side[(me, s)][0] += won
            side[(me, s)][1] += 1
            for key in (f"map={mapn}", f"hp={tag[1][2:]}", f"order={tag[2]}", f"zone={tag[3][1:]}"):
                dim[(me, key)][0] += won
                dim[(me, key)][1] += 1
            k = reason_kind(r["reason"])
            (reasons if won else lossreasons)[me][k] += 1
            inval[me] += int(r["invalid" + s])
            maxms[me] = max(maxms[me], int(r["maxMs" + s]))
            dmg[me] += int(r["dmg" + s])
            yellow[me] += int(r.get("yellow" + s, 0) or 0)

    print(f"총 {len(rows)} 게임\n")
    print("== 종합 승률 ==")
    print(f"{'bot':10s} {'win%':>7s} {'games':>6s} {'asA':>7s} {'asB':>7s} {'avgDmg':>7s} {'invalid':>8s} {'maxMs':>6s} {'yellow':>7s}")
    for b in sorted(bots, key=lambda b: -wins[b] / max(1, games[b])):
        print(f"{b:10s} {pct(wins[b], games[b]):>7s} {games[b]:6d} {pct(*side[(b, 'A')]):>7s} {pct(*side[(b, 'B')]):>7s}"
              f" {dmg[b] / max(1, games[b]):7.1f} {inval[b]:8d} {maxms[b]:6d} {yellow[b]:7d}")

    print("\n== 상대별 승률 (행 기준) ==")
    print(f"{'':10s}" + "".join(f"{o:>9s}" for o in bots))
    for b in bots:
        print(f"{b:10s}" + "".join(f"{(pct(*vs[(b, o)]) if o != b else '   -   '):>9s}" for o in bots))

    keys = sorted({k for (_, k) in dim})
    print("\n== 시나리오별 승률 ==")
    print(f"{'':14s}" + "".join(f"{b:>9s}" for b in bots))
    for k in keys:
        print(f"{k:14s}" + "".join(f"{pct(*dim[(b, k)]):>9s}" for b in bots))

    print("\n== 승리 사유 / 패배 사유 ==")
    for b in bots:
        print(f"{b:10s} WIN  " + ", ".join(f"{k}:{v}" for k, v in sorted(reasons[b].items(), key=lambda x: -x[1])))
        print(f"{'':10s} LOSS " + ", ".join(f"{k}:{v}" for k, v in sorted(lossreasons[b].items(), key=lambda x: -x[1])))


if __name__ == "__main__":
    main(sys.argv[1:])
