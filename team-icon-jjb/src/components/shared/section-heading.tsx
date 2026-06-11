import { cn } from "@/lib/utils";
import { Reveal } from "@/components/shared/reveal";

interface SectionHeadingProps {
  /** Petit libellé rouge au-dessus du titre (ex. « Notre équipe »). */
  eyebrow?: string;
  title: string;
  description?: string;
  align?: "left" | "center";
  className?: string;
}

/** En-tête de section homogène : eyebrow, titre display et chapeau. */
export function SectionHeading({
  eyebrow,
  title,
  description,
  align = "left",
  className,
}: SectionHeadingProps) {
  return (
    <Reveal
      className={cn(
        "mb-12 max-w-3xl md:mb-16",
        align === "center" && "mx-auto text-center",
        className,
      )}
    >
      {eyebrow && (
        <p
          className={cn(
            "mb-3 flex items-center gap-3 font-display text-sm font-semibold tracking-[0.25em] text-primary uppercase",
            align === "center" && "justify-center",
          )}
        >
          <span aria-hidden="true" className="h-px w-8 bg-primary" />
          {eyebrow}
          {align === "center" && (
            <span aria-hidden="true" className="h-px w-8 bg-primary" />
          )}
        </p>
      )}
      <h2 className="font-display text-3xl font-bold tracking-wide text-balance uppercase sm:text-4xl md:text-5xl">
        {title}
      </h2>
      {description && (
        <p className="mt-4 text-base leading-relaxed text-muted-foreground md:text-lg">
          {description}
        </p>
      )}
    </Reveal>
  );
}
