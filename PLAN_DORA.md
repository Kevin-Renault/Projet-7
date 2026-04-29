# Plan DORA

Ce document explique simplement ce que sont les métriques DORA et comment les relier au projet MicroCRM.

## 1. DORA, c'est quoi ?

DORA désigne 4 métriques utilisées pour évaluer la performance d'une chaîne de livraison logicielle.

L'objectif n'est pas de faire un score "pour faire joli", mais de savoir si l'équipe livre souvent, vite, sans casser, et si elle sait revenir en arrière rapidement en cas de problème.

## 2. Les 4 métriques DORA

### 2.1 Deployment frequency

Fréquence de déploiement.

- Ce que ça mesure : combien de fois une nouvelle version est mise en production ou publiée.
- Ce que ça dit : si le projet avance par petites livraisons régulières ou par gros lots rares.
- Exemple MicroCRM : nombre de releases GitHub Actions ou de déploiements Docker par semaine ou par mois.

### 2.2 Lead time for changes

Temps de livraison d'un changement.

- Ce que ça mesure : le temps entre le premier commit d'un changement et sa disponibilité réelle dans l'environnement cible, jusqu'au déploiement ou à la publication du livrable.
- Ce que ça dit : si la chaîne CI/CD est rapide ou trop lente.
- Exemple MicroCRM : temps entre un premier commit sur une branche de travail et la fin du job `release` de la CI, puisque c'est lui qui publie effectivement les artefacts après validation qualité et validation Docker.

Si tu veux une lecture plus large, tu peux aussi suivre le temps jusqu'au merge sur `main`, mais pour une mesure DORA ici, le point de fin le plus clair est la publication réussie par la CI.

### 2.3 Change failure rate

Taux d'échec des changements.

- Ce que ça mesure : la part des déploiements qui provoquent un incident, une régression ou une correction urgente.
- Ce que ça dit : si les changements livrés sont fiables.
- Exemple MicroCRM : déploiements qui cassent le front, le back, les logs ELK ou les tests d'intégration.

### 2.4 MTTR

Mean Time To Restore, ou temps moyen de remise en service.

- Ce que ça mesure : le temps moyen nécessaire pour revenir à un état stable après un incident.
- Ce que ça dit : si l'équipe sait diagnostiquer et corriger rapidement.
- Exemple MicroCRM : temps entre l'apparition d'un problème Docker, backend ou Kibana, et le retour à un fonctionnement normal.

## 3. À quoi ça sert dans MicroCRM ?

Pour ce projet, DORA sert surtout à montrer que la chaîne de livraison est suivie de façon concrète.

Tu peux t'en servir pour dire :

- est-ce que les commits sont testés rapidement ?
- est-ce que les déploiements Docker sont fiables ?
- est-ce qu'un incident peut être détecté et corrigé vite ?
- est-ce que la CI et le monitoring donnent un vrai signal sur la qualité du projet ?

## 4. Sources de données possibles

Tu peux relier les métriques DORA à ce que le projet produit déjà :

- **GitHub Actions** : succès ou échec des pipelines, temps d'exécution, fréquence des runs.
- **Git history** : date d'un commit, date d'un merge, fréquence des changements.
- **Docker Compose / Docker image** : date de publication ou de reconstruction d'image.
- **SonarQube Cloud** : qualité, dette technique, anomalies détectées.
- **ELK / Kibana** : erreurs, logs front/back, incidents applicatifs.
- **Tests** : taux de réussite, régressions détectées.

## 5. Comment l'écrire simplement dans le rapport

Tu n'as pas besoin d'inventer un gros système de mesure.

Tu peux dire par exemple :

- la fréquence de déploiement est observée via les exécutions de CI et les builds Docker;
- le lead time est estimé entre le push et la validation finale du pipeline;
- le taux d'échec est calculé à partir des pipelines en erreur ou des déploiements corrigés;
- le MTTR est estimé à partir du temps entre l'apparition d'un incident dans les logs et sa résolution.

### 5.1 Hypothèses explicites pour CFR et MTTR

Pour éviter toute ambiguïté, le projet retient les hypothèses suivantes:

