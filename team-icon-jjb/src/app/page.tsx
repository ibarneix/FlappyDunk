import { Disciplines } from "@/components/sections/disciplines";
import { Hero } from "@/components/sections/hero";
import { Quote } from "@/components/sections/quote";
import { Team } from "@/components/sections/team";
import { Values } from "@/components/sections/values";

/**
 * Page d'accueil — vitrine une page : chaque section est ancrée
 * (`#accueil`, `#pourquoi`, `#equipe`…) et reliée à la navigation.
 */
export default function HomePage() {
  return (
    <main id="contenu">
      <Hero />
      <Quote />
      <Values />
      <Disciplines />
      <Team />
    </main>
  );
}
