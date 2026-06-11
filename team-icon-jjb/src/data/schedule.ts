import type { ScheduleEntry, ScheduleFilter, Weekday } from "@/lib/types";

/** Ordre d'affichage des jours dans le tableau des horaires. */
export const weekdays: Weekday[] = [
  "Lundi",
  "Mardi",
  "Mercredi",
  "Jeudi",
  "Vendredi",
  "Samedi",
];

/** Filtres affichés au-dessus du tableau des horaires. */
export const scheduleFilters: { id: ScheduleFilter; label: string }[] = [
  { id: "tous", label: "Tous les cours" },
  { id: "enfants", label: "Enfants" },
  { id: "adultes", label: "Adultes" },
  { id: "gi", label: "Gi" },
  { id: "nogi", label: "No-Gi" },
  { id: "femmes", label: "Femmes" },
];

/**
 * Planning hebdomadaire — 6 jours sur 7.
 *
 * ⚠️ Seul le créneau du mardi midi (Dojo Résilience) est confirmé
 * publiquement par le club. Les autres créneaux sont INDICATIFS
 * (`confirmed: false`) : remplacez les horaires par le planning officiel
 * avant la mise en production. Une note "planning indicatif" est affichée
 * automatiquement tant qu'un créneau non confirmé est présent.
 */
export const schedule: ScheduleEntry[] = [
  {
    id: "lun-jjb-adultes",
    day: "Lundi",
    start: "19:00",
    end: "20:30",
    title: "JJB Gi — Adultes & Ados",
    audiences: ["adultes"],
    style: "gi",
    locationId: "stella-maris",
    note: "Technique & sparring",
    confirmed: false,
  },
  {
    id: "mar-nogi-midi",
    day: "Mardi",
    start: "12:30",
    end: "13:45",
    title: "Grappling No-Gi — Adultes",
    audiences: ["adultes"],
    style: "nogi",
    locationId: "resilience",
    note: "Cours du midi au Dojo Résilience",
    confirmed: true,
  },
  {
    id: "mar-fitgirl",
    day: "Mardi",
    start: "19:00",
    end: "20:15",
    title: "#FITGIRL Grappling — 100% Féminin",
    audiences: ["femmes", "adultes"],
    style: "nogi",
    locationId: "stella-maris",
    note: "Self-défense, renforcement & grappling",
    confirmed: false,
  },
  {
    id: "mer-jjb-baby",
    day: "Mercredi",
    start: "14:00",
    end: "15:00",
    title: "JJB Enfants — 4 à 7 ans",
    audiences: ["enfants"],
    style: "gi",
    locationId: "stella-maris",
    note: "Motricité, coordination & jeux d'opposition",
    confirmed: false,
  },
  {
    id: "mer-jjb-kids",
    day: "Mercredi",
    start: "15:15",
    end: "16:30",
    title: "JJB Enfants — 8 à 13 ans",
    audiences: ["enfants"],
    style: "gi",
    locationId: "stella-maris",
    note: "Technique, randori & passage de grades",
    confirmed: false,
  },
  {
    id: "mer-nogi-adultes",
    day: "Mercredi",
    start: "19:00",
    end: "20:30",
    title: "Grappling / Lutte No-Gi — Adultes",
    audiences: ["adultes"],
    style: "nogi",
    locationId: "stella-maris",
    note: "Lutte, contrôles & soumissions",
    confirmed: false,
  },
  {
    id: "jeu-fitgirl",
    day: "Jeudi",
    start: "19:00",
    end: "20:15",
    title: "#FITGIRL Grappling — 100% Féminin",
    audiences: ["femmes", "adultes"],
    style: "nogi",
    locationId: "stella-maris",
    note: "Self-défense, renforcement & grappling",
    confirmed: false,
  },
  {
    id: "ven-jjb-adultes",
    day: "Vendredi",
    start: "19:00",
    end: "20:30",
    title: "JJB Gi — Adultes & Ados",
    audiences: ["adultes"],
    style: "gi",
    locationId: "stella-maris",
    note: "Technique & sparring",
    confirmed: false,
  },
  {
    id: "sam-jjb-enfants",
    day: "Samedi",
    start: "10:00",
    end: "11:00",
    title: "JJB Enfants — Tous niveaux",
    audiences: ["enfants"],
    style: "gi",
    locationId: "stella-maris",
    note: "Dès 4 ans",
    confirmed: false,
  },
  {
    id: "sam-jjb-team",
    day: "Samedi",
    start: "11:00",
    end: "12:30",
    title: "JJB Gi — Adultes / Team Compétition",
    audiences: ["adultes"],
    style: "gi",
    locationId: "stella-maris",
    note: "Préparation spécifique compétition",
    confirmed: false,
  },
];

/** Prédicat de filtrage du planning. */
export function matchesFilter(
  entry: ScheduleEntry,
  filter: ScheduleFilter,
): boolean {
  if (filter === "tous") return true;
  if (filter === "gi" || filter === "nogi") return entry.style === filter;
  return entry.audiences.includes(filter);
}
