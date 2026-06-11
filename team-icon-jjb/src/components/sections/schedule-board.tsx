"use client";

import * as React from "react";
import { AnimatePresence, motion, useReducedMotion } from "framer-motion";
import { Clock, Info, MapPin } from "lucide-react";

import {
  matchesFilter,
  schedule,
  scheduleFilters,
  weekdays,
} from "@/data/schedule";
import { locationById } from "@/data/locations";
import { site } from "@/data/site";
import type { ScheduleEntry, ScheduleFilter } from "@/lib/types";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

/** Libellés courts des publics, affichés en badge sur chaque créneau. */
const audienceLabels: Record<ScheduleEntry["audiences"][number], string> = {
  enfants: "Enfants",
  adultes: "Adultes",
  femmes: "100% Féminin",
};

/**
 * Tableau des horaires filtrable (Enfants, Adultes, Gi, No-Gi, Femmes).
 * Les filtres sont de vrais boutons à bascule (`aria-pressed`) et la grille
 * s'anime au changement de filtre via Framer Motion.
 */
export function ScheduleBoard() {
  const [filter, setFilter] = React.useState<ScheduleFilter>("tous");
  const reduceMotion = useReducedMotion();

  const visibleDays = weekdays
    .map((day) => ({
      day,
      entries: schedule.filter(
        (entry) => entry.day === day && matchesFilter(entry, filter),
      ),
    }))
    .filter(({ entries }) => entries.length > 0);

  const hasIndicativeSlots = schedule.some((entry) => !entry.confirmed);

  return (
    <div>
      {/* Filtres */}
      <div
        role="group"
        aria-label="Filtrer les cours"
        className="mb-8 flex flex-wrap gap-2.5"
      >
        {scheduleFilters.map(({ id, label }) => {
          const active = filter === id;
          return (
            <button
              key={id}
              type="button"
              aria-pressed={active}
              onClick={() => setFilter(id)}
              className={cn(
                "rounded-full border px-4 py-2 font-display text-sm font-medium tracking-wider uppercase transition-colors duration-200",
                active
                  ? "border-transparent bg-primary text-primary-foreground shadow-[0_6px_20px_-8px_var(--primary)]"
                  : "border-border bg-card text-muted-foreground hover:border-foreground/30 hover:text-foreground",
              )}
            >
              {label}
            </button>
          );
        })}
      </div>

      {/* Grille des jours */}
      {visibleDays.length > 0 ? (
        <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          <AnimatePresence mode="popLayout" initial={false}>
            {visibleDays.map(({ day, entries }) => (
              <motion.div
                key={day}
                layout={!reduceMotion}
                initial={reduceMotion ? false : { opacity: 0, scale: 0.97 }}
                animate={{ opacity: 1, scale: 1 }}
                exit={reduceMotion ? undefined : { opacity: 0, scale: 0.97 }}
                transition={{ duration: 0.25 }}
                className="rounded-xl border border-border bg-card"
              >
                <h3 className="border-b border-border px-5 py-3.5 font-display text-base font-bold tracking-[0.2em] text-accent uppercase">
                  {day}
                </h3>
                <ul className="divide-y divide-border">
                  {entries.map((entry) => {
                    const location = locationById[entry.locationId];
                    return (
                      <li key={entry.id} className="px-5 py-4">
                        <div className="flex items-center gap-2 text-sm font-semibold text-foreground tabular-nums">
                          <Clock className="size-4 text-primary" aria-hidden="true" />
                          {entry.start} – {entry.end}
                        </div>
                        <p className="mt-1.5 font-medium">{entry.title}</p>
                        {entry.note && (
                          <p className="mt-0.5 text-xs text-muted-foreground">
                            {entry.note}
                          </p>
                        )}
                        <div className="mt-2.5 flex flex-wrap items-center gap-1.5">
                          {entry.audiences.map((audience) => (
                            <Badge key={audience} variant="outline">
                              {audienceLabels[audience]}
                            </Badge>
                          ))}
                          <Badge variant={entry.style === "gi" ? "default" : "accent"}>
                            {entry.style === "gi" ? "Gi" : "No-Gi"}
                          </Badge>
                          {entry.locationId !== "stella-maris" && (
                            <Badge variant="solid" className="gap-1">
                              <MapPin className="size-3" aria-hidden="true" />
                              {location.name}
                            </Badge>
                          )}
                        </div>
                      </li>
                    );
                  })}
                </ul>
              </motion.div>
            ))}
          </AnimatePresence>
        </div>
      ) : (
        <p className="rounded-xl border border-border bg-card px-6 py-10 text-center text-muted-foreground">
          Aucun cours ne correspond à ce filtre pour le moment.
        </p>
      )}

      {/* Notes de bas de planning */}
      <div className="mt-8 space-y-2 text-sm text-muted-foreground">
        <p className="flex items-start gap-2.5">
          <MapPin className="mt-0.5 size-4 shrink-0 text-primary" aria-hidden="true" />
          Sauf mention contraire, tous les cours ont lieu au dojo du gymnase
          Stella Maris (40-42 promenade de la Barre, Anglet).
        </p>
        {hasIndicativeSlots && (
          <p className="flex items-start gap-2.5">
            <Info className="mt-0.5 size-4 shrink-0 text-accent" aria-hidden="true" />
            Planning indicatif susceptible d’évoluer en cours de saison —
            confirmez votre créneau au{" "}
            <a
              href={site.contact.phoneHref}
              className="font-medium text-foreground underline-offset-4 hover:underline"
            >
              {site.contact.phoneDisplay}
            </a>
            .
          </p>
        )}
      </div>
    </div>
  );
}
