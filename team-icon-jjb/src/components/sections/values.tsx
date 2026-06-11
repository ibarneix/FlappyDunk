import { values } from "@/data/values";
import { FeatureBlock } from "@/components/shared/feature-block";
import { SectionHeading } from "@/components/shared/section-heading";

/** Section « Pourquoi nous rejoindre ? » — valeurs et pédagogie. */
export function Values() {
  return (
    <section id="pourquoi" className="scroll-mt-24">
      <div className="container-site py-20 md:py-28">
        <SectionHeading
          eyebrow="Valeurs & pédagogie"
          title="Pourquoi nous rejoindre ?"
          description="Plus qu'un sport, une école de vie. Notre pédagogie repose sur cinq piliers, du premier cours d'éveil jusqu'au plus haut niveau de compétition."
        />
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {values.map((value, index) => (
            <FeatureBlock key={value.id} value={value} index={index} />
          ))}
        </div>
      </div>
    </section>
  );
}
