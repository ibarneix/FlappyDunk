"use client";

import Link from "next/link";
import { motion, useReducedMotion } from "framer-motion";
import { ChevronDown } from "lucide-react";

import { site } from "@/data/site";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";

const headlineLines = [
  { text: "Team Icon", accent: false },
  { text: "Jiu-Jitsu", accent: true },
  { text: "Pays Basque", accent: false },
] as const;

const highlights = [
  "Dès 4 ans",
  "Gi & No-Gi",
  "6 jours / 7",
  "Loisir & Compétition",
] as const;

/**
 * Section d'ouverture : titre display monumental, baseline, texte
 * d'introduction et appels à l'action. Entrée animée en cascade,
 * désactivée si l'utilisateur préfère réduire les animations.
 */
export function Hero() {
  const reduceMotion = useReducedMotion();

  const entrance = (delay: number) =>
    reduceMotion
      ? {}
      : {
          initial: { opacity: 0, y: 28 },
          animate: { opacity: 1, y: 0 },
          transition: {
            duration: 0.7,
            delay,
            ease: [0.21, 0.47, 0.32, 0.98] as const,
          },
        };

  return (
    <section
      id="accueil"
      className="glow-primary relative flex min-h-svh items-center overflow-hidden"
    >
      <div aria-hidden="true" className="bg-tatami absolute inset-0" />
      {/* Liseré « ceinture » décoratif */}
      <div
        aria-hidden="true"
        className="absolute top-0 right-[12%] hidden h-full w-px bg-gradient-to-b from-transparent via-primary/30 to-transparent lg:block"
      />

      <div className="container-site relative pt-(--header-height) pb-16">
        <motion.p
          {...entrance(0)}
          className="mb-6 inline-flex items-center gap-2.5 rounded-full border border-border bg-card/60 px-4 py-1.5 text-xs font-medium tracking-[0.2em] text-muted-foreground uppercase backdrop-blur"
        >
          <span aria-hidden="true" className="size-1.5 rounded-full bg-primary" />
          Anglet · Côte Basque — Académie fondée en {site.foundingYear}
        </motion.p>

        <h1 className="font-display font-bold tracking-wide uppercase">
          {headlineLines.map((line, index) => (
            <motion.span
              key={line.text}
              {...entrance(0.08 * (index + 1))}
              className={`block text-5xl leading-[1.04] sm:text-7xl lg:text-8xl ${
                line.accent ? "text-primary" : "text-foreground"
              }`}
            >
              {line.text}
            </motion.span>
          ))}
        </h1>

        <motion.p
          {...entrance(0.4)}
          className="mt-6 font-display text-lg font-medium tracking-wider text-accent uppercase sm:text-xl"
        >
          {site.baseline}
        </motion.p>

        <motion.p
          {...entrance(0.5)}
          className="mt-5 max-w-2xl text-base leading-relaxed text-muted-foreground md:text-lg"
        >
          Discipline jeune et novatrice, connue notamment à travers le MMA, le
          Jiu-Jitsu Brésilien se pratique essentiellement au sol. Véritable
          partie d’échecs humaine, il demande des capacités intellectuelles
          autant que physiques. Adapté à toutes et à tous dès 4 ans, en loisir
          comme en compétition.
        </motion.p>

        <motion.div {...entrance(0.6)} className="mt-9 flex flex-wrap gap-4">
          <Button asChild size="lg">
            <Link href="#contact">Réserver un cours d’essai</Link>
          </Button>
          <Button asChild size="lg" variant="outline">
            <Link href="#horaires">Voir les horaires</Link>
          </Button>
        </motion.div>

        <motion.ul {...entrance(0.7)} className="mt-10 flex flex-wrap gap-2.5">
          {highlights.map((item) => (
            <li key={item}>
              <Badge variant="outline" className="px-3.5 py-1.5 text-sm">
                {item}
              </Badge>
            </li>
          ))}
        </motion.ul>
      </div>

      <motion.div
        aria-hidden="true"
        className="absolute bottom-6 left-1/2 -translate-x-1/2 text-muted-foreground"
        initial={reduceMotion ? undefined : { opacity: 0 }}
        animate={
          reduceMotion ? undefined : { opacity: [0, 1, 1, 0], y: [0, 8, 8, 0] }
        }
        transition={{ duration: 2.4, repeat: Infinity, delay: 1.2 }}
      >
        <ChevronDown className="size-6" />
      </motion.div>
    </section>
  );
}
