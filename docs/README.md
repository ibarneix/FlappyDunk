# Site YBJJ Academy

Site vitrine one-page de la **YBJJ Academy** (Jiu-Jitsu Brésilien – Yannick Beven, Anglet, Côte Basque).

## Structure

```
docs/
├── index.html          # Page unique (accueil, fondateur, philosophie, cours, contact)
├── css/styles.css      # Styles (palette centralisée en variables CSS)
├── js/main.js          # Menu mobile, animations d'apparition, lien actif
└── assets/
    ├── logo.svg        # Logo badge complet (hero, footer)
    └── logo-mark.svg   # Emblème compact (navbar, favicon)
```

## Ouvrir le site

Ouvrir simplement `index.html` dans un navigateur, ou servir le dossier :

```bash
cd docs && python3 -m http.server 8000
# puis http://localhost:8000
```

## Mettre en ligne avec GitHub Pages

Sur GitHub : **Settings → Pages → Source : Deploy from a branch**, choisir la
branche et le dossier `/docs`. Le site sera servi automatiquement.

## Remplacer par le logo officiel

Les deux fichiers SVG du dossier `assets/` sont une **recréation** du logo
(vague + ceinture noire 5ᵉ dan). Pour utiliser le logo officiel :

1. Déposer le fichier dans `assets/` (ex. `assets/logo.png`).
2. Remplacer les `src` dans `index.html` (occurrences de `assets/logo.svg`
   et `assets/logo-mark.svg`).

## Adapter les couleurs

Toute la palette est définie au début de `css/styles.css` (`:root`).
Modifier ces variables suffit à recolorer tout le site, par exemple pour
caler les teintes exactes du logo officiel :

```css
--navy-900: #0B1B2B;  /* fond sombre */
--ocean-400: #3FB0D8; /* bleu vague */
--sand-400: #E3C36B;  /* or sable (accents) */
```

## À valider avant publication

Les informations de la section Contact (adresse Moskova HQ, téléphone,
manager, email) proviennent d'annuaires sportifs récents : **à faire
confirmer par Yannick** avant la mise en ligne.

Prévoir également une photo officielle de Yannick Beven pour remplacer le
visuel « YB » de la section Fondateur (emplacement indiqué en commentaire
dans `index.html`).
