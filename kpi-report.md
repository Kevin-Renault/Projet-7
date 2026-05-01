# Rapport KPI automatique

- Période analysée: 2025-12-15 08:33 UTC -> 2100-01-01 00:00 UTC
- Périmètre: runs du workflow `ci.yml` sur `main` avec event `push`

## Tableau de synthèse

| KPI | Valeur | Source | Note |
| --- | ---: | --- | --- |
| Temps moyen de build backend | 1.38 min | Job `build-and-push-backend` dans `ci.yml` | Moyenne sur la période |
| Temps moyen de build frontend | 1.04 min | Job `build-and-push-frontend` dans `ci.yml` | Moyenne sur la période |
| Temps moyen des tests | 2.00 min | Job `test-application` dans `ci.yml` | Moyenne sur la période |
| Temps moyen de SonarQube | 1.21 min | Job `sonar-analysis` dans `ci.yml` | Moyenne sur la période |
| Temps moyen de la pipeline complète | 6.43 min | Run complet du workflow `ci.yml` | De `run_started_at` à `updated_at` |
| Taux de succès CI | 90.91% | Runs du workflow `ci.yml` | Succès / total des runs analysés |

## Tableau journalier du temps max de pipeline

| Jour (UTC) | Nombre de runs | Temps max pipeline (min) | Run concerné | Écart vs veille |
| --- | ---: | ---: | --- | ---: |
| 2026-04-02 | 2 | 5.77 | 23908118753 | n/a |
| 2026-04-16 | 1 | 4.57 | 24526267110 | -1.20 |
| 2026-04-17 | 5 | 9.35 | 24566801921 | +4.78 |
| 2026-04-23 | 1 | 7.57 | 24846567027 | -1.78 |
| 2026-04-24 | 2 | 5.92 | 24885363832 | -1.65 |
| 2026-04-28 | 1 | 4.92 | 25053448936 | -1.00 |
| 2026-04-29 | 5 | 10.82 | 25103787135 | +5.90 |
| 2026-04-30 | 3 | 6.78 | 25174663071 | -4.03 |
| 2026-05-01 | 2 | 6.22 | 25210609136 | -0.57 |

## Méthode de calcul

- Les runs sont extraits via l'API GitHub Actions pour `ci.yml` sur `main`.
- Les durées sont calculées avec `started_at` et `completed_at` de chaque job.
- La durée complète de pipeline est calculée entre `run_started_at` et `updated_at` pour chaque run.
- Le tableau journalier retient pour chaque jour le run ayant la durée totale maximale, avec son écart par rapport à la veille.
- Les indicateurs suivent les dimensions utiles du projet: build, tests, qualité SonarQube et stabilité du pipeline.
- Si les dates sont vides, le script prend une fenêtre large couvrant l'historique disponible.
- Le rapport est généré au format Markdown et peut être publié comme artefact.
