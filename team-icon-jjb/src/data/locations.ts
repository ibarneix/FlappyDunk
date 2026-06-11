import type { TrainingLocation } from "@/lib/types";

/** Les deux lieux d'entraînement du club à Anglet. */
export const locations: TrainingLocation[] = [
  {
    id: "stella-maris",
    name: "Gymnase du Collège Stella Maris",
    role: "Dojo principal",
    address: "40-42 promenade de la Barre",
    city: "64600 Anglet",
    description:
      "Tous les cours ont lieu dans le dojo du gymnase Stella Maris, un " +
      "bâtiment flambant neuf inauguré en 2022, à deux pas de l'océan et de " +
      "la plage de la Barre.",
    features: [
      "Bâtiment neuf (2022) à deux pas de l'océan",
      "Vastes tatamis, chauffés en hiver",
      "Grands vestiaires hommes / femmes avec douches",
      "Zone d'échauffement dédiée",
      "Grand parking gratuit",
    ],
    mapEmbedUrl:
      "https://www.google.com/maps?q=Coll%C3%A8ge%20Stella%20Maris%2C%2040%20Promenade%20de%20la%20Barre%2C%2064600%20Anglet&output=embed",
    directionsUrl:
      "https://www.google.com/maps/search/?api=1&query=Coll%C3%A8ge%20Stella%20Maris%2C%2040-42%20Promenade%20de%20la%20Barre%2C%2064600%20Anglet",
  },
  {
    id: "resilience",
    name: "Dojo Résilience",
    role: "Dojo secondaire — cours du mardi 12h30",
    address: "2 allée Pégase",
    city: "64600 Anglet",
    description:
      "Le cours du mardi midi (12h30 – 13h45) se déroule au Dojo Résilience, " +
      "salle partenaire située allée Pégase à Anglet.",
    features: ["Créneau du mardi 12h30 – 13h45", "Salle partenaire équipée"],
    mapEmbedUrl:
      "https://www.google.com/maps?q=2%20All%C3%A9e%20P%C3%A9gase%2C%2064600%20Anglet&output=embed",
    directionsUrl:
      "https://www.google.com/maps/search/?api=1&query=2%20All%C3%A9e%20P%C3%A9gase%2C%2064600%20Anglet",
  },
];

/** Accès direct à un lieu par identifiant (pour le tableau des horaires). */
export const locationById = Object.fromEntries(
  locations.map((location) => [location.id, location]),
) as Record<TrainingLocation["id"], TrainingLocation>;
