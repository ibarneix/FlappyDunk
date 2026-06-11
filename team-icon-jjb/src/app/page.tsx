import { Contact } from "@/components/sections/contact";
import { Disciplines } from "@/components/sections/disciplines";
import { Hero } from "@/components/sections/hero";
import { Pricing } from "@/components/sections/pricing";
import { Quote } from "@/components/sections/quote";
import { Schedule } from "@/components/sections/schedule";
import { Team } from "@/components/sections/team";
import { TrainingCenters } from "@/components/sections/training-centers";
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
      <Schedule />
      <TrainingCenters />
      <Pricing />
      <Contact />
    </main>
  );
}
