FROM ghcr.io/navikt/poao-baseimages/java:17
COPY /target/veilarbvedtakinfo.jar app.jar
