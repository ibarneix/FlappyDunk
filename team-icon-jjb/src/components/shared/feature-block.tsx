import {
  Brain,
  Handshake,
  Sparkles,
  TrendingUp,
  Trophy,
  type LucideIcon,
} from "lucide-react";

import type { Value } from "@/lib/types";
import { Card, CardContent } from "@/components/ui/card";
import { Reveal } from "@/components/shared/reveal";

/** Résolution du nom d'icône (donnée sérialisable) vers le composant lucide. */
const icons: Record<Value["icon"], LucideIcon> = {
  "trending-up": TrendingUp,
  handshake: Handshake,
  brain: Brain,
  trophy: Trophy,
  sparkles: Sparkles,
};

interface FeatureBlockProps {
  value: Value;
  /** Index dans la grille, pour l'apparition en cascade. */
  index?: number;
}

/**
 * Bloc « valeur » réutilisable : icône dans une pastille rouge,
 * titre display et description. Léger lift au survol.
 */
export function FeatureBlock({ value, index = 0 }: FeatureBlockProps) {
  const Icon = icons[value.icon];

  return (
    <Reveal delay={index * 0.08} className="h-full">
      <Card className="group h-full transition-all duration-300 hover:-translate-y-1 hover:border-primary/40">
        <CardContent className="p-6 md:p-7">
          <span className="mb-5 inline-grid size-12 place-items-center rounded-lg bg-primary/12 text-primary transition-colors duration-300 group-hover:bg-primary group-hover:text-primary-foreground">
            <Icon className="size-6" aria-hidden="true" />
          </span>
          <h3 className="font-display text-lg font-semibold tracking-wide uppercase">
            {value.title}
          </h3>
          <p className="mt-2.5 text-sm leading-relaxed text-muted-foreground">
            {value.description}
          </p>
        </CardContent>
      </Card>
    </Reveal>
  );
}
