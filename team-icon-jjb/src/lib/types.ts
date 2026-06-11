/**
 * Types métier partagés par les données (`src/data`) et les composants.
 * Le contenu éditorial du site est entièrement typé : toute modification
 * incohérente est signalée à la compilation (TypeScript strict).
 */

/** Jours d'entraînement (le club est ouvert 6 jours sur 7). */
export type Weekday =
  | "Lundi"
  | "Mardi"
  | "Mercredi"
  | "Jeudi"
  | "Vendredi"
  | "Samedi";

/** Publics accueillis sur un créneau. */
export type Audience = "enfants" | "adultes" | "femmes";

/** Tenue / format de pratique. */
export type Style = "gi" | "nogi";

/** Filtres proposés par le tableau des horaires. */
export type ScheduleFilter = "tous" | Audience | Style;

/** Identifiants des lieux d'entraînement. */
export type LocationId = "stella-maris" | "resilience";

export interface ScheduleEntry {
  id: string;
  day: Weekday;
  /** Heure de début, format "HH:MM". */
  start: string;
  /** Heure de fin, format "HH:MM". */
  end: string;
  title: string;
  audiences: Audience[];
  style: Style;
  locationId: LocationId;
  /** Précision affichée sous le titre (tranche d'âge, niveau…). */
  note?: string;
  /**
   * `false` tant que le créneau n'a pas été confirmé par le club —
   * affiché tel quel mais signalé comme indicatif dans la section.
   */
  confirmed: boolean;
}

export interface TrainingLocation {
  id: LocationId;
  name: string;
  role: string;
  address: string;
  city: string;
  description: string;
  /** Atouts du lieu (parking, douches, tatamis chauffés…). */
  features: string[];
  /** URL d'embed Google Maps (sans clé API). */
  mapEmbedUrl: string;
  /** Lien d'itinéraire Google Maps. */
  directionsUrl: string;
}

export interface Teacher {
  id: string;
  name: string;
  /** Rôle au sein de la team (ex. « Responsable Technique Monde »). */
  role: string;
  /** Grade : ceinture et degré. */
  belt: string;
  /** Initiales affichées dans le monogramme tant qu'aucune photo n'est fournie. */
  initials: string;
  /** Chemin d'une photo dans /public (optionnel). */
  photo?: string;
  bio: string;
  /** Palmarès et faits d'armes, mis en avant avec un marqueur doré. */
  highlights: string[];
}

export interface Value {
  id: string;
  title: string;
  description: string;
  /** Nom de l'icône lucide-react associée (résolue côté composant). */
  icon: "trending-up" | "handshake" | "brain" | "trophy" | "sparkles";
}

export interface Discipline {
  id: string;
  name: string;
  tagline: string;
  description: string;
  badges: string[];
}

export interface PricingItem {
  id: string;
  title: string;
  price: string;
  description: string;
  /** `true` pour les éléments obligatoires (ex. licence CFJJB). */
  required: boolean;
}

export interface NavItem {
  label: string;
  href: `#${string}` | `/${string}`;
}
