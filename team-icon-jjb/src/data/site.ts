/**
 * Identité et coordonnées officielles du club.
 * Source unique de vérité pour le layout, le footer, le SEO et le JSON-LD.
 */
export const site = {
  name: "Team Icon Jiu-Jitsu Pays Basque",
  shortName: "ICON JJB 64",
  baseline: "Académie de Jiu-Jitsu Brésilien et de Grappling à Anglet",
  description:
    "Académie de Jiu-Jitsu Brésilien et de Grappling à Anglet, au Pays Basque. " +
    "Cours Gi et No-Gi pour enfants dès 4 ans, ados et adultes, grappling 100% féminin, " +
    "loisir ou compétition — dans un dojo neuf à deux pas de l'océan.",
  url: "https://www.iconjjb64.fr",
  locale: "fr_FR",
  foundingYear: 2023,

  contact: {
    /** Gaël Obert (président) / Juliana Calabria (professeure). */
    phoneDisplay: "06 74 65 19 59",
    phoneHref: "tel:+33674651959",
    email: "contact@iconjjb64.fr",
    contactNames: "Gaël Obert / Juliana Calabria",
  },

  social: {
    instagramHandle: "@iconjjb64",
    instagram: "https://www.instagram.com/iconjjb64/",
    facebook:
      "https://www.facebook.com/p/ICON-JIU-JITSU-Team-Pays-Basque-100091819447899/",
    helloasso:
      "https://www.helloasso.com/associations/icon-jiu-jitsu-team-pays-basque",
    helloassoAdhesion:
      "https://www.helloasso.com/associations/icon-jiu-jitsu-team-pays-basque/adhesions/fiche-d-adhesion-au-club",
  },

  association: {
    legalName: "ICON JIU JITSU TEAM - PAYS BASQUE",
    rna: "W641013950",
    president: "Gaël Obert",
  },
} as const;

export type Site = typeof site;
