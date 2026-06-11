import { cn } from "@/lib/utils";

interface LogoProps {
  className?: string;
  /** Masque le texte pour ne garder que le monogramme. */
  compact?: boolean;
}

/**
 * Logotype du club : monogramme rouge + wordmark.
 * Remplaçable par le logo officiel (SVG/PNG) sans toucher au layout.
 */
export function Logo({ className, compact = false }: LogoProps) {
  return (
    <span className={cn("flex items-center gap-3", className)}>
      <svg
        viewBox="0 0 32 32"
        aria-hidden="true"
        className="size-9 shrink-0 drop-shadow-[0_4px_12px_rgba(225,29,46,0.4)]"
      >
        <rect width="32" height="32" rx="7" fill="var(--primary)" />
        <path d="M12.5 7h7v3.5h-7z" fill="#fff" />
        <path d="M14 10.5h4V21.5h-4z" fill="#fff" />
        <path d="M12.5 21.5h7V25h-7z" fill="#fff" />
        <path d="M0 22 32 12v3.5L0 25.5Z" fill="var(--accent)" opacity="0.9" />
      </svg>
      {!compact && (
        <span className="flex flex-col leading-none">
          <span className="font-display text-lg font-bold tracking-widest uppercase">
            Team Icon
          </span>
          <span className="text-[0.65rem] font-medium tracking-[0.28em] text-muted-foreground uppercase">
            Jiu-Jitsu Pays Basque
          </span>
        </span>
      )}
    </span>
  );
}
