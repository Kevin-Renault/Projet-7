# Plan de sauvegarde, de reprise et de mise à jour

Ce document décrit ce qu'il faut sauvegarder, ce qu'il faut restaurer, et comment remettre le projet en état après un incident.

## 1. Ce que l'on sauvegarde

### Données métier

- les entités `Person` et `Organization` créées dans l'application;
- les relations entre les personnes et les organisations;
- les données nécessaires à la restitution du comportement attendu de l'application.

### Configuration de supervision

- les exports Kibana, comme [misc/kibana/export.ndjson](misc/kibana/export.ndjson);
- la configuration ELK locale, si elle est adaptée ou complétée;
- les fichiers de configuration Docker du projet.

### Limite actuelle du projet

Le backend utilise HSQLDB. Si la base reste en mémoire ou non persistée, la sauvegarde des données métier n'a pas de valeur durable entre deux redémarrages. Dans ce cas, le plan sert surtout de cadre documentaire.

## 2. Objectif de reprise

La reprise consiste à remettre le projet dans un état exploitable après une perte de données, une erreur de configuration ou un incident de déploiement.

Concrètement, il faut pouvoir :

- redémarrer les services Docker;
- retrouver les données métier si une base persistante est utilisée;
- réimporter les objets Kibana si nécessaire;
- retrouver les logs et dashboards attendus.

## 3. Procédure de reprise

### Cas simple: redémarrage de l'environnement

1. arrêter les conteneurs si besoin;
2. relancer la stack ELK;
3. relancer l'application avec `docker compose up -d --build`;
4. vérifier l'accès au front, au back et à Kibana.

### Cas avec restauration de configuration Kibana

1. ouvrir Kibana;
2. aller dans **Stack Management**;
3. ouvrir **Saved Objects**;
4. importer [misc/kibana/export.ndjson](misc/kibana/export.ndjson);
5. vérifier la data view `microcrm-logs-*` et le dashboard importé.

### Cas avec restauration de données métier

Si une base persistante est mise en place plus tard, restaurer la sauvegarde de la base avant de redémarrer le back.

## 4. Plan de mise à jour

### Mises à jour applicatives

- mettre à jour le code source du front et du back;
- relancer les tests automatisés;
- rebuild les images Docker;
- vérifier les logs dans Kibana après déploiement.

### Mises à jour des dépendances

- vérifier les changements de version Gradle, Angular, Node, Spring Boot et des composants ELK;
- tester les changements dans un environnement local avant publication;
- s'assurer que les exports Kibana restent compatibles.

### Mises à jour de l'environnement

- conserver des versions cohérentes entre les images Docker et les dépendances du projet;
- éviter les mises à jour non testées sur la stack ELK ou sur la base de données.

## 5. Action automatisée de restauration

Une action automatisée pertinente serait de rejouer l'import Kibana depuis [misc/kibana/export.ndjson](misc/kibana/export.ndjson) après un déploiement ou une remise à zéro de l'espace Kibana.

Si une base persistante est ajoutée plus tard, une sauvegarde/restauration automatique de la base pourra compléter ce plan.

## 6. Résumé

Le périmètre de sauvegarde/reprise du projet couvre surtout :

- les données métier;
- la configuration Kibana;
- la configuration Docker et d'environnement.

Le sujet reste simple tant que la base n'est pas persistée. Le plan devient réellement opérationnel dès qu'un stockage durable est ajouté.