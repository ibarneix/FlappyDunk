import { team } from "@/data/team";
import { SectionHeading } from "@/components/shared/section-heading";
import { TeacherCard } from "@/components/shared/teacher-card";

/** Section « Notre Équipe » — l'encadrement technique, du monde au tatami. */
export function Team() {
  return (
    <section id="equipe" className="scroll-mt-24 border-y border-border bg-card/30">
      <div className="container-site py-20 md:py-28">
        <SectionHeading
          eyebrow="Notre équipe"
          title="Une lignée de champions"
          description="Du fondateur mondial d'ICON Jiu-Jitsu à votre professeure sur le tatami d'Anglet : un encadrement technique d'exception, titré au plus haut niveau international."
        />
        <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          {team.map((teacher, index) => (
            <TeacherCard key={teacher.id} teacher={teacher} index={index} />
          ))}
        </div>
      </div>
    </section>
  );
}
