import type { PricingItem } from "@/lib/types";

/**
 * Informations d'adhésion — saison sportive.
 * Les montants des formules (selon âge et nombre de cours) sont gérés sur
 * HelloAsso ; le site ne duplique que les éléments fixes communiqués par le club.
 */
export const pricingItems: PricingItem[] = [
  {
    id: "licence-cfjjb",
    title: "Licence CFJJB",
    price: "41 €",
    description:
      "Obligatoire pour la pratique du JJB, elle inclut l'assurance accident. " +
      "Elle est comprise dans toutes les formules d'adhésion.",
    required: true,
  },
  {
    id: "passeport-sportif",
    title: "Passeport sportif (compétition)",
    price: "25 €",
    description:
      "Nécessaire uniquement pour participer aux compétitions (adultes). " +
      "Valable 10 ans.",
    required: false,
  },
  {
    id: "reduction-famille",
    title: "Réduction famille",
    price: "−20 € / −30 €",
    description:
      "Sur l'adhésion annuelle la moins chère : −20 € pour le 2ᵉ membre " +
      "d'une même famille, −30 € pour le 3ᵉ.",
    required: false,
  },
  {
    id: "paiement",
    title: "Paiement flexible",
    price: "1×, 3× ou mensuel",
    description:
      "Règlement par carte bancaire en une fois, en 3 fois sans frais " +
      "ou en mensualités via HelloAsso.",
    required: false,
  },
];
