import { Mail, MapPin, Phone } from "lucide-react";

import { site } from "@/data/site";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { InstagramIcon } from "@/components/shared/social-icons";
import { Reveal } from "@/components/shared/reveal";
import { SectionHeading } from "@/components/shared/section-heading";

/**
 * Section « Contact » : appel à l'action cours d'essai + coordonnées
 * complètes (téléphone, e-mail, Instagram, adresse du dojo principal).
 */
export function Contact() {
  return (
    <section id="contact" className="glow-primary relative scroll-mt-24 overflow-hidden">
      <div className="container-site py-20 md:py-28">
        <div className="grid items-center gap-12 lg:grid-cols-2">
          <div>
            <SectionHeading
              eyebrow="Contact"
              title="Prêt·e à monter sur le tatami ?"
              description="Enfant, adulte, débutant·e ou compétiteur·rice confirmé·e : venez découvrir le Jiu-Jitsu Brésilien lors d'un cours d'essai. Un kimono de prêt et un sourire suffisent."
              className="mb-8"
            />
            <Reveal delay={0.1}>
              <div className="flex flex-wrap gap-4">
                <Button asChild size="lg">
                  <a href={site.contact.phoneHref}>
                    <Phone aria-hidden="true" />
                    {site.contact.phoneDisplay}
                  </a>
                </Button>
                <Button asChild size="lg" variant="outline">
                  <a href={`mailto:${site.contact.email}`}>
                    <Mail aria-hidden="true" />
                    Écrire un e-mail
                  </a>
                </Button>
              </div>
              <p className="mt-3 text-sm text-muted-foreground">
                Vos interlocuteurs : {site.contact.contactNames}
              </p>
            </Reveal>
          </div>

          <Reveal delay={0.15}>
            <Card>
              <CardContent className="space-y-5 p-6 md:p-8">
                <h3 className="font-display text-lg font-semibold tracking-[0.2em] uppercase">
                  Nous trouver / nous suivre
                </h3>
                <a
                  href={site.contact.phoneHref}
                  className="flex items-center gap-3.5 text-sm transition-colors hover:text-primary"
                >
                  <span className="grid size-10 shrink-0 place-items-center rounded-lg bg-primary/12 text-primary">
                    <Phone className="size-5" aria-hidden="true" />
                  </span>
                  <span>
                    <span className="block font-semibold">{site.contact.phoneDisplay}</span>
                    <span className="text-muted-foreground">
                      {site.contact.contactNames}
                    </span>
                  </span>
                </a>
                <a
                  href={`mailto:${site.contact.email}`}
                  className="flex items-center gap-3.5 text-sm transition-colors hover:text-primary"
                >
                  <span className="grid size-10 shrink-0 place-items-center rounded-lg bg-primary/12 text-primary">
                    <Mail className="size-5" aria-hidden="true" />
                  </span>
                  <span>
                    <span className="block font-semibold">{site.contact.email}</span>
                    <span className="text-muted-foreground">Réponse rapide</span>
                  </span>
                </a>
                <a
                  href={site.social.instagram}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center gap-3.5 text-sm transition-colors hover:text-primary"
                >
                  <span className="grid size-10 shrink-0 place-items-center rounded-lg bg-primary/12 text-primary">
                    <InstagramIcon className="size-5" aria-hidden="true" />
                  </span>
                  <span>
                    <span className="block font-semibold">
                      {site.social.instagramHandle}
                    </span>
                    <span className="text-muted-foreground">
                      Actus, stages et vie du club en photos
                    </span>
                  </span>
                </a>
                <div className="flex items-center gap-3.5 text-sm">
                  <span className="grid size-10 shrink-0 place-items-center rounded-lg bg-primary/12 text-primary">
                    <MapPin className="size-5" aria-hidden="true" />
                  </span>
                  <span>
                    <span className="block font-semibold">
                      Gymnase du Collège Stella Maris
                    </span>
                    <span className="text-muted-foreground">
                      40-42 promenade de la Barre, 64600 Anglet
                    </span>
                  </span>
                </div>
              </CardContent>
            </Card>
          </Reveal>
        </div>
      </div>
    </section>
  );
}
