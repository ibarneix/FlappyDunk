/* ============================================================
   YBJJ ACADEMY — interactions
   GSAP + ScrollTrigger + Lenis (vendorisés en local).
   Dégradation progressive : sans JS ou en mouvement réduit,
   tout le contenu reste accessible.
   ============================================================ */
(function () {
  'use strict';

  var html = document.documentElement;
  html.classList.add('js-enabled');

  var reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  var hasGsap = typeof window.gsap !== 'undefined';
  var animate = hasGsap && !reduceMotion;

  if (animate) {
    html.classList.add('js-anim');
    gsap.registerPlugin(ScrollTrigger);
  } else {
    html.classList.add('no-motion');
  }

  /* ---------- Découpage des titres en caractères ---------- */
  document.querySelectorAll('[data-split]').forEach(function (line) {
    var text = line.textContent;
    line.setAttribute('aria-label', text);
    line.textContent = '';
    Array.prototype.forEach.call(text, function (ch) {
      var s = document.createElement('span');
      s.className = 'char';
      s.setAttribute('aria-hidden', 'true');
      s.innerHTML = ch === ' ' ? '&nbsp;' : ch;
      line.appendChild(s);
    });
  });

  if (animate) {
    gsap.set('.hero-title .char, .founder-name .char', { yPercent: 110 });
  }

  /* ---------- Lenis : smooth scroll ---------- */
  var lenis = null;
  if (animate && typeof window.Lenis !== 'undefined') {
    lenis = new Lenis({ lerp: 0.095, wheelMultiplier: 1.05 });
    lenis.on('scroll', ScrollTrigger.update);
    gsap.ticker.add(function (time) { lenis.raf(time * 1000); });
    gsap.ticker.lagSmoothing(0);
  }

  function scrollToTarget(target) {
    var el = typeof target === 'string' ? document.querySelector(target) : target;
    if (!el) return;
    if (lenis) lenis.scrollTo(el, { offset: 0, duration: 1.4 });
    else el.scrollIntoView({ behavior: reduceMotion ? 'auto' : 'smooth' });
  }

  document.addEventListener('click', function (e) {
    var a = e.target.closest('a[href^="#"]');
    if (!a) return;
    var id = a.getAttribute('href');
    if (id.length > 1 && document.querySelector(id)) {
      e.preventDefault();
      closeMenu();
      scrollToTarget(id);
    }
  });

  /* ---------- Préloader ---------- */
  var preloader = document.getElementById('preloader');
  var countEl = document.getElementById('preload-count');
  var barEl = document.getElementById('preload-bar');
  var introStarted = false;

  function finishPreloader() {
    if (introStarted) return;
    introStarted = true;
    preloader.classList.add('done');
    html.classList.add('is-ready');
    preloader.addEventListener('transitionend', function () {
      preloader.remove();
    }, { once: true });
    if (animate) heroIntro();
  }

  if (animate) {
    var progress = { v: 0 };
    var loaded = false;
    window.addEventListener('load', function () { loaded = true; });
    gsap.to(progress, {
      v: 100,
      duration: 1.5,
      ease: 'power2.inOut',
      onUpdate: function () {
        var v = Math.round(progress.v);
        countEl.textContent = v;
        barEl.style.width = v + '%';
      },
      onComplete: function () {
        // attendre la fin du chargement réel, au maximum 2 s de plus
        var waited = 0;
        var t = setInterval(function () {
          waited += 100;
          if (loaded || waited >= 2000) { clearInterval(t); finishPreloader(); }
        }, 100);
      }
    });
  } else {
    countEl.textContent = '100';
    barEl.style.width = '100%';
    finishPreloader();
  }
  // Sécurité absolue : jamais bloqué derrière le préloader
  setTimeout(finishPreloader, 6000);

  /* ---------- Intro du hero ---------- */
  function heroIntro() {
    var tl = gsap.timeline({ defaults: { ease: 'power4.out' } });
    tl.to('.hero-title .char', { yPercent: 0, duration: 1.1, stagger: 0.028 }, 0.1)
      .to('.hero .hero-badge[data-intro]', { opacity: 1, y: 0, duration: 1.1 }, 0.2)
      .fromTo('.hero-badge-tilt', { scale: 0.7, rotate: -8 }, { scale: 1, rotate: 0, duration: 1.2, ease: 'expo.out' }, 0.2)
      .to('.hero-copy [data-intro]', { opacity: 1, y: 0, duration: 0.9, stagger: 0.12 }, 0.45);
  }

  /* ---------- Révélations au scroll ---------- */
  if (animate) {
    // éléments du hero gérés par l'intro : on les exclut
    var revealEls = gsap.utils.toArray('[data-reveal]');
    ScrollTrigger.batch(revealEls, {
      start: 'top 88%',
      once: true,
      onEnter: function (batch) {
        gsap.to(batch, { opacity: 1, y: 0, duration: 1, ease: 'power3.out', stagger: 0.12 });
      }
    });

    // Nom du fondateur : lettres au scroll
    gsap.to('.founder-name .char', {
      yPercent: 0,
      duration: 1,
      ease: 'power4.out',
      stagger: 0.04,
      scrollTrigger: { trigger: '.founder-name', start: 'top 82%', once: true }
    });

    // Parallaxe du watermark
    gsap.to('.hero-watermark', {
      yPercent: -28,
      ease: 'none',
      scrollTrigger: { trigger: '.hero', start: 'top top', end: 'bottom top', scrub: true }
    });

    // Légère profondeur sur le badge au scroll
    gsap.to('.hero-badge', {
      yPercent: 14,
      ease: 'none',
      scrollTrigger: { trigger: '.hero', start: 'top top', end: 'bottom top', scrub: true }
    });

    // Empilement des piliers : la carte du dessous s'assombrit légèrement
    gsap.utils.toArray('.pillar-card').forEach(function (card, i, all) {
      if (i === all.length - 1) return;
      gsap.to(card, {
        scale: 0.96,
        filter: 'brightness(0.72)',
        transformOrigin: 'center top',
        ease: 'none',
        scrollTrigger: {
          trigger: all[i + 1],
          start: 'top bottom',
          end: 'top top+=' + (140 + i * 24),
          scrub: true
        }
      });
    });
  }

  /* ---------- Compteurs ---------- */
  if (animate) {
    gsap.utils.toArray('[data-count]').forEach(function (el) {
      var target = parseFloat(el.getAttribute('data-count'));
      var obj = { v: 0 };
      ScrollTrigger.create({
        trigger: el,
        start: 'top 92%',
        once: true,
        onEnter: function () {
          gsap.to(obj, {
            v: target,
            duration: 1.6,
            ease: 'power2.out',
            onUpdate: function () { el.textContent = Math.round(obj.v); }
          });
        }
      });
    });
  } else {
    document.querySelectorAll('[data-count]').forEach(function (el) {
      el.textContent = el.getAttribute('data-count');
    });
  }

  /* ---------- Marquees réactifs à la vitesse de scroll ---------- */
  if (animate) {
    var marqueeTweens = [];
    document.querySelectorAll('[data-marquee]').forEach(function (m) {
      var track = m.querySelector('.marquee-track');
      var speed = parseFloat(m.getAttribute('data-marquee-speed')) || 26;
      var reverse = m.hasAttribute('data-marquee-reverse');
      var tween = gsap.fromTo(track,
        { xPercent: reverse ? -50 : 0 },
        { xPercent: reverse ? 0 : -50, ease: 'none', duration: speed, repeat: -1 });
      marqueeTweens.push(tween);
    });
    var velocity = 0;
    if (lenis) lenis.on('scroll', function (e) { velocity = e.velocity || 0; });
    gsap.ticker.add(function () {
      var boost = 1 + Math.min(Math.abs(velocity) / 60, 2.5);
      marqueeTweens.forEach(function (t) {
        t.timeScale(gsap.utils.interpolate(t.timeScale(), boost, 0.08));
      });
    });
  }

  /* ---------- Canvas : particules du hero ---------- */
  (function heroCanvas() {
    if (!animate) return;
    var canvas = document.getElementById('hero-canvas');
    if (!canvas) return;
    var ctx = canvas.getContext('2d');
    var dpr = Math.min(window.devicePixelRatio || 1, 2);
    var w, h, parts = [];
    var COUNT = window.innerWidth < 768 ? 36 : 80;

    function resize() {
      w = canvas.clientWidth;
      h = canvas.clientHeight;
      canvas.width = w * dpr;
      canvas.height = h * dpr;
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    }
    function spawn(randomY) {
      return {
        x: Math.random() * w,
        y: randomY ? Math.random() * h : h + 10,
        r: 0.8 + Math.random() * 2.2,
        s: 0.25 + Math.random() * 0.8,
        drift: (Math.random() - 0.5) * 0.5,
        red: Math.random() < 0.55,
        a: 0.25 + Math.random() * 0.5,
        ph: Math.random() * Math.PI * 2
      };
    }
    function init() {
      resize();
      parts = [];
      for (var i = 0; i < COUNT; i++) parts.push(spawn(true));
    }
    var running = true;
    document.addEventListener('visibilitychange', function () {
      running = document.visibilityState === 'visible';
    });
    var t = 0;
    function frame() {
      if (running && h > 0) {
        t += 0.016;
        ctx.clearRect(0, 0, w, h);
        for (var i = 0; i < parts.length; i++) {
          var p = parts[i];
          p.y -= p.s;
          p.x += p.drift + Math.sin(t + p.ph) * 0.18;
          if (p.y < -12 || p.x < -12 || p.x > w + 12) parts[i] = spawn(false);
          ctx.globalAlpha = p.a;
          ctx.fillStyle = p.red ? '#D0201E' : '#8E8E96';
          ctx.beginPath();
          ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
          ctx.fill();
        }
        ctx.globalAlpha = 1;
      }
      requestAnimationFrame(frame);
    }
    window.addEventListener('resize', resize);
    init();
    requestAnimationFrame(frame);
  })();

  /* ---------- Curseur personnalisé ---------- */
  (function cursor() {
    if (!animate || !window.matchMedia('(pointer: fine)').matches) return;
    var dot = document.getElementById('cursor-dot');
    var ring = document.getElementById('cursor-ring');
    var dotX = gsap.quickTo(dot, 'x', { duration: 0.08, ease: 'power2.out' });
    var dotY = gsap.quickTo(dot, 'y', { duration: 0.08, ease: 'power2.out' });
    var ringX = gsap.quickTo(ring, 'x', { duration: 0.45, ease: 'power3.out' });
    var ringY = gsap.quickTo(ring, 'y', { duration: 0.45, ease: 'power3.out' });
    gsap.set([dot, ring], { xPercent: -50, yPercent: -50 });
    window.addEventListener('mousemove', function (e) {
      dotX(e.clientX); dotY(e.clientY);
      ringX(e.clientX); ringY(e.clientY);
    });
    document.addEventListener('mouseover', function (e) {
      if (e.target.closest('a, button, [data-cursor]')) ring.classList.add('is-active');
    });
    document.addEventListener('mouseout', function (e) {
      if (e.target.closest('a, button, [data-cursor]')) ring.classList.remove('is-active');
    });
  })();

  /* ---------- Boutons magnétiques ---------- */
  if (animate && window.matchMedia('(pointer: fine)').matches) {
    document.querySelectorAll('[data-magnetic]').forEach(function (el) {
      var xTo = gsap.quickTo(el, 'x', { duration: 0.4, ease: 'power3.out' });
      var yTo = gsap.quickTo(el, 'y', { duration: 0.4, ease: 'power3.out' });
      el.addEventListener('mousemove', function (e) {
        var r = el.getBoundingClientRect();
        xTo((e.clientX - r.left - r.width / 2) * 0.32);
        yTo((e.clientY - r.top - r.height / 2) * 0.32);
      });
      el.addEventListener('mouseleave', function () {
        gsap.to(el, { x: 0, y: 0, duration: 0.7, ease: 'elastic.out(1, 0.4)' });
      });
    });
  }

  /* ---------- Inclinaison 3D du badge ---------- */
  (function tilt() {
    if (!animate || !window.matchMedia('(pointer: fine)').matches) return;
    var el = document.getElementById('badge-tilt');
    if (!el) return;
    var zone = el.closest('.hero-badge');
    var rx = gsap.quickTo(el, 'rotationX', { duration: 0.6, ease: 'power3.out' });
    var ry = gsap.quickTo(el, 'rotationY', { duration: 0.6, ease: 'power3.out' });
    gsap.set(el, { transformPerspective: 900 });
    zone.addEventListener('mousemove', function (e) {
      var r = el.getBoundingClientRect();
      ry(((e.clientX - r.left) / r.width - 0.5) * 16);
      rx(-((e.clientY - r.top) / r.height - 0.5) * 16);
    });
    zone.addEventListener('mouseleave', function () { rx(0); ry(0); });
  })();

  /* ---------- Header : repli / fond ---------- */
  var header = document.getElementById('site-header');
  var lastY = 0;
  function onScrollPos(y) {
    header.classList.toggle('scrolled', y > 40);
    if (menu.classList.contains('open')) { header.classList.remove('hidden'); lastY = y; return; }
    if (y > 300 && y > lastY + 4) header.classList.add('hidden');
    else if (y < lastY - 4) header.classList.remove('hidden');
    lastY = y;
  }
  if (lenis) lenis.on('scroll', function (e) { onScrollPos(e.scroll); });
  else window.addEventListener('scroll', function () { onScrollPos(window.scrollY); }, { passive: true });

  /* ---------- Menu mobile ---------- */
  var toggle = document.getElementById('nav-toggle');
  var menu = document.getElementById('menu-principal');
  function closeMenu() {
    menu.classList.remove('open');
    toggle.setAttribute('aria-expanded', 'false');
  }
  toggle.addEventListener('click', function () {
    var open = menu.classList.toggle('open');
    toggle.setAttribute('aria-expanded', String(open));
  });
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') closeMenu();
  });

  /* ---------- Lien actif ---------- */
  var sections = ['accueil', 'academie', 'fondateur', 'philosophie', 'cours', 'contact']
    .map(function (id) { return document.getElementById(id); })
    .filter(Boolean);
  if ('IntersectionObserver' in window) {
    var io = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (!entry.isIntersecting) return;
        document.querySelectorAll('.nav-link').forEach(function (l) {
          l.classList.toggle('active', l.getAttribute('href') === '#' + entry.target.id);
        });
      });
    }, { rootMargin: '-42% 0px -52% 0px' });
    sections.forEach(function (s) { io.observe(s); });
  }

  /* ---------- Divers ---------- */
  var yearEl = document.getElementById('year');
  if (yearEl) yearEl.textContent = String(new Date().getFullYear());
})();
