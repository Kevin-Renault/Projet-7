# Rapport DORA automatique

- Branche d'exécution: main
- Début demandé: 2026-04-13
- Fin demandée: <vide>
- Règle par défaut: si une date est vide, le workflow prend toute la vie du flux concerné.

> Le flux branches -> dev mesure l'intégration intermédiaire.
> Le flux dev -> main mesure la livraison principale retenue pour la lecture DORA.

## Flux branches -> dev

- Période retenue: 2026-04-13T00:00:00+00:00 -> 2026-04-23T18:17:15+02:00

| Date merge | PR | Branche source | Premier commit | Lead time | Sujet |
| --- | ---: | --- | --- | ---: | --- |
| 2026-04-15 15:43 | #14 | refactor/ci | 0b2ee0d9 (2026-04-02 15:43) | 312.01 h | Merge pull request #14 from Kevin-Renault/refactor/ci |
| 2026-04-15 16:03 | #15 | refactor/ci | 8919b425 (2026-04-15 16:03) | 0.01 h | Merge pull request #15 from Kevin-Renault/refactor/ci |
| 2026-04-15 16:37 | #16 | refactor/ci | db8eae44 (2026-04-15 16:33) | 0.06 h | Merge pull request #16 from Kevin-Renault/refactor/ci |
| 2026-04-15 17:59 | #17 | perf/ci-pipeline-optimization | c0ef0521 (2026-04-15 16:58) | 1.01 h | Merge pull request #17 from Kevin-Renault/perf/ci-pipeline-optimization |
| 2026-04-16 12:12 | #18 | perf/ci-pipeline-optimization | 3888d01e (2026-04-16 12:08) | 0.07 h | Merge pull request #18 from Kevin-Renault/perf/ci-pipeline-optimization |
| 2026-04-16 16:22 | #20 | fix/sonar-quality-issues | e6d8c33f (2026-04-16 12:35) | 3.79 h | Merge pull request #20 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-16 18:08 | #22 | fix/sonar-quality-issues | 191dc32c (2026-04-16 17:23) | 0.74 h | Merge pull request #22 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-17 10:49 | #25 | fix/sonar-quality-issues | 93259e08 (2026-04-17 10:05) | 0.74 h | Merge pull request #25 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-17 11:00 | #27 | fix/sonar-quality-issues | 8ed5c6d2 (2026-04-17 10:56) | 0.07 h | Merge pull request #27 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-17 11:46 | #28 | fix/sonar-quality-issues | eb63d729 (2026-04-17 11:42) | 0.06 h | Merge pull request #28 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-17 12:15 | #29 | fix/sonar-quality-issues | 05cbaf71 (2026-04-17 12:13) | 0.03 h | Merge pull request #29 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-17 12:37 | #31 | fix/sonar-quality-issues | 83d6c239 (2026-04-17 12:36) | 0.02 h | Merge pull request #31 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-17 14:03 | #34 | fix/release-optimization | f8875321 (2026-04-17 14:01) | 0.03 h | Merge pull request #34 from Kevin-Renault/fix/release-optimization |
| 2026-04-17 14:14 | #35 | fix/release-optimization | de757c62 (2026-04-17 14:13) | 0.01 h | Merge pull request #35 from Kevin-Renault/fix/release-optimization |
| 2026-04-17 14:21 | #36 | fix/release-optimization | 340be704 (2026-04-17 14:21) | 0.01 h | Merge pull request #36 from Kevin-Renault/fix/release-optimization |
| 2026-04-17 14:35 | #37 | fix/release-optimization | 96ff0100 (2026-04-17 14:30) | 0.08 h | Merge pull request #37 from Kevin-Renault/fix/release-optimization |
| 2026-04-17 14:57 | #38 | fix/sonar-quality-issues | 9ae7fd3a (2026-04-17 14:48) | 0.16 h | Merge pull request #38 from Kevin-Renault/fix/sonar-quality-issues |
| 2026-04-17 16:33 | #40 | fix/package-front | 8d99bffb (2026-04-17 16:26) | 0.12 h | Merge pull request #40 from Kevin-Renault/fix/package-front |
| 2026-04-21 18:00 | #42 | feat/ci-periodic-tests | 45a08e0f (2026-04-21 17:14) | 0.76 h | Merge pull request #42 from Kevin-Renault/feat/ci-periodic-tests |
| 2026-04-23 16:17 | #43 | feature/elk-logging-dashboard | 68e3d881 (2026-04-20 14:51) | 73.43 h | Merge pull request #43 from Kevin-Renault/feature/elk-logging-dashboard |

