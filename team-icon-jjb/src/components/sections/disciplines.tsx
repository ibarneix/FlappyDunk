import { disciplines } from "@/data/disciplines";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Reveal } from "@/components/shared/reveal";
import { SectionHeading } from "@/components/shared/section-heading";

/** Section « Nos Disciplines » : JJB Gi, Grappling/Lutte, Self-Défense féminine. */
export function Disciplines() {
  return (
    <section id="disciplines" className="scroll-mt-24">
      <div className="container-site py-20 md:py-28">
        <SectionHeading
          eyebrow="Nos disciplines"
          title="Gi, No-Gi & Self-Défense"
          description="Trois pratiques complémentaires, un même socle : le combat au sol, enseigné avec exigence et bienveillance."
        />
        <div className="grid gap-5 lg:grid-cols-3">
          {disciplines.map((discipline, index) => (
            <Reveal key={discipline.id} delay={index * 0.08} className="h-full">
              <Card className="group relative h-full overflow-hidden transition-all duration-300 hover:-translate-y-1 hover:border-primary/40">
                <span
                  aria-hidden="true"
                  className="absolute inset-x-0 top-0 h-1 bg-gradient-to-r from-primary via-primary/40 to-transparent"
                />
                <CardContent className="p-6 md:p-7">
                  <p className="font-display text-xs font-semibold tracking-[0.25em] text-accent uppercase">
                    {discipline.tagline}
                  </p>
                  <h3 className="mt-2 font-display text-2xl font-bold tracking-wide uppercase">
                    {discipline.name}
                  </h3>
                  <p className="mt-3 text-sm leading-relaxed text-muted-foreground">
                    {discipline.description}
                  </p>
                  <ul className="mt-5 flex flex-wrap gap-2">
                    {discipline.badges.map((badge) => (
                      <li key={badge}>
                        <Badge variant="outline">{badge}</Badge>
                      </li>
                    ))}
                  </ul>
                </CardContent>
              </Card>
            </Reveal>
          ))}
        </div>
      </div>
    </section>
  );
}