- Un changement est compté comme "déployé" quand le job `release` de la CI est exécuté jusqu'à la publication (circuit automatique) ou quand la release manuelle publie ses artefacts (circuit manuel).
- Un échec de changement (CFR) est retenu si au moins un de ces signaux est observé après release:
	- rollback/tag correctif urgent,
	- pipeline de correction immédiate suite à incident prod,
	- incident applicatif confirmé dans les logs (`ERROR`) lié au changement livré.
- Le MTTR est mesuré entre l'horodatage de détection de l'incident et l'horodatage de retour à l'état stable (pipeline vert + service rétabli).

Formules utilisées:

- `CFR = (nombre de releases en échec / nombre total de releases) * 100`
- `MTTR = somme(des temps de rétablissement) / nombre d'incidents`

### 5.2 Protocole de mesure reproductible

1. Extraire les runs GitHub Actions des workflows de release sur la période.
2. Lister les releases publiées et leurs timestamps.
3. Corréler avec les incidents applicatifs observés dans ELK (niveau `ERROR`, services front/back).
4. Calculer CFR et MTTR avec les formules ci-dessus.
5. Documenter les limites si des événements manquent (ex: incident non tracé).

## 6. Ce qu'on attend généralement d'un bon document DORA

- une définition simple de chaque métrique;
- une explication adaptée au projet;
- une idée de la source de mesure;
- une limite claire si la donnée n'est pas encore réellement industrialisée.

## 7. Tableau provisoire DORA et KPI

Ce tableau est pensé comme un support de documentation provisoire. L'idée est de le renseigner sur au moins 3 exécutions de pipeline et d'y distinguer clairement les métriques issues de la CI/CD de celles issues de l'application via ELK.

### 7.1 Flux branches -> `dev`

Le flux branches -> `dev` correspond à l'intégration intermédiaire. Il regroupe les PR de fonctionnalité, les correctifs, les ajustements de tests, le refactoring et la préparation ELK. Dans l'historique visible, on compte 29 PR de ce type, ce qui montre un rythme plus dense que la livraison finale.

| Indicateur | Type | Source | Méthode de calcul | Valeur provisoire | Analyse commentée |
| --- | --- | --- | --- | --- | --- |
| Fréquence d'intégration vers `dev` | KPI flux | Git history | Nombre de PR mergées dans `dev` sur une période donnée | 29 PR visibles dans l'historique | Flux très actif: il sert à stabiliser les changements avant la livraison finale. |
| Lead time d'intégration | KPI flux | Git history | Temps entre le premier commit d'une branche et son merge dans `dev` | À calculer sur 3 PR représentatives | Donne une lecture de la vitesse de travail de l'équipe sur le flux préparatoire. |
| Taux de rework | KPI flux | GitHub Actions + Git history | Nombre de PR réouvertes ou corrigées après review / nombre total de PR vers `dev` | À relever sur les PR du flux | Permet de voir si les branches arrivent bien préparées ou si elles nécessitent beaucoup de retours. |
| Temps de tests avant intégration | KPI pipeline | GitHub Actions | Durée des tests exécutés avant le merge vers `dev` | À relever sur 3 runs minimum | Indique si la consolidation du code avant intégration reste rapide. |
| Qualité SonarQube avant intégration | KPI qualité | SonarQube Cloud | État de qualité après les changements destinés à `dev` | Plusieurs correctifs sonar visibles | Le flux vers `dev` est fortement influencé par les corrections de qualité et de test. |
| Fréquence d'erreurs détectées tôt | KPI application | ELK / Kibana | Nombre d'erreurs observées pendant les tests ou les vérifications locales | Non mesuré ici | Sert à repérer les problèmes avant qu'ils n'atteignent la livraison finale. |

### 7.2 Flux `dev` -> `main`

Le flux `dev` -> `main` correspond à la livraison principale. Il regroupe les PR déjà consolidées, les releases CI et les tags semantic-release. Dans l'historique visible, on compte 10 merges de `dev` vers `main`, ce qui correspond au vrai rythme de livraison à retenir pour la lecture DORA.

