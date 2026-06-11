"use client";

import * as React from "react";
import Link from "next/link";
import * as Dialog from "@radix-ui/react-dialog";
import { Menu, Phone, X } from "lucide-react";

import { navigation } from "@/data/navigation";
import { InstagramIcon } from "@/components/shared/social-icons";
import { site } from "@/data/site";
import { Button } from "@/components/ui/button";
import { Logo } from "@/components/shared/logo";
import { cn } from "@/lib/utils";

/**
 * Barre de navigation fixe : transparente en haut de page, fond
 * translucide + flou dès que l'on scrolle. Menu plein écran accessible
 * (Radix Dialog : focus trap, Échap, aria) sur mobile.
 */
export function Navbar() {
  const [scrolled, setScrolled] = React.useState(false);
  const [open, setOpen] = React.useState(false);

  React.useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 8);
    onScroll();
    window.addEventListener("scroll", onScroll, { passive: true });
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  return (
    <header
      className={cn(
        "fixed inset-x-0 top-0 z-50 h-(--header-height) transition-colors duration-300",
        scrolled
          ? "border-b border-border bg-background/85 backdrop-blur-md"
          : "border-b border-transparent bg-transparent",
      )}
    >
      <div className="container-site flex h-full items-center justify-between gap-4">
        <Link href="#accueil" aria-label="Retour en haut de page">
          <Logo />
        </Link>

        {/* Navigation bureau */}
        <nav aria-label="Navigation principale" className="hidden lg:block">
          <ul className="flex items-center gap-7">
            {navigation.map((item) => (
              <li key={item.href}>
                <Link
                  href={item.href}
                  className="group relative font-display text-sm font-medium tracking-widest text-muted-foreground uppercase transition-colors hover:text-foreground"
                >
                  {item.label}
                  <span
                    aria-hidden="true"
                    className="absolute -bottom-1.5 left-0 h-0.5 w-0 bg-primary transition-all duration-300 group-hover:w-full"
                  />
                </Link>
              </li>
            ))}
          </ul>
        </nav>

        <div className="hidden lg:block">
          <Button asChild size="sm">
            <Link href="#contact">Cours d’essai</Link>
          </Button>
        </div>

        {/* Menu mobile */}
        <Dialog.Root open={open} onOpenChange={setOpen}>
          <Dialog.Trigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className="lg:hidden"
              aria-label="Ouvrir le menu"
            >
              <Menu className="size-6" />
            </Button>
          </Dialog.Trigger>
          <Dialog.Portal>
            <Dialog.Overlay className="fixed inset-0 z-50 bg-background/60 backdrop-blur-sm" />
            <Dialog.Content className="fixed inset-y-0 right-0 z-50 flex w-full max-w-sm flex-col border-l border-border bg-background p-6 shadow-2xl">
              <Dialog.Title className="sr-only">Menu de navigation</Dialog.Title>
              <Dialog.Description className="sr-only">
                Liens vers les sections du site et coordonnées du club.
              </Dialog.Description>

              <div className="flex items-center justify-between">
                <Logo compact />
                <Dialog.Close asChild>
                  <Button variant="ghost" size="icon" aria-label="Fermer le menu">
                    <X className="size-6" />
                  </Button>
                </Dialog.Close>
              </div>

              <nav aria-label="Navigation mobile" className="mt-10 flex-1">
                <ul className="flex flex-col gap-1">
                  {navigation.map((item) => (
                    <li key={item.href}>
                      <Link
                        href={item.href}
                        onClick={() => setOpen(false)}
                        className="block rounded-md px-3 py-3 font-display text-xl font-semibold tracking-widest uppercase transition-colors hover:bg-foreground/5 hover:text-primary"
                      >
                        {item.label}
                      </Link>
                    </li>
                  ))}
                </ul>
              </nav>

              <div className="mt-6 flex flex-col gap-3 border-t border-border pt-6">
                <Button asChild>
                  <Link href="#contact" onClick={() => setOpen(false)}>
                    Réserver un cours d’essai
                  </Link>
                </Button>
                <div className="flex items-center justify-between text-sm text-muted-foreground">
                  <a
                    href={site.contact.phoneHref}
                    className="inline-flex items-center gap-2 hover:text-foreground"
                  >
                    <Phone className="size-4" aria-hidden="true" />
                    {site.contact.phoneDisplay}
                  </a>
                  <a
                    href={site.social.instagram}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="inline-flex items-center gap-2 hover:text-foreground"
                  >
                    <InstagramIcon className="size-4" aria-hidden="true" />
                    {site.social.instagramHandle}
                  </a>
                </div>
              </div>
            </Dialog.Content>
          </Dialog.Portal>
        </Dialog.Root>
      </div>
    </header>
  );
}
