# Guide de lancement

[Read in English](README.md)

[Lire le guide complet ici](docs/GUIDE.md)

Ce guide permet d'installer et de faire tourner le projet en local, de la base de données au travers du backend jusqu'au frontend.

---

## Prérequis

Assurez-vous d'installer les outils logiciels suivants avant de commencer :

| Outil | Usage | Version |
| --- | --- | --- |
| Java | Pour compiler et exécuter le backend | Java 25 LTS |
| Gradle | Pour lancer le backend | | 
| Node.js + npm | Pour exécuter le frontend | |
<!-- | Git | Pour cloner le projet | -->

<!-- Le **Gradle Wrapper** est inclus dans le projet : pas besoin d'installer Gradle séparément. -->

---

## Clonez le projet

Localisez l'archive du projet dans le dernier Release de ce dépôt GitHub.

Téléchargez cette archive dans un répertoire local "demogl" sur votre machine. 

<!-- 
```bash
git clone https://github.com/sgl-umons/demogl.git
cd demogl
```
-->

Le contenu de ce répertoire devrait suivre la structure suivante (possiblement avec quelques sous-répertoires et fichiers supplémentaires qui ne sont pas pertinent pour ce guide) :

```
demogl/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── .env              ← config backend
├── src/              ← backend
│   ├── main/           ← code source Java     
│   │   ├── java/com/example/demo/
│   │   └── resources/
│   │       └── application.yaml
│   └── test/           ← tests Java
└── frontend/         ← frondend
    ├── .env            ← config frontend
    ├── src/            ← code source frontend
    ├── package.json
    └── vite.config.ts
```

---

## Configurez la base de données PostgreSQL

Les instructions supposent l'utilisation de Supabase. Vous pouvez utilisez tout autre client PostgreSQL compatible avec JDBC.

