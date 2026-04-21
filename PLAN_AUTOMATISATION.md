# Plan de testing périodique, de sécurité et de conteneurisation

Ce document formalise les règles d'automatisation du projet MicroCRM avant la mise en œuvre technique du pipeline.

## 1. Plan de testing périodique

### Objectif

Garantir la validation continue du comportement attendu de l'application, la non-régression fonctionnelle et la détection rapide des anomalies côté backend et frontend.

### Types de tests exécutés

- **Backend** : tests unitaires et tests d'intégration Spring Boot lancés avec Gradle.
- **Frontend** : tests unitaires Angular/Karma lancés avec npm et Chrome/Chromium.
- **Vérifications d'intégration** : validation du démarrage applicatif et du bon fonctionnement des conteneurs Docker quand cela est pertinent.

### Moments d'exécution

- **À chaque `push`** sur une branche de travail.
- **À chaque `pull request`** avant fusion.
- **De manière périodique sur la branche principale** : cette règle est prévue dans la stratégie de test, mais elle n'est pas encore activée dans le workflow actuel.
- **Évolutif** : si le besoin se confirme, une planification de type nightly ou hebdomadaire pourra être ajoutée plus tard pour refaire un contrôle complet.

### Règles retenues

- Les dépendances doivent être installées avant l'exécution des tests.
- Les tests doivent être lancés avant toute étape de build d'image ou de publication.
- Les résultats des tests doivent être archivés comme artefacts de CI pour pouvoir être consultés après exécution.
- Les exécutions périodiques sur la branche principale sont un objectif de gouvernance, pas encore une automatisation effective dans le dépôt.

### Indicateurs attendus

- Tests backend et frontend exécutés automatiquement.
- Résultats récupérables dans GitHub Actions.
- Régression détectée avant publication d'une image ou d'un package.

## 2. Plan de sécurité

### Rôle de SonarQube Cloud

SonarQube Cloud est utilisé comme analyseur qualité et sécurité du code source. Il intervient après l'exécution des tests afin de bénéficier des fichiers de couverture et des classes compilées.

### Types de problèmes surveillés

- **Vulnérabilités** détectées dans le code.
- **Code smells** signalant une dette technique ou une mauvaise maintenabilité.
- **Bugs potentiels** ou comportements suspects.
- **Security hotspots** nécessitant une revue manuelle.

### Bonnes pratiques attendues dans la CI

- Ne jamais versionner les secrets dans le dépôt.
- Utiliser des secrets GitHub Actions pour les jetons d'accès et les clés de publication.
- Installer les dépendances dans un environnement reproductible.
- Réutiliser les caches quand cela améliore la vitesse sans compromettre la fiabilité.
- Faire passer l'analyse Sonar avant toute publication finale.
- Bloquer la release si les tests ou l'analyse qualité échouent.

### Objectif sécurité

- Réduire le risque d'introduire une régression ou une vulnérabilité.
- Donner un signal rapide sur la qualité du code avant toute mise en production.
- Maintenir un niveau de dette technique compatible avec un projet maintenable.

## 3. Principes de conteneurisation et de déploiement

### Rôle des Dockerfiles existants

Les Dockerfiles servent à construire des images reproductibles pour :

- le backend Spring Boot,
- le frontend Angular,
- une image standalone combinant les deux pour des usages de démonstration ou d'exécution locale.

Ils permettent de figer les étapes de build et d'exécution dans un format portable.

### Rôle de `docker-compose`

`docker-compose.yml` orchestre le démarrage conjoint du backend et du frontend en local.

`docker-compose-elk.yml` orchestre la stack ELK dédiée au monitoring local.

Le rôle de Docker Compose est de :

- simplifier le lancement local,
- harmoniser les réseaux entre services,
- exposer les ports nécessaires,
- permettre un environnement proche de celui utilisé en automatisation.

### Stratégie de déploiement envisagée

- Construire des images Docker candidates pendant la CI.
- Valider ces images dans des conteneurs proches du contexte réel d'utilisation.
- Publier les images versionnées uniquement si les contrôles bloquants sont validés.
- Utiliser des tags d'images candidats temporaires pour la validation, puis des tags de release pour les livrables officiels.

### Points de vigilance

- Les versions des images doivent rester cohérentes entre backend, frontend et monitoring.
- Les images candidates ne doivent pas être confondues avec les images de release.
- Le déploiement automatisé ne doit démarrer qu'après les validations qualité et sécurité.
- Le pipeline doit rester lisible et explicable dans un contexte de projet de formation.

## 4. Synthèse opérationnelle

Cette stratégie d'automatisation repose sur l'enchaînement suivant :

1. exécution des tests backend et frontend,
2. analyse qualité et sécurité avec SonarQube Cloud,
3. construction des images Docker candidates,
4. validation de l'exécution conteneurisée,
5. publication des images versionnées si tout est conforme.

L'objectif est de garantir un pipeline simple, reproductible et cohérent avec les exigences du projet full-stack Java / Angular.