| Indicateur | Type | Source | Méthode de calcul | Valeur provisoire | Analyse commentée |
| --- | --- | --- | --- | --- | --- |
| Deployment frequency | DORA | GitHub Actions / releases CI | Nombre de jobs `release` réussis sur une période donnée | 8 releases en 7 jours | C'est le meilleur indicateur du rythme de livraison réel. |
| Lead time for changes | DORA | Git history + GitHub Actions | Temps entre le premier commit d'un changement et la fin du job `release` | À estimer par PR/release | C'est le point de mesure le plus propre pour DORA dans ce projet. |
| Change failure rate | DORA | GitHub Actions + ELK | Nombre de releases ayant généré un incident / nombre total de releases | Pas de signal d'incident visible dans l'extraction | À confirmer avec les logs ELK et l'historique des échecs de pipeline. |
| MTTR | DORA | ELK / logs applicatifs | Temps entre la détection d'un incident et le retour à un état stable | Non mesuré dans cette extraction | Impossible à conclure avec Git seul; il faut croiser les logs ELK et les incidents réels. |
| Temps de build de release | KPI pipeline | GitHub Actions | Durée du job de publication finale | À relever sur 3 runs minimum | Permet de vérifier si la publication finale reste fluide malgré les validations en amont. |
| Temps de tests de release | KPI pipeline | GitHub Actions | Durée totale des tests qui précèdent le job `release` | À relever sur 3 runs minimum | Sert à voir si la chaîne de validation ralentit la livraison. |
| Qualité SonarQube de livraison | KPI qualité | SonarQube Cloud | État global au moment du passage vers `main` | Plusieurs correctifs sonar présents sur la période | La qualité conditionne directement la capacité à livrer sur `main`. |
| Fréquence des erreurs applicatives | KPI application | ELK / Kibana | Nombre d'événements de niveau erreur après livraison | Non mesuré ici | À suivre après les livraisons pour relier stabilité et changement. |
| Pic de logs après release | KPI application | ELK / Kibana | Volume maximal de logs sur un intervalle court après déploiement | Non mesuré ici | Utile pour repérer un comportement anormal juste après la livraison. |

Méthode de lecture recommandée : les quatre métriques DORA décrivent la performance de livraison, tandis que les KPI supplémentaires servent à expliquer pourquoi le pipeline est rapide, lent, fiable ou fragile.

Méthode de calcul simple pour le rapport :

- prendre les horodatages GitHub Actions pour estimer les durées CI/CD;
- utiliser les logs ELK pour compter les erreurs et les pics d'activité;
- conserver les valeurs sur plusieurs exécutions afin d'éviter un résultat isolé;
- noter quand une mesure est approximative, par exemple si la production n'est pas encore persistée.

Le workflow [.github/workflows/dora-report.yml](.github/workflows/dora-report.yml) permet de régénérer automatiquement le rapport avec une période personnalisée.

## 8. Préconisation liée aux déploiements

Si on veut réduire l'impact d'un déploiement en production sur les utilisateurs en cours, il faut soit prévoir un déploiement sans coupure côté développement, soit mettre en place un load balancing avec bascule progressive du trafic.

Dans ce projet, cette préconisation reste surtout théorique pour l'instant, car il n'y a pas encore d'architecture multi-instance ni de routage en temps réel.

Il reste aussi un point important: avec une base persistée, il faudrait gérer la synchronisation de la BDD entre versions ou entre instances. Ici, ce sujet est limité par le fait que le projet ne repose pas encore sur une base de données durable exposée en production.

## 9. Version courte à retenir

DORA = 4 indicateurs pour savoir si l'équipe livre souvent, rapidement, sans casse, et récupère vite après incident.

Dans MicroCRM, ils peuvent être reliés à :

- la CI GitHub Actions;
- les builds Docker;
- les tests;
- les logs ELK;
- les corrections d'incident.

## 10. Préconisations opérationnelles (version projet)

- Réduire la variance du lead time en imposant une revue PR sous 24h pour les changements orientés release.
- Suivre CFR/MTTR à chaque sprint avec une fenêtre glissante de 30 jours.
- Conserver la release automatique pour la cadence standard, et la release manuelle pour les jalons exceptionnels.
- Ajouter une alerte Kibana sur le volume d'erreurs post-release pour accélérer la détection d'incident.
