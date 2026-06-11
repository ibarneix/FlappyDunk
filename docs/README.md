# Site YBJJ Academy — Jiu Jitsu Team

Site vitrine one-page de la **YBJJ Academy** (Yannick Beven, Anglet — Côte Basque).
Design noir / rouge / blanc dérivé du logo officiel du club, inspiré des sites
de pilotes F1 : préloader animé, smooth scroll (Lenis), animations au scroll
(GSAP + ScrollTrigger), curseur personnalisé, marquees réactifs à la vitesse de
scroll, particules en canvas, boutons magnétiques, badge 3D au survol.

## Structure

```
docs/
├── index.html              # Page unique (5 sections + footer)
├── css/styles.css          # Styles — palette centralisée en variables CSS
├── js/main.js              # Interactions (préloader, scroll, curseur…)
├── js/vendor/              # GSAP, ScrollTrigger, Lenis (embarqués en local)
└── assets/
    ├── logo.png            # ⚠ LOGO AFFICHÉ PARTOUT (voir ci-dessous)
    ├── logo.svg            # Recréation vectorielle du logo (option)
    ├── logo-mark.svg       # Emblème compact vectoriel (option)
    └── fonts/              # Polices Anton & Archivo auto-hébergées
```

Tout est embarqué (polices, librairies) : **le site fonctionne hors-ligne,
sans CDN ni service externe** (seule la carte Google Maps de la section
contact nécessite Internet).

## ⚠ Remplacer le logo par le fichier original

L'image du chat ne peut pas être transférée telle quelle dans le dépôt par
l'assistant : `assets/logo.png` contient donc une recréation provisoire.
**Pour afficher le logo original à l'identique** : déposez votre fichier à la
place de `docs/assets/logo.png` (même nom). Rien d'autre à changer — il
apparaîtra automatiquement dans le préloader, la barre de navigation, le hero,
le footer et l'onglet du navigateur. L'image est recadrée en cercle à
l'affichage, idéalement un fichier carré ≥ 800×800 px.

Sur GitHub : ouvrir le dossier `docs/assets/` → `Add file` → `Upload files`
→ glisser votre `logo.png` → `Commit`.

## Lancer en local (sans Docker)

```bash
cd docs && python3 -m http.server 8000
# → http://localhost:8000
```

## Déploiement Docker (recommandé)

Depuis la racine du dépôt :

```bash
docker compose up -d --build
# → http://localhost:8080
```

ou sans compose :

```bash
docker build -t ybjj-academy .
docker run -d --name ybjj-academy -p 8080:80 --restart unless-stopped ybjj-academy
```

L'image utilise nginx (alpine) avec une configuration optimisée et vérifiée :
compression gzip, cache long pour polices/images, cache moyen pour CSS/JS,
HTML toujours frais, en-têtes de sécurité, healthcheck intégré.

- Changer le port : éditer `ports:` dans `docker-compose.yml` (ex. `"80:80"`).
- Mise à jour après modification du site : `docker compose up -d --build`.
- Arrêter : `docker compose down`.

## Alternative gratuite : GitHub Pages

Settings → Pages → *Deploy from a branch* → choisir la branche et le dossier
`/docs`. Le site est alors servi par GitHub sans serveur à gérer.

## Adapter les couleurs

Toute la palette est dans `:root` au début de `css/styles.css` :

```css
--noir:  #0A0A0B;   /* fond principal */
--rouge: #D0201E;   /* rouge du logo */
--creme: #F4F2EE;   /* sections claires */
```

## À valider avant mise en ligne publique

Les informations de contact (adresse Moskova HQ, téléphone, manager, email)
proviennent d'annuaires sportifs récents : **à faire confirmer par Yannick**.
