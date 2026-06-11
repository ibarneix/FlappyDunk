import type { Metadata } from "next";
import Link from "next/link";

import { site } from "@/data/site";
import { Button } from "@/components/ui/button";

export const metadata: Metadata = {
  title: "Mentions légales",
  description: `Mentions légales du site ${site.url} — ${site.association.legalName}.`,
  robots: { index: false },
};

/** Page de mentions légales (obligation légale française, LCEN). */
export default function MentionsLegalesPage() {
  return (
    <main id="contenu" className="container-site max-w-3xl pt-(--header-height)">
      <article className="py-16 md:py-20">
        <h1 className="font-display text-4xl font-bold tracking-wide uppercase">
          Mentions légales
        </h1>

        <div className="mt-10 space-y-10 text-sm leading-relaxed text-muted-foreground [&_h2]:font-display [&_h2]:text-lg [&_h2]:font-semibold [&_h2]:tracking-wide [&_h2]:uppercase [&_h2]:text-foreground [&_p]:mt-3">
          <section>
            <h2>Éditeur du site</h2>
            <p>
              {site.association.legalName}, association loi 1901 déclarée en
              préfecture des Pyrénées-Atlantiques sous le numéro RNA{" "}
              {site.association.rna}, dont le siège est situé à Anglet (64600).
              <br />
              Directeur de la publication : {site.association.president},
              président de l’association.
              <br />
              Contact : {site.contact.email} — {site.contact.phoneDisplay}.
            </p>
          </section>

          <section>
            <h2>Hébergement</h2>
            <p>
              Site hébergé par Vercel Inc., 340 S Lemon Ave #4133, Walnut, CA
              91789, États-Unis — vercel.com.
            </p>
          </section>

          <section>
            <h2>Propriété intellectuelle</h2>
            <p>
              L’ensemble des contenus de ce site (textes, visuels, logotypes)
              est la propriété de l’association ou de ses partenaires. Toute
              reproduction non autorisée est interdite.
            </p>
          </section>

          <section>
            <h2>Données personnelles</h2>
            <p>
              Ce site ne dépose aucun cookie et ne collecte aucune donnée
              personnelle. Les inscriptions en ligne sont traitées par
              HelloAsso, responsable de traitement pour les données saisies sur
              sa plateforme. Pour toute question relative à vos données,
              écrivez-nous à {site.contact.email}.
            </p>
          </section>
        </div>

        <Button asChild variant="outline" className="mt-12">
          <Link href="/">← Retour à l’accueil</Link>
        </Button>
      </article>
    </main>
  );
}
