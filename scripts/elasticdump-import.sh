#!/usr/bin/env bash
set -euo pipefail

# Import dumps produced by elasticdump
# Usage: ./scripts/elasticdump-import.sh ./dumps

DUMPDIR=${1:-./dumps}

if ! command -v elasticdump >/dev/null 2>&1; then
  echo "elasticdump not found. Install with: npm install -g elasticdump" >&2
  exit 1
fi

if [ ! -d "$DUMPDIR" ]; then
  echo "Dump directory $DUMPDIR not found" >&2
  exit 1
fi

for f in "$DUMPDIR"/*.json; do
  [ -e "$f" ] || continue
  idx=$(basename "$f" .json)
  echo "Importing $f -> index $idx"
  elasticdump --input="$f" --output="http://localhost:9200/$idx" --type=data
done

echo "Import complete. Check indices with: curl 'http://localhost:9200/_cat/indices?v' | grep microcrm-logs"
