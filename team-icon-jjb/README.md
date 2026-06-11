# Team Icon Jiu-Jitsu Pays Basque — Site vitrine

Refonte intégrale du site [iconjjb64.fr](https://www.iconjjb64.fr) : académie de
Jiu-Jitsu Brésilien et de Grappling à Anglet (64), Pays Basque.

## Stack

| Brique | Choix |
| --- | --- |
| Framework | **Next.js 16** (App Router, pages 100 % statiques) |
| Langage | **TypeScript** (mode `strict`) |
| Styles | **Tailwind CSS v4** (config CSS-first via `@theme`, mobile-first) |
| Composants | Primitives style **shadcn/ui** sur **Radix UI** (Dialog, Slot) |
| Animations | **Framer Motion** (reveal au scroll, micro-interactions, `prefers-reduced-motion` respecté) |
| Icônes | lucide-react (+ SVG inline pour les marques Instagram/Facebook) |
| Polices | Oswald (display) & Inter (texte) via `next/font` — zéro layout shift |
| SEO | Metadata API, JSON-LD `SportsActivityLocation`, `sitemap.xml`, `robots.txt`, manifest PWA |

Aucune dépendance superflue, aucun cookie, aucun tracker : le site est
prérendu statiquement, taillé pour un score Lighthouse maximal.

## Démarrage

```bash
npm install
npm run dev    # http://localhost:3000
npm run build  # build de production
npm run lint   # ESLint
```

## Déploiement sur Vercel

Le projet se déploie tel quel : importez le dépôt sur Vercel et définissez le
**Root Directory** sur `team-icon-jjb/`. Aucune variable d'environnement
n'est requise.

## Architecture

```
src/
├── app/                    # Pages (App Router) + SEO
│   ├── layout.tsx          # Layout global : polices, metadata, Navbar/Footer, JSON-LD
│   ├── page.tsx            # Accueil (one-page à ancres)
│   ├── mentions-legales/   # Page légale (LCEN)
│   ├── sitemap.ts robots.ts manifest.ts icon.svg
│   └── globals.css         # Thème : variables CSS noir/blanc/rouge/or (@theme)
├── components/
│   ├── ui/                 # Primitives shadcn-style : Button, Badge, Card
│   ├── layout/             # Navbar (menu mobile Radix Dialog), Footer
│   ├── sections/           # Hero, Quote, Values, Disciplines, Team,
│   │                       # Schedule (+ ScheduleBoard filtrable),
│   │                       # TrainingCenters, Pricing, Contact
│   ├── shared/             # Reveal (Framer), SectionHeading, FeatureBlock,
│   │                       # TeacherCard, Logo, icônes sociales
│   └── seo/                # JSON-LD Schema.org
├── data/                   # ✏️ TOUT le contenu éditorial, typé
│   ├── site.ts             # Identité, contact, réseaux, HelloAsso, RNA
│   ├── navigation.ts       # Entrées du menu
│   ├── values.ts           # « Pourquoi nous rejoindre ? »
│   ├── team.ts             # Équipe technique (bios, palmarès)
│   ├── disciplines.ts      # JJB Gi / Grappling-Lutte / Self-défense #FITGIRL
│   ├── schedule.ts         # Planning hebdomadaire filtrable
│   ├── locations.ts        # Dojos (Stella Maris, Résilience) + cartes
│   └── pricing.ts          # Adhésion (licence CFJJB, passeport, famille…)
└── lib/                    # utils (cn) + types métier partagés
```

Tout le contenu se modifie dans `src/data/` sans toucher aux composants.

## ⚠️ À faire avant la mise en production

1. **Horaires** (`src/data/schedule.ts`) : seul le créneau du **mardi
   12h30–13h45 au Dojo Résilience** est confirmé publiquement par le club.
   Les autres créneaux sont **indicatifs** (`confirmed: false`) — remplacez-les
   par le planning officiel puis passez `confirmed` à `true` pour retirer la
   mention « planning indicatif » affichée automatiquement.
2. **Photos** : déposez les portraits dans `public/images/equipe/` et
   renseignez `photo` dans `src/data/team.ts` (le monogramme sert de
   fallback). Ajoutez aussi vos photos de dojo/tatamis si souhaité.
3. **Logo officiel** : remplacez le monogramme (`components/shared/logo.tsx`
   et `app/icon.svg`) par le logo ICON du club si vous disposez du fichier.
4. **Mentions légales** : vérifiez le siège social exact de l'association.

## Contenu intégré (source : iconjjb64.fr & HelloAsso)

- Hero, intro et citation d'Helio Gracie
- 5 valeurs pédagogiques (« Pourquoi nous rejoindre ? »)
- Équipe : Maître Zé Marcello (CN 6ᵉ degré, fondateur ICON), Maître Anderson
  Pereira (référent France), Juliana Calabria (responsable Pays Basque)
- Disciplines : JJB Gi, Grappling/Lutte No-Gi, Self-Défense Féminine #FITGIRL
- Training Center : Gymnase Stella Maris (dojo principal, bâtiment 2022,
  tatamis chauffés, parking gratuit) & Dojo Résilience (mardi midi)
- Adhésion : licence CFJJB 41 € incluse, passeport sportif 25 €/10 ans,
  réduction famille (−20 €/−30 €), paiement 1×/3×/mensuel via HelloAsso
- Contact : 06 74 65 19 59 (Gaël Obert / Juliana), contact@iconjjb64.fr,
  Instagram @iconjjb64, Facebook
