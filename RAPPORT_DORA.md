# Rapport DORA automatique

- Branche d'exécution: main
- Début demandé: <vide>
- Fin demandée: <vide>
- Règle par défaut: si une date est vide, le workflow prend toute la vie du flux concerné.

> Le flux branches -> dev mesure l'intégration intermédiaire.
> Le flux dev -> main mesure la livraison principale retenue pour la lecture DORA.

## Flux branches -> dev

- Période retenue: 2026-04-29T15:21:30+02:00 -> 2026-04-29T15:21:30+02:00

| Date merge | PR | Branche source | Premier commit | Lead time | Sujet |
| --- | ---: | --- | --- | ---: | --- |
| 2026-04-29 13:21 | #57 | feature/DORA-Report-finalisation | b468b089 (2026-04-29 13:14) | 0.11 h | Merge pull request #57 from Kevin-Renault/feature/DORA-Report-finalisation |

- Nombre d'éléments: 1
- Lead time moyen: 0.11 h

## Flux dev -> main

- Période retenue: 2026-04-02T16:05:26+02:00 -> 2026-04-29T13:37:19+02:00

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

- Nombre d'éléments: 16
- Lead time moyen: 33.52 h

## Tableau final des 4 métriques DORA

| Métrique DORA | Valeur | Source | Note |
| --- | ---: | --- | --- |
| Deployment frequency | 2.86 déploiements/semaine | GitHub Actions (, runs succès sur ) | 11 runs succès sur 13 runs analysés |
| Lead time for changes | 33.52 h | Git history ( -> ) | Moyenne sur la période retenue |
| Change failure rate (CFR) | 15.38% | GitHub Actions (, conclusions ) | 2 échecs sur 13 runs succès+échec |
| MTTR | 89.57 h | GitHub Actions ( -> prochain ) | 2 incidents résolus, 0 non résolus dans la fenêtre |

## Méthode de calcul

- Les merges sont extraits depuis l'historique Git avec git log sur les branches dev et main.
- Le lead time est estimé entre le premier commit de la PR et le merge final.
- Deployment frequency, CFR et MTTR sont calculés depuis l'API GitHub Actions sur le workflow ci.yml (branche main, event push).
- CFR utilise les runs terminés avec conclusion success/failure: CFR = failures / (success + failure).
- MTTR mesure le temps entre un run failure et le prochain run success.
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

## Préconisations opérationnelles et KPIs de suivi

### Alerte sur échec CI — priorité haute

**Constat** : le MTTR mesuré est de 89.57 h, principalement à cause d'un délai de correction long après un pipeline en échec (pause week-end probable).

**Préconisation** : déclencher une notification immédiate (GitHub notification / Slack / email) dès qu'un run `ci.yml` se termine avec `conclusion: failure` sur `main`.

Cible de correction : **≤ 4 heures ouvrées** après l'alerte.

Mise en œuvre possible dans GitHub Actions :

```yaml
# Extrait à ajouter dans ci.yml, job notify-failure
on:
  workflow_run:
    workflows: ["CI"]
    types: [completed]

jobs:
  notify:
    if: ${{ github.event.workflow_run.conclusion == 'failure' }}
    runs-on: ubuntu-latest
    steps:
      - name: Notify failure
        run: |
          echo "Pipeline CI en échec sur main — intervention requise sous 4h ouvrées"
          # Remplacer par un appel webhook Slack/Teams/email selon l'infrastructure
```

### MTTR comme KPI opérationnel

**Oui, le MTTR est un KPI à part entière**, et pas uniquement une métrique DORA.

| Caractéristique | Détail |
| --- | --- |
| Type | KPI opérationnel de résilience |
| Périmètre DORA | Élite : < 1 h — High : < 24 h — Medium : < 1 sem — Low : > 1 sem |
| Valeur mesurée ici | 89.57 h → niveau **Low** |
| Biais identifié | Inclut les pauses week-end ; mesure tous les échecs CI, pas uniquement les incidents production |
| Objectif cible | Ramener le MTTR à < 24 h (niveau High) via alerte immédiate + rotation d'astreinte définie |
| Suivi recommandé | Régénérer le rapport DORA après chaque sprint pour observer la tendance |

Le MTTR est le KPI le plus actionnable ici : une seule action (alerte + SLA de correction) permet de passer directement du niveau Low au niveau High sur ce projet.
