import { locations } from "@/data/locations";
import { site } from "@/data/site";

/**
 * Données structurées Schema.org (SportsActivityLocation) injectées dans le
 * layout : nom, coordonnées, adresses des dojos, réseaux sociaux, fondateur.
 * Le `<` est échappé conformément aux recommandations Next.js (anti-XSS).
 */
export function JsonLd() {
  const jsonLd = {
    "@context": "https://schema.org",
    "@type": "SportsActivityLocation",
    "@id": `${site.url}/#club`,
    name: site.name,
    alternateName: site.shortName,
    legalName: site.association.legalName,
    description: site.description,
    url: site.url,
    telephone: "+33674651959",
    email: site.contact.email,
    foundingDate: String(site.foundingYear),
    founder: {
      "@type": "Person",
      name: site.association.president,
    },
    sport: ["Brazilian Jiu-Jitsu", "Grappling"],
    priceRange: "€",
    address: {
      "@type": "PostalAddress",
      streetAddress: locations[0].address,
      addressLocality: "Anglet",
      postalCode: "64600",
      addressCountry: "FR",
    },
    location: locations.map((location) => ({
      "@type": "Place",
      name: location.name,
      address: {
        "@type": "PostalAddress",
        streetAddress: location.address,
        addressLocality: "Anglet",
        postalCode: "64600",
        addressCountry: "FR",
      },
    })),
    sameAs: [site.social.instagram, site.social.facebook, site.social.helloasso],
  };

  return (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{
        __html: JSON.stringify(jsonLd).replace(/</g, "\\u003c"),
      }}
    />
  );
}
