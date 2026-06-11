import { Reveal } from "@/components/shared/reveal";

/** Citation fondatrice d'Helio Gracie, mise en majesté entre deux liserés. */
export function Quote() {
  return (
    <section
      aria-label="Citation d’Helio Gracie"
      className="border-y border-border bg-card/40"
    >
      <div className="container-site py-20 md:py-24">
        <Reveal className="mx-auto max-w-4xl text-center">
          <figure>
            <span
              aria-hidden="true"
              className="mx-auto mb-8 block font-display text-7xl leading-none text-primary"
            >
              “
            </span>
            <blockquote className="text-xl leading-relaxed font-light text-balance text-foreground md:text-2xl lg:text-[1.7rem]">
              Supposez toujours que votre adversaire sera plus grand, plus fort
              et plus rapide que vous&nbsp;; afin que vous appreniez à vous fier
              aux techniques, au timing et à l’effet de levier plutôt qu’à la
              force brute.
            </blockquote>
            <figcaption className="mt-8 font-display text-sm font-semibold tracking-[0.3em] text-accent uppercase">
              — Helio Gracie
              <span className="mt-1.5 block text-xs font-normal tracking-[0.2em] text-muted-foreground">
                Co-fondateur du Jiu-Jitsu Brésilien
              </span>
            </figcaption>
          </figure>
        </Reveal>
      </div>
    </section>
  );
}
