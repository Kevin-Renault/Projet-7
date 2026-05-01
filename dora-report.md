# Rapport DORA automatique

- Branche d'exécution: main
- Début demandé: <vide>
- Fin demandée: <vide>
- Règle par défaut: si une date est vide, le workflow prend toute la vie du flux concerné.

> Le flux branches -> dev mesure l'intégration intermédiaire.
> Le flux dev -> main mesure la livraison principale retenue pour la lecture DORA.

## Flux branches -> dev

- Période retenue: 2026-04-29T15:21:30+02:00 -> 2026-05-01T11:57:32+02:00

| Date merge | PR | Branche source | Premier commit | Lead time | Sujet |
| --- | ---: | --- | --- | ---: | --- |
| 2026-04-29 13:21 | #57 | feature/DORA-Report-finalisation | b468b089 (2026-04-29 13:14) | 0.11 h | Merge pull request #57 from Kevin-Renault/feature/DORA-Report-finalisation |
| 2026-04-30 15:13 | #59 | chore/finalisation-project | c5db9c1f (2026-04-30 14:07) | 1.09 h | Merge pull request #59 from Kevin-Renault/chore/finalisation-project |
| 2026-04-30 16:34 | #61 | chore/finalisation-project | d644f2cc (2026-04-30 15:50) | 0.74 h | Merge pull request #61 from Kevin-Renault/chore/finalisation-project |
| 2026-04-30 17:10 | #63 | chore/finalisation-project | 8c53885a (2026-04-30 17:05) | 0.09 h | Merge pull request #63 from Kevin-Renault/chore/finalisation-project |
| 2026-05-01 09:57 | #65 | chore/finalisation-project | 9f8cf752 (2026-05-01 09:35) | 0.37 h | Merge pull request #65 from Kevin-Renault/chore/finalisation-project |

- Nombre d'éléments: 5
- Lead time moyen: 0.48 h

## Flux dev -> main

- Période retenue: 2026-04-02T16:05:26+02:00 -> 2026-05-01T12:06:10+02:00

| Date merge | PR | Branche source | Premier commit | Lead time | Sujet |
| --- | ---: | --- | --- | ---: | --- |
| 2026-04-02 14:05 | #4 | dev | 9325fb71 (2026-04-01 14:58) | 23.12 h | Merge pull request #4 from Kevin-Renault/dev |
| 2026-04-02 15:26 | #13 | dev | e9e08334 (2026-04-02 14:19) | 1.11 h | Merge pull request #13 from Kevin-Renault/dev |
| 2026-04-16 15:23 | #19 | dev | 0b2ee0d9 (2026-04-02 15:43) | 335.66 h | Merge pull request #19 from Kevin-Renault/dev |
| 2026-04-16 17:12 | #21 | dev | e6d8c33f (2026-04-16 12:35) | 4.62 h | Merge pull request #21 from Kevin-Renault/dev |
| 2026-04-16 18:10 | #23 | dev | 10092901 (2026-04-16 17:13) | 0.94 h | Merge pull request #23 from Kevin-Renault/dev |
| 2026-04-17 11:05 | #26 | dev | 93259e08 (2026-04-17 10:05) | 1.00 h | Merge pull request #26 from Kevin-Renault/dev |
| 2026-04-17 12:22 | #30 | dev | eb63d729 (2026-04-17 11:42) | 0.67 h | Merge pull request #30 from Kevin-Renault/dev |
| 2026-04-17 13:10 | #33 | dev | 05047af4 (2026-04-17 12:26) | 0.73 h | Merge pull request #33 from Kevin-Renault/dev |
| 2026-04-17 15:10 | #39 | dev | 5cb8f97b (2026-04-17 13:14) | 1.94 h | Merge pull request #39 from Kevin-Renault/dev |
| 2026-04-17 16:44 | #41 | dev | 8d99bffb (2026-04-17 16:26) | 0.30 h | Merge pull request #41 from Kevin-Renault/dev |
| 2026-04-23 16:26 | #44 | dev | a48f25f2 (2026-04-17 16:47) | 143.65 h | Merge pull request #44 from Kevin-Renault/dev |
| 2026-04-24 10:09 | #47 | dev | 0c9d4809 (2026-04-24 09:51) | 0.30 h | Merge pull request #47 from Kevin-Renault/dev |
| 2026-04-24 10:41 | #50 | dev | 4e5948f8 (2026-04-24 10:22) | 0.33 h | Merge pull request #50 from Kevin-Renault/dev |
| 2026-04-28 12:42 | #52 | dev | 80fe94c3 (2026-04-28 12:36) | 0.11 h | Merge pull request #52 from Kevin-Renault/dev |
| 2026-04-29 10:29 | #54 | dev | 11785faa (2026-04-28 12:45) | 21.72 h | Merge pull request #54 from Kevin-Renault/dev |
| 2026-04-29 11:37 | #56 | dev | 2b3da056 (2026-04-29 11:26) | 0.17 h | Merge pull request #56 from Kevin-Renault/dev |
| 2026-04-30 15:38 | #60 | dev | c5db9c1f (2026-04-30 14:07) | 1.52 h | Merge pull request #60 from Kevin-Renault/dev |
| 2026-04-30 16:42 | #62 | dev | d644f2cc (2026-04-30 15:50) | 0.87 h | Merge pull request #62 from Kevin-Renault/dev |
| 2026-04-30 17:23 | #64 | dev | 97917563 (2026-04-30 16:47) | 0.60 h | Merge pull request #64 from Kevin-Renault/dev |
| 2026-05-01 10:06 | #66 | dev | 9f8cf752 (2026-05-01 09:35) | 0.51 h | Merge pull request #66 from Kevin-Renault/dev |

- Nombre d'éléments: 20
- Lead time moyen: 26.99 h

## Tableau final des 4 métriques DORA

| Métrique DORA | Valeur | Source | Note |
| --- | ---: | --- | --- |
| Deployment frequency | 4.37 déploiements/semaine | GitHub Actions (, runs succès sur ) | 18 runs succès sur 20 runs analysés |
| Lead time for changes | 26.99 h | Git history ( -> ) | Moyenne sur la période retenue |
| Change failure rate (CFR) | 10.00% | GitHub Actions (, conclusions ) | 2 échecs sur 20 runs succès+échec |
| MTTR | 89.57 h | GitHub Actions ( -> prochain ) | 2 incidents résolus, 0 non résolus dans la fenêtre |

## Méthode de calcul

- Les merges sont extraits depuis l'historique Git avec git log sur les branches dev et main.
- Le lead time est estimé entre le premier commit de la PR et le merge final.
- Deployment frequency, CFR et MTTR sont calculés depuis l'API GitHub Actions sur le workflow ci.yml (branche main, event push).
- CFR utilise les runs terminés avec conclusion success/failure: CFR = failures / (success + failure).
- MTTR mesure le temps entre un run failure et le prochain run success.
- Par défaut, si start_date ou end_date sont vides, le workflow prend la vie complète du flux considéré.
- Le rapport est écrit dans artifacts/dora-report.md et peut être publié comme artefact.

