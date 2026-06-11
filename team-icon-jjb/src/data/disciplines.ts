import type { Discipline } from "@/lib/types";

/** Les trois disciplines enseignées à l'académie. */
export const disciplines: Discipline[] = [
  {
    id: "jjb",
    name: "Jiu-Jitsu Brésilien",
    tagline: "L'art doux, en kimono (Gi)",
    description:
      "Discipline jeune et novatrice popularisée par le MMA, le Jiu-Jitsu " +
      "Brésilien se pratique essentiellement au sol. Véritable partie " +
      "d'échecs humaine, il sollicite autant les capacités intellectuelles " +
      "que physiques : technique, timing et effet de levier priment sur la " +
      "force brute.",
    badges: ["Dès 4 ans", "Gi", "Loisir & Compétition"],
  },
  {
    id: "grappling",
    name: "Grappling / Lutte",
    tagline: "Le combat de préhension, sans kimono (No-Gi)",
    description:
      "Version sans kimono du combat au sol, le grappling mêle lutte, " +
      "contrôles et soumissions. Plus rapide et plus athlétique, c'est la " +
      "base du sol en MMA et une discipline de compétition à part entière " +
      "(IBJJF, ADCC, AJP).",
    badges: ["Ados & Adultes", "No-Gi", "Lutte"],
  },
  {
    id: "self-defense",
    name: "Self-Défense Féminine",
    tagline: "#FITGIRL Grappling — 100% féminin",
    description:
      "Des ateliers interactifs réservés aux femmes pour apprendre à réagir " +
      "face aux situations dangereuses : techniques de base de self-défense, " +
      "renforcement musculaire et endurance, dans un cadre bienveillant " +
      "animé par Juliana Calabria.",
    badges: ["100% Féminin", "2 cours / semaine", "Tous niveaux"],
  },
];