1. Allez sur [supabase.com](https://supabase.com), créez un compte, et connectez vous.
2. Créez un nouveau projet.
3. **Choisissez un mot de passe pour la base de données au moment de la création. Gardez-le pour se connecter à la DB après.**
4. Une fois le projet créé, allez dans **Connect** pour récupérer les informations de connexion.
5. Choisissez **Direct Connection string**
6. Choisissez le mode **Session pooler**.
7. Choisissez le type **JDBC**.

Gardez ces quatres informations à portée de main pour l'étape suivante :

- host
- username
- port
- password (le mot de passe choisi à l'étape 3)

---

## Configurez le backend

### Créez les variables d'environnement

Vous avez besoin de variables d'environnement pour accéder à la base de données distante.
Pour ce faire, créez un fichier `.env` à la **racine du projet** (là où se trouve `build.gradle`) :

```
DB_URL='jdbc:postgresql://[HOST]:[PORT]/postgres'
DB_USERNAME='[USERNAME]'
DB_PASSWORD='[PASSWORD]'
```
**(`postgres` est le nom par défaut de la base de données dans Supabase. Si vous le changez, n'oubliez pas de modifiez aussi le fichier `.env`)**

Voici un exemple de fichier `.env` :

```
DB_URL='jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres'
DB_USERNAME='postgres.tttrpqqpsixghjtrkenj'
DB_PASSWORD='mysecretpassword'
```

**Attention!** Ne committez et pushez **jamais** les fichiers `.env` dans votre dépôt Git. Ils contiennent des informations confidentielles et sensibles (telles que votre nom d'utilisateur et votre mot de passe) que vous ne souhaitez pas voir utilisées à mauvais escient par des tiers ! Veillez à ajouter les fichiers `.env` au fichier `.gitignore` afin d'éviter de les pusher par inadvertance.

---

## Lancez le backend

### Option A (recommandé) - Lancez Gradle d'un terminal

Chargez le contenu du `.env` dans l'environnement **avant** de lancer Gradle, depuis la racine du projet. La méthode dépend de l'OS utilisé.

#### Linux / macOS :

Dans un terminal à la racine du projet :

```bash
export $(cat .env | xargs)
```

ou alors (pour exporter une variable à la fois) :

```bash
export DB_URL=...
export DB_USERNAME=...
export DB_PASSWORD=...
```

Puis lancez l'API :

```bash
./gradlew bootRun
```

Vérifiez que les variables d'environnement sont bien actives :

```bash
export -p
```

#### Windows (PowerShell) :

```powershell
$env:DB_URL="..."
$env:DB_USERNAME="..."
$env:DB_PASSWORD="..."
```

Puis lancez l'API :

```bash
.\gradlew bootRun       # Windows
```

Vérifiez que les variables d'environnement sont bien actives :

```bash
Get-ChildItem env:
```

### Option B (si vous avez besoin de consulter et éditer le code source) - Exécution dans une IDE

Ce guide explique seulement pour IntelliJ IDEA car c'est l'IDE que nous recommandons.

#### Installer un plugin

IntelliJ ne charge pas automatiquement les fichiers `.env`.
Installez le plugin **EnvFile** (par Borys Pierov) :

1. `Settings → Plugins` → recherchez **EnvFile** → installez et redémarrez IntelliJ.
2. Ouvrez la configuration de lancement Spring Boot (`Run → Edit Configurations` ou alors voir capture d'écran).

   ![image1](docs/images/image1.png)

3. Pour activer **EnvFile**, cochez "Enable EnvFile", puis ajoutez le fichier `.env` du backend.

   ![image2](docs/images/image2.png)

Une fois configuré, IntelliJ injecte automatiquement les variables à chaque lancement - plus besoin de les exporter manuellement (c'est persistant).

### Résultat

L'API est disponible sur :

```
http://localhost:8080
```

---
## Remplissez la base de données

Une fois votre API lancée, dans un autre terminal, lancez le script `seed.ts` situé dans `frontend/src/config/`
(L'API doit être lancée et la base de données doit être vide.)

```bash
node seed.ts
```

---

## Documentation de l'API

Générée automatiquement à partir des `@RestController` (dépendance `springdoc-openapi-starter-webmvc-ui`) :

| Format | URL |
| --- | --- |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| OpenAPI YAML | `http://localhost:8080/v3/api-docs.yaml` |

Pour tester les endpoints directement sans outil externe (Postman/Insomnia), IntelliJ Ultimate propose un onglet natif **Endpoints** (`View → Tool Windows → Endpoints`) - vérifier que les plugins **Spring** et **Spring Web** sont activés, sinon les endpoints n'y apparaîtront pas.

---

## 7. Configurer et lancer le frontend

Depuis le dossier `frontend` :

```bash
npm install
```

Créer un fichier `frontend/.env` :

```
VITE_API_URL=http://localhost:8080
```

Vite injecte automatiquement les variables préfixées `VITE_` donc aucune configuration supplémentaire nécessaire.

Lancer le serveur de développement (depuis le dossier `frontend`) :

```bash
npm run dev
```

Le frontend est disponible sur :

```
http://localhost:5173
```

---

## 8. Lancement complet - résumé

Une fois les deux `.env` configurés (backend et frontend) :

```bash
# Terminal 1 - Backend (depuis la racine du projet)
export $(cat .env | xargs)   # ou via IntelliJ EnvFile
./gradlew bootRun

# Terminal 2 - Frontend
cd frontend
npm install
npm run dev
```

Puis ouvrir :

```
http://localhost:5173 (et API sur http://localhost:8080)
```

---

## 9. Dépannage

| Problème | Cause probable | Solution |
| --- | --- | --- |
| `.env` non pris en compte dans IntelliJ | Chargement automatique absent | Installer le plugin **EnvFile** et l'activer dans la config de lancement |
| Connexion à la base impossible via Direct connection | Incompatibilité réseau IPv6/IPv4 | Utiliser le **Session Pooler** de Supabase à la place |
| Variables `.env` non actives dans un nouveau terminal | Les exports ne persistent pas entre sessions | export $(cat .env \| xargs) |
| Endpoints absents de l'onglet Endpoints (IntelliJ) | Plugins Spring/Web désactivés | Vérifier leur activation dans `Settings → Plugins` |

---

## 10. Variables d'environnement - récapitulatif

**Backend** (`.env`, à la racine du projet) :

```
DB_URL=jdbc:postgresql://[HOST]:[PORT]/postgres
DB_USERNAME=[USERNAME]
DB_PASSWORD=[PASSWORD]
```
**(postgres est le nom de base de données par défaut dans Supabase)**

**Frontend** (`frontend/.env`) :

```
VITE_API_URL=http://localhost:8080
```

En production, `VITE_API_URL` doit pointer vers l'URL publique du backend déployé, et il faudra aussi ajouter l'adresse du frontend déployé dans le backend dans `config/CorsConfig`.

**Ne jamais committer les fichiers `.env`** - vérifier leur présence dans `.gitignore`.