- Nombre d'éléments: 20
- Lead time moyen: 19.66 h

## Flux dev -> main

- Période retenue: 2026-04-13T00:00:00+00:00 -> 2026-04-23T18:26:57+02:00

| Date merge | PR | Branche source | Premier commit | Lead time | Sujet |
| --- | ---: | --- | --- | ---: | --- |
| 2026-04-16 15:23 | #19 | dev | 0b2ee0d9 (2026-04-02 15:43) | 335.66 h | Merge pull request #19 from Kevin-Renault/dev |
| 2026-04-16 17:12 | #21 | dev | e6d8c33f (2026-04-16 12:35) | 4.62 h | Merge pull request #21 from Kevin-Renault/dev |
| 2026-04-16 18:10 | #23 | dev | 10092901 (2026-04-16 17:13) | 0.94 h | Merge pull request #23 from Kevin-Renault/dev |
| 2026-04-17 11:05 | #26 | dev | 93259e08 (2026-04-17 10:05) | 1.00 h | Merge pull request #26 from Kevin-Renault/dev |
| 2026-04-17 12:22 | #30 | dev | eb63d729 (2026-04-17 11:42) | 0.67 h | Merge pull request #30 from Kevin-Renault/dev |
| 2026-04-17 13:10 | #33 | dev | 05047af4 (2026-04-17 12:26) | 0.73 h | Merge pull request #33 from Kevin-Renault/dev |
| 2026-04-17 15:10 | #39 | dev | 5cb8f97b (2026-04-17 13:14) | 1.94 h | Merge pull request #39 from Kevin-Renault/dev |
| 2026-04-17 16:44 | #41 | dev | 8d99bffb (2026-04-17 16:26) | 0.30 h | Merge pull request #41 from Kevin-Renault/dev |
| 2026-04-23 16:26 | #44 | dev | a48f25f2 (2026-04-17 16:47) | 143.65 h | Merge pull request #44 from Kevin-Renault/dev |

- Nombre d'éléments: 9
- Lead time moyen: 54.39 h

## Méthode de calcul

- Les merges sont extraits depuis l'historique Git avec git log sur les branches dev et main.
- Le lead time est estimé entre le premier commit de la PR et le merge final.
- Par défaut, si start_date ou end_date sont vides, le workflow prend la vie complète du flux considéré.
- Le rapport est écrit dans artifacts/dora-report.md et peut être publié comme artefact.

## CFR et MTTR (complément)

### Hypothèses utilisées

- Le périmètre de changement couvre les releases CI automatiques et les releases manuelles publiées.
- Un échec de changement est retenu en cas de rollback, correctif urgent post-release, ou incident confirmé dans les logs applicatifs.
- Le retour à l'état stable est validé par un pipeline vert et un service fonctionnel.

### Formules

- `CFR = (releases en échec / releases totales) * 100`
- `MTTR = somme des durées de rétablissement / nombre d'incidents`

### Statut sur ce rapport

- Lead time et fréquence: mesurés dans les tableaux ci-dessus.
- CFR: non consolidé automatiquement dans cette extraction (données incident/release à corréler).
- MTTR: non consolidé automatiquement dans cette extraction (journal d'incident non structuré sur la période).

### Actions pour la prochaine itération

1. Ajouter un export automatique des runs de release (succès/échec) dans le workflow DORA.
2. Ajouter un journal d'incidents simple (timestamp début/fin, cause, PR/release liée).
3. Publier CFR et MTTR calculés à chaque génération de rapport.
