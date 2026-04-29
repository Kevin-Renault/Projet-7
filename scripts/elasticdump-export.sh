#!/usr/bin/env bash
set -euo pipefail

# Export microcrm indices using elasticdump
# Usage: ./scripts/elasticdump-export.sh

if ! command -v elasticdump >/dev/null 2>&1; then
  echo "elasticdump not found. Install with: npm install -g elasticdump" >&2
  exit 1
fi

mkdir -p ./dumps
echo "Fetching indices..."
INDICES=$(curl -s 'http://localhost:9200/_cat/indices?h=index' | grep '^microcrm-logs-' || true)

if [ -z "$INDICES" ]; then
  echo "No microcrm indices found. Ensure Elasticsearch is running." >&2
  exit 1
fi

for idx in $INDICES; do
  outfile="dumps/${idx}.json"
  echo "Exporting index $idx -> $outfile"
  elasticdump --input="http://localhost:9200/$idx" --output="$outfile" --type=data
done

echo "Export complete. Files in ./dumps/"
