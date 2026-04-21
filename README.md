<p align="center">
   <img src="./front/src/favicon.png" width="192px" />
</p>

# MicroCRM (P7 - Développeur Full-Stack - Java et Angular - Mettez en œuvre l'intégration et le déploiement continu d'une application Full-Stack)

MicroCRM est une application de démonstration basique ayant pour être objectif de servir de socle pour le module "P7 - Développeur Full-Stack".

L'application MicroCRM est une implémentation simplifiée d'un ["CRM" (Customer Relationship Management)](https://fr.wikipedia.org/wiki/Gestion_de_la_relation_client). Les fonctionnalités sont limitées à la création, édition et la visualisations des individus liés à des organisations.

![Page d'accueil](./misc/screenshots/screenshot_1.png)
![Édition de la fiche d'un individu](./misc/screenshots/screenshot_2.png)

## Code source

### Organisation

Ce [monorepo](https://en.wikipedia.org/wiki/Monorepo) contient les 2 composantes du projet "MicroCRM":

- La partie serveur (ou "backend"), en Java SpringBoot 3;
- La partie cliente (ou "frontend"), en Angular 17.

### Démarrer avec les sources

#### Serveur

##### Dépendances

- [OpenJDK >= 17](https://openjdk.org/)

##### Procédure

1. Se positionner dans le répertoire `back` avec une invite de commande:

   ```shell
   cd back
   ```

2. Construire le JAR:

   ```shell
   # Sur Linux
   ./gradlew build

   # Sur Windows
   gradlew.bat build
   ```

3. Démarrer le service:

   ```shell
   java -jar build/libs/microcrm-0.0.1-SNAPSHOT.jar
   ```

Puis ouvrir l'URL http://localhost:8080 dans votre navigateur.

#### Client

##### Dépendances

- [NPM >= 10.2.4](https://www.npmjs.com/)

##### Procédure

1. Se positionner dans le répertoire `front` avec une invite de commande:

   ```shell
   cd front
   ```

2. (La première fois seulement) Installer les dépendances NodeJS:

   ```shell
   npm install
   ```

3. Démarrer le service de développement:

   ```shell
   npx @angular/cli serve
   ```

Puis ouvrir l'URL http://localhost:4200 dans votre navigateur.

### Exécution des tests

#### Client

**Dépendances**

- Google Chrome ou Chromium

Dans votre terminal:

```shell
cd front
CHROME_BIN=</path/to/google/chrome> npm test
```

#### Serveur

Dans votre terminal:

```shell
cd back
./gradlew test
```

### Qualité de code SonarQube

L'analyse SonarQube est branchée dans la CI GitHub Actions après les tests.

#### Prérequis

- Un serveur SonarQube ou SonarQube Cloud
- Un token SonarQube valide
- Une clé de projet SonarQube, ici `microcrm-projet-7`

#### Configuration GitHub

Ajoute dans les paramètres du dépôt :

- un secret `SONAR_TOKEN`
- la clé d'organisation SonarCloud `kevin-renault`

La CI utilise maintenant SonarCloud (`https://sonarcloud.io`) plutôt qu'un serveur local.

#### Rapports pris en compte

- couverture Java : `back/build/reports/jacoco/test/jacocoTestReport.xml`
- couverture Angular : `front/coverage/olympic-games-starter/lcov.info`

L'analyse couvre le backend et le frontend dans un seul projet SonarQube.

### Points critiques relevés et corrigés

- Côté front, les ressources CSS et les assets de production sont maintenant fingerprintés avec un hash grâce à `outputHashing: all` dans le build Angular.
- Ce point était critique pour éviter la persistance de fichiers CSS obsolètes après déploiement et limiter les comportements incohérents liés au cache navigateur ou au cache CDN.
- La correction est appliquée au build de production du frontend, ce qui garantit des noms de fichiers uniques à chaque nouvelle version.

### Préconisations d'usage

- Préférer une architecture multi-couche côté back, avec séparation entre API, service métier et persistance.
- Ajouter un handler global pour les erreurs côté backend afin de centraliser les réponses d'erreur et éviter les retours incohérents.
- Valider les données d'entrée au plus tôt, par exemple avec des contraintes Bean Validation sur les DTO ou les entités exposées.
- Conserver les logs métier et les logs techniques séparés pour faciliter le diagnostic dans ELK et dans la CI.

### Images Docker

#### Client

##### Construire l'image

```shell
docker build --target front -t orion-microcrm-front:latest .
```

##### Exécuter l'image

```shell
docker run -it --rm -p 80:80 -p 443:443 orion-microcrm-front:latest
```

L'application sera disponible sur https://localhost.

#### Serveur

##### Construire l'image

```shell
docker build --target back -t orion-microcrm-back:latest .
```

##### Exécuter l'image

```shell
docker run -it --rm -p 8080:8080 orion-microcrm-back:latest
```

L'API sera disponible sur http://localhost:8080.

#### Tout en un

```shell
docker build --target standalone -t orion-microcrm-standalone:latest .
```

##### Exécuter l'image

```shell
docker run -it --rm -p 8080:8080 -p 80:80 -p 443:443 orion-microcrm-standalone:latest
```

L'application sera disponible sur https://localhost et l'API sur http://localhost:8080.

### Stack ELK locale

Pour centraliser les logs applicatifs en local, lancer d'abord la stack ELK dédiée :

```shell
docker compose -f docker-compose-elk.yml up -d
```

Puis démarrer l'application avec le compose principal :

```shell
docker compose up -d --build
```

Les logs JSON du back sont envoyés à Logstash quand le profil `elk` est actif. Kibana est ensuite disponible sur http://localhost:5601.

#### Utilisation de Kibana

Une fois Kibana ouvert, cliquer sur **Explore on my own**, puis :

1. aller dans **Stack Management**;
2. ouvrir **Data Views**;
3. cliquer sur **Create data view**;
4. saisir l'index `microcrm-logs-*`;
5. valider la création.

Ensuite, utiliser **Discover** pour consulter les logs bruts, filtrer par niveau `INFO`, `WARN` ou `ERROR`, puis créer si besoin un premier dashboard pour suivre le volume de logs et les erreurs.
