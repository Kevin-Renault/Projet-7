# Pipeline CI : Règles De Performance Et De Publication

## Objectif

Ce document résume les améliorations appliquées au pipeline GitHub Actions avec deux objectifs :

1. Améliorer la vitesse d'exécution et la fiabilité sans nuire à la lisibilité.
2. Rendre explicites les règles de publication : ce qui peut être publié, ce qui n'est qu'un artefact candidat temporaire, et ce qui doit être supprimé.

Le workflow concerné est [.github/workflows/ci.yml](./workflows/ci.yml).

## Logique Actuelle Du Pipeline

Le pipeline est organisé dans l'ordre suivant :

1. `setup`
2. `test-application`
3. En parallèle après les tests :
   - `sonar-analysis`
   - `build-and-push-backend`
   - `build-and-push-frontend`
4. Validation Docker des images candidates :
   - `validate-docker-backend-with-pulled-image`
   - `validate-docker-frontend-with-pulled-image`
5. `release`
6. `merge-report`
7. `cleanup-candidate-images`

Cette structure permet de garder le workflow lisible tout en réduisant les temps d'attente inutiles entre les jobs.

## Améliorations De Performance Appliquées

### 1. Les métadonnées d'image sont calculées une seule fois

Le job `setup` calcule et expose :

- `repo_lower`
- `image_tag`
- `backend_image`
- `frontend_image`

Cela évite de recalculer les références d'images dans plusieurs jobs et réduit les risques d'incohérence de configuration.

### 2. Le cache Gradle est activé là où il est utile

Le cache Gradle est activé dans :

- `test-application`
- `sonar-analysis`
- `release`

Cela réduit les coûts liés aux résolutions répétées de dépendances et aux recompilations backend.

### 3. Le cache npm est activé là où les dépendances frontend sont installées

Le cache npm est activé dans :

- `test-application`
- `release`

Le cache est basé sur `front/package-lock.json` pour garder une configuration explicite et simple à comprendre.

### 4. Le cache Docker Buildx est activé pour les images candidates et les images de release

Le cache Buildx est utilisé pour :

- `build-and-push-backend`
- `build-and-push-frontend`
- le build de l'image backend de release
- le build de l'image frontend de release

Des scopes distincts sont utilisés pour le backend et le frontend afin de garder un comportement de cache prévisible :

- `scope=back`
- `scope=front`

### 5. L'analyse Sonar s'exécute désormais en parallèle des builds Docker candidats

Les images Docker candidates sont construites après les tests, sans attendre la fin de Sonar.

Cela améliore la durée totale du pipeline tout en gardant Sonar bloquant pour la release finale.

### 6. La concurrence du workflow annule les runs obsolètes

Le workflow utilise un bloc `concurrency` pour annuler les anciens runs encore en cours sur la même branche ou la même pull request.

Cela améliore l'utilisation des ressources et évite de perdre du temps sur des exécutions devenues obsolètes.

### 7. Les timeouts protègent le pipeline contre les jobs bloqués

Des valeurs `timeout-minutes` explicites sont définies sur les jobs longs ou susceptibles de se bloquer.

Cela ne rend pas les runs sains plus rapides, mais améliore la fiabilité opérationnelle en arrêtant plus tôt les exécutions figées.

## Images Candidates Et Images De Release

Le pipeline distingue volontairement deux catégories d'images Docker.

### Images candidates

Les images candidates sont des artefacts techniques de CI utilisés uniquement pour valider le pipeline.

Caractéristiques :

- construites avant la publication finale
- taguées à partir du tag calculé pour la CI
- utilisées par les jobs de validation Docker
- non considérées comme des livrables officiels publiés

Exemples :

- `ghcr.io/<repo>/backend:<image_tag>`
- `ghcr.io/<repo>/frontend:<image_tag>`

### Images de release

Les images de release sont des livrables officiels publiés uniquement lorsque tous les contrôles bloquants sont validés.

Caractéristiques :

- publiées uniquement dans le job `release`
- versionnées avec la version semantic-release
- destinées à être conservées

