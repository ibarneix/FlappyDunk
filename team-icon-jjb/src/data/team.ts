import type { Teacher } from "@/lib/types";

/** Équipe technique — du fondateur mondial à la professeure résidente. */
export const team: Teacher[] = [
  {
    id: "ze-marcello",
    name: "Maître Zé Marcello",
    role: "Responsable Technique Monde",
    belt: "Ceinture noire 6ᵉ degré",
    initials: "ZM",
    bio:
      "Ceinture noire 6ᵉ degré sous Sergio « Bolão » Souza et fondateur " +
      "d'ICON Jiu-Jitsu, Maître Zé Marcello est une figure légendaire du " +
      "Jiu-Jitsu Brésilien. Il supervise la ligne technique de toutes les " +
      "académies ICON à travers le monde.",
    highlights: [
      "Fondateur d'ICON Jiu-Jitsu",
      "Multiple champion du monde et d'Europe",
      "Encore titré en 2022",
      "Formé sous Sergio « Bolão » Souza",
    ],
  },
  {
    id: "anderson-pereira",
    name: "Maître Anderson Pereira",
    role: "Référent Technique France",
    belt: "Ceinture noire de JJB",
    initials: "AP",
    bio:
      "Tombé dans les arts martiaux dès l'âge de 5 ans par le judo, " +
      "Anderson Pereira a obtenu sa ceinture noire de Jiu-Jitsu Brésilien " +
      "sous Zé Marcello. Compétiteur accompli devenu formateur de champions, " +
      "il est le référent technique d'ICON pour la France.",
    highlights: [
      "Champion d'Europe Gi",
      "Champion du Monde IBJJF No-Gi",
      "Ceinture noire sous Zé Marcello",
      "Débuts au judo à 5 ans",
    ],
  },
  {
    id: "juliana-calabria",
    name: "Juliana Calabria",
    role: "Professeure — Responsable Team Icon Pays Basque",
    belt: "Ceinture noire 1ᵉʳ degré",
    initials: "JC",
    bio:
      "Ceinture noire 1ᵉʳ degré et compétitrice internationale, Juliana " +
      "Calabria dirige les entraînements de la Team Icon Pays Basque. " +
      "Pédagogue reconnue auprès des enfants comme des adultes, elle anime " +
      "également un cours de grappling 100% féminin.",
    highlights: [
      "Double Championne d'Europe IBJJF",
      "Or européen Gi 2020",
      "Or européen No-Gi 2022",
      "Cours de grappling 100% féminin",
    ],
  },
];
