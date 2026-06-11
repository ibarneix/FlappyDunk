import { SectionHeading } from "@/components/shared/section-heading";
import { ScheduleBoard } from "@/components/sections/schedule-board";

/** Section « Horaires » : planning hebdomadaire filtrable, 6 jours sur 7. */
export function Schedule() {
  return (
    <section id="horaires" className="scroll-mt-24 border-y border-border bg-card/30">
      <div className="container-site py-20 md:py-28">
        <SectionHeading
          eyebrow="Planning"
          title="Tableau des horaires"
          description="Des cours 6 jours sur 7, pour toutes et tous : enfants dès 4 ans, ados, adultes, Gi, No-Gi et créneaux 100% féminins. Filtrez pour trouver le vôtre."
        />
        <ScheduleBoard />
      </div>
    </section>
  );
}