Exemples :

- `ghcr.io/<repo>/backend:v<release_version>`
- `ghcr.io/<repo>/frontend:v<release_version>`

## Ce Qui Bloque La Publication Finale

La publication finale est bloquée tant que tous les contrôles requis ne sont pas au vert.

### Conditions obligatoires avant `release`

1. `test-application` doit réussir.
2. `sonar-analysis` doit réussir.
3. `validate-docker-backend-with-pulled-image` doit réussir.
4. `validate-docker-frontend-with-pulled-image` doit réussir.
5. L'événement doit être un `push` sur `main` ou `dev`.

Si une seule de ces conditions n'est pas remplie, `release` ne s'exécute pas.

## Ce Qui Se Passe Pendant La Release

Lorsque les conditions bloquantes sont satisfaites, le job `release` peut :

1. calculer la version finale semantic-release
2. publier le package frontend sur GitHub Packages
3. publier le package backend sur GitHub Packages
4. publier l'image Docker backend versionnée
5. publier l'image Docker frontend versionnée

Ce sont les seuls artefacts considérés comme des sorties finales de publication.

## Règles De Nettoyage

Le pipeline inclut un job dédié `cleanup-candidate-images`.

### Objectif du nettoyage

L'objectif est de supprimer les images Docker temporaires candidates poussées uniquement pour la validation CI.

Cela évite de conserver dans GHCR des versions de conteneurs qui ne sont pas des releases.

### Comportement du nettoyage

Le job de nettoyage :

- s'exécute avec `if: always()`
- s'exécute après Sonar, les validations Docker et `release`
- supprime le tag de l'image candidate backend correspondant au run courant
- supprime le tag de l'image candidate frontend correspondant au run courant

Cela signifie que les images candidates temporaires sont traitées comme des artefacts jetables.

### Pourquoi le nettoyage est effectué même si la release réussit

Même après une release réussie, les images candidates ne sont plus utiles parce que :

- les images finales versionnées existent déjà
- les tags candidats n'ont servi qu'à la validation
- conserver les deux créerait du bruit inutile dans GHCR

## Résumé Des Décisions De Publication

### La publication est autorisée quand

- les tests passent
- l'analyse Sonar passe
- la validation Docker backend passe
- la validation Docker frontend passe
- le workflow s'exécute sur une branche `push` autorisée

### La publication est refusée quand

- les tests échouent
- l'analyse Sonar échoue
- la validation de l'image candidate backend échoue
- la validation de l'image candidate frontend échoue
- le workflow ne s'exécute pas sur `main` ou `dev`

### Le nettoyage est déclenché quand

- le workflow atteint la phase de nettoyage
- indépendamment du succès ou de l'échec global
- pour les tags d'images candidates temporaires du run courant

## Principes De Lisibilité Conservés

Le pipeline a volontairement été gardé lisible pour un projet de formation.

Les choix suivants ont été faits intentionnellement :

- les jobs backend et frontend restent séparés et explicites
- la logique de validation Docker reste visible directement dans le workflow
- les caches sont ajoutés directement dans les jobs au lieu de masquer la logique derrière trop d'abstraction
- aucune stratégie matrix n'a été introduite pour les jobs backend/frontend
- aucun découpage en reusable workflow n'a été introduit

Le résultat est un workflow qui reste compréhensible dans un seul fichier tout en bénéficiant d'améliorations de performance significatives.

## Évolutions Futures À Étudier Avec Prudence

Évolutions possibles, uniquement si elles restent compréhensibles pour le public du projet :

1. déplacer les dépendances `semantic-release` dans un ensemble de dépendances géré par le projet au lieu de les installer globalement
2. vérifier si l'installation de Chrome peut être simplifiée ou remplacée
3. ajuster plus finement les timeouts selon les durées mesurées des jobs
4. ajouter une attente explicite du Quality Gate si Sonar doit échouer strictement sur ce statut

Ces évolutions ne doivent être appliquées que si elles conservent un workflow facile à expliquer et à maintenir.