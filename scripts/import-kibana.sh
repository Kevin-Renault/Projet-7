#!/usr/bin/env bash
set -euo pipefail

KIBANA_URL="${KIBANA_URL:-http://localhost:5601}"
EXPORT_FILE="${1:-misc/kibana/export.ndjson}"

echo "Waiting for Kibana at ${KIBANA_URL}..."
until curl -sSf ${KIBANA_URL}/api/status >/dev/null 2>&1; do
  sleep 2
done

echo "Importing ${EXPORT_FILE} into Kibana ${KIBANA_URL}"
curl -sS -X POST "${KIBANA_URL}/api/saved_objects/_import?overwrite=true" -H "kbn-xsrf: true" -F file=@${EXPORT_FILE} || true

echo "Import completed (or skipped if already present)." 
