import { CircleCheck, ExternalLink, MapPin, Navigation } from "lucide-react";

import { locations } from "@/data/locations";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Reveal } from "@/components/shared/reveal";
import { SectionHeading } from "@/components/shared/section-heading";

/**
 * Section « Training Center » : les deux dojos d'Anglet, leurs atouts,
 * une carte Google Maps (chargée en différé, sans clé API) et l'itinéraire.
 */
export function TrainingCenters() {
  return (
    <section id="training-center" className="scroll-mt-24">
      <div className="container-site py-20 md:py-28">
        <SectionHeading
          eyebrow="Training Center"
          title="Nos lieux d’entraînement"
          description="Un dojo principal flambant neuf face à l'océan, et une salle partenaire pour le cours du mardi midi — tous deux à Anglet, avec accès facile."
        />
        <div className="grid gap-6 lg:grid-cols-2">
          {locations.map((location, index) => (
            <Reveal key={location.id} delay={index * 0.1} className="h-full">
              <Card className="flex h-full flex-col overflow-hidden">
                <CardContent className="flex flex-1 flex-col p-6 md:p-7">
                  <Badge
                    variant={location.id === "stella-maris" ? "solid" : "accent"}
                    className="self-start"
                  >
                    {location.role}
                  </Badge>
                  <h3 className="mt-3 font-display text-2xl font-bold tracking-wide uppercase">
                    {location.name}
                  </h3>
                  <p className="mt-1.5 flex items-center gap-2 text-sm text-muted-foreground">
                    <MapPin className="size-4 text-primary" aria-hidden="true" />
                    {location.address}, {location.city}
                  </p>
                  <p className="mt-4 text-sm leading-relaxed text-muted-foreground">
                    {location.description}
                  </p>

                  <ul className="mt-5 grid gap-2 sm:grid-cols-2">
                    {location.features.map((feature) => (
                      <li
                        key={feature}
                        className="flex items-start gap-2 text-sm text-foreground/90"
                      >
                        <CircleCheck
                          className="mt-0.5 size-4 shrink-0 text-accent"
                          aria-hidden="true"
                        />
                        {feature}
                      </li>
                    ))}
                  </ul>

                  <div className="mt-6 overflow-hidden rounded-lg border border-border">
                    <iframe
                      src={location.mapEmbedUrl}
                      title={`Carte — ${location.name}, ${location.address}, ${location.city}`}
                      loading="lazy"
                      referrerPolicy="no-referrer-when-downgrade"
                      allowFullScreen
                      className="h-56 w-full border-0 grayscale-[35%] contrast-[1.05]"
                    />
                  </div>

                  <div className="mt-5 flex flex-wrap gap-3">
                    <Button asChild size="sm">
                      <a
                        href={location.directionsUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        <Navigation aria-hidden="true" />
                        Itinéraire
                      </a>
                    </Button>
                    <Button asChild size="sm" variant="outline">
                      <a
                        href={location.directionsUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        <ExternalLink aria-hidden="true" />
                        Voir sur Google Maps
                      </a>
                    </Button>
                  </div>
                </CardContent>
              </Card>
            </Reveal>
          ))}
        </div>
      </div>
    </section>
  );
}
