#!/usr/bin/env bash
set -euo pipefail

# Start ELK stack and restore an Elasticsearch snapshot
# Usage: ./scripts/restore-snapshot.sh <snapshot_repo> <snapshot_name>

REPO=${1:-my_backup}
SNAPSHOT=${2:-snapshot_latest}

COMPOSE_FILE="docker-compose-elk.yml"

echo "Starting ELK stack (docker compose -f ${COMPOSE_FILE} up -d)..."
docker compose -f "${COMPOSE_FILE}" up -d

echo "Waiting for Elasticsearch to be available on http://localhost:9200 ..."
until curl -sSf http://localhost:9200/ >/dev/null 2>&1; do
  printf '.'; sleep 2
done
echo "\nElasticsearch is up."

echo "Registering snapshot repository (filesystem) named '${REPO}' (if not exists)."
curl -s -X PUT "http://localhost:9200/_snapshot/${REPO}" -H 'Content-Type: application/json' -d'
{
  "type": "fs",
  "settings": { "location": "/usr/share/elasticsearch/snapshots", "compress": true }
}' || true

echo "Attempting to restore snapshot '${SNAPSHOT}' from repo '${REPO}'..."
curl -s -X POST "http://localhost:9200/_snapshot/${REPO}/${SNAPSHOT}/_restore?wait_for_completion=true" -H 'Content-Type: application/json' -d'{}'

echo "Restore request sent. Check Elasticsearch _cat/indices to verify indices restored."
echo "Example: curl 'http://localhost:9200/_cat/indices?v' | grep microcrm-logs"
