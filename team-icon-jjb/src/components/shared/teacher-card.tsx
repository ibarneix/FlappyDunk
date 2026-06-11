import Image from "next/image";
import { Medal } from "lucide-react";

import type { Teacher } from "@/lib/types";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Reveal } from "@/components/shared/reveal";

interface TeacherCardProps {
  teacher: Teacher;
  /** Index dans la grille, pour l'apparition en cascade. */
  index?: number;
}

/**
 * Carte d'un membre de l'équipe technique : portrait (ou monogramme tant
 * qu'aucune photo n'est fournie dans `/public`), grade, bio et palmarès.
 */
export function TeacherCard({ teacher, index = 0 }: TeacherCardProps) {
  return (
    <Reveal delay={index * 0.1} className="h-full">
      <Card className="group flex h-full flex-col overflow-hidden transition-all duration-300 hover:-translate-y-1 hover:border-primary/40">
        <CardContent className="flex flex-1 flex-col p-6 md:p-7">
          <div className="flex items-center gap-4">
            {teacher.photo ? (
              <Image
                src={teacher.photo}
                alt={`Portrait de ${teacher.name}`}
                width={72}
                height={72}
                className="size-18 rounded-full border-2 border-primary/50 object-cover"
              />
            ) : (
              <span
                aria-hidden="true"
                className="grid size-18 shrink-0 place-items-center rounded-full bg-gradient-to-br from-primary to-primary-strong font-display text-xl font-bold text-primary-foreground ring-2 ring-accent/60 ring-offset-2 ring-offset-card"
              >
                {teacher.initials}
              </span>
            )}
            <div>
              <h3 className="font-display text-xl leading-tight font-semibold tracking-wide uppercase">
                {teacher.name}
              </h3>
              <p className="mt-1 text-sm font-medium text-primary">{teacher.role}</p>
            </div>
          </div>

          {/* Ceinture noire stylisée */}
          <div className="mt-5 flex items-center gap-3">
            <span
              aria-hidden="true"
              className="flex h-2.5 w-24 overflow-hidden rounded-full"
            >
              <span className="grow bg-zinc-900 ring-1 ring-border ring-inset" />
              <span className="w-7 bg-primary" />
              <span className="w-2 bg-zinc-900 ring-1 ring-border ring-inset" />
            </span>
            <Badge variant="accent">{teacher.belt}</Badge>
          </div>

          <p className="mt-4 text-sm leading-relaxed text-muted-foreground">
            {teacher.bio}
          </p>

          <ul className="mt-5 space-y-2 border-t border-border pt-5">
            {teacher.highlights.map((highlight) => (
              <li key={highlight} className="flex items-start gap-2.5 text-sm">
                <Medal className="mt-0.5 size-4 shrink-0 text-accent" aria-hidden="true" />
                <span>{highlight}</span>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </Reveal>
  );
}
