import { ExternalLink } from "lucide-react";

import { pricingItems } from "@/data/pricing";
import { site } from "@/data/site";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Reveal } from "@/components/shared/reveal";
import { SectionHeading } from "@/components/shared/section-heading";

/**
 * Section « Adhésion » : licence CFJJB, passeport compétition, réductions
 * famille et facilités de paiement — inscription en ligne via HelloAsso.
 */
export function Pricing() {
  return (
    <section id="adhesion" className="scroll-mt-24 border-y border-border bg-card/30">
      <div className="container-site py-20 md:py-28">
        <SectionHeading
          eyebrow="Adhésion"
          title="Rejoindre la team"
          description="Les formules d'adhésion annuelles varient selon l'âge et le nombre de cours par semaine. L'inscription et le paiement se font en ligne, simplement et de façon sécurisée, via HelloAsso."
        />

        <div className="grid gap-5 sm:grid-cols-2 xl:grid-cols-4">
          {pricingItems.map((item, index) => (
            <Reveal key={item.id} delay={index * 0.08} className="h-full">
              <Card className="flex h-full flex-col transition-all duration-300 hover:-translate-y-1 hover:border-primary/40">
                <CardContent className="flex flex-1 flex-col p-6">
                  <Badge
                    variant={item.required ? "solid" : "outline"}
                    className="self-start"
                  >
                    {item.required ? "Obligatoire" : "Selon profil"}
                  </Badge>
                  <p className="mt-4 font-display text-3xl font-bold text-accent">
                    {item.price}
                  </p>
                  <h3 className="mt-1 font-display text-base font-semibold tracking-wide uppercase">
                    {item.title}
                  </h3>
                  <p className="mt-2.5 text-sm leading-relaxed text-muted-foreground">
                    {item.description}
                  </p>
                </CardContent>
              </Card>
            </Reveal>
          ))}
        </div>

        <Reveal className="mt-10 flex flex-col items-start gap-4 rounded-xl border border-border bg-card p-6 sm:flex-row sm:items-center sm:justify-between md:p-8">
          <div>
            <p className="font-display text-lg font-semibold tracking-wide uppercase">
              Inscription en ligne
            </p>
            <p className="mt-1 text-sm text-muted-foreground">
              Fiche d’adhésion, formules détaillées et paiement sécurisé sur
              HelloAsso — ou renseignez-vous directement au club.
            </p>
          </div>
          <div className="flex flex-wrap gap-3">
            <Button asChild>
              <a
                href={site.social.helloassoAdhesion}
                target="_blank"
                rel="noopener noreferrer"
              >
                S’inscrire sur HelloAsso
                <ExternalLink aria-hidden="true" />
              </a>
            </Button>
            <Button asChild variant="outline">
              <a href="#contact">Poser une question</a>
            </Button>
          </div>
        </Reveal>
      </div>
    </section>
  );
}
