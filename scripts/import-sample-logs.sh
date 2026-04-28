#!/usr/bin/env bash
set -euo pipefail

ES_URL="${ES_URL:-http://elasticsearch:9200}"
SAMPLE_FILE="${1:-/sample-logs.jsonl}"
INDEX_NAME="${2:-microcrm-logs-sample}"

echo "Waiting for Elasticsearch at ${ES_URL}..."
until curl -sSf ${ES_URL} >/dev/null 2>&1; do
  sleep 2
done

if [ ! -f "${SAMPLE_FILE}" ]; then
  echo "Sample file ${SAMPLE_FILE} not found; exiting."
  exit 0
fi

TMP_BULK="/tmp/bulk.ndjson"
rm -f "$TMP_BULK"

# Build bulk payload: index header per line followed by source line
awk -v idx="$INDEX_NAME" '{print "{\"index\":{\"_index\":\"" idx "\"}}"; print $0}' "$SAMPLE_FILE" > "$TMP_BULK"

echo "Uploading sample logs to index ${INDEX_NAME}..."
curl -sS -H "Content-Type: application/x-ndjson" -XPOST "${ES_URL}/_bulk?refresh=true" --data-binary @"$TMP_BULK" || {
  echo "Bulk upload failed" >&2
  exit 1
}

echo "Sample logs imported into ${INDEX_NAME}."
