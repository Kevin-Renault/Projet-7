# Projet-7
Mettez en œuvre l'intégration et le déploiement continu d'une application Full-Stack

## Stratégie de branches / Branching Strategy

Ce dépôt suit une stratégie de branches stricte, appliquée via des workflows GitHub Actions.

### Branches principales

| Branche | Rôle |
|---------|------|
| `main`  | Branche de production. Ne reçoit que des fusions venant de `dev` ou de `bugfix/*`. |
| `dev`   | Branche d'intégration. Toutes les nouvelles branches fonctionnelles sont créées à partir d'elle. |

### Règles

1. **Fusion vers `main`** : Seules les branches `dev` et `bugfix/*` peuvent ouvrir une pull request vers `main`.
2. **Création de branches** : Toutes les nouvelles branches doivent être créées à partir de `dev`, à l'exception des branches `bugfix/*` qui sont exemptées de cette vérification (elles peuvent donc être créées depuis `main` pour corriger rapidement un bug en production).

### Nommage des branches

| Type       | Préfixe      | Exemple               | Base autorisée |
|------------|--------------|-----------------------|----------------|
| Bugfix     | `bugfix/`    | `bugfix/login-error`  | `main` ou `dev` |
| Feature    | Libre        | `feature/new-ui`      | `dev` uniquement |
| Autre      | Libre        | `refactor/cleanup`    | `dev` uniquement |

### Workflows CI

- **`check-pr-source-branch.yml`** : Vérifie que toute PR vers `main` provient de `dev` ou d'une branche `bugfix/*`.
- **`check-branch-naming.yml`** : Vérifie que toute nouvelle branche (hors `bugfix/*`) est issue de `dev`.
