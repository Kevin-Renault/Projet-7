from __future__ import annotations

import datetime as dt
import json
import os
import subprocess
from pathlib import Path
from urllib.request import Request, urlopen


def parse_iso(value: str) -> dt.datetime:
    return dt.datetime.fromisoformat(value.replace("Z", "+00:00"))


def normalize_start(value: str) -> dt.datetime:
    if not value:
        return dt.datetime(1970, 1, 1, tzinfo=dt.timezone.utc)
    if len(value) == 10:
        value = f"{value}T00:00:00+00:00"
    return parse_iso(value)


def normalize_end(value: str) -> dt.datetime:
    if not value:
        return dt.datetime(2100, 1, 1, tzinfo=dt.timezone.utc)
    if len(value) == 10:
        value = f"{value}T23:59:59+00:00"
    return parse_iso(value)


def fetch_json(url: str, token: str) -> dict:
    request = Request(
        url,
        headers={
            "Authorization": f"Bearer {token}",
            "Accept": "application/vnd.github+json",
            "X-GitHub-Api-Version": "2022-11-28",
        },
    )
    with urlopen(request) as response:
        return json.loads(response.read().decode("utf-8"))


def average(values: list[float]) -> str:
    if not values:
        return "n/a"
    return f"{sum(values) / len(values):.2f}"


def format_readable_date(value: dt.datetime) -> str:
    return value.astimezone(dt.timezone.utc).strftime("%Y-%m-%d %H:%M UTC")


def branch_creation_date() -> dt.datetime:
    completed = subprocess.run(
        ["git", "log", "--first-parent", "--reverse", "--format=%aI", "HEAD"],
        check=True,
        capture_output=True,
        text=True,
    )
    first_line = next((line for line in completed.stdout.splitlines() if line.strip()), "")
    if not first_line:
        return dt.datetime(1970, 1, 1, tzinfo=dt.timezone.utc)
    return parse_iso(first_line)


def collect_relevant_runs(repo: str, token: str, since: dt.datetime, until: dt.datetime) -> list[dict[str, object]]:
    runs_url = f"https://api.github.com/repos/{repo}/actions/workflows/ci.yml/runs?branch=main&event=push&per_page=100"
    payload = fetch_json(runs_url, token)

    relevant_runs: list[dict[str, object]] = []
    for run in payload.get("workflow_runs", []):
        created_at = run.get("created_at")
        run_id = run.get("id")
        conclusion = run.get("conclusion")
        if not created_at or not run_id or conclusion not in {"success", "failure"}:
            continue
        created = parse_iso(created_at)
        if since <= created <= until:
            relevant_runs.append({"id": run_id, "created": created, "conclusion": conclusion})

    relevant_runs.sort(key=lambda item: item["created"])
    return relevant_runs


def collect_durations(
    repo: str,
    token: str,
    runs: list[dict[str, object]],
    since: dt.datetime,
    until: dt.datetime,
) -> tuple[dict[str, list[float]], int, int]:
    durations: dict[str, list[float]] = {
        "build-and-push-backend": [],
        "build-and-push-frontend": [],
        "test-application": [],
        "sonar-analysis": [],
    }
    success_count = 0
    failure_count = 0

    for run in runs:
        if run["conclusion"] == "success":
            success_count += 1
        else:
            failure_count += 1

        jobs_url = f"https://api.github.com/repos/{repo}/actions/runs/{run['id']}/jobs?per_page=100"
        jobs_payload = fetch_json(jobs_url, token)
        for job in jobs_payload.get("jobs", []):
            name = job.get("name")
            started_at = job.get("started_at")
            completed_at = job.get("completed_at")
            if name not in durations or not started_at or not completed_at:
                continue
            started = parse_iso(started_at)
            completed = parse_iso(completed_at)
            if since <= started <= until and since <= completed <= until:
                durations[name].append(max((completed - started).total_seconds() / 60, 0))

    return durations, success_count, failure_count


def write_report(
    output_path: Path,
    since: dt.datetime,
    until: dt.datetime,
    durations: dict[str, list[float]],
    success_count: int,
    total_runs: int,
) -> None:
    success_rate = f"{(success_count / total_runs) * 100:.2f}" if total_runs else "n/a"
    readable_since = format_readable_date(since)
    readable_until = format_readable_date(until)

    output_path.parent.mkdir(parents=True, exist_ok=True)
    with output_path.open("w", encoding="utf-8") as stream:
        stream.write("# Rapport KPI automatique\n\n")
        stream.write(f"- Période analysée: {readable_since} -> {readable_until}\n")
        stream.write("- Périmètre: runs du workflow `ci.yml` sur `main` avec event `push`\n\n")

        stream.write("## Tableau de synthèse\n\n")
        stream.write("| KPI | Valeur | Source | Note |\n")
        stream.write("| --- | ---: | --- | --- |\n")
        stream.write(f"| Temps moyen de build backend | {average(durations['build-and-push-backend'])} min | Job `build-and-push-backend` dans `ci.yml` | Moyenne sur la période |\n")
        stream.write(f"| Temps moyen de build frontend | {average(durations['build-and-push-frontend'])} min | Job `build-and-push-frontend` dans `ci.yml` | Moyenne sur la période |\n")
        stream.write(f"| Temps moyen des tests | {average(durations['test-application'])} min | Job `test-application` dans `ci.yml` | Moyenne sur la période |\n")
        stream.write(f"| Temps moyen de SonarQube | {average(durations['sonar-analysis'])} min | Job `sonar-analysis` dans `ci.yml` | Moyenne sur la période |\n")
        stream.write(f"| Taux de succès CI | {success_rate}% | Runs du workflow `ci.yml` | Succès / total des runs analysés |\n\n")

        stream.write("## Méthode de calcul\n\n")
        stream.write("- Les runs sont extraits via l'API GitHub Actions pour `ci.yml` sur `main`.\n")
        stream.write("- Les durées sont calculées avec `started_at` et `completed_at` de chaque job.\n")
        stream.write("- Les indicateurs suivent les dimensions utiles du projet: build, tests, qualité SonarQube et stabilité du pipeline.\n")
        stream.write("- Si les dates sont vides, le script prend une fenêtre large couvrant l'historique disponible.\n")
        stream.write("- Le rapport est généré au format Markdown et peut être publié comme artefact.\n")


def main() -> None:
    repo = os.environ["GITHUB_REPOSITORY"]
    token = os.environ["GITHUB_TOKEN"]
    start_date = os.environ.get("START_DATE", "")
    end_date = os.environ.get("END_DATE", "")
    output_path = Path(os.environ.get("OUTPUT_PATH", "artifacts/kpi-report.md"))

    since = normalize_start(start_date) if start_date else branch_creation_date()
    until = normalize_end(end_date)
    runs = collect_relevant_runs(repo, token, since, until)
    durations, success_count, _ = collect_durations(repo, token, runs, since, until)
    write_report(output_path, since, until, durations, success_count, len(runs))


if __name__ == "__main__":
    main()