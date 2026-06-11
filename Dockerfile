# ============================================================
# YBJJ Academy — image de production
# Site statique servi par nginx (léger, rapide, fiable).
#   docker build -t ybjj-academy .
#   docker run -d -p 8080:80 ybjj-academy
# ============================================================
FROM nginx:1.27-alpine

# Configuration nginx optimisée (gzip, cache, en-têtes de sécurité)
COPY deploy/nginx.conf /etc/nginx/conf.d/default.conf

# Le site
COPY docs/ /usr/share/nginx/html/

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
  CMD wget -qO- http://localhost/ > /dev/null || exit 1
